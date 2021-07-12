package com.phytal.sarona.ui.assignments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.data.db.entities.Assignment
import com.phytal.sarona.databinding.AssignmentItemBinding

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
            var score = assignment.score
            if (assignment.score.matches(Regex("\\d+"))) {
                score = String.format("%.2f", score)
            }
            binding.textViewGrade.text = score
            val maxPointsString = "/" + String.format("%.2f", assignment.max_points)
            binding.textViewMaxGrade.text = maxPointsString
            binding.assignmentCard.strokeColor = when (assignment.type_of_grade) {
                "Major Grades" -> Color.parseColor("#b37bfc")
                "Minor Grades" -> Color.parseColor("#7bc4fc")
                "Non-graded" -> Color.parseColor("#fcb37b")
                else -> Color.parseColor("#c4fc7b")
            }
        }
    }
}
