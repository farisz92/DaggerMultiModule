package com.example.dagger.di

import com.example.api.interfaces.StepsViewModel
import com.example.core.StepsRepository
import com.example.core.scopes.StepsScope
import dagger.Subcomponent

@StepsScope
@Subcomponent(modules = [StepsModule::class])
interface StepsComponent {
    fun stepsViewModel(): StepsViewModel
    fun stepsRepository(): StepsRepository

    @Subcomponent.Factory
    interface Factory {
        fun create(): StepsComponent
    }
}
