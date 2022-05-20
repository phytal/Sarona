package com.phytal.sarona.ui.assignments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomappbar.BottomAppBar
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Assignment
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.db.entities.GradeType
import com.phytal.sarona.databinding.AddAssignmentDialogBinding
import com.phytal.sarona.databinding.AssignmentDialogBinding
import com.phytal.sarona.databinding.FragmentCourseViewBinding
import com.phytal.sarona.ui.base.ScopedFragment
import com.phytal.sarona.util.MVAccelerateDecelerateInterpolator
import kotlinx.coroutines.launch
import kotlin.math.round
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
        activity?.findViewById<BottomAppBar>(R.id.bottom_app_bar)?.menu?.findItem(R.id.menu_refresh)?.isVisible =
            false

        binding = FragmentCourseViewBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = assignmentsAdapter

        binding.gradeCategories.adapter = gradeTypeAdapter
        binding.addGradeButton.setOnClickListener {
            addMockAssignment()
        }
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI() = launch {
        assignmentsAdapter.setAssignmentList(ArrayList(course.assignments))
        gradeTypeAdapter.submitList(course.grade_types)

        binding.courseName.text = course.name
        setProgressBar(course.average)
    }

    private fun setProgressBar(courseGrade: Double) {
        // grade is multiplied by 10^2 to handle decimals
        ObjectAnimator.ofInt(
            binding.gradeProgressBar, "progress",
            (courseGrade * 100).roundToInt()
        ).apply {
            duration = 1000
            interpolator = MVAccelerateDecelerateInterpolator()
            start()
        }
        binding.courseGrade.text =
            if (courseGrade != 100.0) String.format("%.2f", courseGrade) else 100.0.toString()
    }

    private fun reCalculateAverage() {
        val grades = DoubleArray(course.grade_types.size)
        val weights = DoubleArray(course.grade_types.size)
        if (weights.isEmpty()) return
        for (assignment in assignmentsAdapter.assignments) {
            if (assignment.score == "X" || assignment.score == "")
                continue
            var categoryIndex = -1
            for (i in 0..course.grade_types.size) {
                if (course.grade_types[i].category == assignment.type_of_grade) {
                    categoryIndex = i
                    break
                }
            }
            if (assignment.weighted_score == "N/A") {
                grades[categoryIndex] += assignment.score.toDouble()
                continue
            }
            grades[categoryIndex] += assignment.percentage.toDouble() * assignment.weight.toDouble()
            weights[categoryIndex] += assignment.weight.toDouble()
        }
        var avg = 0.0
        val categories: ArrayList<GradeType?> = ArrayList()
        for (i in course.grade_types.indices)
            categories.add(null)

        for (i in grades.indices) {
            val sum = grades[i] / weights[i]
            if (course.grade_types.size > 1)
                avg += sum * course.grade_types[i].weight / 100
            else
                avg = sum
            categories[i] =
                GradeType(
                    gradeTypeAdapter.currentList[i].category,
                    sum,
                    gradeTypeAdapter.currentList[i].weight
                )
        }
        gradeTypeAdapter.submitList(categories)

        setProgressBar(avg)
    }

    override fun onAssignmentClick(cardView: View, position: Int) {
        val binding = AssignmentDialogBinding.inflate(layoutInflater)
        val assignment = assignmentsAdapter.assignments[position]

        binding.assignmentName.text = assignment.title_of_assignment
        val categoryString = "Category: " + assignment.type_of_grade
        binding.assignmentCategory.text = categoryString
        binding.dateAssigned.text = assignment.date_assigned
        binding.dateDue.text = assignment.date_due
        val scoreString = assignment.score + " / " + assignment.total_points
        binding.score.text = scoreString
        val weightedScoreString =
            assignment.weighted_score + " / " + assignment.weighted_total_points
        binding.weightedPoints.text = weightedScoreString
        binding.weight.text = assignment.weight
        val percentageString = assignment.percentage + "%"
        binding.percentage.text = percentageString

        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        builder.setView(binding.root)
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    override fun onAssignmentLongClick(cardView: View, position: Int) {
        addMockAssignment(assignmentsAdapter.assignments[position], position)
    }

    private fun addMockAssignment(assignment: Assignment? = null, position: Int = -1) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val dialogBinding = AddAssignmentDialogBinding.inflate(layoutInflater)

        val gradeCategories = ArrayList<String>()
        for (type in course.grade_types) {
            gradeCategories.add(type.category)
        }

        val adapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, gradeCategories)
        dialogBinding.inputCategorySpinner.setAdapter(adapter)

        // fills dialog if assignment is passed in
        if (assignment != null) {
            dialogBinding.addAssignmentText.text = "Edit Assignment"
            dialogBinding.inputCategorySpinner.setText(assignment.type_of_grade, false)
            dialogBinding.inputAssignmentName.setText(assignment.title_of_assignment)
            dialogBinding.inputScore.setText(assignment.score)
            dialogBinding.inputMaxScore.setText(assignment.max_points)
            dialogBinding.inputWeight.setText(assignment.weight)
        }

        dialogBinding.addAssignmentButton.setOnClickListener {
            val category = dialogBinding.inputCategorySpinner.text.toString()

            if (category.isNotEmpty()) {
                var containsCategory = false
                for (cat in course.grade_types) {
                    if (cat.category == category) {
                        containsCategory = true
                        break
                    }
                }
                if (containsCategory &&
                    !dialogBinding.inputScore.text.isNullOrEmpty() &&
                    !dialogBinding.inputMaxScore.text.isNullOrEmpty() &&
                    !dialogBinding.inputWeight.text.isNullOrEmpty() &&
                    dialogBinding.inputScore.text.toString().isDouble() &&
                    dialogBinding.inputMaxScore.text.toString().isDouble() &&
                    dialogBinding.inputWeight.text.toString().isDouble()
                ) {
                    val title = dialogBinding.inputAssignmentName.text.toString()
                    val score = dialogBinding.inputScore.text.toString().toDouble().toString()
                    val maxScore = dialogBinding.inputMaxScore.text.toString().toDouble().toString()
                    val weight = dialogBinding.inputWeight.text.toString().toDouble().toString()
                    val percentage = if (maxScore == "N/A" || score == "N/A") {
                        "N/A"
                    } else
                        (round(score.toDouble() / maxScore.toDouble() * 10000) / 100).toString()
                    val weightedScore = if (weight == "N/A" || score == "N/A") {
                        "N/A"
                    } else
                        (score.toDouble() * weight.toDouble()).toString()
                    val weightedTotalPoints = if (weight == "N/A" || maxScore == "N/A") {
                        "N/A"
                    } else
                        (maxScore.toDouble() * weight.toDouble()).toString()
                    if (assignment != null) {
                        assignmentsAdapter.editAssignment(
                            Assignment(
                                assignment.comment,
                                assignment.date_assigned,
                                assignment.date_due,
                                maxScore,
                                percentage,
                                score,
                                title,
                                maxScore,
                                category,
                                weight,
                                weightedTotalPoints,
                                weightedScore
                            ), position
                        )
                    } else {
                        assignmentsAdapter.addAssignment(
                            Assignment(
                                null,
                                "",
                                "",
                                maxScore,
                                percentage,
                                score,
                                title,
                                maxScore,
                                category,
                                weight,
                                weightedTotalPoints,
                                weightedScore
                            )
                        )
                    }
                    reCalculateAverage()
                    builder.dismiss()
                }
            }
        }

        builder.setView(dialogBinding.root)
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    private fun String.isDouble(): Boolean {
        return when (toDoubleOrNull()) {
            null -> false
            else -> true
        }
    }
}