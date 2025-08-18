package com.example.core.featureprovision

abstract class SelfRegisteringFeaturePlugin: ScopeAwareFeaturePlugin {
    companion object {
        private val registry = mutableListOf<ScopeAwareFeaturePlugin>()

        fun registerPlugin(plugin: ScopeAwareFeaturePlugin) {
            registry.add(plugin)
        }

        fun getAllPlugins(): List<ScopeAwareFeaturePlugin> = registry.toList()
    }

    init {
        registerPlugin(this)
    }
}