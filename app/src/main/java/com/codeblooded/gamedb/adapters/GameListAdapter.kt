package com.codeblooded.gamedb.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Game
import com.squareup.picasso.Picasso
import java.util.ArrayList

/**
 * Created by tejas on 8/5/17.
 */
class GameListAdapter(internal var context: Context, internal var games : ArrayList<Game>)
    : RecyclerView.Adapter<GameListAdapter.ViewHolder>(){

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var name : TextView = view.findViewById(R.id.list_item_game_name)
        var image : ImageView = view.findViewById(R.id.list_item_game_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_game,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val game = games.get(position)

        holder!!.name.text = game.name
        Picasso.with(context)
                .load("https:"+game.img_url)
                .placeholder(R.drawable.ic_image_grey_24dp)
                .error(R.drawable.ic_image_grey_24dp)
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return games.size
    }
}