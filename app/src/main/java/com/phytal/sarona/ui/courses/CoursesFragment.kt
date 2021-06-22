package com.phytal.sarona.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.data.network.ConnectivityInterceptorImpl
import com.phytal.sarona.data.network.HacApiService
import com.phytal.sarona.databinding.FragmentCoursesBinding
import com.phytal.sarona.ui.adapters.CoursesAdapter
import com.phytal.sarona.ui.base.ScopedFragment
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CoursesFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<CurrentCourseViewModelFactory>()

    private lateinit var viewModel: CourseViewModel
    private var disposable: Disposable? = null
//    private lateinit var courseNetworkDataSource: CourseNetworkDataSourceImpl
    private lateinit var adapter: CoursesAdapter
    private lateinit var binding: FragmentCoursesBinding


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
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater)

        val root = binding.root
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        adapter = CoursesAdapter(object : CoursesAdapter.OnItemClickListener {
            override fun onItemClick(course : Course) {
                Toast.makeText(context, "Item Clicked", Toast.LENGTH_LONG).show()
            }
        }) //TODO: navigate to CourseFragment

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // init onClickListeners
        val backBtn: ImageView = root.findViewById(R.id.back_btn) as ImageView
        backBtn.setOnClickListener {
            Toast.makeText(context, "Back Button Clicked", Toast.LENGTH_LONG).show()
        }

        val forBtn: ImageView = root.findViewById(R.id.forward_btn) as ImageView
        forBtn.setOnClickListener {
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CourseViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch{
        val currentCourses = viewModel.courses.await()
        currentCourses.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            binding.groupLoading.visibility = View.GONE
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
            binding.textViewLoading.text = input
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
