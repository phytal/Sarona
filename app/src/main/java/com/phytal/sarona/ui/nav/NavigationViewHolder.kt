package com.phytal.sarona.ui.nav

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.phytal.sarona.R
import com.phytal.sarona.databinding.NavDividerItemLayoutBinding
import com.phytal.sarona.databinding.NavMenuItemLayoutBinding
import com.phytal.sarona.util.themeColor

sealed class NavigationViewHolder<T : NavigationModelItem>(
    view: View
) : RecyclerView.ViewHolder(view) {

    abstract fun bind(navItem : T)

    class NavMenuItemViewHolder(
        private val binding: NavMenuItemLayoutBinding,
        private val listener: NavigationAdapter.NavigationAdapterListener
    ) : NavigationViewHolder<NavigationModelItem.NavMenuItem>(binding.root) {

        override fun bind(navItem: NavigationModelItem.NavMenuItem) {
            binding.run {
                navMenuItem = navItem
                navListener = listener
                executePendingBindings()
            }
//            binding.navItemTitle.setTextColor(binding.root.context.getColor(R.color.color_on_surface_emphasis_medium))
        }
    }

    class NavDividerViewHolder(
        private val binding: NavDividerItemLayoutBinding
    ) : NavigationViewHolder<NavigationModelItem.NavDivider>(binding.root) {
        override fun bind(navItem: NavigationModelItem.NavDivider) {
            binding.navDivider = navItem
            binding.executePendingBindings()
        }
    }
}