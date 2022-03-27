package com.test.module.convert.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.databinding.RowItemBinding

class CurrencyAdapter constructor(
    private val items: List<Pair<String, Double>>
) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].apply {
            holder.binding.currency = "${this.first} - ${this.second}"
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(internal val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}