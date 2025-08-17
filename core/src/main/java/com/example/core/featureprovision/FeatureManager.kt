package com.example.core.featureprovision

interface FeatureManager {
    fun <T> getFeature(clazz: Class<T>): T?
    fun inject(target: Any)
}