package com.phytal.sarona.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.CurrentCourse
import com.phytal.sarona.data.db.entities.CurrentCourseList

class CourseListAdapter : RecyclerView.Adapter<CourseListAdapter.CourseHolder>() {

    private var courses: List<CurrentCourse> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CourseHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item, parent, false)
        return CourseHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: CourseHolder, position: Int) {
        val currentCourse = courses[position]
        holder.textViewTitle.text = currentCourse.courseName
        holder.textViewDescription.setText(currentCourse.courseId)
        holder.textViewPriority.text = currentCourse.courseAverage.toString()
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun setCourses(courses: CurrentCourseList) {
        this.courses = courses.courses
        notifyDataSetChanged()
    }

    class CourseHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        val textViewPriority: TextView = itemView.findViewById(R.id.text_view_grade)
    }
}
