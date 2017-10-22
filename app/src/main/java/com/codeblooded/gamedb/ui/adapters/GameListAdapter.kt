package com.codeblooded.gamedb.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Game
import com.codeblooded.gamedb.ui.activities.DetailActivity
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by tejas on 8/5/17.
 */
class GameListAdapter(internal var context: Context, internal var games: ArrayList<Game>, internal var list: Boolean)
    : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var root: View = view.findViewById(R.id.root)
        var name: TextView = view.findViewById(R.id.list_item_game_name)
        var image: ImageView = view.findViewById(R.id.list_item_game_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (list) return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_game, parent, false))
        else return ViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_item_game, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val game = games.get(position)

        holder!!.name.text = game.name
        Picasso.with(context)
                .load("https:" + game.img_url)
                .placeholder(R.drawable.ic_image_grey_24dp)
                .error(R.drawable.ic_image_grey_24dp)
                .into(holder.image)
        holder.root.setOnClickListener { view ->
            val intent = Intent(context, DetailActivity::class.java)
            var bundle: Bundle? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.image.transitionName = "cover"
                val p = Pair.create(holder.image as View, "cover")
                bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, p).toBundle()
            }
            intent.putExtra(Constants.GAME, game)
            context.startActivity(intent, bundle)

        }

    }

    fun onClick(view: View) {

    }

    override fun getItemCount(): Int {
        return games.size
    }
}