package com.example.core.di

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.example.core.featureprovision.ScopeAwareFeatureManager

interface FeatureInjector {
    fun inject(target: Any)
    fun clearActivityScope(activity: ComponentActivity)
    fun clearFragmentScope(fragment: Fragment)
    fun getFeatureManager(): ScopeAwareFeatureManager
}