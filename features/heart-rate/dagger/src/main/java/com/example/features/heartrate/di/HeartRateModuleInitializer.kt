package com.example.features.heartrate.di

import com.example.core.di.prod.CoreComponent

class HeartRateModuleInitializer(coreComponent: CoreComponent) {
    init {
        HeartRateFeaturePlugin(coreComponent)
    }
}