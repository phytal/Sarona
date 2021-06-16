package com.phytal.sarona.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.db.entities.CurrentCourseList
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
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class ClassesFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<CurrentCourseViewModelFactory>()

    val JOB_TIMEOUT = 15000L
    private lateinit var viewModel: CourseViewModel
    private var disposable: Disposable? = null
    private lateinit var courseNetworkDataSource: CourseNetworkDataSourceImpl
    private lateinit var adapter: CourseListAdapter

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

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        adapter = CourseListAdapter()
        recyclerView.adapter = adapter

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

            group_loading.visibility = View.GONE
//            updateLastUpdated(ZonedDateTime.now())

            adapter.setCourses(it)
        })
    }

//    private fun updateLastUpdated(time: ZonedDateTime) {
//        val formatter = DateTimeFormatter.ofPattern("MM/sarona_logo HH:mm")
//        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Last updated ${time.format(formatter)}"
//    }

    private suspend fun setNewTextOnMainThread(input: String) {
        withContext(Main) {
            textView_loading.text = input
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
