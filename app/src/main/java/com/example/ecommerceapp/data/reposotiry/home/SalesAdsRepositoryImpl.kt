package com.example.ecommerceapp.data.reposotiry.home

import android.util.Log
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.sale_ads.SalesAdModel
import com.example.ecommerceapp.domain.toUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SalesAdsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SalesAdsRepository {
    val TAG = "HomeFragment"
    override  fun getSalesAds() = flow {
        try {
            emit(Resource.Loading())
            val salesAds =
                firestore.collection("sales_ads")
                    .get().await().toObjects(SalesAdModel::class.java)
            Log.d(TAG, "getSalesAds: ${salesAds.get(0).id}")

            emit(Resource.Success(salesAds.map { it.toUIModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }


}