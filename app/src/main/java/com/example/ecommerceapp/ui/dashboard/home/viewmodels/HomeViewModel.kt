package com.example.ecommerceapp.ui.dashboard.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.reposotiry.home.category.CategoriesRepository
import com.example.ecommerceapp.data.reposotiry.home.sales.SalesAdsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    salesAdsRepository: SalesAdsRepository,
    private val categoriesRepository: CategoriesRepository,
) : ViewModel() {


    val salesAdsState = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )
    val categoriesState = categoriesRepository.getCategories().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )
//    val countryState = userPreferenceRepository.getUserCountry().stateIn(
//        viewModelScope + IO,
//        started = SharingStarted.Eagerly,
//        initialValue = CountryData.getDefaultInstance()
//    )

    fun startTimer() {
        salesAdsState.value.data?.forEach { it.startCountdown() }
    }
    fun stopTimer() {
        salesAdsState.value.data?.forEach { it.stopCountdown() }
    }
}
