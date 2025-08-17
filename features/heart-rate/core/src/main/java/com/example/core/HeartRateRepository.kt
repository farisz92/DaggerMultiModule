package com.example.core

import javax.inject.Inject

class HeartRateRepository @Inject constructor() {
    fun getBpm() = (60..120).random()
}