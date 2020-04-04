package com.example.sarona.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sarona.R

class CalculatorFragment : Fragment() {

    private lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        calculatorViewModel =
                ViewModelProviders.of(this).get(CalculatorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calculator, container, false)
        val textView: TextView = root.findViewById(R.id.text_calculator)
        calculatorViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
