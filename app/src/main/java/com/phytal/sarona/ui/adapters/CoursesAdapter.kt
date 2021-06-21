package com.phytal.sarona.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.db.entities.CourseList

class CoursesAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CoursesAdapter.CourseHolder>() {

    interface OnItemClickListener {
        fun onItemClick(course : Course)
    }

    private var courses : List<Course> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CourseHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item, parent, false)
        return CourseHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: CourseHolder, position: Int) {
        val course = courses[position]
        holder.textViewTitle.text = course.name
        holder.textViewDescription.text = course.course
        holder.textViewPriority.text = course.average.toString()
        holder.itemView.setOnClickListener { listener }
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun setCourses(courses: CourseList) {
        this.courses = courses.yearCourses[0]
        notifyDataSetChanged()
    }

    class CourseHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        val textViewPriority: TextView = itemView.findViewById(R.id.text_view_grade)
    }
}
