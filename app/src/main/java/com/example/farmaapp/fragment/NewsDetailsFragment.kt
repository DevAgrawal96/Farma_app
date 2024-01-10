package com.example.farmaapp.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.custom.Constants.NEWS_ARR
import com.example.farmaapp.custom.Constants.NEWS_POSITION
import com.example.farmaapp.custom.Constants.T
import com.example.farmaapp.custom.Constants.UTC
import com.example.farmaapp.databinding.FragmentNewsDetailsBinding
import com.example.farmaapp.model.newsModels.Result
import com.example.farmaapp.utils.log
import com.example.farmaapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class NewsDetailsFragment : Fragment() {
    val newsDetailsMainViewModel by viewModels<MainViewModel>()
    private var _binding: FragmentNewsDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)
        setOnBackPressed()
        return binding.root
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeListener()
        settingData()
    }

    private fun initializeListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun settingData() {
        val position = arguments?.getInt(NEWS_POSITION)
        newsDetailsMainViewModel.getNewsDataFromDB().observe(viewLifecycleOwner) { data ->
            Glide.with(requireContext()).load(data.articles.results[position!!].image)
                .placeholder(R.drawable.news_placeholder)
                .into(binding.newsImg)
            binding.detailNewsTitle.text = data.articles.results[position].title
            binding.newsDetails.text = data.articles.results[position].body
            val utc = TimeZone.getTimeZone(UTC)

            val inputFormat: DateFormat = SimpleDateFormat(
                Constants.NEWS_CHAR_PATTERN_IN,
                Locale.ENGLISH
            )
            inputFormat.timeZone = utc

            val outputFormat: DateFormat = SimpleDateFormat(
                Constants.NEWS_CHAR_PATTERN_OUT,
                Locale.ENGLISH
            )
            outputFormat.timeZone = utc

            val dateInput =
                inputFormat.parse(data.articles.results[position].dateTime.split(T)[1])

            val output = outputFormat.format(dateInput!!)
            binding.newsDateTime.text =
                getString(R.string.publish_s, output)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}