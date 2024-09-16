package com.example.ecommerceapp.ui.auth.country

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.reposotiry.auth.country.CountryRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepository
import com.example.ecommerceapp.domain.toUIModel
import com.example.ecommerceapp.ui.auth.country.model.CountryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countriesRepository: CountryRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : ViewModel() {

    private val countriesState = countriesRepository.getCountries().stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val countriesUIModelState = countriesState.mapLatest { countries ->
        countries.map { country ->

            Log.d("CountriesViewModel", "countriesUIModelState: $country")
            country.toUIModel()
        }
    }

    fun saveUserCountry(country: CountryUIModel) {
        viewModelScope.launch {
            userPreferenceRepository.saveUserCountry(country)
        }
    }
}