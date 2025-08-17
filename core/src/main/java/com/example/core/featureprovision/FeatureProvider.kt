package com.example.core.featureprovision

interface FeatureProvider<T> {
    fun provide(): T
    fun canProvide(): Boolean = true
}