package com.example.testbuttonview.exposeAnimation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testbuttonview.R
import kotlinx.android.synthetic.main.no_view_animator_utils.*

/**
 * Created by anchaoguang on 2019-11-26.
 *
 */
class NoAnimatorActivity : AppCompatActivity() {
    var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_view_animator_utils)
        start_animator.setOnClickListener {
            if (i > 2) {
                i = 1
            }
            myView.startAnimator(i)
            i += 1
        }
    }
}