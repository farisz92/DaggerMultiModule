package com.example.core.lifecycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.core.di.FeatureInjectorProvider
import com.example.core.di.injectFeatures

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures() // Automatic injection
        onFeaturesInjected()
    }

    override fun onDestroy() {
        super.onDestroy()
        val injector = (requireActivity().application as? FeatureInjectorProvider)?.getFeatureInjector()
        injector?.clearFragmentScope(this)
    }

    // Called after features are injected
    protected open fun onFeaturesInjected() {}
}
