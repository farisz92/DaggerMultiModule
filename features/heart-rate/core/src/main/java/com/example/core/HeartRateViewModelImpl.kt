package com.example.core

import android.util.Log
import com.example.api.interfaces.HeartRateViewModel
import java.util.UUID
import javax.inject.Inject

class HeartRateViewModelImpl @Inject constructor(
    private val repository: HeartRateRepository
): HeartRateViewModel {
    private val _instanceId: String = "Faris"
    override val instanceId: String
        get() = _instanceId

    override fun bpm() = repository.getBpm()

    init {
        Log.d("HeartRateVM", "Leviathan - HeartRateViewModel initialized")
    }
}