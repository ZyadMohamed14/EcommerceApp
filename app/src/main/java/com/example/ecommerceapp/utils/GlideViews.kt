package com.example.ecommerceapp.utils

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.ecommerceapp.R

object GlideViews {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        val loading = getGlideCircleLoading(view)

        imageUrl?.let {
            Glide.with(view.context)
                .load(it)
                .placeholder(R.drawable.loadinganimation)
                // Consider removing caching for dynamic images or use a more specific caching strategy
                // .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(view)
        }
    }
  //  @JvmStatic
  @BindingAdapter("normalImageUrl")
  @JvmStatic
  fun categoryUrl(view: ImageView, imageUrl: String?) {
      val loading = getGlideCircleLoading(view)

      Glide.with(view.context).load(imageUrl).placeholder(loading)
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .transform(CenterInside()).into(view)
  }

    @BindingAdapter("circleImageUrl")
    @JvmStatic
    fun circleNormalImage(view: ImageView, imageUrl: String?) {
        val loading = getGlideCircleLoading(view)
        Glide.with(view.context).load(imageUrl).placeholder(loading)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(CircleCrop()).into(view)
    }
    fun getGlideCircleLoading(view: ImageView): CircularProgressDrawable {
        return CircularProgressDrawable(view.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            setColorSchemeColors(ContextCompat.getColor(view.context, R.color.primary_color))
            start()
        }
    }
}





