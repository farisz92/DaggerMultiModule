package com.example.core.scopes

import com.example.core.di.FeatureScope

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectFeature(val scope: FeatureScope = FeatureScope.APP)