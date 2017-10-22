package com.codeblooded.gamedb.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Genre
import com.codeblooded.gamedb.ui.adapters.GenreListAdapter
import com.codeblooded.gamedb.util.RestClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_list.*
import org.json.JSONArray

/**
 * Created by tejas on 10/22/17.
 */
class GenreListFragment : Fragment() {

    lateinit var textview: TextView
    lateinit var progressDialog: ProgressDialog
    lateinit var genreList : ArrayList<Genre>
    lateinit var pref: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // !!. is a non-null asserted call
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)

        textview = view.findViewById(R.id.centerTextView)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        pref = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        getGenres("/genres/")//?fields=id,name,url&count=10")
        return view
    }



    fun getGenres(baseUrl: String) {

        if(RestClient.isNetworkConnected(context)) {

            progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Fetching Genres")
            progressDialog.show()

            val params = RequestParams()
            params.put("fields","id,name,url")
            params.put("count","10")

            RestClient.addHeaders()
            RestClient.get(baseUrl, params, object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    Log.e("Response:-> ",response.toString())
                    progressDialog.cancel()
                    updateUI(response)

                }

            })
        }
        else{
            Toast.makeText(context,"No network", Toast.LENGTH_SHORT).show()
            textview.text = "No Genres"
        }

    }

    fun updateUI(response : JSONArray?){
        Log.e("GenreListFragment","updateUI")
        genreList = ArrayList<Genre>()
        for(i in 0..(response!!.length() - 1)){
            val obj = response.getJSONObject(i)
            val id: String = obj.getString("id")
            val name: String = obj.getString("name")
            val url: String = obj.getString("url")

            val genre = Genre()
            genre.id = id
            genre.name = name
            genre.url = url

            genreList.add(genre)
        }
        //progressDialog.cancel()

        if (genreList.size == 0)
            textview.text = getString(R.string.empty_list)

        recyclerView.adapter = GenreListAdapter(context, genreList)
    }

}