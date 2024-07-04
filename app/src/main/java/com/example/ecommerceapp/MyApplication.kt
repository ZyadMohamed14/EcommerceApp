package com.example.ecommerceapp

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.example.ecommerceapp.databinding.ItemSalesAdBinding
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.schedulers.Schedulers
@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        listenToNetworkConnectivity()

    }

    @SuppressLint("CheckResult")
    fun listenToNetworkConnectivity() {
        ReactiveNetwork.observeInternetConnectivity().subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe { isConnected: Boolean ->
                Log.d(TAG, "Connected to internet: $isConnected")
                FirebaseCrashlytics.getInstance().setCustomKey("connected_to_internet", isConnected)
            }
    }
    companion object {
        private const val TAG = "MyApplication"
    }
}
