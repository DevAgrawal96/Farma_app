package com.example.farmaapp.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.farmaapp.R
import com.example.farmaapp.adapter.NewsAdapter
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.custom.Constants.CHECK_INTERNET_TOAST_MSG
import com.example.farmaapp.custom.Constants.NEWS_ARR
import com.example.farmaapp.custom.Constants.NEWS_POSITION
import com.example.farmaapp.custom.Constants.NEWS_TABLE_INDEX
import com.example.farmaapp.databinding.FragmentNewsBinding
import com.example.farmaapp.model.NewsDBModel
import com.example.farmaapp.model.WeatherDBModel
import com.example.farmaapp.model.newsModels.Result
import com.example.farmaapp.modules.DataStoreProvider
import com.example.farmaapp.utils.ChangeFragment
import com.example.farmaapp.utils.isOnline
import com.example.farmaapp.utils.log
import com.example.farmaapp.utils.saveLocationData
import com.example.farmaapp.viewmodel.MainViewModel
import com.example.farmaapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.phntech.phncareer.util.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NewsFragment : Fragment() {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    val newsWeatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val newsMainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        setOnBackPressed()
        return binding.root
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_newsFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.newsShimmerContainer.visibility = View.VISIBLE
        binding.newsShimmerContainer.startShimmer()
        initializeAdapter()
        refreshHome()
    }

    private fun refreshHome() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.newsRv.visibility = View.GONE
            binding.newsShimmerContainer.visibility = View.VISIBLE
            binding.newsShimmerContainer.startShimmer()
            if (isOnline(requireContext())) {
                newsMainViewModel.getNewsDataFromApi()
                newsMainViewModel.newsData.observe(viewLifecycleOwner, Observer {
                    newsMainViewModel.addNewsDataInDB(
                        NewsDBModel(
                            NEWS_TABLE_INDEX,
                            it.articles,
                        )
                    )
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.newsShimmerContainer.stopShimmer()
                        binding.newsShimmerContainer.visibility = View.GONE
                        binding.newsRv.visibility = View.VISIBLE
                    }
                })
                binding.swipeRefresh.isRefreshing = false
            } else {
                Toast.makeText(
                    requireContext(),
                    CHECK_INTERNET_TOAST_MSG,
                    Toast.LENGTH_SHORT
                ).show()
                binding.newsShimmerContainer.stopShimmer()
                binding.newsShimmerContainer.visibility = View.GONE
                binding.newsRv.visibility = View.VISIBLE
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun initializeAdapter() {
        val callback = object : ChangeFragment {
            override fun next(data: Any, position: Int) {
                findNavController().navigate(R.id.newsDetailsFragment, Bundle().apply {
                    putInt(NEWS_POSITION, position)
                })
            }
        }
        val adapter = NewsAdapter(callback, true)
        newsMainViewModel.getNewsDataFromDB().observe(viewLifecycleOwner) {
            adapter.setData(it.articles.results as ArrayList<Result>)
            lifecycleScope.launch(Dispatchers.Main) {
                binding.newsShimmerContainer.stopShimmer()
                binding.newsShimmerContainer.visibility = View.GONE
                binding.newsRv.visibility = View.VISIBLE
            }
        }


        binding.newsRv.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}