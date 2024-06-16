package com.example.ecommerceapp.ui.dashboard.home.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.ui.dashboard.home.adapter.CategoriesAdapter
import com.example.ecommerceapp.ui.dashboard.home.adapter.SalesAdAdapter
import com.example.ecommerceapp.ui.dashboard.home.model.CategoryUIModel
import com.example.ecommerceapp.ui.dashboard.home.model.SalesAdUIModel
import com.example.ecommerceapp.ui.dashboard.home.viewmodels.HomeViewModel
import com.example.ecommerceapp.utils.CircleView
import com.example.ecommerceapp.utils.DepthPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val TAG = "HomeFragment"


    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        iniViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        return binding.root
    }

    private fun iniViewModel() {
        lifecycleScope.launch {
            viewModel.salesAdsState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        Log.d(TAG, "iniViewModel: Loading")
                     binding.saleAdsShimmerView.root.startShimmer()
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "iniViewModel: Success")
                        binding.saleAdsShimmerView.root.stopShimmer()
                        binding.saleAdsShimmerView.root.visibility = View.GONE
                       initSalesAdsView(resources.data)

                    }

                    is Resource.Error -> {
                        Log.d(TAG, "iniViewModel: erroer")

                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.categoriesState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        Log.d(TAG, "iniViewModel: categories Loading")
                    }

                    is Resource.Success -> {
//                        binding.categoriesShimmerView.root.stopShimmer()
//                        binding.categoriesShimmerView.root.visibility = View.GONE
                        Log.d(TAG, "iniViewModel: categories Success = ${resources.data}")
                        initCategoriesView(resources.data)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "iniViewModel: categories Error")
                    }
                }
            }
        }

    }

    private fun initSalesAdsView(salesAds: List<SalesAdUIModel>?) {
        Log.d(TAG, "initSalesAdsView: ${salesAds?.size}")
        if (salesAds.isNullOrEmpty()) {
            return
        }
        val salesAdUIModels = mutableListOf<SalesAdUIModel>()
        initializeIndicators(salesAds.size)
        Log.d(TAG, "****************")
        Log.d(TAG, "{${salesAds.get(0).imageUrl}}")


        val salesAdapter = SalesAdAdapter(lifecycleScope, salesAds)

        binding.saleAdsViewPager.apply {
            adapter = salesAdapter
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object :
                androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(position)
                }
            })
        }
//
//            lifecycleScope.launch(IO) {
//                tickerFlow(5000).collect {
//                    withContext(Main) {
//                        binding.saleAdsViewPager.setCurrentItem(
//                            (binding.saleAdsViewPager.currentItem + 1) % salesAds.size, true
//                        )
//                    }
//                }
//            }



    }
    private fun initCategoriesView(data: List<CategoryUIModel>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        val categoriesAdapter = CategoriesAdapter(data)
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }
    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }
    private var indicators = mutableListOf<CircleView>()
    private fun initializeIndicators(count: Int) {
        for (i in 0 until count) {
            val circleView = CircleView(requireContext())
            val params = LinearLayout.LayoutParams(
                20, 20
            )
            params.setMargins(8, 0, 8, 0) // Margin between circles
            circleView.setLayoutParams(params)
            circleView.setRadius(10f) // Set radius
            circleView.setColor(
                if (i == 0) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            ) // First indicator is red
            circleView.setOnClickListener {
                binding.saleAdsViewPager.setCurrentItem(i, true)
            }
            indicators.add(circleView)
            binding.indicatorView.addView(circleView)
        }

    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicators.size) {
            indicators[i].setColor(
                if (i == position) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            )
        }
    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }


}