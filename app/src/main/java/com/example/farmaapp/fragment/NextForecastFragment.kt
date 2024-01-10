package com.example.farmaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.farmaapp.R
import com.example.farmaapp.adapter.HourlyForecastAdapter
import com.example.farmaapp.adapter.NextForecastAdapter
import com.example.farmaapp.custom.Constants.FORECAST_ITEM_POSITION
import com.example.farmaapp.custom.Constants.FORECAST_ITEM_POSITION_ARR
import com.example.farmaapp.databinding.FragmentNextForecastBinding
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.model.newsModels.EachDayHourly
import com.example.farmaapp.utils.ChangeFragment
import com.example.farmaapp.utils.ChangeFragmentWithData
import com.example.farmaapp.utils.log
import com.example.farmaapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NextForecastFragment : Fragment() {
    private var _binding: FragmentNextForecastBinding? = null
    private val binding get() = _binding!!
    val forecastMainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNextForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdapter()
        initializeListener()
    }

    private fun initializeListener() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initializeAdapter() {
        val callback = object : ChangeFragmentWithData {
            override fun next(data: Any, date: String) {
                findNavController().navigate(
                    R.id.action_nextForecastFragment_to_singleDayForecastFragment,
                    Bundle().apply {
                        putString(FORECAST_ITEM_POSITION, date)
                        putParcelable(
                            FORECAST_ITEM_POSITION_ARR,
                            data as Hourly
                        )

                    })
            }
        }
        val nextForecastAdapter = NextForecastAdapter(callback)
        forecastMainViewModel.getWeatherInfo().observe(viewLifecycleOwner) { weatherData ->
            nextForecastAdapter.setData(weatherData.hourly)
        }
        binding.nextForecastRv.adapter = nextForecastAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}