package com.example.farmaapp.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.farmaapp.R
import com.example.farmaapp.adapter.BannerAdapter
import com.example.farmaapp.adapter.NewsAdapter
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.custom.Constants.CHECK_INTERNET_TOAST_MSG
import com.example.farmaapp.custom.Constants.COLON
import com.example.farmaapp.custom.Constants.LOCATION_REQUEST_CODE
import com.example.farmaapp.custom.Constants.NEWS_TABLE_INDEX
import com.example.farmaapp.custom.Constants.T
import com.example.farmaapp.custom.Constants.WEATHER_TABLE_INDEX
import com.example.farmaapp.databinding.FragmentHomeBinding
import com.example.farmaapp.fragment.banners.BannerOneFragment
import com.example.farmaapp.fragment.banners.BannerTwoFragment
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
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider
    private var count = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val homeWeatherViewModel by viewModels<WeatherViewModel>()
    val mainViewModel by viewModels<MainViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setOnBackPressed()
        return binding.root
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.newsShimmerContainer.startShimmer()
        initAdapter()
        initializeWeather()
        initListeners()
        initializePager()

    }

    override fun onStart() {
        super.onStart()
        setupLocationUpdate()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
//                    getGeoLocation()
                    setupLocationUpdate()
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
//                    getGeoLocation()
                    setupLocationUpdate()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationUpdate() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .apply {
                setMinUpdateDistanceMeters(100F)
                setIntervalMillis(20000)
                setGranularity(Granularity.GRANULARITY_FINE)
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build()
        val locationSettingRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
        val settingClient = LocationServices.getSettingsClient(requireActivity())

            .checkLocationSettings(locationSettingRequest).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest, locationCallback,
                        Looper.getMainLooper()
                    )
                    log("success", task.isSuccessful.toString())
                    getGeoLocation()
                } else {
                    if (task.exception is ResolvableApiException) {
                        try {
                            val resolvableApiException = task.exception as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                requireActivity(),
                                LOCATION_REQUEST_CODE
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult == null) {
                log("location", "")
            } else {
                log("location", locationResult.locations.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getGeoLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != (PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener {
                        if (it == null) {
                            Toast.makeText(
                                requireContext(),
                                "null",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            lifecycleScope.launch(Dispatchers.IO) {
                                log("lon_lat", "lon:${it.longitude} lat:${it.latitude}")
                                saveLocationData(
                                    requireContext(),
                                    dataStoreProvider,
                                    Constants.LOCATION_KEY,
                                    "lon:${it.longitude} lat:${it.latitude}"
                                )
                                lifecycleScope.launch(Dispatchers.Main) {
                                    refreshHome(it.longitude, it.latitude)
                                }
                            }
//                        Toast.makeText(
//                            this,
//                            "lon:${it.longitude} lat:${it.latitude}",
//                            Toast.LENGTH_SHORT
//                        ).show()
                            Log.e("tag", "lon:${it.longitude} lat:${it.latitude}")

                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }

    }

    private fun refreshHome(longitude: Double, latitude: Double) {
        binding.swipeRefresh.setOnRefreshListener {
            binding.newsRv.visibility = View.GONE
            binding.newsShimmerContainer.visibility = View.VISIBLE
            binding.newsShimmerContainer.startShimmer()
            if (isOnline(requireContext())) {
                homeWeatherViewModel.getWeatherData(latitude, longitude)
                homeWeatherViewModel.weatherData.observe(viewLifecycleOwner, Observer {
                    log("weather", it.toString())
                    mainViewModel.addWeatherInfo(
                        WeatherDBModel(
                            WEATHER_TABLE_INDEX,
                            it.current_weather,
                            it.elevation,
                            it.generationtime_ms,
                            it.hourly,
                            it.hourly_units,
                            it.latitude,
                            it.longitude,
                            it.timezone,
                            it.timezone_abbreviation,
                            it.utc_offset_seconds
                        )
                    )
                })
                mainViewModel.getNewsDataFromApi()
                mainViewModel.newsData.observe(viewLifecycleOwner, Observer {
                    log("newsModel", it.toString())
                    mainViewModel.addNewsDataInDB(
                        NewsDBModel(
                            NEWS_TABLE_INDEX,
                            it.articles,
                        )
                    )
                })
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.newsShimmerContainer.stopShimmer()
                    binding.newsShimmerContainer.visibility = View.GONE
                    binding.newsRv.visibility = View.VISIBLE
                }, 100)

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

    private fun initAdapter() {
        val callback = object : ChangeFragment {
            override fun next(data: Any, position: Int) {
                findNavController().navigate(R.id.newsDetailsFragment, Bundle().apply {
                    putInt(Constants.NEWS_POSITION, position)
                })
            }
        }
        val adapter = NewsAdapter(callback, false)
        binding.newsRv.adapter = adapter
        mainViewModel.getNewsDataFromDB().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setData(it.articles.results as ArrayList<Result>)
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.newsShimmerContainer.stopShimmer()
                    binding.newsShimmerContainer.visibility = View.GONE
                    binding.newsRv.visibility = View.VISIBLE
                }

            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun initializeWeather() {
        mainViewModel.getWeatherInfo().observe(viewLifecycleOwner) { weatherData ->
            binding.apply {
                tempTxt.text =
                    getString(
                        R.string.temp_,
                        weatherData.current_weather.temperature.toString()
                    )
                windSpeedTxt.text =
                    getString(
                        R.string.wind_speed,
                        weatherData.current_weather.windspeed.toString()
                    )
            }
            val rightNow = Calendar.getInstance()
            val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
            weatherData.hourly.time.forEachIndexed { index, time ->
                if ((time.split(T)[1].split(COLON)[0]) == currentHourIn24Format.toString()) {
                    val humidity = (weatherData.hourly.relativehumidity_2m[index])
                    binding.humidityTxt.text = "$humidity %"

                }
            }
        }
    }

    private fun initializePager() {
        var count = 0
        val fragmentList = arrayListOf<Fragment>(
            BannerOneFragment(),
            BannerTwoFragment()
        )

        val adapter =
            BannerAdapter(fragmentList, childFragmentManager, lifecycle)
        binding.bannerPager.adapter = adapter
        TabLayoutMediator(
            binding.tabLayout,
            binding.bannerPager
        ) { tab, position -> }.attach()

        val update = Runnable {
            if (count == fragmentList.size) {
                count = 0
            }
            binding.bannerPager.setCurrentItem(count++, true)
        }
        Timer().apply {
            this.schedule(object : TimerTask() {
                override fun run() {
                    Handler(Looper.getMainLooper()).post(update)
                }
            }, 1000, 4000)
        }
    }

    private fun initListeners() {
        binding.weatherContainer.setOnClickListener {
            findNavController().navigate(R.id.weatherFragment)
        }
        binding.mainNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_item -> {
                    findNavController().navigate(R.id.homeFragment)
                }

                R.id.weather_item -> {
                    findNavController().navigate(R.id.weatherFragment)
                }

                R.id.market_rate_item -> {
                    findNavController().navigate(R.id.marketRateFragment)
                }

                R.id.news_item -> {
                    findNavController().navigate(R.id.newsFragment)
                }

            }
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}