package com.example.farmaapp.fragment.banners

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.farmaapp.custom.Constants.BANNER_TWO_URL
import com.example.farmaapp.databinding.FragmentBannerTwoBinding


class BannerTwoFragment : Fragment() {
    private var _binding: FragmentBannerTwoBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBannerTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide
            .with(requireContext())
            .load(BANNER_TWO_URL)
            .into(binding.bannerOne);
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}