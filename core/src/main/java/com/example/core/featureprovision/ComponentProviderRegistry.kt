package com.example.core.featureprovision

import com.example.core.scopes.FeatureScope

object ComponentProviderRegistry {
    private val factories = mutableMapOf<String, ScopedComponentFactory<*>>()

    fun <T> registerFactory(featureId: String, factory: ScopedComponentFactory<T>) {
        factories[featureId] = factory
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(featureId: String, scope: FeatureScope, componentType: Class<T>): T? {
        val factory = factories[featureId] as? ScopedComponentFactory<T>
        return when (scope) {
            FeatureScope.APP -> factory?.createAppScoped()
            FeatureScope.ACTIVITY -> factory?.createActivityScoped()
            FeatureScope.FRAGMENT -> factory?.createFragmentScoped()
            FeatureScope.VIEWMODEL -> factory?.createViewModelScoped()
        }
    }
}
