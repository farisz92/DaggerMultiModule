package com.example.dagger.di

import com.example.api.interfaces.StepsViewModel
import com.example.core.StepsRepository
import com.example.core.featureprovision.FeatureProvider
import com.example.core.featureprovision.LazyFeatureProvider

class StepsFeaturePlugin(private val stepsComponentFactory: StepsComponent.Factory) :
    com.example.core.featureprovision.FeaturePlugin {
    override val featureId = "steps"

    override fun getProviders(): Map<Class<*>, FeatureProvider<*>> {
        return mapOf(
            StepsViewModel::class.java to LazyFeatureProvider {
                stepsComponentFactory.create().stepsViewModel()
            },
            StepsRepository::class.java to LazyFeatureProvider {
                stepsComponentFactory.create().stepsRepository()
            }
        )
    }
}