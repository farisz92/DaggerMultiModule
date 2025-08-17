package com.example.app.main

import android.app.Application
import android.util.Log
import com.example.app.di.AppComponent
import com.example.app.di.DaggerAppComponent
import com.example.app.di.EnterpriseFeatureManager
import com.example.core.di.FeatureInjector
import com.example.core.di.FeatureInjectorProvider
import com.example.core.di.prod.CoreComponent
import com.example.core.di.prod.CoreProvider
import com.example.core.di.prod.DaggerCoreComponent
import com.example.core.featureprovision.FeatureManager

class MyApplication : Application(), FeatureInjectorProvider {
    lateinit var appComponent: AppComponent
    private lateinit var featureManager: FeatureManager

    override fun onCreate() {
        super.onCreate()

        Log.d("MyApplication", "onCreate - creating components")

        try {
            val core: CoreComponent = DaggerCoreComponent.create()
            CoreProvider.init(core)

            appComponent = DaggerAppComponent.builder()
                .coreComponent(core)
                .build()

            // Use new enterprise manager
            featureManager = EnterpriseFeatureManager(appComponent)

            Log.d("MyApplication", "onCreate - components created successfully")
        } catch (e: Exception) {
            Log.e("MyApplication", "Failed to create components", e)
            throw e
        }
    }

    override fun getFeatureInjector(): FeatureInjector = object : FeatureInjector {
        override fun inject(target: Any) = featureManager.inject(target)
    }
}
