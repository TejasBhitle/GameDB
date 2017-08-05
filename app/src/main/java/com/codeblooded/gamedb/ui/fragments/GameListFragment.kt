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


        var textview = view.findViewById(R.id.center_text) as TextView
        textview.setText("Games")

        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //val gameListAdapter = GameListAdapter(context,getGames())
        //recyclerView.adapter = gameListAdapter

        return view
    }

    /*fun getGames():ArrayList<Game>{
        var games : ArrayList<Game>

        return games
    }*/

}