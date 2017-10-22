package com.codeblooded.gamedb.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.codeblooded.gamedb.Constants
import com.codeblooded.gamedb.R
import com.codeblooded.gamedb.ui.fragments.GameListFragment

class SearchActivity : AppCompatActivity() {

    lateinit var fm: FragmentManager
    lateinit var gameListFragment: GameListFragment
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        gameListFragment = GameListFragment.newInstance(true,"")

        pref = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        fm = supportFragmentManager
        if (savedInstanceState == null) {
            val ft = fm.beginTransaction() as FragmentTransaction
            ft.add(R.id.frame, gameListFragment)
            ft.commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
