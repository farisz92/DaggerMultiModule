package com.example.core.lifecycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.core.di.FeatureInjectorProvider
import com.example.core.di.injectFeatures
import com.example.core.featureprovision.ScopeAwareFeatureManager

abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()

        onFeaturesInjected()
    }

    override fun onDestroy() {
        super.onDestroy()
        val injector = (application as? FeatureInjectorProvider)?.getFeatureInjector()
        injector?.clearActivityScope(this)
    }

    protected fun getFeatureManager(): ScopeAwareFeatureManager {
        val manager = (application as? FeatureInjectorProvider)?.getFeatureInjector()?.getFeatureManager()
            ?: error("Application must implement FeatureInjectionAware")
        return manager
    }

    protected open fun onFeaturesInjected() {

    }
}
