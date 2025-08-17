package com.example.dagger.di

import android.util.Log
import com.example.api.interfaces.HeartRateViewModel
import com.example.core.HeartRateRepository
import com.example.core.HeartRateViewModelImpl
import com.example.core.scopes.FeatureScope
import com.example.core.scopes.HeartRateScope
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