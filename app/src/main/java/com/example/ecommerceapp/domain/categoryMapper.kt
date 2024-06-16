package com.example.ecommerceapp.domain

import com.example.ecommerceapp.data.model.category.CategoryModel
import com.example.ecommerceapp.ui.dashboard.home.model.CategoryUIModel

fun CategoryModel.toUIModel(): CategoryUIModel {
    return CategoryUIModel(
        id = id, name = name, icon = icon
    )
}