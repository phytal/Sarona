package com.phytal.sarona.ui.assignments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.GradeDiffCallback
import com.phytal.sarona.data.db.entities.GradeType
import com.phytal.sarona.databinding.GradeItemBinding
import kotlin.math.roundToInt

class GradeTypeAdapter: ListAdapter<GradeType, GradeTypeHolder>(GradeDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeTypeHolder {
        return GradeTypeHolder(
            GradeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GradeTypeHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class GradeTypeHolder (
    private val binding: GradeItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(gradeType: GradeType) {
        binding.gradeCategory.text = gradeType.category
        binding.gradeAverage.text = String.format("%.2f", ((gradeType.grade * 100).roundToInt()/100.0))
        val weightString = "Weight: " + gradeType.weight
        binding.gradeWeight.text = weightString
        binding.gradeType = gradeType
        //TODO: un-hardcode colors
        binding.assignmentCard.strokeColor = when (gradeType.category) {
            "Major Grades" -> ContextCompat.getColor(binding.root.context, R.color.grades_1)
            "Minor Grades" -> ContextCompat.getColor(binding.root.context, R.color.grades_2)
            "Non-graded" -> ContextCompat.getColor(binding.root.context, R.color.grades_3)
            else -> ContextCompat.getColor(binding.root.context, R.color.grades_4)
        }
    }
}

