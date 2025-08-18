package com.example.core.di.test

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreTestModule::class])
interface CoreTestComponent
