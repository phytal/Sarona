package com.phytal.sarona.ui.assignments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Assignment
import com.phytal.sarona.databinding.AssignmentItemBinding
import java.lang.Math.round
import kotlin.math.roundToInt

class AssignmentsAdapter(
    private val listener: AssignmentAdapterListener
) : RecyclerView.Adapter<AssignmentsAdapter.AssignmentHolder>() {

    interface AssignmentAdapterListener {
        fun onAssignmentClick(cardView: View, position: Int)
        fun onAssignmentLongClick(cardView: View, position: Int)
    }

    var assignments: ArrayList<Assignment> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentHolder {
        return AssignmentHolder(
            AssignmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: AssignmentHolder, position: Int) {
        holder.bind(assignments[position])
    }

    override fun getItemCount(): Int {
        return assignments.size
    }

    fun setAssignmentList(assignments: ArrayList<Assignment>) {
        this.assignments = assignments
        notifyDataSetChanged()
    }

    fun addAssignment(assignment: Assignment) {
        assignments.add(0, assignment)
        notifyItemInserted(0)
    }

    fun editAssignment(assignment: Assignment, position: Int) {
        assignments[position] = assignment
        notifyItemChanged(position)
    }

    class AssignmentHolder(
        private val binding: AssignmentItemBinding,
        val listener: AssignmentAdapterListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onAssignmentClick(v, adapterPosition)
        }

        override fun onLongClick(v: View): Boolean {
            listener.onAssignmentLongClick(v, adapterPosition)
            return true
        }

        fun bind(assignment: Assignment) {
            binding.textViewTitle.text = assignment.title_of_assignment
            binding.textViewDescription.text = assignment.date_due
            val score =
                if (assignment.score.matches(Regex("-?\\d+(\\.\\d+)?")) && assignment.score.toDouble() < 100) {
                    String.format("%.2f", assignment.score.toDouble())
                } else assignment.score
            binding.textViewGrade.text = score
            val maxPointsString = if (assignment.score.matches(Regex("\\d+"))) {
                "/" + String.format("%.2f", assignment.max_points.toDouble())
            } else assignment.max_points
            binding.textViewMaxGrade.text = maxPointsString
            binding.assignmentCard.strokeColor = when (assignment.type_of_grade) {
                "Major Grades" -> ContextCompat.getColor(binding.root.context, R.color.grades_1)
                "Minor Grades" -> ContextCompat.getColor(binding.root.context, R.color.grades_2)
                "Non-graded" -> ContextCompat.getColor(binding.root.context, R.color.grades_3)
                else -> ContextCompat.getColor(binding.root.context, R.color.grades_4)
            }
        }
    }
}
