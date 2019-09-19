package com.example.customviewlib

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

/**
 * Created by VanNhat on 05/09/2019.
 */
class DotIndicator {

    private var paint: Paint? = null
    private var center: PointF
    var currentRadius: Int? = null

    init {
        paint = Paint()
        paint?.isAntiAlias = true
        center = PointF()
    }

    fun setColor(color: Int) {
        paint?.color = color
    }

    fun setAlpha(alpha: Int) {
        paint?.alpha = alpha
    }

    fun setCenter(x: Float, y: Float) {
        center.set(x, y)
    }

    fun draw(canvas: Canvas) {
        currentRadius?.let { radius ->
            paint?.let { p ->
                canvas.drawCircle(center.x, center.y, radius.toFloat(), p)
            }
        }
    }
}
