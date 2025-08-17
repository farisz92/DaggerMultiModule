package com.example.core

import com.example.api.interfaces.HeartRateViewModel
import javax.inject.Inject

class HeartRateViewModelImpl @Inject constructor(
    private val repository: HeartRateRepository
): HeartRateViewModel {
    override fun bpm() = repository.getBpm()
}