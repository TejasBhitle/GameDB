package com.codeblooded.gamedb.ui.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Game
import com.codeblooded.gamedb.ui.activities.LOG
import com.codeblooded.gamedb.ui.adapters.GameListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * Created by tejas on 10/20/17.
 */
class FavoritesFragment : Fragment() {

    lateinit var textview: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        textview = view.findViewById(R.id.centerTextView)
        progressDialog = ProgressDialog(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val task = FavoriteFetchAsyncTask(activity as Activity)
        progressDialog.setMessage(getString(R.string.please_wait))
        progressDialog.show()
        task.execute()
    }

    inner class FavoriteFetchAsyncTask(activity: Activity) : AsyncTask<String, Void, ArrayList<Game>>() {

        val activity = activity

        override fun doInBackground(vararg p0: String?): ArrayList<Game> {
            var favorites = ArrayList<Game>()

            val root = FirebaseDatabase.getInstance().reference
            val uid = FirebaseAuth.getInstance().currentUser?.uid as String
            val ref = root.child(uid).child(Constants.FAVORITES)

            ref.keepSynced(true)

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    favorites.clear()
                    for (snapshot in dataSnapshot.children) {
                        val id = snapshot.child(Constants.ID).value as Long
                        val name = snapshot.child(Constants.NAME).value as String
                        val description = snapshot.child(Constants.DESCRIPTION).value as String
                        val url = snapshot.child(Constants.URL).value as String
                        val storyline = snapshot.child(Constants.STORYLINE).value as String
                        val userRating = snapshot.child(Constants.USER_RATING).value.toString()
                        val criticRating = snapshot.child(Constants.CRITIC_RATING).value.toString()
                        val imgUrl = snapshot.child(Constants.IMG_URL).value as String
                        val bgUrl = snapshot.child(Constants.BG_URL).value as String
                        val releaseDate = snapshot.child(Constants.RELEASE_DATE).value as String

                        val game = Game()
                        game.id = id
                        game.name = name
                        game.description = description
                        game.url = url
                        game.storyline = storyline
                        game.user_rating = userRating
                        game.critic_rating = criticRating
                        game.img_url = imgUrl
                        game.bg_url = bgUrl
                        game.release_date = releaseDate

                        favorites.add(game)
                    }

                    updateUI(activity, favorites)
                    Log.e("DoINBackground", "Data Fetched")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(LOG, "onCancelled " + databaseError)
                }
            }

            ref.addValueEventListener(valueEventListener)
            return favorites// useless
        }
    }

    fun updateUI(context: Context, favorites: ArrayList<Game>) {
        progressDialog.cancel()
        Log.e(LOG, "updateUI")
        if(favorites.size != 0) {
            textview.visibility = View.GONE
            val adapter = GameListAdapter(context, favorites, false)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = adapter
        }
        else{
            recyclerView.removeAllViews()
            textview.text = "No Favorites"
            textview.visibility = View.VISIBLE
        }

    }


}