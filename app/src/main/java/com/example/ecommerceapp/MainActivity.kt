package com.example.ecommerceapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferencesRepositoryImpl
import com.example.ecommerceapp.ui.AuthActivity
import com.example.ecommerceapp.ui.user.UserViewModel
import com.example.ecommerceapp.ui.user.UserViewModelFactory
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserPreferencesRepositoryImpl(this@MainActivity))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Main) {
            val isLoggedIn = userViewModel.isUserLoggedIn().first()
            if (isLoggedIn) {
                setContentView(R.layout.activity_main)
            } else {
                userViewModel.setIsLoggedIn(userLoginState = false)
                goToAuthActivity()
            }
        }
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
            // Add a callback that's called when the splash screen is animating to the
            // app content.
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView, View.TRANSLATION_Y, 0f, -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 1000L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        } else {
            setTheme(R.style.Theme_Ecommerceapp)
        }
    }

}

