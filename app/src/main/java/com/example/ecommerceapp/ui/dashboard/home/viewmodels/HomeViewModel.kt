package com.example.ecommerceapp.ui.dashboard.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.sale_ads.SalesAdModel
import com.example.ecommerceapp.data.reposotiry.home.SalesAdsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    salesAdsRepository: SalesAdsRepository
) : ViewModel() {


    val salesAdsState = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    fun startTimer() {
        salesAdsState.value.data?.forEach { it.startCountdown() }
    }
    fun stopTimer() {
        salesAdsState.value.data?.forEach { it.stopCountdown() }
    }
}
