package com.example.app.di

import com.example.core.di.prod.CoreComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class],
    dependencies = [com.example.core.di.prod.CoreComponent::class],
)

interface AppComponent
