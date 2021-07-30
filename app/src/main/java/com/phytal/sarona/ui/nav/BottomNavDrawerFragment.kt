package com.phytal.sarona.ui.nav

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.shape.MaterialShapeDrawable
import com.phytal.sarona.R
import com.phytal.sarona.databinding.FragmentBottomNavDrawerBinding
import com.phytal.sarona.util.themeColor
import kotlin.LazyThreadSafetyMode.NONE

/**
 * A [Fragment] which acts as a bottom navigation drawer.
 */
class BottomNavDrawerFragment :
    Fragment(),
    NavigationAdapter.NavigationAdapterListener {

    private lateinit var binding: FragmentBottomNavDrawerBinding

    private val behavior: BottomSheetBehavior<FrameLayout> by lazy(NONE) {
        from(binding.backgroundContainer)
    }

    private val bottomSheetCallback = BottomNavigationDrawerCallback()

    private val sandwichSlideActions = mutableListOf<OnSandwichSlideAction>()

    private val navigationListeners: MutableList<NavigationAdapter.NavigationAdapterListener> =
      mutableListOf()

    private val backgroundShapeDrawable: MaterialShapeDrawable by lazy(NONE) {
        val backgroundContext = binding.backgroundContainer.context
        MaterialShapeDrawable(
            backgroundContext,
            null,
            R.attr.bottomSheetStyle,
            0
        ).apply {
            when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    fillColor = ColorStateList.valueOf(
                        requireContext().getColor(R.color.sarona_white_300)
                    )
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    fillColor = ColorStateList.valueOf(
                        backgroundContext.themeColor(R.attr.colorPrimarySurfaceVariant)
                    )
                }
            }

            elevation = resources.getDimension(R.dimen.plane_08)
            initializeElevationOverlay(requireContext())
        }
    }

    private val foregroundShapeDrawable: MaterialShapeDrawable by lazy(NONE) {
        val foregroundContext = binding.foregroundContainer.context
        MaterialShapeDrawable(
            foregroundContext,
            null,
            R.attr.bottomSheetStyle,
            0
        ).apply {
            when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    fillColor = ColorStateList.valueOf(
                        requireContext().getColor(R.color.sarona_white_50)
                    )
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    fillColor = ColorStateList.valueOf(
                        foregroundContext.themeColor(R.attr.colorPrimarySurface)
                    )
                }
            }

            elevation = resources.getDimension(R.dimen.plane_16)
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER
            initializeElevationOverlay(requireContext())
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setTopEdge(
                    SemiCircleEdgeCutoutTreatment(
                        resources.getDimension(R.dimen.grid_1),
                        resources.getDimension(R.dimen.grid_3),
                        0F,
                        resources.getDimension(R.dimen.navigation_drawer_profile_image_size_padded)
                    )
                )
                .build()
        }
    }

    private val closeDrawerOnBackPressed = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            close()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, closeDrawerOnBackPressed)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomNavDrawerBinding.inflate(inflater, container, false)
        binding.foregroundContainer.setOnApplyWindowInsetsListener { view, windowInsets ->
            // Record the window's top inset so it can be applied when the bottom sheet is slide up
            // to meet the top edge of the screen.
            view.setTag(
                R.id.tag_system_window_inset_top,
                windowInsets.systemWindowInsetTop
            )
            windowInsets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            backgroundContainer.background = backgroundShapeDrawable
            foregroundContainer.background = foregroundShapeDrawable

            scrimView.setOnClickListener { close() }

            bottomSheetCallback.apply {
                // Scrim view transforms
                addOnSlideAction(AlphaSlideAction(scrimView))
                addOnStateChangedAction(VisibilityStateAction(scrimView))
                // Recycler transforms
                addOnStateChangedAction(ScrollToTopStateAction(navRecyclerView))
                // Close the sandwiching account picker if open
                // If the drawer is open, pressing the system back button should close the drawer.
                addOnStateChangedAction(object : OnStateChangedAction {
                    override fun onStateChanged(sheet: View, newState: Int) {
                        closeDrawerOnBackPressed.isEnabled = newState != STATE_HIDDEN
                    }
                })
            }

            behavior.addBottomSheetCallback(bottomSheetCallback)
            behavior.state = STATE_HIDDEN

            val adapter = NavigationAdapter(this@BottomNavDrawerFragment)

            navRecyclerView.adapter = adapter
            NavigationModel.navigationList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            NavigationModel.setNavigationMenuItemChecked(0)
        }
    }

    fun toggle() {
        when (behavior.state) {
            STATE_HIDDEN -> open()
            STATE_HALF_EXPANDED,
            STATE_EXPANDED,
            STATE_COLLAPSED -> close()
        }
    }

    fun open() {
        behavior.state = STATE_HALF_EXPANDED
    }

    fun close() {
        behavior.state = STATE_HIDDEN
    }

    fun addOnSlideAction(action: OnSlideAction) {
        bottomSheetCallback.addOnSlideAction(action)
    }

    fun addOnStateChangedAction(action: OnStateChangedAction) {
        bottomSheetCallback.addOnStateChangedAction(action)
    }

    fun addNavigationListener(listener: NavigationAdapter.NavigationAdapterListener) {
        navigationListeners.add(listener)
    }

    /**
     * Add actions to be run when the slide offset (animation progress) or the sandwiching account
     * picker has changed.
     */
    fun addOnSandwichSlideAction(action: OnSandwichSlideAction) {
        sandwichSlideActions.add(action)
    }

    override fun onNavMenuItemClicked(item: NavigationModelItem.NavMenuItem) {
        NavigationModel.setNavigationMenuItemChecked(item.id)
        close()
        navigationListeners.forEach { it.onNavMenuItemClicked(item) }
    }
}