package com.example.ecommerceapp.data.reposotiry.home.category

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.ui.dashboard.home.model.CategoryUIModel
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    fun getCategories(): Flow<Resource<List<CategoryUIModel>>>
}