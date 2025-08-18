package com.example.core.featureprovision

interface FeaturePlugin {
    val featureId: String

    fun getProviders(): Map<Class<*>, LazyFeatureProvider<*>>
    fun initialize() {}
}