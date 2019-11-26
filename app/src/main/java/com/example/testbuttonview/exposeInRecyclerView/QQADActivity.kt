package com.example.testbuttonview.exposeInRecyclerView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testbuttonview.R
import kotlinx.android.synthetic.main.activity_expose_view_in_recycler.*

/**
 * Created by anchaoguang on 2019-11-26.
 *
 */
class QQADActivity : AppCompatActivity() {

    private val mAdapter: RecyclerViewAdapter by lazy { RecyclerViewAdapter() }
    private val mData = arrayListOf(mutableMapOf<Int, String>())
    private var map = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expose_view_in_recycler)
        initData()
        initView()

    }

    private fun initView() {
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = mAdapter
        mAdapter.setData(mData, this)
    }

    private fun initData() {
        mData.clear()
        for (i in 1..20) {
            map = mutableMapOf()
            map[1] = "textView"
            map[2] = "一个很长的字符串"
            mData.add(map)
        }

        map = mutableMapOf()
        map[1] = "imageView"
        map[2] = "imageView的揭露图片"
        mData.add(map)

        for (i in 1..20) {
            map = mutableMapOf()
            map[1] = "textView"
            map[2] = "一个textview"
            mData.add(map)
        }
    }
}