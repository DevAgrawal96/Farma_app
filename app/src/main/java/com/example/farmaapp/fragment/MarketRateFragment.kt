package com.example.farmaapp.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants.MARKET_RATE_WEB_HIDE_BY_ID_USING_ELEMENT_URL
import com.example.farmaapp.custom.Constants.MARKET_RATE_WEB_HIDE_CLASS_USING_ELEMENT_URL
import com.example.farmaapp.custom.Constants.MARKET_RATE_WEB_URL
import com.example.farmaapp.databinding.FragmentMarketRateBinding


class MarketRateFragment : Fragment() {
    private var _binding: FragmentMarketRateBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarketRateBinding.inflate(inflater, container, false)
        setOnBackPressed()
        return binding.root
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_marketRateFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeWebView()
    }


    private fun initializeWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // hide element by class name
                binding.webView.loadUrl(
                    MARKET_RATE_WEB_HIDE_CLASS_USING_ELEMENT_URL
                )
                binding.webView.settings.javaScriptEnabled = false

                // hide element by id
                binding.webView.loadUrl(
                    MARKET_RATE_WEB_HIDE_BY_ID_USING_ELEMENT_URL
                )
            }
        }
        binding.webView.loadUrl(MARKET_RATE_WEB_URL)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}