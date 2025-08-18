package com.example.dagger.di

import com.example.core.di.prod.CoreComponent

class StepsModuleInitializer(coreComponent: com.example.core.di.prod.CoreComponent) {
    init {
        StepsFeaturePlugin(coreComponent)
    }
}