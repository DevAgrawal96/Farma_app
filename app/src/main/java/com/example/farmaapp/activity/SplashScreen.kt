package com.example.farmaapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.custom.Constants.LOCATION_KEY
import com.example.farmaapp.custom.Constants.NEWS_TABLE_INDEX
import com.example.farmaapp.custom.Constants.PUNE_LATITUDE
import com.example.farmaapp.custom.Constants.PUNE_LON
import com.example.farmaapp.custom.Constants.WEATHER_TABLE_INDEX
import com.example.farmaapp.databinding.ActivitySplashScreenBinding
import com.example.farmaapp.model.NewsDBModel
import com.example.farmaapp.model.WeatherDBModel
import com.example.farmaapp.modules.DataStoreProvider
import com.example.farmaapp.utils.getLocationData
import com.example.farmaapp.utils.log
import com.example.farmaapp.utils.makeToast
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataStoreProvider: DataStoreProvider
    val mainViewModel by viewModels<MainViewModel>()
    val splashScreenWeatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()
        initializeListener()
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
        LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationSettingRequest).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest, locationCallback,
                        Looper.getMainLooper()
                    )
                    getGeoLocation()
                } else {
                    if (task.exception is ResolvableApiException) {
                        try {
                            val resolvableApiException = task.exception as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this,
                                Constants.LOCATION_REQUEST_CODE
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
    }

    private fun initializeListener() {
        binding.retry.setOnClickListener {
            binding.retry.visibility = View.GONE
            binding.connectionLoseText.visibility = View.GONE
            setupLocationUpdate()
        }
    }

    private fun nextActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }, 2000)
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun getWeatherData(longitude: Double, latitude: Double) {
        if (isOnline(this)) {
            splashScreenWeatherViewModel.getWeatherData(latitude, longitude)
            splashScreenWeatherViewModel.weatherData.observe(this, Observer {
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
            mainViewModel.newsData.observe(this) {
                mainViewModel.addNewsDataInDB(
                    NewsDBModel(
                        NEWS_TABLE_INDEX,
                        it.articles,
                    )
                )
            }
            nextActivity()
        } else {
            binding.retry.visibility = View.VISIBLE
            binding.connectionLoseText.visibility = View.VISIBLE
            binding.animationView.setAnimation(R.raw.error_network)
        }

    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
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
            setupLocationUpdate()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    setupLocationUpdate()
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    setupLocationUpdate()
                }

                else -> {
                    makeToast("allow location for checking weather!")
                    // No location access granted.
                }
            }
        }

    @SuppressLint("MissingPermission")
    private fun getGeoLocation() {
        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    if (it == null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                getWeatherData(PUNE_LON, PUNE_LATITUDE)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        lifecycleScope.launch(Dispatchers.IO) {
                            saveLocationData(
                                context = this@SplashScreen,
                                dataStoreProvider,
                                LOCATION_KEY,
                                "lon:${it.longitude} lat:${it.latitude}"
                            )
                            lifecycleScope.launch(Dispatchers.Main) {
                                getWeatherData(it.longitude, it.latitude)
                            }
                        }
                    }
                }
        } else {
            Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onStart() {
        super.onStart()
        setupLocationUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
