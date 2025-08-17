package com.example.dagger.di

import android.util.Log
import com.example.api.interfaces.StepsViewModel
import com.example.core.StepsRepository
import com.example.core.StepsViewModelImpl
import com.example.core.scopes.StepsScope
import dagger.Module
import dagger.Provides

@Module
class StepsModule {
    @Provides
    @StepsScope
    fun provideStepsRepository(): StepsRepository {
        Log.d("StepsModule", "Creating StepsRepository")
        return StepsRepository()
    }

    @Provides
    @StepsScope
    fun provideStepsViewModel(
        stepsRepository: StepsRepository
    ): StepsViewModel {
        Log.d("StepsModule", "Creating StepsViewModel")
        return StepsViewModelImpl(stepsRepository)
    }
}