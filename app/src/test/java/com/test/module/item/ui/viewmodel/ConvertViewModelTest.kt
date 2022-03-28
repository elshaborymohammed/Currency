package com.test.module.item.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import com.google.gson.reflect.TypeToken
import com.test.base.TestHelper
import com.test.base.captureValues
import com.test.data.model.Resource
import com.test.module.convert.domain.entity.LatestResponse
import com.test.module.convert.ui.viewmodel.ConvertViewModel
import com.test.rule.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConvertViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var viewModel: ConvertViewModel
    lateinit var mock: LatestResponse

    @Before
    fun setup() {
        mock = setUpGetSuccess()
    }

    private fun setUpGetSuccess(): LatestResponse {
        val response: LatestResponse = TestHelper.loadJson(
            TestHelper.MOCK_DATA_PATH,
            TypeToken.getParameterized(LatestResponse::class.java).type
        )

        Mockito.`when`(viewModel.rate("", "")).thenReturn(
            MutableLiveData(Resource.Success(response))
        )

        return response
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getList() {
        coroutineScope.runBlockingTest {
            viewModel.rate("", "").captureValues {
                coroutineScope.advanceTimeBy(1000)
                Truth.assertThat(value).isEqualTo(Resource.Success(mock))
            }
        }
    }
}