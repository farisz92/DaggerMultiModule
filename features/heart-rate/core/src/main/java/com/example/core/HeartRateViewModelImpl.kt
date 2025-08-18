package com.example.core

import com.example.api.interfaces.HeartRateViewModel
import java.util.UUID
import javax.inject.Inject

class HeartRateViewModelImpl @Inject constructor(
    private val repository: HeartRateRepository
): HeartRateViewModel {
    private val _instanceId: String = UUID.randomUUID().toString()
    override val instanceId: String
        get() = _instanceId

    override fun bpm() = repository.getBpm()
}