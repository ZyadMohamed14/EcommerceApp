package com.example.ecommerceapp.data.reposotiry.home.category

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.category.CategoryModel
import com.example.ecommerceapp.domain.toUIModel
import com.example.ecommerceapp.ui.dashboard.home.model.CategoryUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    CategoriesRepository {
    override fun getCategories(): Flow<Resource<List<CategoryUIModel>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val categories = firestore.collection("categories").get().await()
                    .toObjects(CategoryModel::class.java)
               // Log.d(TAG, "categories = ${categories}")

                // repeat categories item 10 times
              val repeatCategories = mutableListOf<CategoryModel>()
               repeat(10) {
                   repeatCategories.addAll(categories)
               }

                emit(Resource.Success(repeatCategories.map { it.toUIModel() }))
            } catch (e: Exception) {
               // Log.e(TAG, "getCategories: error", e)
                emit(Resource.Error(e))
            }
        }
    }
}