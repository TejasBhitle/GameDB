package com.codeblooded.gamedb.ui.fragments

import android.app.ProgressDialog
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
import com.codeblooded.gamedb.IdCallBackListener
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.adapters.GameListAdapter
import com.codeblooded.gamedb.model.Game
import com.codeblooded.gamedb.util.IdsUtil
import com.codeblooded.gamedb.util.RestClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_list.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by tejas on 8/5/17.
 */
class GameListFragment :Fragment(){

    lateinit var textview: TextView
    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // !!. is a non-null asserted call
        val view = inflater!!.inflate(R.layout.fragment_list,container,false)


         textview = view.findViewById(R.id.centerTextView) as TextView

        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context,2)


        getGames()
        return view
    }

    fun getGames(){
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching Games")
        progressDialog.show()
        var idsUtil : IdsUtil = IdsUtil("/games/",object : IdCallBackListener{
            override fun method(ids: String) {

                RestClient.get("/games/$ids", RequestParams(),object : JsonHttpResponseHandler(){

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
        })
    }

    fun updateUI(response : JSONArray?){
        val games : ArrayList<Game> = ArrayList()
        for (i in 0..(response!!.length() - 1)){
            val obj = response.getJSONObject(i)
            val name = obj.get("name").toString()
            //val url = obj.getString("url").toString()
            val url = obj.getJSONObject("cover").getString("url")
            val game = Game()
            game.name = name
            game.img_url = url
            games.add(game)

        }
        progressDialog.cancel()
        if(games.size == 0)
            textview.text = "No Games"

        recyclerView.adapter = GameListAdapter(context,games)

        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()

    }

}