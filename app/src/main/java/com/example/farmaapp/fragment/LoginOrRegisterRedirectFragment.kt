package com.example.farmaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.farmaapp.R
import com.example.farmaapp.databinding.FragmentLoginOrRegisterRedirectBinding


class LoginOrRegisterRedirectFragment : Fragment() {

    private var _binding: FragmentLoginOrRegisterRedirectBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginOrRegisterRedirectBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeListener()
    }

    private fun initializeListener() {
        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginOrRegisterRedirectFragment_to_loginFragment)
        }
    }


}