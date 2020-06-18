package com.phytal.sarona.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.phytal.sarona.R
import com.phytal.sarona.ui.viewmodels.GradesViewModel

class GradesFragment : Fragment() {

    private lateinit var gradesViewModel: GradesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gradesViewModel =
                ViewModelProvider(this).get(GradesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_assignments, container, false)
        //val textView: TextView = root.findViewById(R.id.text_grades)
//        gradesViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bottom_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item?.itemId == R.id.action_add_course) {
//            // Add a course
//            Toast.makeText(this.context, "toast", Toast.LENGTH_SHORT).show()
//            return true
//        }
        return super.onOptionsItemSelected(item)
    }
}
