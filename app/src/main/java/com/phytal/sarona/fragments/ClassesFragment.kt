package com.phytal.sarona.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.phytal.sarona.R
import com.phytal.sarona.viewmodels.ClassesViewModel

class ClassesFragment : Fragment() {

    private lateinit var classesViewModel: ClassesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        classesViewModel =
                ViewModelProviders.of(this).get(ClassesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_classes, container, false)
        //val textView: TextView = root.findViewById(R.id.text_classes)
        //classesViewModel.text.observe(viewLifecycleOwner, Observer {
         //   textView.text = it
        //})
        return root
    }
}
