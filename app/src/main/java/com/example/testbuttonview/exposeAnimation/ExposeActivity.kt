package com.example.testbuttonview.exposeAnimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.testbuttonview.R
import kotlinx.android.synthetic.main.activity_expose.*
import kotlin.math.hypot

/**
 * Created by anchaoguang on 2019-11-25.
 *
 */
class ExposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expose)

        start_expose_animation.setOnClickListener {
            // radius必须放在点击事件里面,不然还未初始化好：：得不到width和height两个的参数
            val radius = hypot(imageView.width.toDouble(), imageView.height.toDouble()).toFloat()
            val centerX = imageView.width/2
            val centerY = imageView.height/2

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (imageView.visibility == View.VISIBLE) {
                    val animator = ViewAnimationUtils.createCircularReveal(imageView, centerX, centerY, radius, 0f)
                    animator.duration = 3000
                    animator.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            imageView.visibility = View.GONE
                        }
                    })
                    animator.start()
                } else {
                    val animator = ViewAnimationUtils.createCircularReveal(imageView, centerX, centerY, 0f, radius)
                    animator.duration = 3000
                    imageView.visibility = View.VISIBLE
                    animator.start()
                }
            }
        }
    }
}