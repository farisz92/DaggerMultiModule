package com.example.core.featureprovision

data class LazyFeatureProvider<T>(
    private val factory: () -> T
) {
    private var instance: T? = null

    fun provide(): T {
        return instance ?: factory().also { instance = it }
    }
}
