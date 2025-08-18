# DaggerMultiModule

//  package com.example.core.featureprovision

// import com.example.core.scopes.FeatureScope

// object ComponentProviderRegistry {
// private val factories = mutableMapOf<String, ScopedComponentFactory<*>>()

    // New: caches per featureId & scope
    private val appScopeCache = mutableMapOf<String, Any>()
    private val activityScopeCache = mutableMapOf<Any, MutableMap<String, Any>>() // scopeKey = activity
    private val fragmentScopeCache = mutableMapOf<Any, MutableMap<String, Any>>() // scopeKey = fragment
    private val viewModelScopeCache = mutableMapOf<Any, MutableMap<String, Any>>() // scopeKey = viewmodel

    fun <T> registerFactory(featureId: String, factory: ScopedComponentFactory<T>) {
        factories[featureId] = factory
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(featureId: String, scope: FeatureScope, scopeKey: Any?): T? {
        val factory = factories[featureId] as? ScopedComponentFactory<T> ?: return null

        return when (scope) {
            FeatureScope.APP -> appScopeCache.getOrPut(featureId) { factory.createAppScoped() as Any } as T
            FeatureScope.ACTIVITY -> {
                requireNotNull(scopeKey)
                val activityMap = activityScopeCache.getOrPut(scopeKey) { mutableMapOf() }
                activityMap.getOrPut(featureId) { factory.createActivityScoped() as Any } as T
            }
            FeatureScope.FRAGMENT -> {
                requireNotNull(scopeKey)
                val fragmentMap = fragmentScopeCache.getOrPut(scopeKey) { mutableMapOf() }
                fragmentMap.getOrPut(featureId) { factory.createFragmentScoped() as Any } as T
            }
            FeatureScope.VIEWMODEL -> {
                requireNotNull(scopeKey)
                val viewModelMap = viewModelScopeCache.getOrPut(scopeKey) { mutableMapOf() }
                viewModelMap.getOrPut(featureId) { factory.createViewModelScoped() as Any } as T
            }
        }
    }
}
