package com.example.ecommerceapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.databinding.ActivityMainBinding
import com.example.ecommerceapp.ui.dashboard.account.AccountFragment
import com.example.ecommerceapp.ui.auth.AuthActivity
import com.example.ecommerceapp.ui.auth.usermodel.UserViewModel
import com.example.ecommerceapp.ui.dashboard.cart.CartFragment
import com.example.ecommerceapp.ui.dashboard.explor.ExploreFragment
import com.example.ecommerceapp.ui.dashboard.home.adapter.HomeViewPagerAdapter
import com.example.ecommerceapp.ui.dashboard.home.fragments.HomeFragment
import com.example.ecommerceapp.ui.dashboard.offers.OffersFragment
import com.facebook.internal.Utility.logd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
     val userViewModel: UserViewModel by viewModels ()

    private var _bindig: ActivityMainBinding? = null
    private val binding get() = _bindig!!
    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)

 val isLoggedIn = runBlocking { userViewModel.isUserLoggedIn().first() }
        if (!isLoggedIn) {
          //  Log.d("benz", "is User LoggedIn: $isLoggedIn")
            goToAuthActivity()
            return
        }



      //  Log.d("benz", "is User LoggedIn: $isLoggedIn")
        _bindig = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initViewModel()
        initViews()
    }
    private fun initViews() {
        initViewPager()
        initBottomNavigationView()
    }
    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> binding.homeViewPager.currentItem = 0
                R.id.exploreFragment -> binding.homeViewPager.currentItem = 1
                R.id.cartFragment -> binding.homeViewPager.currentItem = 2
                R.id.offerFragment -> binding.homeViewPager.currentItem = 3
                R.id.accountFragment -> binding.homeViewPager.currentItem = 4
            }
            true
        }
    }


    private fun initViewPager() {
        val fragments = listOf(
            HomeFragment(),
            ExploreFragment(),
            CartFragment(),
            OffersFragment(),
            AccountFragment()
        )
        binding.homeViewPager.offscreenPageLimit = fragments.size
        binding.homeViewPager.adapter = HomeViewPagerAdapter(this, fragments)
        binding.homeViewPager.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavigationView.menu.getItem(position).isChecked = true
                }
            }
        )
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).apply {
        }
        startActivity(intent)
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
    private fun logOut() {
        lifecycleScope.launch {
            userViewModel.logOut()
            goToAuthActivity()
        }
    }
    private fun initViewModel() {
        lifecycleScope.launch {
            val userDetails = runBlocking { userViewModel.getUserDetails().first() }
          //  Log.d(TAG, "initViewModel: user details ${userDetails.email}")

            userViewModel.userDetailsState.collect {
               // Log.d(TAG, "initViewModel: user details updated ${it?.email}")
            }

        }
    }
}
