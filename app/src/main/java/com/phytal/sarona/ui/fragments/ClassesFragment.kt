package com.phytal.sarona.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.ui.adapters.CourseListAdapter
import com.phytal.sarona.data.CourseViewModel
import com.phytal.sarona.data.network.ConnectivityInterceptorImpl
import com.phytal.sarona.data.network.CourseNetworkDataSourceImpl
import com.phytal.sarona.data.network.HacApiService
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_classes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ClassesFragment : Fragment() {

    private lateinit var classesViewModel: CourseViewModel
    private var disposable: Disposable? = null

    private val hacApiServe by lazy {
        HacApiService(
            ConnectivityInterceptorImpl(
                this.requireContext()
            )
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_classes, container, false)
        val courseNetworkDataSource = CourseNetworkDataSourceImpl(hacApiServe)
        val progressBar : ProgressBar = root.findViewById(R.id.progress_bar)

        progressBar.visibility = View.VISIBLE

        courseNetworkDataSource.downloadedCurrentCourse.observe(viewLifecycleOwner, Observer {
            textView2.text = it.toString()
            progressBar.visibility = View.GONE
        })
        //TODO: Make timeout and disposable
        CoroutineScope(Dispatchers.Default).launch {
            courseNetworkDataSource.fetchCurrent("https://hac.friscoisd.org", "Zhang.W", "William826")
        }

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

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
