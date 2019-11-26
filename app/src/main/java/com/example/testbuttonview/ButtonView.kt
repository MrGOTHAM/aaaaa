package com.example.testbuttonview

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Created by anchaoguang on 2019-11-25.
 * 1.实现view的三个构造器
 * 2.其中的参数分别为：Context：上下文、AttributeSet attrs： 从xml中定义的参数、int defStyleAttr ：主题中优先级最高的属性、int defStyleRes  ： 优先级次之的内置于View的style
 * 其中优先级依次为： Xml直接定义 > xml中style引用 > defStyleAttr > defStyleRes > theme直接定义
 * 3.在第三个构造器中，初始化画笔（图形画笔， 文字画笔， 打钩画笔），对动画结束进行监听
 * 4.重写 onSizeChanged方法， w,h为控件宽高，并且初始化ok路径动画的路径，再初始化所有动画
 * 5.分别定义矩形变圆角矩形、圆角矩形变圆、向上移动view、圆中打钩动画方法
 * 6.将每种动画都放入统一的animatorSet中
 * 7.重写onDraw()方法，定义长方形变为圆形的方法（参数从【矩形变为圆角矩形】【圆角矩形变圆】取）
 * 定义写文字的方法，使用参数确定是否执行绘制打钩的动画（如果不用参数确定，一开始就会有勾）
 * 8.定义开始动画的方法，从activity中调用，定义结束时重置的方法，在监听到动画结束时调用
 */
class ButtonView : View {

    private val mPaint: Paint by lazy { Paint() }
    private val mTextPaint: Paint by lazy { Paint() }
    private val mOkPaint: Paint by lazy { Paint() }
    private val mPath: Path by lazy { Path() }
    private val mRectF: RectF by lazy { RectF() }
    private val mTextRect: Rect by lazy { Rect() }
    private val mAnimationSet: AnimatorSet by lazy { AnimatorSet() }

    private var mWith: Int = 0
    private var mHeight: Int = 0
    private var mAngleCircle: Int = 0
    private var mDefDistance = 0
    private var mCurrentDistance = 0
    private var mStartOk: Boolean = false

    private lateinit var mPathMeasure: PathMeasure
    private lateinit var mRectToAngle: ValueAnimator
    private lateinit var mAngleToCircle: ValueAnimator
    private lateinit var mMoveUpAnimator: ObjectAnimator
    private lateinit var mOkAnimator: ValueAnimator


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initPaint()
    }

    private fun initPaint() {
        mPaint.color = Color.GREEN
        mPaint.strokeWidth = 4f
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL

        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = 40f
        mTextPaint.color = Color.WHITE
        // 字符串水平居中
        mTextPaint.textAlign = Paint.Align.CENTER

        mOkPaint.isAntiAlias = true
        mOkPaint.color = Color.WHITE
        mOkPaint.style = Paint.Style.STROKE
        mOkPaint.strokeWidth = 10f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWith = w
        mHeight = h
        mDefDistance = (w - h) / 2
        initAnimators()


        mAnimationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                reset()
            }
        })
        initOkPath()
    }

    private fun initAnimators() {
        setRectToAngle()
        setAngleToCircle()
        setMoveUpAnimator()
        setOkPathAnimator()

        mAnimationSet.play(mMoveUpAnimator)
            .before(mOkAnimator)
            .after(mAngleToCircle)
            .after(mRectToAngle)
    }

    // 矩形变圆角，圆角的半径逐渐变大
    private fun setRectToAngle() {
        mRectToAngle = ValueAnimator.ofInt(0, height / 2)
        mRectToAngle.duration = 1000
        mRectToAngle.addUpdateListener { animation ->
            mAngleCircle = animation.animatedValue as Int
            invalidate()
        }
    }

    // 圆角矩形过度到圆， 动态计算出过度过程中移动的距离， 字体透明度变化情况
    private fun setAngleToCircle() {
        mAngleToCircle = ValueAnimator.ofInt(0, mDefDistance)
        mAngleToCircle.duration = 1000
        mAngleToCircle.addUpdateListener { animation ->
            mCurrentDistance = animation.animatedValue as Int
            // 因为是int，必须先乘255再除
            val alpha = 255 - (mCurrentDistance * 255 / mDefDistance)
            mTextPaint.alpha = alpha
            invalidate()
        }
    }

    private fun setMoveUpAnimator() {
        mMoveUpAnimator = ObjectAnimator.ofFloat(this, "translationY", this.translationY, this.translationY - 300f)
        mMoveUpAnimator.duration = 1000
        mMoveUpAnimator.interpolator = AccelerateDecelerateInterpolator()
    }

    private fun setOkPathAnimator() {
        mOkAnimator = ValueAnimator.ofFloat(1f, 0f)
        mOkAnimator.duration = 1000
        mOkAnimator.addUpdateListener { animation ->
            mStartOk = true
            val value = animation.animatedValue as Float
            val effect =
                DashPathEffect(floatArrayOf(mPathMeasure.length, mPathMeasure.length), value * mPathMeasure.length)
            mOkPaint.pathEffect = effect
            invalidate()
        }
    }

    private fun initOkPath() {
        mPathMeasure = PathMeasure()
        mPath.moveTo(mDefDistance.toFloat() + height / 8f * 3, height / 2f)
        mPath.lineTo(mDefDistance.toFloat() + height / 2f, height / 5f * 3)
        mPath.lineTo(mDefDistance.toFloat() + height / 3f * 2, height / 5f * 2)

        mPathMeasure = PathMeasure(mPath, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRectToCircle(canvas)
        drawPaintText(canvas)

        if (mStartOk) {
            canvas.drawPath(mPath, mOkPaint)
        }
    }

    private fun drawRectToCircle(canvas: Canvas) {
        // 算的是两边圆角的 圆心距离
        mRectF.left = mCurrentDistance.toFloat()
        mRectF.top = 0f
        mRectF.right = width.toFloat() - mCurrentDistance.toFloat()
        mRectF.bottom = height.toFloat()
        canvas.drawRoundRect(mRectF, mAngleCircle.toFloat(), mAngleCircle.toFloat(), mPaint)
    }

    private fun drawPaintText(canvas: Canvas) {
        mTextRect.top = 0
        mTextRect.left = 0
        mTextRect.bottom = height
        mTextRect.right = width
        // 字体间距
        val fontMetrics = mTextPaint.fontMetricsInt
        val baseLine = (mTextRect.bottom + mTextRect.top - fontMetrics.top - fontMetrics.bottom) / 2
        canvas.drawText("确认按钮", mTextRect.centerX().toFloat(), baseLine.toFloat(), mTextPaint)
    }

    fun start() {
        mAnimationSet.start()
    }

    private fun reset() {
        mStartOk = false
        mAngleCircle = 0
        mCurrentDistance = 0
        mDefDistance = (mWith - mHeight) / 2
        mTextPaint.alpha = 255
        translationY += 300
        invalidate()
    }
}