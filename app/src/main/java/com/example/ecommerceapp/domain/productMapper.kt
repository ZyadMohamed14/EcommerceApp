package com.example.ecommerceapp.domain

import com.example.ecommerceapp.data.model.ProductModel
import com.example.ecommerceapp.ui.products.ProductUIModel

fun ProductUIModel.toProductModel(): ProductModel {
    return ProductModel(
        id = id,
        name = name,
        description = description,
        categoriesIDs = categoriesIDs,
        images = images,
        price = price,
        rate = rate,
        salePercentage = salePercentage,
        saleType = saleType,
        colors = colors
    )
}

fun ProductModel.toProductUIModel(): ProductUIModel {
    return ProductUIModel(
        id = id ?: throw IllegalArgumentException("Product ID is missing"),
        name = name ?: "No Name",
        description = description ?: "No Description",
        categoriesIDs = categoriesIDs ?: emptyList(),
        images = images ?: emptyList(),
        price = price ?: 0,
        rate = rate ?: 0f,
        salePercentage = salePercentage,
        saleType = saleType,
        colors = colors ?: emptyList()
    )
}