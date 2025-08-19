package com.example.core.featureprovision

import com.example.core.di.FeatureScope
import com.example.core.featureprovision.LazyFeatureDelegate.Companion.create
import kotlin.reflect.KClass

fun <T : Any> Any.lazyFeature(
    featureClass: KClass<T>,
    scope: FeatureScope,
    managerProvider: () -> ScopeAwareFeatureManager
): Lazy<T> = lazy {
    create(
        featureClass = featureClass,
        scope = scope,
        scopeKey = if (scope != FeatureScope.APP) this@lazyFeature else null,
        featureManager = managerProvider()
    ).get()
}
