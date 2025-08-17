package com.example.core.scopes

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectFeature(val scope: FeatureScope = FeatureScope.APP)