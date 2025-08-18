package com.example.dagger.di

import com.example.api.interfaces.HeartRateViewModel
import com.example.core.di.FeatureScope
import com.example.core.di.prod.CoreComponent
import dagger.BindsInstance
import dagger.Component

@HeartRateScope
@Component(
    modules = [HeartRateModule::class],
    dependencies = [CoreComponent::class]
)
interface HeartRateComponent {
    fun heartRateViewModel(): HeartRateViewModel

    @Component.Factory
    interface Factory {
        fun create(
            coreComponent: CoreComponent,
            @BindsInstance scope: FeatureScope
        ): HeartRateComponent
    }
}
