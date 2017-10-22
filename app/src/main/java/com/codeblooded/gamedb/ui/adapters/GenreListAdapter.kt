package com.codeblooded.gamedb.ui.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Genre
import com.codeblooded.gamedb.ui.activities.GenreListActivity

/**
 * Created by tejas on 10/22/17.
 */
class GenreListAdapter(internal var context: Context, internal var genres: ArrayList<Genre>)
    : RecyclerView.Adapter<GenreListAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var root: View = view.findViewById(R.id.root)
        var nameTextView: TextView = view.findViewById(R.id.nameTextView)
        //var image: ImageView = view.findViewById(R.id.list_item_game_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_item_genre, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val genre : Genre = genres.get(position)
        holder?.nameTextView?.text = genre.name

        holder!!.root.setOnClickListener {
            val intent = Intent(context,GenreListActivity::class.java)
            intent.putExtra(Constants.ID, genre.id)
            intent.putExtra(Constants.NAME, genre.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return genres.size
    }
}