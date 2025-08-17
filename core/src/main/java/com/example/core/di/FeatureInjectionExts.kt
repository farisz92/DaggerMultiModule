package com.example.core.di

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

fun ComponentActivity.injectFeatures() {
    val injector = (application as? com.example.core.di.FeatureInjectorProvider)?.getFeatureInjector()
        ?: throw IllegalStateException("Application must implement FeatureInjectorProvider")
    injector.inject(this)
}

fun Fragment.injectFeatures() {
    val injector = (requireActivity().application as? com.example.core.di.FeatureInjectorProvider)?.getFeatureInjector()
        ?: throw IllegalStateException("Application must implement FeatureInjectorProvider")
    injector.inject(this)
}

fun ViewModel.injectFeatures(application: Application) {
    val injector = (application as? com.example.core.di.FeatureInjectorProvider)?.getFeatureInjector()
        ?: throw IllegalStateException("Application must implement FeatureInjectorProvider")
    injector.inject(this)
}
