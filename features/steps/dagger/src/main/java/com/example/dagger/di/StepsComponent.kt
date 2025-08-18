package com.example.dagger.di

import com.example.api.interfaces.StepsViewModel
import com.example.core.di.FeatureScope
import com.example.core.di.prod.CoreComponent
import dagger.BindsInstance
import dagger.Component

@StepsScope
@Component(
    modules = [StepsModule::class],
    dependencies = [CoreComponent::class]
)
interface StepsComponent {
    fun stepsViewModel(): StepsViewModel

    @Component.Factory
    interface Factory {
        fun create(
            coreComponent: com.example.core.di.prod.CoreComponent,
            @BindsInstance scope: FeatureScope
        ): StepsComponent
    }
}

