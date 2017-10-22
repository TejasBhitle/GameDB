package com.codeblooded.gamedb.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Game
import com.codeblooded.gamedb.ui.adapters.GameListAdapter
import com.codeblooded.gamedb.util.FragmentUtility
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

    private var search by FragmentUtility<Boolean>()

    companion object {
        fun newInstance(search: Boolean) = GameListFragment().apply {
            this.search = search
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // !!. is a non-null asserted call
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)


        textview = view.findViewById(R.id.centerTextView)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        if (search)
            recyclerView.layoutManager = LinearLayoutManager(context)
        else
            recyclerView.layoutManager = GridLayoutManager(context, 2)

        pref = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        if (!search) getGames("/games/?fields=*&order=" + pref.getString(Constants.SORT, Constants.POPULARITY))
        return view
    }

    fun getGames(baseUrl: String) {

        if(RestClient.isNetworkConnected(context)) {

            progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Fetching Games")
            progressDialog.show()

            RestClient.addHeaders()
            RestClient.get(baseUrl, RequestParams(), object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    updateUI(response)
                }

            })
        }
        else{
            Toast.makeText(context,"No network",Toast.LENGTH_SHORT).show()
            textview.text = "No games"
        }

    }

    fun updateUI(response: JSONArray?) {
        val gamesList: ArrayList<Game> = ArrayList()
        val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
        for (i in 0..(response!!.length() - 1)) {
            val obj = response.getJSONObject(i)
            var id: Long = 0
            var name = ""
            var description = ""
            var url: String = ""
            var storyline: String = ""
            var user_rating: String = ""
            var critic_rating: String = ""
            var img_url: String = ""
            var bg_url: String = ""
            var games: JSONArray = JSONArray()
            var release_date: String = ""
            var screenshots: JSONArray = JSONArray()
            var videos: JSONArray = JSONArray()
            try {
                id = obj.getLong("id")
                name = obj.get("name").toString()
                if (obj.has("summary")) description = obj.get("summary").toString().replace(" \n  \n", "\n", true)
                if (obj.has("url")) url = obj.getString("url")
                if (obj.has("storyline")) storyline = obj.getString("storyline")
                if (obj.has("rating")) user_rating = obj.getString("rating")
                if (obj.has("aggregated_rating")) critic_rating = obj.getString("aggregated_rating")
                if (obj.has("cover")) img_url = obj.getJSONObject("cover").getString("url")
                if (obj.has("games")) games = obj.getJSONArray("games")
                if (obj.has("first_release_date")) release_date = sdf.format(Date(obj.get("first_release_date") as Long))
                if (obj.has("screenshots")) {
                    screenshots = obj.getJSONArray("screenshots")
                    bg_url = (screenshots.get(0) as JSONObject).getString("url")
                }
                if (obj.has("videos")) videos = obj.getJSONArray("videos")
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
            game.img_url = img_url.replace("t_thumb", "t_cover_big")
            game.bg_url = bg_url.replace("t_thumb", "t_screenshot_big")
            game.games = games
            game.release_date = release_date
            game.screenshots = screenshots
            game.videos = videos
            gamesList.add(game)

        }
        progressDialog.cancel()
        if (gamesList.size == 0)
            textview.text = getString(R.string.empty_list)

        recyclerView.adapter = GameListAdapter(context, gamesList, search, false)

    }

}