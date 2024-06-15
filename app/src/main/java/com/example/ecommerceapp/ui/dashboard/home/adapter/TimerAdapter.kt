package com.example.ecommerceapp.ui.dashboard.home.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object TimerAdapter{
    @JvmStatic
    @BindingAdapter("countdownTimer", "lifecycleScope")
    fun timerChanges(
        view: TextView,
        timerState: MutableStateFlow<String>?,
        lifecycleScope: LifecycleCoroutineScope?
    ) {
        lifecycleScope?.launch {
            timerState?.collectLatest {
                view.text = it
            }
        }
    }
}