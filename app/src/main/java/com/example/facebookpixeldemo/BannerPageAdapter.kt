package com.example.facebookpixeldemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

/**
 * Created by VanNhat on 18/09/2019.
 */
class BannerPageAdapter(var bannerUrlList: List<String>? = null) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val rootView = LayoutInflater.from(container.context)
            .inflate(R.layout.page_banner_item, container, false)
        val holder = ViewHolder(rootView)
        bannerUrlList?.let {
            val data = it[position]
            Glide.with(container.context).load(data).into(holder.pagerImage)
        }
        container.addView(rootView)
        return rootView
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view === item
    }

    override fun getCount(): Int {
        return bannerUrlList?.size ?: 0
    }

    internal class ViewHolder(rootView: View) {
        var pagerImage: ImageView = rootView.findViewById(R.id.iv_banner)
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}
