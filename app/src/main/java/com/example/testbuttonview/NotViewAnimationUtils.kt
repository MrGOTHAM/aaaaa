package com.example.testbuttonview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout

/**
 * Created by anchaoguang on 2019-11-26.
 *
 */
class NotViewAnimationUtils : FrameLayout {

    private val mPaint: Paint by lazy { Paint() }
    private val mLayout: RectF by lazy { RectF() }
    private val mClipPath: Path by lazy { Path() }
    private val mOnClipPath: Path by lazy { Path() }
    private lateinit var mStartAnimator: ValueAnimator
    private var mAnimatorValue: Float = 0.0f
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet!!, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initAttrs()
    }

    private fun initAttrs() {
        mPaint.color = Color.WHITE
        mPaint.isAntiAlias = true
        setWillNotDraw(false)
        initAnimator()
        // 硬件层
        setLayerType(LAYER_TYPE_HARDWARE, mPaint)
    }

    private fun initAnimator() {
        mStartAnimator = ValueAnimator()
        mStartAnimator.duration = 400
        mStartAnimator.interpolator = AccelerateDecelerateInterpolator()
        mStartAnimator.addUpdateListener {
            mAnimatorValue = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mLayout.set(0f, 0f, w.toFloat(), h.toFloat())
        refreshRegion(this)
    }

    private fun refreshRegion(view: View) {
        val w = view.width
        val h = view.height
        val area = RectF()
        area.top = view.paddingTop.toFloat()
        area.left = view.paddingLeft.toFloat()
        area.right = w - view.paddingRight.toFloat()
        area.bottom = h - view.paddingBottom.toFloat()
        mClipPath.reset()

        val center = PointF(w / 2f, h / 2f)
        val left = area.width() / 2 * (1 - mAnimatorValue)
        val right = area.width() / 2 * (1 + mAnimatorValue)
        val rect = RectF(left, area.top, right, area.bottom)
        mClipPath.addRect(rect, Path.Direction.CW)
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        super.draw(canvas)
        drawOpPath(canvas)
        canvas.restore()
    }

    private fun drawOpPath(canvas: Canvas) {
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        mOnClipPath.reset()

        mOnClipPath.addRect(0f, 0f, mLayout.width(), mLayout.height(), Path.Direction.CW)

        mOnClipPath.op(mClipPath, Path.Op.DIFFERENCE)
        canvas.drawPath(mOnClipPath, mPaint)
    }

    override fun invalidate() {
        refreshRegion(this)
        super.invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mStartAnimator != null) {
            mStartAnimator.cancel()
            mStartAnimator.removeAllUpdateListeners()
            mStartAnimator.removeAllListeners()
        }
    }

    fun startAnimator(i: Int) {
        if (i == 1) {
            mStartAnimator.setFloatValues(0f, 1f)
        } else {
            mStartAnimator.setFloatValues(1f, 0f)
        }
        mStartAnimator.start()
    }
}