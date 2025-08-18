package com.example.core

import com.example.api.interfaces.StepsViewModel
import java.util.UUID
import javax.inject.Inject

class StepsViewModelImpl @Inject constructor(
    private val repository: StepsRepository
): StepsViewModel {
    private val _instanceId: String = UUID.randomUUID().toString()
    override val instanceId: String
        get() = _instanceId

    override fun steps() = repository.getSteps()
}