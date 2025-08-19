package com.example.app.main

import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.example.app.di.AppComponent
import com.example.app.di.DaggerAppComponent
import com.example.app.di.EnterpriseScopeAwareFeatureManager
import com.example.core.di.FeatureInjector
import com.example.core.di.FeatureInjectorProvider
import com.example.core.di.FeatureScope
import com.example.core.di.prod.CoreComponent
import com.example.core.di.prod.CoreProvider
import com.example.core.di.prod.DaggerCoreComponent
import com.example.core.featureprovision.ScopeAwareFeatureManager
import com.example.dagger.di.HeartRateModuleInitializer
import com.example.dagger.di.StepsModuleInitializer

class MyApplication : Application(), FeatureInjectorProvider {
    lateinit var appComponent: AppComponent

    private lateinit var scopeAwareFeatureManager: ScopeAwareFeatureManager

    override fun onCreate() {
        super.onCreate()
        logMemoryUsage("App Startup")

        try {
            val core: CoreComponent = DaggerCoreComponent.create()
            CoreProvider.init(core)
            logMemoryUsage("After core component")

            appComponent = DaggerAppComponent.builder()
                .coreComponent(core)
                .build()

            logMemoryUsage("After app component")

            loadFeatures(core)

            // Use new enterprise manager
            scopeAwareFeatureManager = EnterpriseScopeAwareFeatureManager()

            logMemoryUsage("FeatureManager created")
        } catch (e: Exception) {
            Log.e("MyApplication", "Failed to create components", e)
            throw e
        }
    }

    override fun getFeatureInjector(): FeatureInjector = object : FeatureInjector {
        override fun inject(target: Any) = scopeAwareFeatureManager.inject(target)
        override fun clearActivityScope(activity: ComponentActivity) {
            scopeAwareFeatureManager.clearScope(FeatureScope.ACTIVITY, activity)
        }
        override fun clearFragmentScope(fragment: Fragment) {
            scopeAwareFeatureManager.clearScope(FeatureScope.FRAGMENT, fragment)
        }
        override fun getFeatureManager(): ScopeAwareFeatureManager = scopeAwareFeatureManager
    }

    private fun loadFeatures(coreComponent: CoreComponent) {
        logMemoryUsage("Loading Features")
        loadStepsFeature(coreComponent)
        loadHeartRateFeature(coreComponent)
        logMemoryUsage("Features Loaded")
    }

    private fun loadStepsFeature(coreComponent: CoreComponent) {
        StepsModuleInitializer(coreComponent)
        logMemoryUsage("Steps Loaded")
    }

    private fun loadHeartRateFeature(coreComponent: CoreComponent) {
        HeartRateModuleInitializer(coreComponent)
        logMemoryUsage("HearRate Loaded")
    }

    private fun logMemoryUsage(tag: String) {
        val runtime = Runtime.getRuntime()
        val usedMemoryKB = (runtime.totalMemory() - runtime.freeMemory()) / 1024
        Log.d("Memory", "$tag - Used memory: ${usedMemoryKB}KB")
    }
}
