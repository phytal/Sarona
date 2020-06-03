package com.phytal.sarona.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.ui.adapters.CourseListAdapter
import com.phytal.sarona.data.network.ConnectivityInterceptorImpl
import com.phytal.sarona.data.network.CourseNetworkDataSourceImpl
import com.phytal.sarona.data.network.HacApiService
import com.phytal.sarona.ui.base.ScopedFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_classes.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ClassesFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<CurrentCourseViewModelFactory>()

    val JOB_TIMEOUT = 15000L
    private lateinit var viewModel: CourseViewModel
    private var disposable: Disposable? = null
    private lateinit var courseNetworkDataSource: CourseNetworkDataSourceImpl
    private lateinit var text: TextView

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

        val progressBar : ProgressBar = root.findViewById(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adapter = CourseListAdapter()
        recyclerView.adapter = adapter

//        classesViewModel =
//            ViewModelProvider(this).get(CourseViewModel::class.java)
//        classesViewModel.getAllCourses()
//            .observe(viewLifecycleOwner,
//                Observer { courses -> adapter.setCourses(courses) })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CourseViewModel::class.java)

        bindUI()
    }

    private suspend fun getCourseData(hacLink: String, username: String, password: String) {
        withContext(IO) {
            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                courseNetworkDataSource.fetchCurrent(hacLink, username, password)
            }
            if(job == null){
                val cancelMessage = " Cancelling job.. Job took longer than $JOB_TIMEOUT ms"
                println("debug: $cancelMessage")
                setNewTextOnMainThread(cancelMessage)
            }
        }
    }

    private fun bindUI() = launch{
        val currentCourses = viewModel.courses.await()
        currentCourses.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            val f = it
            textView2.text = it.toString()
        })
    }

    private suspend fun setNewTextOnMainThread(input: String) {
        withContext(Main) {
            text.text = input
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
