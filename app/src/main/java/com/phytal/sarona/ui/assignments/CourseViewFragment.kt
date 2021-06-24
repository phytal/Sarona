package com.phytal.sarona.ui.assignments

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.phytal.sarona.R
import com.phytal.sarona.databinding.FragmentCourseViewBinding
import com.phytal.sarona.ui.base.ScopedFragment
import com.phytal.sarona.util.themeColor

class CourseViewFragment : ScopedFragment() {
    private val args: CourseViewFragmentArgs by navArgs()
    private val courseId: String by lazy(LazyThreadSafetyMode.NONE) {args.courseId}
    private lateinit var binding: FragmentCourseViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // Scope the transition to a view in the hierarchy so we know it will be added under
            // the bottom app bar but over the elevation scale of the exiting HomeFragment.
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentCourseViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // grade is multiplied by 10^2 to account for decimals
        val gradeDummy = 10000
        ObjectAnimator.ofInt(binding.progressBar, "progress", gradeDummy).apply {
            duration = 1500
            interpolator = MVAccelerateDecelerateInterpolator()
            start()
        }
        binding.courseGrade.text = "100.00"
    }

}

class MVAccelerateDecelerateInterpolator : Interpolator {
    // easeInOutQuint
    override fun getInterpolation(t: Float): Float {
        var x = t * 2.0f
        if (t < 0.5f) return 0.5f * x * x * x * x * x
        x = (t - 0.5f) * 2 - 1
        return 0.5f * x * x * x * x * x + 1
    }
}