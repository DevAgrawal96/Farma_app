package com.example.farmaapp.fragment

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.farmaapp.R
import com.example.farmaapp.adapter.HourlyForecastAdapter
import com.example.farmaapp.custom.Constants.APRIL
import com.example.farmaapp.custom.Constants.AUGUST
import com.example.farmaapp.custom.Constants.COLON
import com.example.farmaapp.custom.Constants.DECEMBER
import com.example.farmaapp.custom.Constants.FEBRUARY
import com.example.farmaapp.custom.Constants.JANUARY
import com.example.farmaapp.custom.Constants.JULY
import com.example.farmaapp.custom.Constants.JUNE
import com.example.farmaapp.custom.Constants.MARCH
import com.example.farmaapp.custom.Constants.MAY
import com.example.farmaapp.custom.Constants.MM
import com.example.farmaapp.custom.Constants.NOVEMBER
import com.example.farmaapp.custom.Constants.OCTOBER
import com.example.farmaapp.custom.Constants.SEPTEMBER
import com.example.farmaapp.custom.Constants.T
import com.example.farmaapp.databinding.FragmentWeatherBinding
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.modules.DataStoreProvider
import com.example.farmaapp.utils.getKeyValue
import com.example.farmaapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class WeatherFragment : Fragment() {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    val weatherMainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        setOnBackPressed()
        return binding.root
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_weatherFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeListener()
        gettingWeatherData()

    }

    private fun initializeListener() {
        binding.nextSevenDay.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_nextForecastFragment)
        }
    }

    private fun initializeAdapterData(hourly: Hourly) {
        val adapter = HourlyForecastAdapter()
        adapter.setData(hourly)
        binding.hourlyForecastRv.adapter = adapter
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun gettingWeatherData() {
        val rightNow = Calendar.getInstance()
        val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        weatherMainViewModel.getWeatherInfo().observe(viewLifecycleOwner) { weatherData ->
            //set adapter data
            initializeAdapterData(weatherData.hourly)

            //getting address using lon,lat
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: ArrayList<Address> =
                geocoder.getFromLocation(
                    weatherData.latitude,
                    weatherData.longitude,
                    1
                ) as ArrayList<Address>

            binding.apply {
                address.text = getString(
                    R.string.address_s,
                    addresses[0].locality
                )
                windDirValue.text =
                    getString(R.string.temp_, weatherData.current_weather.winddirection.toString())
                weatherCode.text =
                    getKeyValue()[weatherData.current_weather.weathercode]!!.weatherCode
                weatherImg.setImageResource(getKeyValue()[weatherData.current_weather.weathercode]!!.weather_img)
                tempText.text =
                    getString(
                        R.string.temp_,
                        weatherData.current_weather.temperature.toString()
                    )
            }
            //fetch today's humidity from humidity array
            weatherData.hourly.time.forEachIndexed { index, time ->
                if ((time.split(T)[1].split(COLON)[0]) == currentHourIn24Format.toString()) {
                    val humidity = (weatherData.hourly.relativehumidity_2m[index])
                    binding.humidityValue.text = "$humidity %"
                }
                binding.windSpeed.text =
                    "${weatherData.current_weather.windspeed} km/h"
            }
        }

        val selected = arrayOf(
            JANUARY, FEBRUARY, MARCH, APRIL,
            MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
        )

        SimpleDateFormat(MM).apply { Date().apply { format(this) } }
        binding.date.text = getString(
            R.string.date_,
            selected[rightNow.get(Calendar.MONTH)], rightNow[Calendar.DAY_OF_MONTH].toString()
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}