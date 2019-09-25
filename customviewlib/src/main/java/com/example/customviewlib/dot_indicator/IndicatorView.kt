package com.example.customviewlib.dot_indicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.customviewlib.R

/**
 * Created by VanNhat on 05/09/2019.
 */
class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr), IndicatorInterface, ViewPager.OnPageChangeListener {

    private var viewPager: ViewPager? = null
    private var dots = mutableListOf<DotIndicator>()
    private var radiusSelected =
        DEFAULT_RADIUS_SELECTED
    private var radiusUnSelected =
        DEFAULT_RADIUS_UNSELECTED
    private var distance =
        DEFAULT_DISTANCE
    private var colorSelected: Int? = null
    private var animateDuration: Long? = null
    private var colorUnSelected: Int? = null
    private var currentPosition: Int? = null
    private var beforePosition: Int? = null
    private var animatorZoomIn: ValueAnimator? = null
    private var animatorZoomOut: ValueAnimator? = null

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.IndicatorView
        )
        radiusSelected = typedArray.getDimensionPixelSize(
            R.styleable.IndicatorView_dot_radius_selected,
            DEFAULT_RADIUS_SELECTED
        )
        radiusUnSelected = typedArray.getDimensionPixelSize(
            R.styleable.IndicatorView_dot_radius_unselected,
            DEFAULT_RADIUS_UNSELECTED
        )
        colorSelected = typedArray.getDimensionPixelSize(
            R.styleable.IndicatorView_dot_color_selected,
            DEFAULT_SELECTED_COLOR
        )
        colorUnSelected = typedArray.getDimensionPixelSize(
            R.styleable.IndicatorView_dot_color_unselected,
            DEFAULT_SELECTED_COLOR
        )
        distance = typedArray.getInt(
            R.styleable.IndicatorView_dot_distance,
            DEFAULT_DISTANCE
        )

        typedArray.recycle()
    }

    override fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.addOnPageChangeListener(this)
        viewPager.adapter?.count?.let { initDot(it) }
        onPageSelected(0)
    }

    override fun setAnimationDuration(duration: Long) {
        animateDuration = duration
    }

    override fun setRadiusSelected(radius: Int) {
        radiusSelected = radius
    }

    override fun setRadiusUnselected(radius: Int) {
        radiusUnSelected = radius
    }

    override fun setDotDistance(distance: Int) {
        this.distance = distance
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        beforePosition = currentPosition
        currentPosition = position

        if (beforePosition == currentPosition) beforePosition = currentPosition!! + 1

        val animatorSet = AnimatorSet()
        animatorSet.duration = animateDuration ?: DEFAULT_ANIMATE_DURATION

        animatorZoomIn = ValueAnimator.ofInt(radiusUnSelected, radiusSelected)

        animatorZoomIn?.addUpdateListener { animation ->
            val newRadius = animation?.animatedValue as Int
            currentPosition?.let { changeNewRadius(it, newRadius) }
        }

        animatorZoomOut = ValueAnimator.ofInt(radiusSelected, radiusUnSelected)
        animatorZoomOut?.addUpdateListener { animation ->
            val newRadius = animation?.animatedValue as Int
            beforePosition?.let { changeNewRadius(it, newRadius) }
        }

        animatorSet.play(animatorZoomIn).with(animatorZoomOut)
        animatorSet.start()
        createLog("onPageSelected")
    }

    private fun changeNewRadius(positionPerform: Int, newRadius: Int) {
        dots[positionPerform].apply {
            if (currentRadius != newRadius) {
                currentRadius = newRadius
                setAlpha(newRadius * MAX_COLOR_ALPHA / radiusSelected)
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        for (dot in dots) canvas?.let {
            dot.draw(it)
            createLog("onDraw")
        }
    }

    private fun initDot(dotCount: Int) {
        if (dotCount < 2) throw  PagesLessException()

        for (i in 0 until dotCount) dots.add((DotIndicator()))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val yCenter = (height / 2).toFloat()
        val d = distance + 2 * radiusUnSelected
        val firstXCenter = ((width / 2) - (dots.size - 1) * d / 2).toFloat()

        for (i in 0 until dots.size) {
            dots[i].apply {
                setCenter(if (i == 0) firstXCenter else firstXCenter + d * i, yCenter)
                currentRadius = (if (i == currentPosition) radiusSelected else radiusUnSelected)
                setColor(if (i == currentPosition) colorSelected!! else colorUnSelected!!)
                setAlpha(if (i == currentPosition) MAX_COLOR_ALPHA else radiusUnSelected * MAX_COLOR_ALPHA / radiusSelected)
            }
        }

        createLog("onLayout")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = 2 * radiusSelected


        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width =
            if (widthMode == MeasureSpec.EXACTLY) widthSize else 0
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
        createLog("onMeasure")
    }

    private fun createLog(mess: String) {
        Log.d("demoo", mess)
    }

    companion object {
        private const val DEFAULT_ANIMATE_DURATION = 200L
        private const val DEFAULT_RADIUS_SELECTED = 20
        private const val DEFAULT_RADIUS_UNSELECTED = 15
        private const val DEFAULT_DISTANCE = 40
        private const val DEFAULT_SELECTED_COLOR = Color.WHITE
        private const val MAX_COLOR_ALPHA = 255
    }

}