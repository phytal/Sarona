package com.phytal.sarona.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.adapters.CourseListAdapter
import com.phytal.sarona.data.CourseViewModel
import com.phytal.sarona.requests.HacApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ClassesFragment : Fragment() {

    private lateinit var classesViewModel: CourseViewModel
    private var disposable: Disposable? = null

    private val hacApiServe by lazy {
        HacApiService.create()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_classes, container, false)

        val progressBar : ProgressBar = root.findViewById(R.id.progress_bar)
        beginSearch("https://hac.friscoisd.org", "", "")
        progressBar.visibility = View.GONE

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adapter = CourseListAdapter()
        recyclerView.adapter = adapter

        classesViewModel =
            ViewModelProvider(this).get(CourseViewModel::class.java)
        classesViewModel.getAllCourses()
            .observe(viewLifecycleOwner,
                Observer { courses -> adapter.setCourses(courses) })
        return root
    }
    private fun beginSearch(hacLink: String, username: String, password: String) {
        disposable =
            hacApiServe.getAllCourses(hacLink, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> Log.e("f", result.currentAssignmentList[0].courseName) },
                    { error -> Log.e("f",  error.message!!) }
                )
    }


    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
