package com.example.dagger.di

import com.example.core.di.FeatureScope
import com.example.core.featureprovision.ScopedComponentFactory

class StepsComponentFactory(
    private val coreComponent: com.example.core.di.prod.CoreComponent
) : ScopedComponentFactory<StepsComponent> {
    override fun createAppScoped(): StepsComponent =
        DaggerStepsComponent.factory().create(coreComponent, FeatureScope.APP)

    override fun createActivityScoped(): StepsComponent =
        DaggerStepsComponent.factory().create(coreComponent, FeatureScope.ACTIVITY)

    override fun createFragmentScoped(): StepsComponent =
        DaggerStepsComponent.factory().create(coreComponent, FeatureScope.FRAGMENT)

    override fun createViewModelScoped(): StepsComponent =
        DaggerStepsComponent.factory().create(coreComponent, FeatureScope.VIEWMODEL)
}
