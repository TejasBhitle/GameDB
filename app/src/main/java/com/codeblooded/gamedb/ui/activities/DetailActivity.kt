package com.codeblooded.gamedb.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Game
import com.codeblooded.gamedb.ui.adapters.ScreenshotsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import me.relex.circleindicator.CircleIndicator

const val LOG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    lateinit var game: Game
    var isLoggedIn: Boolean = false
    var isFav: Boolean = false
    lateinit var screenshotsAdapter: ScreenshotsAdapter
    lateinit var screenshot_viewPager: ViewPager

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

        screenshot_viewPager = findViewById(R.id.screenshot_viewpager)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            if (isLoggedIn) {
                if (!isFav) {
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_white_24dp))
                    addToFavorites()
                } else {
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_white_24dp))
                    removeFromFavorites()
                }
            } else {
                val snackbar = Snackbar.make(rootview, "Please sign in first", Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.setAction(R.string.sign_in, {
                    startActivity(Intent(this@DetailActivity, SignupActivity::class.java))
                })
                snackbar.show()
            }
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
            val root = FirebaseDatabase.getInstance().reference
            val uid = FirebaseAuth.getInstance().currentUser?.uid as String
            val ref = root.child(uid).child(Constants.FAVORITES).child(game.id.toString())

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    isFav = dataSnapshot.child(uid).child(Constants.FAVORITES).child(game.id.toString()).exists()
                    if (isLoggedIn) {
                        if (isFav) {
                            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_white_24dp))
                        } else {
                            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_white_24dp))
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            root.addListenerForSingleValueEvent(listener)

            Log.e(localClassName, game.name + "\n" + game.description)
            Log.e(localClassName, "https:" + game.img_url)
            Log.e(localClassName, "https:" + game.bg_url)

            toolbar_collapse.title = game.name
            description.text = game.description
            if (game.release_date != "") release_date.text = game.release_date
            //if (game.user_rating != "") user_rating.text = String.format("%.2f", game.user_rating)
            //if (game.critic_rating != "") critic_rating.text = String.format("%.2f", game.critic_rating)

            var userRating: String = game.user_rating
            var criticRating: String = game.critic_rating

            val critic_decimal_index = game.critic_rating.indexOf(".")
            if (criticRating.length > critic_decimal_index + 2)
                criticRating = criticRating.substring(0, critic_decimal_index + 2)


            val user_decimal_index = game.user_rating.indexOf(".")
            if (userRating.length > user_decimal_index + 2)
                userRating = userRating.substring(0, user_decimal_index + 2)

            user_rating.text = userRating
            critic_rating.text = criticRating

            Picasso.with(this)
                    .load("https:" + game.img_url)
                    .placeholder(R.drawable.ic_image_grey_24dp)
                    .error(R.drawable.ic_image_grey_24dp)
                    .into(image)
            Picasso.with(this)
                    .load("https:" + game.bg_url)
                    .into(bg)

            screenshotsAdapter = ScreenshotsAdapter(this, game.screenshots)
            screenshot_viewPager.adapter = screenshotsAdapter
            val indicator: CircleIndicator = findViewById(R.id.screenshot_indicator)
            indicator.setViewPager(screenshot_viewPager)
            screenshotsAdapter.registerDataSetObserver(indicator.dataSetObserver)
        }

    }

    fun addToFavorites() {
        isFav = true

        val root = FirebaseDatabase.getInstance().reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid as String
        val ref = root.child(uid).child(Constants.FAVORITES)

        val pushKey = game.id.toString() //ref.push().key

        val hashMap = game.getHashMap()

        val updateMap = HashMap<String, Any>()
        updateMap.put(pushKey, hashMap)

        ref.updateChildren(updateMap, { databaseError, databaseReference ->

            if (databaseError != null)
                Log.e(LOG, databaseError.message)
            else {
                val snackbar = Snackbar.make(rootview, "Added to Favorites", Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.show()
            }

        })

    }

    fun removeFromFavorites() {
        isFav = false
        val root = FirebaseDatabase.getInstance().reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid as String
        val ref = root.child(uid).child(Constants.FAVORITES).child(game.id.toString())
        ref.removeValue()
        val snackbar = Snackbar.make(rootview, "Removed from Favorites", Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.show()
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
