package com.example.facebookpixeldemo

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        handleEvents()
    }

    private fun initData() {
        Glide.with(this).load(initBannerData()[0]).into(iv_img)
        val pagerAdapter = BannerPageAdapter(initBannerData())
        view_page.adapter = pagerAdapter
        indicator.setViewPager(view_page)

    }

    private fun handleEvents() {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("demoo", "x: ${event?.x}, y:${event?.y}")
        return super.onTouchEvent(event)
    }

    private fun initBannerData(): List<String> {
        return listOf(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTH1WeClCW1I8Se4eirKx-X5PcQgpeOCHoGIW5TGVX9lRR4IzOysQ",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeS6M768irTyH4LK22cu__gpGPcwHVg7unu7uQRk8aJvoHj8g6Vg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNnhS_UHZ69Q6CR5F-gWZ_cF06JrCSjqn0RTnnDH4Vhr6rfhpnJg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTg2Eu-dWCJiDuO2vJQBc3nleS_raMcz-3u1V5t_yYB_7eqSbrp"
        )
    }
}
