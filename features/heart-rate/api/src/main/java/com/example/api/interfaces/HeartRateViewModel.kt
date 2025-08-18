package com.example.api.interfaces

interface HeartRateViewModel {
    val instanceId: String

    fun bpm() : Int
}