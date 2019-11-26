package com.example.testbuttonview.exposeInRecyclerView

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.testbuttonview.R

/**
 * Created by anchaoguang on 2019-11-26.
 *
 */
class HolderImageView(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    var image: LinearLayout? = null

    init {
        image = itemView?.findViewById<LinearLayout>(R.id.holder_image)
    }
}