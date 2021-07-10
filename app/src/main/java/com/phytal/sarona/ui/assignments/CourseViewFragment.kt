package com.phytal.sarona.ui.assignments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Assignment
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.databinding.AddAssignmentDialogBinding
import com.phytal.sarona.databinding.AssignmentDialogBinding
import com.phytal.sarona.databinding.FragmentCourseViewBinding
import com.phytal.sarona.ui.base.ScopedFragment
import com.phytal.sarona.ui.courses.CurrentCourseViewModel
import com.phytal.sarona.ui.courses.CurrentCourseViewModelFactory
import com.phytal.sarona.util.MVAccelerateDecelerateInterpolator
import com.phytal.sarona.util.SpinnerAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt


class CourseViewFragment : ScopedFragment(),
    AssignmentsAdapter.AssignmentAdapterListener {
    private val args: CourseViewFragmentArgs by navArgs()
    private val course: Course by lazy(LazyThreadSafetyMode.NONE) { args.course }
    private lateinit var binding: FragmentCourseViewBinding
    private val assignmentsAdapter = AssignmentsAdapter(this)
    private val gradeTypeAdapter = GradeTypeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseViewBinding.inflate(inflater, container, false)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = assignmentsAdapter

        binding.gradeCategories.adapter = gradeTypeAdapter

        binding.addGradeButton.setOnClickListener {
            addMockAssignment()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI() = launch {
        assignmentsAdapter.setAssignmentList(course.assignments)
        gradeTypeAdapter.submitList(course.grade_types)

        binding.courseName.text = course.name

        // grade is multiplied by 10^2 to handle decimals
        val courseGrade = course.average
        ObjectAnimator.ofInt(
            binding.progressBar, "progress",
            (courseGrade * 100).roundToInt()
        ).apply {
            duration = 1000
            interpolator = MVAccelerateDecelerateInterpolator()
            start()
        }
        binding.courseGrade.text = String.format("%.2f", courseGrade)
    }

    override fun onAssignmentClick(cardView: View, position: Int) {
        val binding = AssignmentDialogBinding.inflate(layoutInflater)
        val assignment = assignmentsAdapter.assignments[position]

        binding.assignmentName.text = assignment.title_of_assignment
        val categoryString = "Category: " + assignment.type_of_grade
        binding.assignmentCategory.text = categoryString
        binding.dateAssigned.text = assignment.date_assigned
        binding.dateDue.text = assignment.date_due
        val scoreString = assignment.score + " / " + assignment.total_points.toString()
        binding.score.text = scoreString
        val weightedScoreString =
            assignment.weighted_score.toString() + " / " + assignment.weighted_total_points.toString()
        binding.weightedPoints.text = weightedScoreString
        binding.weight.text = assignment.weight.toString()
        val percentageString = assignment.percentage.toString() + "%"
        binding.percentage.text = percentageString

        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        builder.setView(binding.root)
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    override fun onAssignmentLongClick(cardView: View, position: Int) {
        addMockAssignment(assignmentsAdapter.assignments[position])
    }

    private fun addMockAssignment(assignment: Assignment? = null) {
        val dialogBinding = AddAssignmentDialogBinding.inflate(layoutInflater)

        val gradeCategories = ArrayList<String>()
        for (type in course.grade_types) {
            gradeCategories.add(type.category)
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, gradeCategories)
        dialogBinding.inputCategorySpinner.setAdapter(adapter)

        // fills dialog if assignment is passed in
        if (assignment != null) {
            dialogBinding.addAssignmentText.text = "Edit Assignment"
            dialogBinding.inputCategorySpinner.setText(assignment.type_of_grade, false)
            dialogBinding.inputAssignmentName.setText(assignment.title_of_assignment)
            dialogBinding.inputScore.setText(assignment.score)
            dialogBinding.inputMaxScore.setText(assignment.max_points.toString())
            dialogBinding.inputWeight.setText(assignment.weight.toString())
        }

        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        builder.setView(dialogBinding.root)
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }
}