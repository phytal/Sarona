package com.phytal.sarona.ui.courses

import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import com.phytal.sarona.R
import com.phytal.sarona.data.db.entities.Course
import com.phytal.sarona.databinding.FragmentCoursesBinding
import com.phytal.sarona.ui.MainActivity
import com.phytal.sarona.ui.base.ScopedFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CoursesFragment : ScopedFragment(), KodeinAware, CoursesAdapter.CourseAdapterListener {
    private val args: CoursesFragmentArgs by navArgs()
    private val markingPeriod: Int by lazy(LazyThreadSafetyMode.NONE) { args.markingPeriod }
    private var currentMp: Int = -1
    private var maxMp: Int = -1
    override val kodein by closestKodein()
    private val currentCourseViewModelFactory by instance<CurrentCourseViewModelFactory>()
    private val pastCourseViewModelFactory by instance<OtherCourseViewModelFactory>()
    private val mpCourseViewModelFactory by instance<MpCourseViewModelFactory>()
    private lateinit var currentCourseViewModel: CurrentCourseViewModel
    private lateinit var pastCourseViewModel: PastCourseViewModel
    private lateinit var mpCourseViewModel: MpCourseViewModel
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

        binding.backBtn.setOnClickListener {
            var mp = markingPeriod
            if (mp == 0)
                mp = currentMp
            if (mp - 1 > 0) {
                val directions =
                    CoursesFragmentDirections.actionGlobalCoursesFragment(mp - 1)
                findNavController().navigate(directions, navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
            } else
                Snackbar.make(
                    binding.root,
                    "Reached the minimum marking period!",
                    Snackbar.LENGTH_SHORT
                ).show()
        }
        binding.forwardBtn.setOnClickListener {
            var mp = markingPeriod
            if (mp == 0)
                mp = currentMp
            if (mp + 1 <= maxMp) {
                val directions =
                    CoursesFragmentDirections.actionGlobalCoursesFragment(mp + 1)
                findNavController().navigate(directions, navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
            } else
                Snackbar.make(
                    binding.root,
                    "Reached the maximum marking period!",
                    Snackbar.LENGTH_SHORT
                ).show()
        }

        activity?.findViewById<BottomAppBar>(R.id.bottom_app_bar)?.setOnMenuItemClickListener {
            var mp = markingPeriod
            if (mp == 0)
                mp = currentMp
            when (it.itemId) {
                R.id.menu_refresh ->
                    refreshMp(mp).start()
                else -> super.onOptionsItemSelected(it)
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            var mp = markingPeriod
            if (mp == 0)
                mp = currentMp
            refreshMp(mp - 1)
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
        mpCourseViewModel =
            ViewModelProvider(this, mpCourseViewModelFactory).get(MpCourseViewModel::class.java)
        bindUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun bindUI() = launch(Main) {
        val currentCourses = currentCourseViewModel.currentCourses.await()
        val otherCourses = pastCourseViewModel.pastCourses.await()

        currentCourses.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Snackbar.make(binding.root, "Failed to fetch current courses", Snackbar.LENGTH_LONG).show()
                return@Observer
            }
            currentMp = it.mp
            maxMp = it.mp
            binding.groupLoading.visibility = View.GONE
            if (markingPeriod == 0 || markingPeriod == currentMp) {
                adapter.setCourses(it)
                if ((markingPeriod == 0 && updatedCourses == 1) || (markingPeriod == currentMp && updatedCourses == maxMp))
                    Snackbar.make(binding.root, "Successfully loaded new data!", Snackbar.LENGTH_SHORT).show()
                updatedCourses++
            }
        })

        otherCourses.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Snackbar.make(binding.root, "Failed to fetch other courses", Snackbar.LENGTH_LONG).show()
                return@Observer
            }
            if (it.isNotEmpty()) {
                maxMp = maxMp.coerceAtLeast(it[it.size - 1].mp)
                if (markingPeriod != currentMp && markingPeriod != 0) {
                    if (markingPeriod < currentMp)
                        adapter.setCourses(it[markingPeriod-1])
                    else
                        adapter.setCourses(it[markingPeriod-2])
                }
            }
        })
    }

    private fun refreshMp(mp: Int) = launch(Main) {
        mpCourseViewModel.setMp(mp)
        val mpCourses = mpCourseViewModel.mpCourses.await()
        if (view != null) {
            mpCourses.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer
                if (refreshed < maxMp) {
                    refreshed++
                    return@Observer
                }
                adapter.setCourses(it)
                binding.swipeRefreshLayout.isRefreshing = false
                Snackbar.make(binding.root, "Successfully loaded new data!", Snackbar.LENGTH_SHORT)
                    .show()
            })
        }
    }

    private suspend fun setNewTextOnMainThread(input: String) {
        withContext(Main) {
            binding.textViewLoading.text = input
        }
    }

    override fun onCourseClick(cardView: View, course: Course) {
//        val courseCardDetailTransitionName = getString(R.string.course_card_detail_transition_name)
//        val extras = FragmentNavigatorExtras(cardView to courseCardDetailTransitionName)
        if (course.assignments.isEmpty())
            return
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
