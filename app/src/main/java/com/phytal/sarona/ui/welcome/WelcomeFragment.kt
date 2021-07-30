package com.phytal.sarona.ui.welcome

import android.animation.ObjectAnimator
import android.content.Context
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
import com.phytal.sarona.ui.courses.CoursesFragmentDirections


class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWelcomeBinding.inflate(inflater)

        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.login_preference_file_key),
            Context.MODE_PRIVATE
        )

        // navigate to CourseFragment if saved login
        if (sharedPref?.getString(getString(R.string.saved_username_key), null) != null &&
            sharedPref.getString(getString(R.string.saved_password_key), null) != null &&
            sharedPref.getString(getString(R.string.saved_link_key), null) != null
        ) {
            val directions =
                CoursesFragmentDirections.actionGlobalCoursesFragment()
            findNavController().navigate(directions)
        }

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