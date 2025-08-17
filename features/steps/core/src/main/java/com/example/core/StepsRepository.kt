package com.example.core

import javax.inject.Inject

class StepsRepository @Inject constructor() {
    fun getSteps() = (1000..5000).random()
}