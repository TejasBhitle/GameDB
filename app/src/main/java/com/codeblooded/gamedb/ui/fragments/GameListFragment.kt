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
import com.codeblooded.gamedb.model.Game
import com.codeblooded.gamedb.ui.adapters.GameListAdapter
import com.codeblooded.gamedb.util.RestClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

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
        val gamesList: ArrayList<Game> = ArrayList()
        val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
        for (i in 0..(response!!.length() - 1)) {
            val obj = response.getJSONObject(i)
            var id: Int? = null
            var name = ""
            var description = ""
            var url: String = ""
            var storyline: String = ""
            var user_rating: Double = 0.0
            var critic_rating: Double = 0.0
            var img_url: String? = null
            var bg_url: String? = null
            var games: JSONArray = JSONArray()
            var release_date: String? = null
            var screenshots: JSONArray = JSONArray()
            var videos: JSONArray = JSONArray()
            try {
                id = obj.getInt("id")
                name = obj.get("name").toString()
                description = obj.get("summary").toString().replace(" \n  \n", "\n", true)
                url = obj.getString("url")
                storyline = obj.getString("storyline")
                user_rating = obj.getDouble("rating")
                critic_rating = obj.getDouble("aggregated_rating")
                img_url = obj.getJSONObject("cover").getString("url")
                bg_url = (obj.getJSONArray("screenshots").get(0) as JSONObject).getString("url")
                games = obj.getJSONArray("games")
                release_date = sdf.format(Date(obj.get("release_date") as String))
                screenshots = obj.getJSONArray("screenshots")
                videos = obj.getJSONArray("videos")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val game = Game()
            game.id = id
            game.name = name
            game.description = description
            game.url = url
            game.storyline = storyline
            game.user_rating = user_rating
            game.critic_rating = critic_rating
            game.img_url = img_url?.replace("t_thumb", "t_cover_big")
            game.bg_url = bg_url?.replace("t_thumb", "t_screenshot_big")
            game.games = games
            game.release_date = release_date
            game.screenshots = screenshots
            game.videos = videos
            gamesList.add(game)

        }
        progressDialog.cancel()
        if (gamesList.size == 0)
            textview.text = getString(R.string.empty_list)

        recyclerView.adapter = GameListAdapter(context, gamesList)

        // Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()

    }

}