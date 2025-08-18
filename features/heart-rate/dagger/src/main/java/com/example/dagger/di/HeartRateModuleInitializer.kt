package com.example.dagger.di

import com.example.core.di.prod.CoreComponent

class HeartRateModuleInitializer(coreComponent: com.example.core.di.prod.CoreComponent) {
    init {
        HeartRateFeaturePlugin(coreComponent)
    }
}