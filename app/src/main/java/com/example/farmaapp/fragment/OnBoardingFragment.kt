package com.example.farmaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.farmaapp.R
import com.example.farmaapp.adapter.BannerAdapter
import com.example.farmaapp.adapter.OnBoardingAdapter
import com.example.farmaapp.databinding.FragmentOnBoardingBinding
import com.example.farmaapp.fragment.banners.BannerOneFragment
import com.example.farmaapp.fragment.banners.BannerTwoFragment
import com.example.farmaapp.fragment.onBoarding.OnBoardingOneFragment
import com.example.farmaapp.fragment.onBoarding.OnBoardingTwoFragment


class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePager()
    }

    private fun initializePager() {
        val fragmentList = arrayListOf<Fragment>(
            OnBoardingOneFragment(),
            OnBoardingTwoFragment()
        )
        val adapter =
            OnBoardingAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        binding.bannerPager.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}