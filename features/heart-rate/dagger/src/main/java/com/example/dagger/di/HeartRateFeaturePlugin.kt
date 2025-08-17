package com.example.dagger.di

import android.util.Log
import com.example.api.interfaces.HeartRateViewModel
import com.example.core.di.prod.CoreComponent
import com.example.core.featureprovision.ComponentProviderRegistry
import com.example.core.featureprovision.FeatureProvider
import com.example.core.featureprovision.SelfRegisteringFeaturePlugin
import com.example.core.scopes.FeatureScope

class HeartRateFeaturePlugin(
    private val coreComponent: CoreComponent
) : SelfRegisteringFeaturePlugin() {
    override val featureId: String = "heartrate"

    override fun getSupportedScopes(): Set<FeatureScope> {
        return setOf(FeatureScope.APP, FeatureScope.ACTIVITY, FeatureScope.FRAGMENT, FeatureScope.VIEWMODEL)
    }

    override fun initialize() {
        Log.d("HeartRatePlugin", "Initializing heart rate feature")
        ComponentProviderRegistry.registerFactory(featureId, HeartRateComponentFactory(coreComponent))
    }

    override fun getProviders(): Map<Class<*>, FeatureProvider<*>> {
        return createScopedProviders(FeatureScope.APP, null)
    }

    override fun createScopedProviders(scope: FeatureScope, scopeKey: Any?): Map<Class<*>, FeatureProvider<*>> {
        val component = ComponentProviderRegistry.getComponent(featureId, scope, Any::class.java)
                as? HeartRateComponent ?: return emptyMap()

        return mapOf(
            HeartRateViewModel::class.java to object : FeatureProvider<HeartRateViewModel> {
                override fun provide(): HeartRateViewModel = component.heartRateViewModel()
            }
        )
    }
}
