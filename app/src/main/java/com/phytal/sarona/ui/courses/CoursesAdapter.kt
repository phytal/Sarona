package com.phytal.sarona.ui.courses

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.db.entities.MarkingPeriod
import com.phytal.sarona.databinding.CourseItemBinding

class CoursesAdapter(
    private val listener: CourseAdapterListener
) : RecyclerView.Adapter<CoursesAdapter.CourseHolder>() {

    interface CourseAdapterListener {
        fun onCourseClick(cardView: View, course: Course)
    }

    private var courses: List<Course> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseHolder {
        return CourseHolder(
            CourseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: CourseHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCourses(mp: MarkingPeriod) {
        this.courses = mp.courses

        notifyDataSetChanged()
    }

    class CourseHolder(
        private val binding: CourseItemBinding,
        listener: CourseAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.run {
                this.listener = listener
            }
        }

        fun bind(course: Course) {
            binding.textViewTitle.text = course.name
            binding.textViewDescription.text = course.course
            binding.textViewGrade.text = String.format("%.2f", course.average)
            binding.course = course
        }
    }
}
