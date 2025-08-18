package com.example.core.di.prod

object CoreProvider {
    private lateinit var coreComponent: CoreComponent

    fun init(core: CoreComponent) {
        coreComponent = core
    }
}