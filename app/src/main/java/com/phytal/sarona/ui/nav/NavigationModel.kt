package com.phytal.sarona.ui.nav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phytal.sarona.R

/**
 * A class which maintains and generates a navigation list to be displayed by [NavigationAdapter].
 */
object NavigationModel {

    const val CLASSES_ID = 0
    const val ABOUT_ID = 1
    const val LOGOUT_ID = 2

    private var navigationMenuItems = mutableListOf(
        NavigationModelItem.NavMenuItem(
            id = CLASSES_ID,
            icon = R.drawable.ic_twotone_classes,
            titleRes = R.string.nav_courses,
            destination = Destinations.CLASSES,
            checked = false
        ),
        NavigationModelItem.NavMenuItem(
            id = ABOUT_ID,
            icon = R.drawable.ic_twotone_about,
            titleRes = R.string.nav_about,
            destination = Destinations.ABOUT,
            checked = false
        ),
        NavigationModelItem.NavMenuItem(
            id = LOGOUT_ID,
            icon = R.drawable.ic_twotone_logout,
            titleRes = R.string.nav_logout,
            destination = Destinations.LOGOUT,
            checked = false
        )
    )

    private val _navigationList: MutableLiveData<List<NavigationModelItem>> = MutableLiveData()
    val navigationList: LiveData<List<NavigationModelItem>>
        get() = _navigationList

    init {
        postListUpdate()
    }

    /**
     * Set the currently selected menu item.
     *
     * @return true if the currently selected item has changed.
     */
    fun setNavigationMenuItemChecked(id: Int): Boolean {
        var updated = false
        navigationMenuItems.forEachIndexed { index, item ->
            val shouldCheck = item.id == id
            if (item.checked != shouldCheck) {
                navigationMenuItems[index] = item.copy(checked = shouldCheck)
                updated = true
            }
        }
        if (updated) postListUpdate()
        return updated
    }
    private fun postListUpdate() {
        _navigationList.value = navigationMenuItems
    }
}

