package com.codeblooded.gamedb.ui.activities

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Game
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import android.widget.Toast
import android.content.Intent
import com.codeblooded.gamedb.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

const val LOG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    lateinit var game : Game
    var isLoggedIn: Boolean = false
    var isFav: Boolean = false

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        isLoggedIn = (currentUser != null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val toolbar_collapse = findViewById<View>(R.id.toolbar_collapse) as CollapsingToolbarLayout
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            if(isLoggedIn) {
                if(!isFav) {
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_white_24dp))
                    addToFavorites()
                }
                else{
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_white_24dp))
                    removeFromFavorites()
                }
            }
            else
                Toast.makeText(this@DetailActivity, "Sign in First", Toast.LENGTH_SHORT).show()

        }

        val description = findViewById<TextView>(R.id.description)
        val image = findViewById<ImageView>(R.id.image)
        val bg = findViewById<ImageView>(R.id.bg_img)
        val user_rating = findViewById<TextView>(R.id.user_rating)
        val critic_rating = findViewById<TextView>(R.id.critic_rating)
        val release_date = findViewById<TextView>(R.id.release_date)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            game = intent.extras.get(Constants.GAME) as Game
            isFav = intent.extras.getBoolean(Constants.IS_FAV_FRAGMENT)
            Log.e(localClassName, game.name + "\n" + game.description)
            Log.e(localClassName,"https:"+game.img_url)
            Log.e(localClassName,"https:"+game.bg_url)
            Log.e(localClassName,"isFav->"+isFav)

            toolbar_collapse.title = game.name
            description.text = game.description
            if (game.release_date != "") release_date.text = game.release_date
            if (game.user_rating != 0.0) user_rating.text = String.format("%.2f", game.user_rating)
            if (game.critic_rating != 0.0) critic_rating.text = String.format("%.2f", game.critic_rating)
            Picasso.with(this)
                    .load("https:"+game.img_url)
                    .placeholder(R.drawable.ic_image_grey_24dp)
                    .error(R.drawable.ic_image_grey_24dp)
                    .into(image)
            Picasso.with(this)
                    .load("https:"+game.bg_url)
                    .into(bg)
        }


        if(isFav)
            fab.setImageDrawable( resources.getDrawable(R.drawable.ic_favorite_white_24dp))
    }

    fun addToFavorites(){

        val root = FirebaseDatabase.getInstance().reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid as String
        val ref = root.child(uid).child(Constants.FAVORITES)

        val pushKey = game.id.toString() //ref.push().key

        val hashMap = game.getHashMap()

        val updateMap = HashMap<String,Any>()
        updateMap.put(pushKey,hashMap)

        ref.updateChildren(updateMap, DatabaseReference.CompletionListener {
            databaseError, databaseReference ->

            if(databaseError != null)
                Log.e(LOG,databaseError.message)
            else
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()

        })

    }

    fun removeFromFavorites(){
        val root = FirebaseDatabase.getInstance().reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid as String
        val ref = root.child(uid).child(Constants.FAVORITES).child(game.id.toString())
        ref.removeValue()
        Toast.makeText(this@DetailActivity,"Removed from Favorites",Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        if (game.url == "") menu.findItem(R.id.action_share).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_share -> {
                val share = Intent(Intent.ACTION_SEND)
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.check_out) + " \"" + game.name + "\"\n" + game.url + "\n")
                share.type = "text/plain"
                if (share.resolveActivity(packageManager) != null) {
                    startActivity(Intent.createChooser(share, resources.getString(R.string.share_via)))
                } else {
                    Toast.makeText(this, R.string.no_share_app_found, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
