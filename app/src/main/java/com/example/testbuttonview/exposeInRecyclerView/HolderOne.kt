package com.example.testbuttonview.exposeInRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testbuttonview.R

/**
 * Created by anchaoguang on 2019-11-26.
 *
 */
class HolderOne(itemView: View?) :RecyclerView.ViewHolder(itemView!!){

    var textName: TextView? = null
    init {
        textName = itemView?.findViewById<TextView>(R.id.name)
    }

}