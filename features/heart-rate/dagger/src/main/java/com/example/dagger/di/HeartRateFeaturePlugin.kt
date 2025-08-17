package com.example.dagger.di

import com.example.api.interfaces.HeartRateViewModel
import com.example.core.HeartRateRepository
import com.example.core.featureprovision.FeatureProvider
import com.example.core.featureprovision.LazyFeatureProvider

class HeartRateFeaturePlugin(private val heartRateComponentFactory: HeartRateComponent.Factory) :
    com.example.core.featureprovision.FeaturePlugin {
    override val featureId = "heart-rate"

    override fun getProviders(): Map<Class<*>, FeatureProvider<*>> {
        return mapOf(
            HeartRateViewModel::class.java to LazyFeatureProvider {
                heartRateComponentFactory.create().heartRateViewModel()
            },
            HeartRateRepository::class.java to LazyFeatureProvider {
                heartRateComponentFactory.create().heartRateRepository()
            }
        )
    }
}