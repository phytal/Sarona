package com.phytal.sarona.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.phytal.sarona.ui.nav.*
import com.phytal.sarona.ui.welcome.LoginFragmentDirections
import com.phytal.sarona.util.contentView
import java.util.*


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
        if (item.destination == Destinations.ABOUT) {
            val url = "https://linktr.ee/simplegrade"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        else if (item.destination == Destinations.LOGOUT) {
            val loginSharedPref = this.getSharedPreferences(
                getString(R.string.login_preference_file_key),
                Context.MODE_PRIVATE
            )

            if (loginSharedPref != null) {
                with(loginSharedPref.edit()) {
                    clear()
                    apply()
                }
            }
            val directions =
                LoginFragmentDirections.actionGlobalLoginFragment()
            findNavController(R.id.nav_host_fragment).navigate(directions)
            Toast.makeText(this, "Successfully logged out!", Toast.LENGTH_LONG).show()
        }
        else
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
            else -> R.id.nav_courses
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
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_settings -> {
                bottomNavDrawer.close()
                showDarkThemeMenu()
            }
        }
        return true
    }

    private fun onDarkThemeMenuItemSelected(itemId: Int): Boolean {
        val sharedPref = this.getSharedPreferences(
            getString(R.string.user_preference_file_key),
            Context.MODE_PRIVATE
        )

        val nightMode = when (itemId) {
            R.id.menu_light -> AppCompatDelegate.MODE_NIGHT_NO
            R.id.menu_dark -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        if (sharedPref != null) {
            with(sharedPref.edit()) {
                putString(
                    getString(R.string.saved_theme_key),
                    itemId.toString()
                )
                apply()
            }
        }

        delegate.localNightMode = nightMode
        return true
    }

    private fun showDarkThemeMenu() {
        MenuBottomSheetDialogFragment(R.menu.dark_theme_bottom_sheet_menu) {
            onDarkThemeMenuItemSelected(it.itemId)
        }.show(supportFragmentManager, null)
    }
}
