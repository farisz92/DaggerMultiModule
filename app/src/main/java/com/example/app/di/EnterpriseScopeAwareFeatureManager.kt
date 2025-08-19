package com.example.app.di

import android.util.Log
import com.example.core.di.FeatureScope
import com.example.core.featureprovision.LazyFeatureDelegate
import com.example.core.featureprovision.LazyFeatureProvider
import com.example.core.featureprovision.ScopeAwareFeatureManager
import com.example.core.featureprovision.SelfRegisteringFeaturePlugin
import com.example.core.scopes.InjectFeature
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

class EnterpriseScopeAwareFeatureManager : ScopeAwareFeatureManager {
    private val appProviders = mutableMapOf<Class<*>, LazyFeatureProvider<*>>()
    private val scopedProviders =
        mutableMapOf<FeatureScope, MutableMap<Any, MutableMap<Class<*>, LazyFeatureProvider<*>>>>()
    private val fieldCache = mutableMapOf<Class<*>, List<Field>>()

    init {
        // Initialize scope maps
        FeatureScope.entries.forEach { scope ->
            if (scope != FeatureScope.APP) {
                scopedProviders[scope] = mutableMapOf()
            }
        }

        // Auto-discover and register all plugins - NO hardcoded feature list!
        initializeDiscoveredPlugins()
    }

    private fun initializeDiscoveredPlugins() {
        val discoveredPlugins = SelfRegisteringFeaturePlugin.getAllPlugins()

        discoveredPlugins.forEach { plugin ->
            plugin.initialize()

            // For app scope, wrap providers in LazyFeatureProvider
            if (plugin.getSupportedScopes().contains(FeatureScope.APP)) {
                plugin.getProviders().forEach { (clazz, provider) ->
                    appProviders[clazz] = LazyFeatureProvider { provider.provide() }
                }
            }

            Log.d("FeatureManager", "Auto-discovered plugin: ${plugin.featureId}")
        }

        Log.d("FeatureManager", "Auto-discovered ${discoveredPlugins.size} plugins")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getFeature(clazz: Class<T>, scope: FeatureScope, scopeKey: Any?): T? {
        return try {
            when (scope) {
                FeatureScope.APP -> (appProviders[clazz] as? LazyFeatureProvider<T>)?.provide()
                else -> {
                    requireNotNull(scopeKey) { "$scope scope requires scopeKey" }
                    getScopedFeature(clazz, scope, scopeKey)
                }
            }
        } catch (e: Exception) {
            Log.w("FeatureManager", "Failed to provide ${clazz.simpleName} for scope $scope", e)
            null
        }
    }

    private fun <T> getScopedFeature(clazz: Class<T>, scope: FeatureScope, scopeKey: Any): T? {
        val scopeProviders = scopedProviders[scope] ?: return null
        val providers = scopeProviders.getOrPut(scopeKey) {
            createProvidersForScope(scope, scopeKey)
        }
        return (providers[clazz] as? LazyFeatureProvider<T>)?.provide()
    }

    private fun createProvidersForScope(
        scope: FeatureScope,
        scopeKey: Any
    ): MutableMap<Class<*>, LazyFeatureProvider<*>> {
        val providers = mutableMapOf<Class<*>, LazyFeatureProvider<*>>()

        SelfRegisteringFeaturePlugin.getAllPlugins()
            .filter { it.getSupportedScopes().contains(scope) }
            .forEach { plugin ->
                try {
                    val pluginProviders = plugin.createScopedProviders(scope, scopeKey)
                    pluginProviders.forEach { (clazz, provider) ->
                        providers[clazz] = LazyFeatureProvider { provider.provide() }
                    }
                } catch (e: Exception) {
                    Log.e(
                        "FeatureManager",
                        "Failed to create providers from ${plugin.featureId}",
                        e
                    )
                }
            }

        return providers
    }

    override fun inject(target: Any) {
        val fields = getInjectableFields(target.javaClass)
        fields.forEach { field ->
            val annotation = field.getAnnotation(InjectFeature::class.java)
            val scope = annotation.scope
            val scopeKey = when (scope) {
                FeatureScope.APP -> null
                else -> target
            }

            // Get the actual feature type from the field's generic type
            val featureClass = field.type as Class<*>
            if (featureClass == LazyFeatureDelegate::class.java) {
                // This is a LazyFeatureDelegate field, we need to get its type parameter
                val genericType = field.genericType as? ParameterizedType
                    ?: throw IllegalArgumentException("LazyFeatureDelegate must be parameterized")
                val actualType = genericType.actualTypeArguments[0] as Class<*>

                // Create a LazyFeatureDelegate for the actual feature type
                val delegate = LazyFeatureDelegate<Any>(
                    scope = scope,
                    scopeKey = scopeKey,
                    featureManager = this,
                    featureClass = actualType as Class<Any>
                )

                field.isAccessible = true
                field.set(target, delegate)
            } else {
                // Original behavior for non-lazy fields
                getFeature(field.type as Class<Any>, scope, scopeKey)?.let { feature ->
                    field.isAccessible = true
                    field.set(target, feature)
                }
            }
        }
    }

    override fun clearScope(scope: FeatureScope, scopeKey: Any) {
        when (scope) {
            FeatureScope.APP -> Log.w("FeatureManager", "Cannot clear app scope")
            else -> {
                scopedProviders[scope]?.remove(scopeKey)
                Log.d("FeatureManager", "Cleared $scope scope for ${scopeKey::class.simpleName}")
            }
        }
    }

    private fun getInjectableFields(clazz: Class<*>): List<Field> {
        return fieldCache.getOrPut(clazz) {
            clazz.declaredFields
                .filter { it.isAnnotationPresent(InjectFeature::class.java) }
                .toList()
        }
    }
}
