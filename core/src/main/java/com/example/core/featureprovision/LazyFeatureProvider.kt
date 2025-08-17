package com.example.core.featureprovision

data class LazyFeatureProvider<T>(
    private val factory: () -> T
) : FeatureProvider<T> {
    private var instance: T? = null

    override fun provide(): T {
        return instance ?: factory().also { instance = it }
    }
}
