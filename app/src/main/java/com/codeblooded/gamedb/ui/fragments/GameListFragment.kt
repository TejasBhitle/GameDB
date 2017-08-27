package com.codeblooded.gamedb.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeblooded.gamedb.POPULARITY
import com.codeblooded.gamedb.PREFERENCES
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.SORT
import com.codeblooded.gamedb.adapters.GameListAdapter
import com.codeblooded.gamedb.model.Game
import com.codeblooded.gamedb.util.RestClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by tejas on 8/5/17.
 */
class GameListFragment : Fragment() {

    lateinit var textview: TextView
    lateinit var progressDialog: ProgressDialog

    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // !!. is a non-null asserted call
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)


        textview = view.findViewById(R.id.centerTextView)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        pref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        getGames()
        return view
    }

    fun getGames() {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching Games")
        progressDialog.show()

        RestClient.addHeaders()
        RestClient.get("/games/?fields=*&order=" + pref.getString(SORT, POPULARITY), RequestParams(), object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
                updateUI(response)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONArray?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                super.onFailure(statusCode, headers, responseString, throwable)
            }

        })

    }

    fun updateUI(response: JSONArray?) {
        val games: ArrayList<Game> = ArrayList()
        for (i in 0..(response!!.length() - 1)) {
            val obj = response.getJSONObject(i)
            var name = ""
            var url: String = ""
            try {
                name = obj.get("name").toString()
                //val url = obj.getString("url").toString()
                url = obj.getJSONObject("cover").getString("url")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val game = Game()
            game.name = name
            game.img_url = url.replace("t_thumb", "t_cover_big")
            games.add(game)

        }
        progressDialog.cancel()
        if (games.size == 0)
            textview.text = getString(R.string.empty_list)

        recyclerView.adapter = GameListAdapter(context, games)

        // Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()

    }

}