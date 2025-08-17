package com.example.app.di

import android.util.Log
import com.example.core.featureprovision.FeatureManager
import com.example.core.featureprovision.FeatureProvider
import com.example.core.scopes.InjectFeature
import com.example.dagger.di.HeartRateFeaturePlugin
import com.example.dagger.di.StepsFeaturePlugin
import java.lang.reflect.Field

class EnterpriseFeatureManager(appComponent: AppComponent): FeatureManager {
    private val providers = mutableMapOf<Class<*>, FeatureProvider<*>>()
    private val fieldCache = mutableMapOf<Class<*>, List<Field>>()

    init {
        // Create plugins in :app - pass component factories to avoid circular deps
        registerPlugins(
            StepsFeaturePlugin(appComponent.stepsComponent()),           // Pass factory, not whole component
            HeartRateFeaturePlugin(appComponent.heartRateComponent())   // Pass factory, not whole component
        )
    }

    private fun registerPlugins(vararg plugins: com.example.core.featureprovision.FeaturePlugin) {
        plugins.forEach { plugin ->
            plugin.initialize()
            plugin.getProviders().forEach { (clazz, provider) ->
                providers[clazz] = provider
            }
            Log.d("FeatureManager", "Registered plugin: ${plugin.featureId}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getFeature(clazz: Class<T>): T? {
        return try {
            (providers[clazz] as? FeatureProvider<T>)?.provide()
        } catch (e: Exception) {
            Log.w("FeatureManager", "Failed to provide ${clazz.simpleName}", e)
            null
        }
    }

    override fun inject(target: Any) {
        val fields = getInjectableFields(target.javaClass)

        fields.forEach { field ->
            getFeature(field.type)?.let { feature ->
                field.isAccessible = true
                field.set(target, feature)
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