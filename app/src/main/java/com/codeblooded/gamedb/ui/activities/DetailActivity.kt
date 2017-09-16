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
import com.codeblooded.gamedb.GAME
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.model.Game
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import android.widget.Toast
import android.content.Intent

class DetailActivity : AppCompatActivity() {
    lateinit var game : Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val toolbar_collapse = findViewById<View>(R.id.toolbar_collapse) as CollapsingToolbarLayout
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Favorite game", Snackbar.LENGTH_LONG).show()
        }

        val description = findViewById<TextView>(R.id.description)
        val image = findViewById<ImageView>(R.id.image)
        val bg = findViewById<ImageView>(R.id.bg_img)
        val user_rating = findViewById<TextView>(R.id.user_rating)
        val critic_rating = findViewById<TextView>(R.id.critic_rating)
        val release_date = findViewById<TextView>(R.id.release_date)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            game = intent.extras.get(GAME) as Game
            Log.e(localClassName, game.name + "\n" + game.description)
            Log.e(localClassName,"https:"+game.img_url)
            Log.e(localClassName,"https:"+game.bg_url)


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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_share -> {
                Snackbar.make(rootview, "Share game", Snackbar.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
