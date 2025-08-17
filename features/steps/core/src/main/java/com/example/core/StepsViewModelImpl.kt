package com.example.core

import com.example.api.interfaces.StepsViewModel
import javax.inject.Inject

class StepsViewModelImpl @Inject constructor(
    private val repository: StepsRepository
): StepsViewModel {
    override fun steps() = repository.getSteps()
}