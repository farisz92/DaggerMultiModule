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
import com.example.core.di.prod.CoreComponent
import com.example.core.di.prod.CoreProvider
import com.example.core.di.prod.DaggerCoreComponent
import com.example.core.featureprovision.ScopeAwareFeatureManager
import com.example.core.featureprovision.SelfRegisteringFeaturePlugin
import com.example.core.scopes.FeatureScope
import com.example.dagger.di.DaggerStepsComponent
import com.example.dagger.di.HeartRateComponent
import com.example.dagger.di.HeartRateFeaturePlugin
import com.example.dagger.di.HeartRateModuleInitializer
import com.example.dagger.di.StepsComponent
import com.example.dagger.di.StepsFeaturePlugin
import com.example.dagger.di.StepsModuleInitializer

class MyApplication : Application(), FeatureInjectorProvider {
    lateinit var appComponent: AppComponent

    private lateinit var scopeAwareFeatureManager: ScopeAwareFeatureManager

    override fun onCreate() {
        super.onCreate()

        Log.d("MyApplication", "onCreate - creating components")

        try {
            val core: CoreComponent = DaggerCoreComponent.create()
            CoreProvider.init(core)

            StepsModuleInitializer(core)
            HeartRateModuleInitializer(core)

            appComponent = DaggerAppComponent.builder()
                .coreComponent(core)
                .build()

            // Use new enterprise manager
            scopeAwareFeatureManager = EnterpriseScopeAwareFeatureManager()

            Log.d("MyApplication", "onCreate - components created successfully")
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
    }
}
