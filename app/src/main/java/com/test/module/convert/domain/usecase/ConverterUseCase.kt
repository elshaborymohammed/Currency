package com.test.module.convert.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.test.data.model.Resource
import com.test.module.convert.data.repository.ConverterRepository
import kotlinx.coroutines.Dispatchers
import java.net.UnknownHostException
import javax.inject.Inject

class ConverterUseCase @Inject constructor(
    private val repository: ConverterRepository,
) {
    fun rate(base: String, symbols: String): LiveData<Resource<Any>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            val run = repository.latest(base, symbols)
            run.body()?.also {
                if (it.success) {
                    emit(Resource.Success(data = it))
                } else {
                    emit(Resource.Error(error = it.error!!))
                }
            }
        } catch (e: Throwable) {
            emit(Resource.Failure(e))
        } catch (e: UnknownHostException) {
            emit(Resource.InternetConnectionFailure)
        }
    }
}