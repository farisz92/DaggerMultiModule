package com.example.core.featureprovision

interface ScopedComponentFactory<T> {
    fun createAppScoped(): T
    fun createActivityScoped(): T
    fun createFragmentScoped(): T
    fun createViewModelScoped(): T
}