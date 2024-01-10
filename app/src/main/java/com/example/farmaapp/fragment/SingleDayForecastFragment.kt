package com.example.farmaapp.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.farmaapp.R
import com.example.farmaapp.adapter.EachDayForecastAdapter
import com.example.farmaapp.custom.Constants.COMMA
import com.example.farmaapp.custom.Constants.FORECAST_ITEM_POSITION
import com.example.farmaapp.custom.Constants.FORECAST_ITEM_POSITION_ARR
import com.example.farmaapp.custom.Constants.HYPHEN
import com.example.farmaapp.custom.Constants.SPACE
import com.example.farmaapp.custom.Constants.T
import com.example.farmaapp.databinding.FragmentSingleDayForecastBinding
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.model.newsModels.EachDayHourly
import com.example.farmaapp.utils.log


class SingleDayForecastFragment : Fragment() {
    private var _binding: FragmentSingleDayForecastBinding? = null
    private val binding get() = _binding!!
    private var date: String? = null
    private lateinit var data: Hourly
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingleDayForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setArgumentData()
        initializeAdapter()
        setViewData()
        initializeListener()
    }

    private fun initializeListener() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setViewData() {
        binding.topAppBar.title = "${date!!.split(COMMA)[0]} ${date!!.split(COMMA)[1]}"
    }

    private fun initializeAdapter() {
        val oneDayForecast = ArrayList<EachDayHourly>()
        data.time.forEachIndexed() { index, time ->
            if ((time.split(T)[0].split(HYPHEN)[2]).toInt() == date!!.split(COMMA)[1].split(SPACE)[0].toInt()) {
                oneDayForecast.add(
                    EachDayHourly(
                        data.is_day[index],
                        data.relativehumidity_2m[index],
                        data.temperature_2m[index],
                        data.time[index],
                        data.weathercode[index]
                    )
                )
            }
        }
        val adapter = EachDayForecastAdapter()
        adapter.setData(oneDayForecast)
        binding.forecastRv.adapter = adapter
    }

    private fun setArgumentData() {
        if (arguments?.getInt(FORECAST_ITEM_POSITION) != null) {
            date = requireArguments().getString(FORECAST_ITEM_POSITION)
            data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireArguments().getParcelable(
                    FORECAST_ITEM_POSITION_ARR,
                    Hourly::class.java
                )!!
            } else {
                requireArguments().getParcelable(FORECAST_ITEM_POSITION_ARR)!!
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}