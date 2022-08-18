package com.phytal.sarona.ui.courses

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.google.android.material.snackbar.Snackbar
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.databinding.FragmentCoursesBinding
import com.phytal.sarona.ui.base.ScopedFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CoursesFragment : ScopedFragment(), KodeinAware, CoursesAdapter.CourseAdapterListener {
    private val args: CoursesFragmentArgs by navArgs()
    private val markingPeriod: Int by lazy(LazyThreadSafetyMode.NONE) { args.markingPeriod }
    private var currentMp: Int = 0
    private var maxMp: Int = 4
    override val kodein by closestKodein()
    private val currentCourseViewModelFactory by instance<CurrentCourseViewModelFactory>()
    private val mpCourseViewModelFactory by instance<MpCourseViewModelFactory>()
    private lateinit var mpCourseViewModel: MpCourseViewModel
    private lateinit var currentCourseViewModel: CurrentCourseViewModel
    private val adapter = CoursesAdapter(this)
    private lateinit var binding: FragmentCoursesBinding
    private var updatedCourses = 0
    private var refreshed = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        if (markingPeriod != 0)
            currentMp = markingPeriod

        binding.backBtn.setOnClickListener {
            if (currentMp - 1 > 0) {
                val directions =
                    CoursesFragmentDirections.actionGlobalCoursesFragment(currentMp - 1)
                findNavController().navigate(directions, navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
            } else
                Snackbar.make(binding.root, "Reached the minimum marking period!", Snackbar.LENGTH_SHORT).show()
        }
        binding.forwardBtn.setOnClickListener {
            if (currentMp + 1 <= maxMp) {
                val directions =
                    CoursesFragmentDirections.actionGlobalCoursesFragment(currentMp + 1)
                findNavController().navigate(directions, navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
            } else
                Snackbar.make(binding.root, "Reached the maximum marking period!", Snackbar.LENGTH_SHORT).show()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshMp(currentMp)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        currentCourseViewModel =
            ViewModelProvider(this, currentCourseViewModelFactory).get(CurrentCourseViewModel::class.java)
        mpCourseViewModel =
            ViewModelProvider(this, mpCourseViewModelFactory).get(MpCourseViewModel::class.java)
        bindUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun bindUI() = launch(Main) {
        mpCourseViewModel.setMp(currentMp)

        if (currentMp != 0) {
            val courses = mpCourseViewModel.mpCourses.await()
            courses.observe(viewLifecycleOwner, Observer {
                binding.groupLoading.visibility = View.GONE
                if (it == null) { Snackbar.make(binding.root,
                    "Failed to fetch marking period $currentMp", Snackbar.LENGTH_LONG).show()
                    return@Observer
                }

                adapter.setCourses(it)
                if (updatedCourses in 2 downTo 1) {
                    Snackbar.make(binding.root, "Successfully loaded new data!", Snackbar.LENGTH_SHORT).show()
                    updatedCourses++
                }
            })
        } else {
            val currentCourses = currentCourseViewModel.currentCourses.await()
            currentCourses.observe(viewLifecycleOwner, Observer {
                binding.groupLoading.visibility = View.GONE
                if (it == null) {
                    Snackbar.make(binding.root, "Failed to fetch current marking period", Snackbar.LENGTH_LONG).show()
                    return@Observer
                }
                currentMp = it.mp

                adapter.setCourses(it)
                binding.swipeRefreshLayout.isRefreshing = false
                if (updatedCourses in 2 downTo 1)
                    Snackbar.make(binding.root, "Successfully loaded new data!", Snackbar.LENGTH_SHORT
                    ).show()
                updatedCourses++
            })
        }
    }

    private fun refreshMp(mp: Int) = launch(Main) {
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (refreshed == 0) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Snackbar.make(binding.root,
                        "Failed to fetch marking period $currentMp", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        timer.start()

        mpCourseViewModel.setMp(mp)
        val mpCourses = mpCourseViewModel.mpCourses.await()
        if (view != null) {
            mpCourses.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer
                if (refreshed > 0 && currentMp != 0) {
                    adapter.setCourses(it)
                    binding.swipeRefreshLayout.isRefreshing = false
                    Snackbar.make(binding.root, "Successfully loaded new data!", Snackbar.LENGTH_SHORT)
                        .show()
                }
                refreshed++
            })
        }
    }

    override fun onCourseClick(cardView: View, course: Course) {
//        val courseCardDetailTransitionName = getString(R.string.course_card_detail_transition_name)
//        val extras = FragmentNavigatorExtras(cardView to courseCardDetailTransitionName)
        if (course.assignments.isEmpty())
            return
        updatedCourses = 0
        val directions = CoursesFragmentDirections.actionNavCoursesToNavCourseView(course)
        findNavController().navigate(directions,
            navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.fade_out
                    popEnter = R.anim.fade_in
                    popExit = R.anim.slide_out_right
                }
            })
    }
}
