package com.example.dagger.di

import android.util.Log
import com.example.api.interfaces.StepsViewModel
import com.example.core.di.prod.CoreComponent
import com.example.core.featureprovision.ComponentProviderRegistry
import com.example.core.featureprovision.FeatureProvider
import com.example.core.featureprovision.SelfRegisteringFeaturePlugin
import com.example.core.scopes.FeatureScope

class StepsFeaturePlugin(
    private val coreComponent: CoreComponent
) : SelfRegisteringFeaturePlugin() {
    override val featureId: String = "steps"

    override fun getSupportedScopes(): Set<FeatureScope> {
        return setOf(FeatureScope.APP, FeatureScope.ACTIVITY, FeatureScope.FRAGMENT)
    }

    override fun initialize() {
        Log.d("StepsPlugin", "Initializing steps feature")
        ComponentProviderRegistry.registerFactory(featureId, StepsComponentFactory(coreComponent))
    }

    override fun getProviders(): Map<Class<*>, FeatureProvider<*>> {
        return createScopedProviders(FeatureScope.APP, null)
    }

    override fun createScopedProviders(scope: FeatureScope, scopeKey: Any?): Map<Class<*>, FeatureProvider<*>> {
        val component = ComponentProviderRegistry.getComponent(featureId, scope, Any::class.java)
                as? StepsComponent ?: return emptyMap()

        return mapOf(
            StepsViewModel::class.java to object : FeatureProvider<StepsViewModel> {
                override fun provide(): StepsViewModel = component.stepsViewModel()
            }
        )
    }
}
