package com.codeblooded.gamedb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.codeblooded.gamedb.ui.fragments.CollectionListFragment
import com.codeblooded.gamedb.ui.fragments.GameListFragment

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var fm : FragmentManager

    lateinit var gameListFragment : GameListFragment
    lateinit var collectionListFragment : CollectionListFragment

    lateinit var pref :SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        gameListFragment = GameListFragment()
        collectionListFragment = CollectionListFragment()

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        if (pref.getBoolean(FIRST_RUN, true)) {
            val i = Intent(this@MainActivity, IntroActivity::class.java)
            startActivity(i)
            //  Make a new preferences editor
            val e = pref.edit()
            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean(FIRST_RUN, false)
            //  Apply changes
            e.apply()
        }

        fm = supportFragmentManager
        if (savedInstanceState == null){
            val ft = fm.beginTransaction() as FragmentTransaction
            ft.add(R.id.frame, gameListFragment)
            ft.commit()
        }


        setupDrawer()
        navigationView.setCheckedItem(R.id.menu_item_games)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.popular -> {
                pref.edit().putString(SORT, POPULARITY).apply()
                gameListFragment.getGames()
            }
            /*R.id.release_date -> {
                pref.edit().putString(SORT, FIRST_RELEASE_DATE).apply()
                gameListFragment.getGames()
            }*/
            R.id.rating -> {
                pref.edit().putString(SORT, RATING).apply()
                gameListFragment.getGames()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawer() {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_games -> {
                    replaceFragment(gameListFragment)
                }
                R.id.menu_item_collections -> {
                    replaceFragment(collectionListFragment)
                }
                R.id.nav_about -> {

                }
            }
            navigationView.setCheckedItem(item.itemId)
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val ft = fm.beginTransaction() as FragmentTransaction
        ft.replace(R.id.frame,fragment)
        ft.commit()
    }

}