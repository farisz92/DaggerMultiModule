package com.example.core.featureprovision

import android.util.Log
import com.example.core.di.FeatureScope
import kotlin.also
import kotlin.jvm.java
import kotlin.reflect.KClass
import kotlin.run

class LazyFeatureDelegate<T : Any>(
    private val scope: FeatureScope,
    private val scopeKey: Any?,
    private val featureManager: ScopeAwareFeatureManager,
    private val featureClass: Class<T>
) {
    companion object {
        inline fun <reified T : Any> create(
            scope: FeatureScope,
            scopeKey: Any?,
            featureManager: ScopeAwareFeatureManager
        ): LazyFeatureDelegate<T> {
            return LazyFeatureDelegate(scope, scopeKey, featureManager, T::class.java)
        }

        fun <T : Any> create(
            featureClass: KClass<T>,
            scope: FeatureScope,
            scopeKey: Any?,
            featureManager: ScopeAwareFeatureManager
        ): LazyFeatureDelegate<T> {
            return LazyFeatureDelegate(scope, scopeKey, featureManager, featureClass.java)
        }
    }

    private var cached: T? = null
    private val lock = Any()

    fun get(): T {
        val cachedValue = cached
        if (cachedValue != null) {
            return cachedValue
        }

        return synchronized(lock) {
            cached ?: run {
                Log.d("LazyFeature", "Creating feature: ${featureClass.simpleName}")
                (featureManager.getFeature(featureClass, scope, scopeKey)
                    ?: throw kotlin.IllegalStateException("Feature ${featureClass.simpleName} not found"))
                    .also { cached = it }
            }
        }
    }
}