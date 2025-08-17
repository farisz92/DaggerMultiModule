package com.example.dagger.di

import com.example.api.interfaces.HeartRateViewModel
import com.example.core.HeartRateRepository
import com.example.core.HeartRateViewModelImpl
import com.example.core.scopes.HeartRateScope
import dagger.Module
import dagger.Provides

@Module
class HeartRateModule {
    @Provides
    @HeartRateScope
    fun providesHeartRateRepository(): HeartRateRepository = HeartRateRepository()

    @Provides
    @HeartRateScope
    fun provideHeartRateViewModel(
        hearRateRepository: HeartRateRepository
    ): HeartRateViewModel = HeartRateViewModelImpl(hearRateRepository)
}