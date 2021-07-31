package com.phytal.sarona.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.transition.MaterialFadeThrough
import com.phytal.sarona.R
import com.phytal.sarona.databinding.ActivityMainBinding
import com.phytal.sarona.ui.courses.CoursesFragment
import com.phytal.sarona.ui.nav.*
import com.phytal.sarona.util.contentView


class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener,
    NavController.OnDestinationChangedListener, NavigationAdapter.NavigationAdapterListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    val binding: ActivityMainBinding by contentView(R.layout.activity_main)
    private val bottomNavDrawer: BottomNavDrawerFragment by lazy(LazyThreadSafetyMode.NONE) {
        supportFragmentManager.findFragmentById(R.id.bottom_nav_drawer) as BottomNavDrawerFragment
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Sarona_DayNight)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setUpBottomNavigation()
    }

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    private fun setUpBottomNavigation() {
        // Wrap binding.run to ensure ContentViewBindingDelegate is calling this Activity's
        // setContentView before accessing views
        binding.run {
            findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener(
                this@MainActivity
            )
        }

        bottomNavDrawer.apply {
            addOnSlideAction(HalfClockwiseRotateSlideAction(binding.bottomAppBarChevron))
            addOnSlideAction(AlphaSlideAction(binding.bottomAppBarTitle, true))
            addOnStateChangedAction(ChangeSettingsMenuStateAction { showSettings ->
                // Toggle between the current destination's BAB menu and the menu which should
                // be displayed when the BottomNavigationDrawer is open.
                binding.bottomAppBar.replaceMenu(
                    if (showSettings) {
                        R.menu.bottom_app_bar_settings_menu
                    } else {
                        R.menu.bottom_app_bar_menu
                    }
                )
            })

            addOnSandwichSlideAction(HalfCounterClockwiseRotateSlideAction(binding.bottomAppBarChevron))
            addNavigationListener(this@MainActivity)
        }

        // Set up the BottomAppBar menu
        binding.bottomAppBar.apply {
            setNavigationOnClickListener {
                bottomNavDrawer.toggle()
            }
            setOnMenuItemClickListener(this@MainActivity)
        }

        // Set up the BottomNavigationDrawer's open/close affordance
        binding.bottomAppBarContentContainer.setOnClickListener {
            bottomNavDrawer.toggle()
        }
    }

    private fun setBottomAppBarForHome(@MenuRes menuRes: Int) {
        binding.run {
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.replaceMenu(menuRes)
            bottomAppBarTitle.visibility = View.VISIBLE
            bottomAppBar.performShow()
        }
    }

    private fun hideBottomAppBar() {
        binding.run {
            bottomAppBar.performHide()
            // Get a handle on the animator that hides the bottom app bar so we can wait to hide
            // the bottom app bar until after it's exit animation finishes.
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    bottomAppBar.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    override fun onNavMenuItemClicked(item: NavigationModelItem.NavMenuItem) {
        navigateToHome(item.titleRes, item.destination)
    }

    private fun navigateToHome(@StringRes titleRes: Int, destination: Destinations) {
        binding.bottomAppBarTitle.text = getString(titleRes)
        currentNavigationFragment?.apply {
            exitTransition = MaterialFadeThrough().apply {
                duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            }
        }
        val directions = when (destination) {
            Destinations.CLASSES -> R.id.nav_courses
            Destinations.GPA -> R.id.nav_gpa
            Destinations.GRADES -> R.id.nav_grades
        }
        findNavController(R.id.nav_host_fragment).navigate(directions)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.nav_welcome -> {
                hideBottomAppBar()
            }
            R.id.nav_login -> {
                hideBottomAppBar()
            }
            R.id.nav_courses -> {
                setBottomAppBarForHome(R.menu.bottom_app_bar_menu)
            }
            R.id.nav_grades -> {
                setBottomAppBarForHome(R.menu.bottom_app_bar_menu)
            }
            R.id.nav_gpa -> {
                setBottomAppBarForHome(R.menu.bottom_app_bar_menu)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_settings -> {
                bottomNavDrawer.close()
                showDarkThemeMenu()
            }
//            R.id.menu_refresh -> {
//                (javaClass )
//            }
        }
        return true
    }

    private fun showDarkThemeMenu() {
        val sharedPref = this.getSharedPreferences(
            getString(R.string.user_preference_file_key),
            Context.MODE_PRIVATE
        )
        MenuBottomSheetDialogFragment(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent.tag == position)
                    return
                parent.tag = position
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Dark" || selectedItem == "Light" || selectedItem == "Default") {
                    val nightMode = when (selectedItem) {
                        "Light" -> AppCompatDelegate.MODE_NIGHT_NO
                        "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }

                    if (sharedPref != null) {
                        with(sharedPref.edit()) {
                            putString(
                                getString(R.string.saved_theme_key),
                                selectedItem
                            )
                            apply()
                        }
                    }
                    delegate.localNightMode = nightMode
                    val prev = supportFragmentManager.findFragmentByTag("settings_menu")
                    if (prev != null) {
                        val df: MenuBottomSheetDialogFragment =
                            prev as MenuBottomSheetDialogFragment
                        df.dismiss()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }, object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent.tag == position)
                    return
                parent.tag = position
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "English" || selectedItem == "Español" || selectedItem == "Français" || selectedItem == "한곡어" || selectedItem == "中文") {
                    // TODO: Change Language

                    if (sharedPref != null) {
                        with(sharedPref.edit()) {
                            putString(
                                getString(R.string.saved_language_key),
                                selectedItem
                            )
                            apply()
                        }
                    }

                    val prev = supportFragmentManager.findFragmentByTag("settings_menu")
                    if (prev != null) {
                        val df: MenuBottomSheetDialogFragment =
                            prev as MenuBottomSheetDialogFragment
                        df.dismiss()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }).show(supportFragmentManager, "settings_menu")
    }
}
