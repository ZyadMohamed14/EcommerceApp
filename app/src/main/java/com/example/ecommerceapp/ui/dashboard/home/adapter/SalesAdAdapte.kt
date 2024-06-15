package com.example.ecommerceapp.ui.dashboard.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ItemSalesAdBinding
import com.example.ecommerceapp.ui.dashboard.home.model.SalesAdUIModel
import com.example.ecommerceapp.utils.CountdownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SalesAdAdapter(
    private val lifecycleScope: LifecycleCoroutineScope,
    private val salesAds: List<SalesAdUIModel>
) : RecyclerView.Adapter<SalesAdAdapter.SalesAdViewHolder>() {
    val TAG = "HomeFragment"

    inner class SalesAdViewHolder(private val binding: ItemSalesAdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(salesAd: SalesAdUIModel) {


            salesAd.startCountdown()
            binding.lifecycleScope = lifecycleScope
            binding.salesAd = salesAd
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesAdViewHolder {
        val binding = ItemSalesAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SalesAdViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SalesAdViewHolder, position: Int) {
        Log.d(TAG, "SalesAdUIModel${salesAds[position].id}")
        holder.bind(salesAds[position])
    }

    override fun getItemCount(): Int = salesAds.size



}
