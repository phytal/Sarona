package com.phytal.sarona.util

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.phytal.sarona.R

class SpinnerAdapter
constructor(context: Context, resource: Int, items: List<String>) :
    ArrayAdapter<String?>(context, resource, items) {
    private val font: Typeface? = ResourcesCompat.getFont(context, R.font.mplus)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.typeface = font
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.typeface = font
        return view
    }
}