package com.example.dagger.di

import com.example.api.interfaces.HeartRateViewModel
import com.example.core.HeartRateRepository
import com.example.core.scopes.HeartRateScope
import dagger.Subcomponent

@HeartRateScope
@Subcomponent(modules = [HeartRateModule::class])
interface HeartRateComponent {
    fun heartRateViewModel(): HeartRateViewModel
    fun heartRateRepository(): HeartRateRepository

    @Subcomponent.Factory
    interface Factory {
        fun create(): HeartRateComponent
    }
}
