//package com.test.module.spot.ui.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import androidx.recyclerview.widget.RecyclerView
//import com.test.module.spot.domain.entity.Convert
//
//class SpotAdapter(
//    private val data: List<Convert>,
//    private val onDisabledClickListener: ((spot: Convert) -> Unit) = {}
//) : RecyclerView.Adapter<SpotAdapter.ViewHolder>() {
//
//    override fun getItemCount(): Int = data.size ?: 0
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            RowSpotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//    }
//
//    inner class ViewHolder(inflate: ViewDataBinding) : RecyclerView.ViewHolder(inflate.root) {
//
//    }
//}