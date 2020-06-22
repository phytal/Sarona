package com.phytal.sarona.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.phytal.sarona.R
import com.phytal.sarona.ui.listeners.NavigationIconClickListener
import com.phytal.sarona.ui.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        (activity as AppCompatActivity).setSupportActionBar(root.toolbar)
        root.toolbar.setNavigationOnClickListener(NavigationIconClickListener(
            requireActivity(),
            root.home_fragment,
            AccelerateDecelerateInterpolator(),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_menu_icon), // Menu open icon
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_menu_close)))
        root.home_fragment.background = context?.getDrawable(R.drawable.sar_background_shape)

        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
