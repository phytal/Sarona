/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phytal.sarona.ui.nav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phytal.sarona.R

/**
 * A class which maintains and generates a navigation list to be displayed by [NavigationAdapter].
 */
object NavigationModel {

    private var navigationMenuItems = mutableListOf(
        NavigationModelItem.NavMenuItem(
            id = 0,
            icon = R.drawable.ic_twotone_classes,
            titleRes = R.string.nav_courses,
            destination = Destinations.CLASSES,
            checked = false
        ),
        NavigationModelItem.NavMenuItem(
            id = 1,
            icon = R.drawable.ic_twotone_grades,
            titleRes = R.string.nav_grades,
            destination = Destinations.GRADES,
            checked = false
        ),
        NavigationModelItem.NavMenuItem(
            id = 2,
            icon = R.drawable.ic_twotone_gpa,
            titleRes = R.string.nav_gpa,
            destination = Destinations.GPA,
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
        return updated
    }
    private fun postListUpdate() {
        _navigationList.value = navigationMenuItems
    }
}

