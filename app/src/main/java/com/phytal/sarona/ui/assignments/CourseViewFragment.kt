package com.phytal.sarona.ui.assignments

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Assignment
import com.phytal.sarona.databinding.FragmentCourseViewBinding
import com.phytal.sarona.ui.base.ScopedFragment
import com.phytal.sarona.ui.courses.CourseViewModel
import com.phytal.sarona.ui.courses.CurrentCourseViewModelFactory
import com.phytal.sarona.util.MVAccelerateDecelerateInterpolator
import com.phytal.sarona.util.themeColor
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt


class CourseViewFragment : ScopedFragment(), KodeinAware, AssignmentsAdapter.AssignmentAdapterListener {
    private val args: CourseViewFragmentArgs by navArgs()
    private val courseId: String by lazy(LazyThreadSafetyMode.NONE) { args.courseId }
    private lateinit var binding: FragmentCourseViewBinding
    override val kodein by closestKodein()
    private val viewModelFactory by instance<CurrentCourseViewModelFactory>()
    private lateinit var viewModel: CourseViewModel
    private val adapter = AssignmentsAdapter(this)

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseViewBinding.inflate(inflater, container, false)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CourseViewModel::class.java)
        bindUI()


    }

    private fun bindUI() = launch {
        val currentCourses = viewModel.courses.await()
        currentCourses.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            for (course in it.yearCourses[0]) {
                if (course.course == courseId) {
                    adapter.setAssignments(course.assignments)

                    binding.courseName.text = course.name

                    // grade is multiplied by 10^2 to handle decimals
                    val courseGrade = course.average
                    ObjectAnimator.ofInt(binding.progressBar, "progress",
                        (courseGrade * 100).roundToInt()
                    ).apply {
                        duration = 1000
                        interpolator = MVAccelerateDecelerateInterpolator()
                        start()
                    }
                    binding.courseGrade.text = String.format("%.2f", courseGrade)

                    break
                }
            }
        })
    }

    override fun onAssignmentClick(cardView: View, assignment: Assignment) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.assignment_dialog, null)
        builder.setView(view)
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }
}