package com.phytal.sarona.ui.assignments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.data.db.entities.Assignment
import com.phytal.sarona.databinding.AssignmentItemBinding

class AssignmentsAdapter(
    private val listener: AssignmentAdapterListener
) : RecyclerView.Adapter<AssignmentsAdapter.AssignmentHolder>() {

    interface AssignmentAdapterListener {
        fun onAssignmentClick(cardView: View, assignment: Assignment)
    }

    private var assignments : List<Assignment> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): AssignmentHolder {
        return AssignmentHolder (
            AssignmentItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(@NonNull holder: AssignmentHolder, position: Int) {
        holder.bind(assignments[position])
    }

    override fun getItemCount(): Int {
        return assignments.size
    }

    fun setAssignments(assignments: List<Assignment>) {
        this.assignments = assignments
        notifyDataSetChanged()
    }

    class AssignmentHolder (
        private val binding: AssignmentItemBinding,
        listener: AssignmentAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.run {
                this.listener = listener
            }
        }

        fun bind(assignment: Assignment) {
            binding.textViewTitle.text = assignment.title_of_assignment
            binding.textViewDescription.text = assignment.date_due
            binding.textViewGrade.text = String.format("%.2f", assignment.score)
            val maxPointsString = "/" + String.format("%.2f", assignment.max_points)
            binding.textViewMaxGrade.text = maxPointsString
            binding.assignment = assignment
        }
    }
}
