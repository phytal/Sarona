package com.phytal.sarona.ui.nav

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.phytal.sarona.databinding.NavDividerItemLayoutBinding
import com.phytal.sarona.databinding.NavMenuItemLayoutBinding

private const val VIEW_TYPE_NAV_MENU_ITEM = 4
private const val VIEW_TYPE_NAV_DIVIDER = 6

class NavigationAdapter(
    private val listener: NavigationAdapterListener
) : ListAdapter<NavigationModelItem, NavigationViewHolder<NavigationModelItem>>(
    NavigationModelItem.NavModelItemDiff
) {

    interface NavigationAdapterListener {
        fun onNavMenuItemClicked(item: NavigationModelItem.NavMenuItem)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NavigationModelItem.NavMenuItem -> VIEW_TYPE_NAV_MENU_ITEM
            is NavigationModelItem.NavDivider -> VIEW_TYPE_NAV_DIVIDER
            else -> throw RuntimeException("Unsupported ItemViewType for obj ${getItem(position)}")
        }
    }

    @Suppress("unchecked_cast")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NavigationViewHolder<NavigationModelItem> {
        return when (viewType) {
            VIEW_TYPE_NAV_MENU_ITEM -> NavigationViewHolder.NavMenuItemViewHolder(
                NavMenuItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
            VIEW_TYPE_NAV_DIVIDER -> NavigationViewHolder.NavDividerViewHolder(
                NavDividerItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw RuntimeException("Unsupported view holder type")
        } as NavigationViewHolder<NavigationModelItem>
    }

    override fun submitList(list: List<NavigationModelItem>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onBindViewHolder(
        holder: NavigationViewHolder<NavigationModelItem>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}