package com.phytal.sarona.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
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
    private var maxMp: Int = -1
    override val kodein by closestKodein()
    private val currentCourseViewModelFactory by instance<CurrentCourseViewModelFactory>()
    private val pastCourseViewModelFactory by instance<PastCourseViewModelFactory>()
    private lateinit var currentCourseViewModel: CurrentCourseViewModel
    private lateinit var pastCourseViewModel: PastCourseViewModel
    private val adapter = CoursesAdapter(this)
    private lateinit var binding: FragmentCoursesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        binding.backBtn.setOnClickListener {
            // markingPeriod is 0, recyclerView already shows most recent mp
            var mp = markingPeriod
            if (mp == 0)
                mp = maxMp
            if (mp - 1 > 0) {
                val directions =
                    CoursesFragmentDirections.actionGlobalCoursesFragment(mp - 1)
                findNavController().navigate(directions, navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
            }
        }
        binding.forwardBtn.setOnClickListener {
            var mp = markingPeriod
            if (mp == 0)
                mp = maxMp
            if (mp + 1 <= maxMp) {
                val directions =
                    CoursesFragmentDirections.actionGlobalCoursesFragment(mp + 1)
                findNavController().navigate(directions, navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        currentCourseViewModel =
            ViewModelProvider(
                this,
                currentCourseViewModelFactory
            ).get(CurrentCourseViewModel::class.java)
        pastCourseViewModel =
            ViewModelProvider(this, pastCourseViewModelFactory).get(PastCourseViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch(Main) {
        val currentCourses = currentCourseViewModel.currentCourses.await()
        val pastCourses = pastCourseViewModel.pastCourses.await()

        currentCourses.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            maxMp = it.mp
            binding.groupLoading.visibility = View.GONE
            if (markingPeriod == 0 || markingPeriod == maxMp)
                adapter.setCourses(it)
        })

        pastCourses.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            if (markingPeriod in 1 until maxMp)
                adapter.setCourses(it[markingPeriod - 1])
        })
    }

    private suspend fun setNewTextOnMainThread(input: String) {
        withContext(Main) {
            binding.textViewLoading.text = input
        }
    }

    override fun onCourseClick(cardView: View, course: Course) {
//        val courseCardDetailTransitionName = getString(R.string.course_card_detail_transition_name)
//        val extras = FragmentNavigatorExtras(cardView to courseCardDetailTransitionName)
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
