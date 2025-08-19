package com.example.features.heartrate.di

import android.util.Log
import com.example.features.heartrate.api.interfaces.HeartRateViewModel
import com.example.features.heartrate.core.HeartRateRepository
import com.example.features.heartrate.core.HeartRateViewModelImpl
import com.example.core.di.FeatureScope
import dagger.Module
import dagger.Provides

@Module
class HeartRateModule {
    @Provides
    @HeartRateScope
    fun provideHeartRateRepository(scope: FeatureScope): HeartRateRepository {
        Log.d("HeartRateModule", "Creating repository for scope: $scope")
        return HeartRateRepository()
    }

    @Provides
    @HeartRateScope
    fun provideHeartRateViewModel(
        repository: HeartRateRepository,
        scope: FeatureScope
    ): HeartRateViewModel {
        Log.d("HeartRateModule", "Creating ViewModel for scope: $scope")
        return HeartRateViewModelImpl(repository)
    }
}