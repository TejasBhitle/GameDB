package com.codeblooded.gamedb.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
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

        gameListFragment = GameListFragment.newInstance(true, "")

        pref = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        fm = supportFragmentManager
        if (savedInstanceState == null) {
            val ft = fm.beginTransaction() as FragmentTransaction
            ft.add(R.id.frame, gameListFragment)
            ft.commit()
        }

    }

    override fun onNewIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            if (query.trim { it <= ' ' } != "")
                gameListFragment.getGames("/games/?fields=*&search=" + Uri.encode(query))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as android.support.v7.widget.SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView.onActionViewExpanded()
        searchView.isIconified = false
        searchView.isQueryRefinementEnabled = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
