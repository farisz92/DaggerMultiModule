package com.example.app.di

import android.util.Log
import com.example.core.featureprovision.ScopeAwareFeatureManager
import com.example.core.featureprovision.FeatureProvider
import com.example.core.featureprovision.SelfRegisteringFeaturePlugin
import com.example.core.scopes.FeatureScope
import com.example.core.scopes.InjectFeature
import java.lang.reflect.Field

class EnterpriseScopeAwareFeatureManager : ScopeAwareFeatureManager {
    private val appProviders = mutableMapOf<Class<*>, FeatureProvider<*>>()
    private val scopedProviders =
        mutableMapOf<FeatureScope, MutableMap<Any, MutableMap<Class<*>, FeatureProvider<*>>>>()
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
        // Get all plugins that have self-registered
        val discoveredPlugins = SelfRegisteringFeaturePlugin.getAllPlugins()

        discoveredPlugins.forEach { plugin ->
            plugin.initialize()

            // For app scope, immediately create providers
            if (plugin.getSupportedScopes().contains(FeatureScope.APP)) {
                plugin.getProviders().forEach { (clazz, provider) ->
                    appProviders[clazz] = provider
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
                FeatureScope.APP -> {
                    (appProviders[clazz] as? FeatureProvider<T>)?.provide()
                }

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

        @Suppress("UNCHECKED_CAST")
        return (providers[clazz] as? FeatureProvider<T>)?.provide()
    }

    private fun createProvidersForScope(
        scope: FeatureScope,
        scopeKey: Any
    ): MutableMap<Class<*>, FeatureProvider<*>> {
        val providers = mutableMapOf<Class<*>, FeatureProvider<*>>()

        // Ask ALL discovered plugins if they support this scope
        SelfRegisteringFeaturePlugin.getAllPlugins()
            .filter { it.getSupportedScopes().contains(scope) }
            .forEach { plugin ->
                try {
                    val pluginProviders = plugin.createScopedProviders(scope, scopeKey)
                    providers.putAll(pluginProviders)
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

            getFeature(field.type, scope, scopeKey)?.let { feature ->
                field.isAccessible = true
                field.set(target, feature)
            }
        }
    }

    override fun clearScope(scope: FeatureScope, scopeKey: Any) {
        when (scope) {
            FeatureScope.APP -> {
                Log.w("FeatureManager", "Cannot clear app scope")
            }

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