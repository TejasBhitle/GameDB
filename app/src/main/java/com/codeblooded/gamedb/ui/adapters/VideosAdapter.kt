package com.codeblooded.gamedb.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codeblooded.gamedb.R
import org.json.JSONArray


/**
 * Created by tejas on 10/22/17.
 */
class VideosAdapter(internal val context: Context, internal val jsonArray: JSONArray)
    : PagerAdapter() {

    private val LOG = "CustomPagerAdapter"
    lateinit var layoutInflator: LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflator.inflate(R.layout.video_item, container, false)

        val imageView: ImageView = view.findViewById(R.id.imageView)
        val id: String = jsonArray.getJSONObject(position).getString("video_id")
        val name = jsonArray.getJSONObject(position).getString("name")

        Glide.with(context)
                .load("https://img.youtube.com/vi/$id/hqdefault.jpg")
                .crossFade()
                .dontTransform()
                .placeholder(R.drawable.ic_image_grey_24dp)
                .error(R.drawable.ic_image_grey_24dp)
                .into(imageView)

        view.findViewById<TextView>(R.id.video_title).text = name

        view.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://youtu.be/$id"))
            context.startActivity(i)
        }

        container.addView(view)
        return view
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = jsonArray.length()

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}