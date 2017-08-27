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
import com.codeblooded.gamedb.*

import com.codeblooded.gamedb.model.Game
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.list_item_game.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val toolbar_collapse = findViewById<View>(R.id.toolbar_collapse) as CollapsingToolbarLayout
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Favorite game", Snackbar.LENGTH_LONG).show()
        }

        val description = findViewById<TextView>(R.id.description)
        val image = findViewById<ImageView>(R.id.image)
        val bg = findViewById<ImageView>(R.id.bg_img)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            val game = intent.extras.get(GAME) as Game
            Log.e(localClassName, game.name + "\n" + game.description)
            toolbar_collapse.setTitle(game.name)
            description.setText(game.description)
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
