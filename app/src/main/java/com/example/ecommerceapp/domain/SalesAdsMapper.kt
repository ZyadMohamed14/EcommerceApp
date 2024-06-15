package com.example.ecommerceapp.domain

import com.example.ecommerceapp.data.model.sale_ads.SalesAdModel
import com.example.ecommerceapp.ui.dashboard.home.model.SalesAdUIModel

fun SalesAdModel.toUIModel(): SalesAdUIModel {
    return SalesAdUIModel(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        type = this.type,
        productId = this.productId,
        categoryId = this.categoryId,
        externalLink = this.externalLink,
        endAt = this.endAt
    )
}