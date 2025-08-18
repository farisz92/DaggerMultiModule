package com.example.dagger.di

import android.util.Log
import com.example.api.interfaces.StepsViewModel
import com.example.core.StepsRepository
import com.example.core.StepsViewModelImpl
import com.example.core.di.FeatureScope
import dagger.Module
import dagger.Provides

@Module
class StepsModule {
    @Provides
    @StepsScope
    fun provideStepsRepository(scope: FeatureScope): StepsRepository {
        Log.d("StepsModule", "Creating repository for scope: $scope")
        return StepsRepository()
    }

    @Provides
    @StepsScope
    fun provideStepsViewModel(
        repository: StepsRepository,
        scope: FeatureScope
    ): StepsViewModel {
        Log.d("StepsModule", "Creating ViewModel for scope: $scope")
        return StepsViewModelImpl(repository)
    }
}