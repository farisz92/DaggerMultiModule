package com.example.dagger.di

import com.example.api.interfaces.StepsViewModel
import com.example.core.di.prod.CoreComponent
import com.example.core.scopes.FeatureScope
import com.example.core.scopes.StepsScope
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
            coreComponent: CoreComponent,
            @BindsInstance scope: FeatureScope
        ): StepsComponent
    }
}

