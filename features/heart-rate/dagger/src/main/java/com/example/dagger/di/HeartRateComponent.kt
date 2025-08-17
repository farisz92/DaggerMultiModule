package com.example.dagger.di

import com.example.api.interfaces.HeartRateViewModel
import com.example.core.di.prod.CoreComponent
import com.example.core.scopes.FeatureScope
import com.example.core.scopes.HeartRateScope
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

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
