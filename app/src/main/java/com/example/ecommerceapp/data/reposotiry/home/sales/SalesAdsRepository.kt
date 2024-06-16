package com.example.ecommerceapp.data.reposotiry.home.sales

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.sale_ads.SalesAdModel
import com.example.ecommerceapp.ui.dashboard.home.model.SalesAdUIModel
import kotlinx.coroutines.flow.Flow

interface SalesAdsRepository {
    fun getSalesAds(): Flow<Resource<List<SalesAdUIModel>>>
}