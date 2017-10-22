package com.codeblooded.gamedb.ui.fragments

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import android.widget.TextView
import android.widget.Toast
import com.codeblooded.gamedb.util.RestClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

/**
 * Created by tejas on 10/22/17.
 */
class GenreListFragment : Fragment() {

    lateinit var textview: TextView
    lateinit var progressDialog: ProgressDialog

    lateinit var pref: SharedPreferences


    fun getGenres(baseUrl: String) {

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
            Toast.makeText(context,"No network", Toast.LENGTH_SHORT).show()
            textview.text = "No games"
        }

    }

    fun updateUI(response : JSONArray?){

    }

}