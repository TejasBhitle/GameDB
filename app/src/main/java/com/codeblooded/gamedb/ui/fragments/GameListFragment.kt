package com.codeblooded.gamedb.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeblooded.gamedb.R
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Created by tejas on 8/5/17.
 */
class GameListFragment :Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // !!. is a non-null asserted call
        val view = inflater!!.inflate(R.layout.fragment_list,container,false)

        centerTextView.text = "Games"

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    /*fun getGames():ArrayList<Game>{
        var games : ArrayList<Game>

        return games
    }*/

}