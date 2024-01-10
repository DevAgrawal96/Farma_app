package com.example.farmaapp.fragment.banners

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants.BANNER_ONE_URL
import com.example.farmaapp.databinding.FragmentBannerOneBinding


class BannerOneFragment : Fragment() {

    private var _binding: FragmentBannerOneBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBannerOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide
            .with(requireContext())
            .load(BANNER_ONE_URL)
            .into(binding.bannerOne);
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}