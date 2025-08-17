package com.example.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.core.di.injectFeatures

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures() // Automatic injection
        onFeaturesInjected()
    }

    // Called after features are injected
    protected open fun onFeaturesInjected() {}
}
