package com.example.dagger.di

import com.example.core.di.prod.CoreComponent

class StepsModuleInitializer(coreComponent: CoreComponent) {
    init {
        StepsFeaturePlugin(coreComponent)
    }
}