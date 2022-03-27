package com.test.module.convert.ui.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.test.R
import com.test.data.model.Resource
import com.test.databinding.FragmentConvertBinding
import com.test.module.convert.domain.entity.LatestResponse
import com.test.module.convert.ui.viewmodel.ConvertViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ConvertFragment : Fragment() {
    private lateinit var dataBinding: FragmentConvertBinding
    private val convertViewModel: ConvertViewModel by viewModels()
    private lateinit var base: String
    private lateinit var rates: Map<String, Double>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.dataBinding = FragmentConvertBinding.inflate(inflater, container, false)
        this.dataBinding.lifecycleOwner = this
        return this.dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.apply {
            spFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    load()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            spTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    calculateFrom(edTo.text.toString().toDoubleOrNull() ?: 0.0)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            edFrom.setOnKeyListener { view, keyCode, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    calculateFrom(edFrom.text.toString().toDoubleOrNull() ?: 0.0)
                }
                false
            }

            edTo.setOnKeyListener { view, keyCode, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    calculateTo(edTo.text.toString().toDoubleOrNull() ?: 0.0)
                }
                false
            }

            swap.setOnClickListener {
                val from = spFrom.selectedItemPosition
                val to = spTo.selectedItemPosition

                spFrom.setSelection(to, true)
                spTo.setSelection(from, true)
            }

            details.setOnClickListener {
                findNavController().navigate(
                    R.id.action_convertFragment_to_otherCurrenciesFragment,
                    OtherCurrenciesFragmentArgs(spFrom.selectedItem.toString()).toBundle()
                )
            }
        }
    }

    private fun calculateFrom(fromValue: Double) {
        dataBinding.apply {
            if (this@ConvertFragment::rates.isInitialized) {
                edTo.setText((fromValue * (rates[spTo.selectedItem] ?: 1.0)).toString())
            }
        }
    }

    private fun calculateTo(toValue: Double) {
        dataBinding.apply {
            if (this@ConvertFragment::rates.isInitialized) {
                edFrom.setText((toValue / (rates[spTo.selectedItem] ?: 1.0)).toString())
            }
        }
    }

    fun load() = convertViewModel.rate(
        dataBinding.spFrom.selectedItem.toString(),
        requireView().resources.getStringArray(R.array.currencies).joinToString(",")
    ).observe(viewLifecycleOwner) {
        this.base = dataBinding.spFrom.selectedItem.toString()
        when (it) {
            is Resource.Success -> {
                dataBinding.progress.isVisible = false
                (it.data as LatestResponse).let { response ->
                    this.rates = response.rates
                    calculateFrom(dataBinding.edFrom.text.toString().toDoubleOrNull() ?: 0.0)
                }
            }
            is Resource.Error -> {
                dataBinding.progress.isVisible = false
                this.rates = emptyMap()
                Toast.makeText(requireContext(), it.error.type, Toast.LENGTH_SHORT)
                    .show()
            }
            is Resource.Failure -> {
                dataBinding.progress.isVisible = false
                this.rates = emptyMap()
                Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT)
                    .show()
            }
            is Resource.InternetConnectionFailure -> {
                dataBinding.progress.isVisible = false
                Toast.makeText(requireContext(), "Lost Internet Connection ", Toast.LENGTH_SHORT)
                    .show()
            }
            is Resource.Loading -> {
                this.rates = emptyMap()
                dataBinding.progress.isVisible = true
            }
            is Resource.Void -> {
                this.rates = emptyMap()
                dataBinding.progress.isVisible = false
            }
        }
        Timber.d(it.toString())
    }
}