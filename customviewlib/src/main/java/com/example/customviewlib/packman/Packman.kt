package com.example.customviewlib.packman

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewlib.R
import kotlin.math.min

class Packman @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private var sectorPaint: Paint? = null
    private var eyePaint: Paint? = null

    private var sector = RectF()
    private var eyeDot = PointF()
    private var foodDots = mutableListOf<FoodDot>()

    private var packmanSize: Float? = null
    private var eyeRadius: Float? = null
    private var iconColor: Int? = null
    private var eyeColor: Int? = null
    private var numberOfDot: Int? = null
    private var startAngel = START_ANGLE
    private var sweepAngel = SWEEP_ANGLE
    private var squareEdge: Float? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Packman)
        iconColor = typedArray.getColor(R.styleable.Packman_icon_color, Color.BLACK)
        eyeColor = typedArray.getColor(R.styleable.Packman_eye_color, Color.WHITE)
        numberOfDot = typedArray.getInteger(R.styleable.Packman_num_dot, 0)

        sectorPaint = Paint()
        sectorPaint?.flags = Paint.ANTI_ALIAS_FLAG // reduce antialiasing, smooths the drawing
        sectorPaint?.color = iconColor!!
        sectorPaint?.style = Paint.Style.FILL

        eyePaint = Paint()
        eyePaint?.flags = Paint.ANTI_ALIAS_FLAG
        eyePaint?.color = eyeColor as Int
        eyePaint?.style = Paint.Style.FILL

        initFoodDots()

        typedArray.recycle()
    }

    private fun initFoodDots() {
        numberOfDot?.let { count ->
            if (count >= 1)
                for (i in 1..count) {
                    foodDots.add(FoodDot())
                }
        }
    }

    private fun createAnimation() {
        val propertySweep = PropertyValuesHolder.ofInt(PROPERTY_SWEEP, 0, 30, 0, 30)

        val propertyPackmanTransaction =
            PropertyValuesHolder.ofInt(PROPERTY_PACKMAN_TRANSACTION, sector.right.toInt(), 300)

        val orginalEyeX = squareEdge!! * 0.6
        val newEyeX = (300 - packmanSize!!) + packmanSize!! * 0.6
        val propertyEyeTransaction = PropertyValuesHolder.ofInt(PROPERTY_EYE_TRANSACTION,
            orginalEyeX.toInt(), newEyeX.toInt())

        val animator = ValueAnimator()
        animator.setValues(propertySweep, propertyPackmanTransaction, propertyEyeTransaction)
        animator.duration = 2000 // happened time

        animator.addUpdateListener { anim ->
            startAngel = anim.getAnimatedValue(PROPERTY_SWEEP) as Int
            sweepAngel = 360 - startAngel * 2

            sector.right = (anim.getAnimatedValue(PROPERTY_PACKMAN_TRANSACTION) as Int).toFloat()
            packmanSize?.let { sector.left = sector.right - it }

            eyeDot.x = (anim.getAnimatedValue(PROPERTY_EYE_TRANSACTION) as Int).toFloat()

            invalidate()
        }

//        val foodDotAnimator = ValueAnimator.ofInt(iconColor!!, Color.WHITE)
//        foodDotAnimator.duration = 500
//        foodDotAnimator.addUpdateListener {
//            for (dot in foodDots) {
//                dot.setColor(it.animatedValue as Int)
//                invalidate()
//            }
//        }
//        foodDotAnimator.start()
//        foodDotAnimator.repeatCount = numberOfDot!!

        animator.start()
        animator.repeatCount = 10
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = DEFAULT_WIDTH_SIZE
        val desiredHeight = DEFAULT_HEIGHT_SIZE
        eyeRadius = DEFAULT_EYE_RADIUS

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize.coerceAtMost(desiredWidth)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> heightSize.coerceAtMost(desiredHeight)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        squareEdge = min(width, height).toFloat()
        squareEdge?.let { side ->
            packmanSize = side

            sector.right = side
            sector.left = 0.toFloat()
            sector.bottom = side
            sector.top = 0.toFloat()

            eyeDot.x = side * 3 / 5
            eyeDot.y = side * 1 / 4

            for (i in 0 until foodDots.size) {
                foodDots[i].apply {
                    setCenter(side + i * DISTANCE_BETWEEN_DOTS, height.toFloat() / 2)
                    //setAlpha(255)
                    setColor(iconColor!!)
                    currentRadius = DEFAULT_FOOD_RADIUS.toInt()
                }
            }
        }

        createAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        sectorPaint?.let {
            canvas?.drawArc(sector, startAngel.toFloat(), sweepAngel.toFloat(), true, it)
        }

        eyePaint?.let {
            canvas?.drawCircle(eyeDot.x, eyeDot.y, DEFAULT_EYE_RADIUS, it)
        }


        for (dot in foodDots)
            canvas?.let { c -> dot.draw(c) }


    }

    companion object {
        private const val PROPERTY_SWEEP = "sweep"
        private const val PROPERTY_PACKMAN_TRANSACTION = "transaction"
        private const val PROPERTY_FOOD_GONE = "PROPERTY_FOOD_GONE"
        private const val PROPERTY_EYE_TRANSACTION = "PROPERTY_EYE_TRANSACTION"
        private const val DEFAULT_HEIGHT_SIZE = 100
        private const val DEFAULT_WIDTH_SIZE = 300
        private const val DEFAULT_EYE_RADIUS = DEFAULT_HEIGHT_SIZE.toFloat() / 10
        private const val DEFAULT_FOOD_RADIUS = DEFAULT_HEIGHT_SIZE.toFloat() / 6
        private const val START_ANGLE = 30
        private const val DISTANCE_BETWEEN_DOTS = 50
        private const val SWEEP_ANGLE = 300
    }
}
