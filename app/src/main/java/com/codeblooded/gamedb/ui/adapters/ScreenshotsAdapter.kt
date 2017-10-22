package com.codeblooded.gamedb.ui.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.codeblooded.gamedb.R
import com.squareup.picasso.Picasso
import org.json.JSONArray

/**
 * Created by tejas on 10/22/17.
 */
class ScreenshotsAdapter(internal val context : Context, internal val jsonArray: JSONArray)
    : PagerAdapter() {

    private val LOG = "CustomPagerAdapter"
    lateinit var layoutInflator : LayoutInflater

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        layoutInflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = layoutInflator.inflate(R.layout.screenshot_item,container,false)

        val imageView: ImageView = view.findViewById(R.id.imageView)
        var url: String = jsonArray.getJSONObject(position + 1).getString("url")
        url = url.replace("t_thumb","t_screenshot_big")

        Picasso.with(context)
                .load("https:"+url)
                .placeholder(R.drawable.ic_image_grey_24dp)
                .error(R.drawable.ic_image_grey_24dp)
                .into(imageView)

        container!!.addView(view)
        return view
    }


    override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

    override fun getCount(): Int = (jsonArray.length() - 1)

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container!!.removeView(`object` as LinearLayout)
    }
}