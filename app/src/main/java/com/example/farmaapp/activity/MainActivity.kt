package com.example.farmaapp.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.farmaapp.R
import com.example.farmaapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        initializeListener()
        hideBottomNav()
    }

    private fun hideBottomNav() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomNavigationView.menu.findItem(R.id.home_).isChecked = true
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                R.id.weatherFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                R.id.marketRateFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                R.id.newsFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }

    private fun initializeListener() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_ -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.weather_ -> {
                    item.isCheckable = true
                    navController.navigate(R.id.weatherFragment)
                    true
                }

                R.id.market_rate -> {
                    item.isCheckable = true
                    navController.navigate(R.id.marketRateFragment)
                    true
                }

                R.id.news -> {
                    item.isCheckable = true
                    navController.navigate(R.id.newsFragment)
                    true
                }

                else -> {
                    item.isCheckable = false
                    false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}