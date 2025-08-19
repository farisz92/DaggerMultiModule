package com.example.features.heartrate.api.interfaces

interface HeartRateViewModel {
    val instanceId: String

    fun bpm() : Int
}