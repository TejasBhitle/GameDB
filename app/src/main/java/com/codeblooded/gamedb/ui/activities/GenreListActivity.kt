package com.codeblooded.gamedb.ui.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.ui.fragments.GameListFragment
import com.codeblooded.gamedb.util.RestClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by tejas on 10/22/17.
 */
class GenreListActivity : AppCompatActivity() {

    lateinit var fm: FragmentManager
    lateinit var gameListFragment: GameListFragment
    lateinit var genreId : String
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_list)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        if(intent.extras != null){
            genreId = intent.extras.getString(Constants.ID)
            title = (intent.extras.getString(Constants.NAME))
            fetchGenre("/genres/"+genreId)
        }

    }

    fun fetchGenre(baseUrl: String){

        if(RestClient.isNetworkConnected(this)) {

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Fetching Games")
            progressDialog.show()

            RestClient.addHeaders()
            RestClient.get(baseUrl, RequestParams(), object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    progressDialog.cancel()
                    val genreObj : JSONObject  = response!!.get(0) as JSONObject
                    val gamesArr : JSONArray = genreObj.getJSONArray("games")
                    val len = if(gamesArr.length() < 10) gamesArr.length() else 10
                    var gameIds : String = "/games/"
                    for(i in 0..(len)){
                        gameIds += gamesArr.get(i).toString()+","
                    }
                    gameIds = gameIds.substring(0,gameIds.length-1)
                    Log.e("gameIds",gameIds)
                    callGameListFragment(gameIds)
                }

            })
        }
        else{
            Toast.makeText(this,"No network", Toast.LENGTH_SHORT).show()
            //textview.text = "No games"
        }

    }

    fun callGameListFragment(url : String){
        gameListFragment = GameListFragment.newInstance(false,url)
        fm = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.add(R.id.frame, gameListFragment)
        ft.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == android.R.id.home)
            onBackPressed()

        return true
    }

}