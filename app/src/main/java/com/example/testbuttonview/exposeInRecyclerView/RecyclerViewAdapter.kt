package com.example.testbuttonview.exposeInRecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testbuttonview.R

/**
 * Created by anchaoguang on 2019-11-26.
 *
 */
class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_1 = 1
        const val ITEM_2 = 2
    }

    private var mContext: Context? = null
    private var mData = mutableListOf(mutableMapOf<Int, String>())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            ITEM_1 -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.holder_one, parent, false)
                HolderOne(view)
            }
            ITEM_2 -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.holder_image_view, parent, false)
                HolderImageView(view)
            }
            else -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.holder_one, parent, false)
                HolderOne(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            mData[position][1] == "textView" -> (holder as HolderOne).textName?.text = mData[position][2]
            mData[position][1] == "imageView" -> (holder as HolderImageView).image
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            mData[position][1] == "textView" -> ITEM_1
            mData[position][1] == "imageView" -> ITEM_2
            else -> ITEM_1
        }
    }

    fun setData(data: ArrayList<MutableMap<Int, String>>, context: Context) {
        mData.clear()
        mData.addAll(data)
        mContext = context
        notifyDataSetChanged()
    }
}