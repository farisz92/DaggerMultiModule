package com.example.core

import android.util.Log
import javax.inject.Inject

class HeartRateRepository @Inject constructor() {
    fun getBpm() = (60..120).random()

    init {
        Log.d("HeartRateRepository", "Leviathan - HeartRateRepository initialized")
    }
}