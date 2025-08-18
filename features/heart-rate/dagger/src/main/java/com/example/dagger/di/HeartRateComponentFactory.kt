package com.example.dagger.di

import com.example.core.di.FeatureScope
import com.example.core.di.prod.CoreComponent
import com.example.core.featureprovision.ScopedComponentFactory

class HeartRateComponentFactory(
    private val coreComponent: CoreComponent
) : ScopedComponentFactory<HeartRateComponent> {
    override fun createAppScoped(): HeartRateComponent {
        return DaggerHeartRateComponent.factory().create(coreComponent, FeatureScope.APP)
    }

    override fun createActivityScoped(): HeartRateComponent {
        return DaggerHeartRateComponent.factory().create(coreComponent,FeatureScope.ACTIVITY)
    }

    override fun createFragmentScoped(): HeartRateComponent {
        return DaggerHeartRateComponent.factory().create(coreComponent,FeatureScope.FRAGMENT)
    }

    override fun createViewModelScoped(): HeartRateComponent {
        return DaggerHeartRateComponent.factory().create(coreComponent,FeatureScope.VIEWMODEL)
    }
}
