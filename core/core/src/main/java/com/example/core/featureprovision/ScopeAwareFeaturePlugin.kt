package com.example.core.featureprovision

import com.example.core.di.FeatureScope

interface ScopeAwareFeaturePlugin : FeaturePlugin {
    fun getSupportedScopes(): Set<FeatureScope>

    fun createScopedProviders(
        scope: FeatureScope,
        scopeKey: Any?
    ): Map<Class<*>, LazyFeatureProvider<*>>
}