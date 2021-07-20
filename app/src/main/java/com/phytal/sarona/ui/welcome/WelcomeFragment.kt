package com.phytal.sarona.ui.welcome

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.phytal.sarona.R
import com.phytal.sarona.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWelcomeBinding.inflate(inflater)

        Glide.with(this)
            .load(R.drawable.sarona_logo)
            .into(binding.welcomeLogo)

        ObjectAnimator.ofFloat(binding.welcomeLogo, "alpha", 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateInterpolator()
            start()
        }

        ObjectAnimator.ofFloat(binding.welcomeContinueButton, "alpha", 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateInterpolator()
            startDelay = 1000
            start()
        }

        ObjectAnimator.ofFloat(binding.welcomeAppDesc, "alpha", 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateInterpolator()
            startDelay = 1000
            start()
        }

        ObjectAnimator.ofFloat(binding.welcomeAppName, "alpha", 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateInterpolator()
            startDelay = 1000
            start()
        }

        binding.welcomeContinueButton.setOnClickListener {
            val directions =
                LoginFragmentDirections.actionGlobalLoginFragment()
            findNavController().navigate(directions)
        }

        return binding.root
    }
}