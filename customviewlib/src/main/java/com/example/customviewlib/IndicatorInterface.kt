package com.example.customviewlib

import androidx.viewpager.widget.ViewPager

/**
 * Created by VanNhat on 05/09/2019.
 */

interface IndicatorInterface {

    @Throws(PagesLessException::class)
    fun setViewPager(viewPager: ViewPager)

    fun setAnimationDuration(duration: Long)

    fun setRadiusSelected(radius: Int)

    fun setRadiusUnselected(radius: Int)

    fun setDotDistance(distance: Int)
}
