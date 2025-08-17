package com.example.app.di

import com.example.core.di.prod.CoreComponent
import com.example.dagger.di.HeartRateComponent
import com.example.dagger.di.StepsComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class],
    dependencies = [CoreComponent::class]
)

interface AppComponent {
    fun stepsComponent(): StepsComponent.Factory
    fun heartRateComponent(): HeartRateComponent.Factory
}