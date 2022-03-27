package com.test.module.convert.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.test.module.convert.domain.usecase.ConverterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val useCase: ConverterUseCase
) : ViewModel() {

    fun rate(base: String, symbols: String) = useCase.rate(base, symbols)
}