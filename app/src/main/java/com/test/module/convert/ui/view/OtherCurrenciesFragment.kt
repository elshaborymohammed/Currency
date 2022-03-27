package com.test.module.convert.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.R
import com.test.data.model.Resource
import com.test.databinding.FragmentListBinding
import com.test.module.convert.domain.entity.LatestResponse
import com.test.module.convert.ui.adapter.CurrencyAdapter
import com.test.module.convert.ui.viewmodel.ConvertViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OtherCurrenciesFragment : Fragment() {
    private lateinit var dataBinding: FragmentListBinding
    private val convertViewModel: ConvertViewModel by viewModels()
    private val args: OtherCurrenciesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.dataBinding = FragmentListBinding.inflate(inflater, container, false)
        this.dataBinding.lifecycleOwner = this
        return this.dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        load()
    }

    fun load() = convertViewModel.rate(
        args.base,
        requireView().resources.getStringArray(R.array.currencies).joinToString(",")
    ).observe(viewLifecycleOwner) {

        when (it) {
            is Resource.Success -> {
                dataBinding.progress.isVisible = false
                (it.data as LatestResponse).let { response ->
                    dataBinding.recyclerView.adapter =
                        CurrencyAdapter(response.rates.map { Pair(it.key, it.value) })
                }
            }
            is Resource.Error -> {
                dataBinding.progress.isVisible = false
                Toast.makeText(requireContext(), it.error.type, Toast.LENGTH_SHORT)
                    .show()
            }
            is Resource.Failure -> {
                dataBinding.progress.isVisible = false
                Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT)
                    .show()
            }
            is Resource.InternetConnectionFailure -> {
                dataBinding.progress.isVisible = false
                Toast.makeText(requireContext(), "Lost Internet Connection ", Toast.LENGTH_SHORT)
                    .show()
            }
            is Resource.Loading -> {
                dataBinding.progress.isVisible = true
            }
            is Resource.Void -> {
                dataBinding.progress.isVisible = false
            }
        }
        Timber.d(it.toString())
    }
}