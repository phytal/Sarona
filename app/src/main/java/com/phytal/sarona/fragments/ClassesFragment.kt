package com.phytal.sarona.fragments

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.adapters.CourseListAdapter
import com.phytal.sarona.data.CourseViewModel
import com.phytal.sarona.data.entities.Course


class ClassesFragment : Fragment() {

    private lateinit var classesViewModel: CourseViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_classes, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adapter = CourseListAdapter()
        recyclerView.adapter = adapter

        classesViewModel =
            ViewModelProvider(this).get(CourseViewModel::class.java)
        classesViewModel.getAllCourses()
            .observe(viewLifecycleOwner,
                Observer { notes -> adapter.setCourses(notes) })
        return root
    }
}
