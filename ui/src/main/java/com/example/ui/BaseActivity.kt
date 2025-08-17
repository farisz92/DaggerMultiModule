package com.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.core.di.injectFeatures

abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()

        onFeaturesInjected()
    }

    protected open fun onFeaturesInjected() {

    }
}
