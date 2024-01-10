package com.example.farmaapp.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.farmaapp.adapter.WeatherListAdapter
import com.example.farmaapp.custom.Constants.HOURLY_DATA_KEY
import com.example.farmaapp.databinding.FragmentTodaysWeatherBinding
import com.example.farmaapp.model.Hourly

class TodaysWeatherFragment : Fragment() {
    private var _binding: FragmentTodaysWeatherBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodaysWeatherBinding.inflate(inflater, container, false)
        setupBackPressed()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdapter()
    }


    private fun initializeAdapter() {
        val adapter = WeatherListAdapter()
        val hourly = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(HOURLY_DATA_KEY, Hourly::class.java)
        } else {
            arguments?.getParcelable(HOURLY_DATA_KEY)
        }
        if (hourly != null) {
            adapter.setData(hourly)
        }
        binding.hourlyWeatherRv.adapter = adapter
    }

    private fun setupBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
    }

}