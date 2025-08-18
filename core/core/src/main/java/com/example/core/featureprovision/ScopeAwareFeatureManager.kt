package com.example.core.featureprovision

import com.example.core.di.FeatureScope

interface ScopeAwareFeatureManager {
    fun <T> getFeature(clazz: Class<T>, scope: FeatureScope, scopeKey: Any? = null): T?
    fun inject(target: Any)
    fun clearScope(scope: FeatureScope, scopeKey: Any)
}