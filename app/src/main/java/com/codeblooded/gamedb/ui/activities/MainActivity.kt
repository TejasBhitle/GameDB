package com.codeblooded.gamedb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.codeblooded.gamedb.ui.activities.SignupActivity
import com.codeblooded.gamedb.ui.fragments.CollectionListFragment
import com.codeblooded.gamedb.ui.fragments.FavoritesFragment
import com.codeblooded.gamedb.ui.fragments.GameListFragment
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var fm: FragmentManager
    lateinit var gameListFragment: GameListFragment
    lateinit var collectionListFragment: CollectionListFragment
    lateinit var favoritesFragment: FavoritesFragment
    var isLoggedIn: Boolean = false
    var isMenuActive: Boolean = true
    lateinit var pref: SharedPreferences

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        isLoggedIn = (currentUser != null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        gameListFragment = GameListFragment()
        collectionListFragment = CollectionListFragment()
        favoritesFragment = FavoritesFragment()

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        pref = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        if (pref.getBoolean(Constants.FIRST_RUN, true)) {
            val i = Intent(this@MainActivity, IntroActivity::class.java)
            startActivity(i)
            //  Make a new preferences editor
            val e = pref.edit()
            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean(Constants.FIRST_RUN, false)
            //  Apply changes
            e.apply()
        }

        fm = supportFragmentManager
        if (savedInstanceState == null) {
            val ft = fm.beginTransaction() as FragmentTransaction
            ft.add(R.id.frame, gameListFragment)
            ft.commit()
        }

        setupDrawer()
        navigationView.setCheckedItem(R.id.menu_item_games)
        navigationView.getHeaderView(0).setOnClickListener {
            startActivity(Intent(this@MainActivity, SignupActivity::class.java))
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val header = navigationView.getHeaderView(0)
        val textview = header.findViewById<TextView>(R.id.header_textView)
        if (currentUser != null)
            textview.text = "Signed in as\n" + currentUser.email
        else textview.text = "Sign in"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isMenuActive)
            menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.popular -> {
                pref.edit().putString(Constants.SORT, Constants.POPULARITY).apply()
                gameListFragment.getGames()
            }
        /*R.id.release_date -> {
            pref.edit().putString(SORT, FIRST_RELEASE_DATE).apply()
            gameListFragment.getGames()
        }*/
            R.id.rating -> {
                pref.edit().putString(Constants.SORT, Constants.RATING).apply()
                gameListFragment.getGames()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawer() {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_games -> {
                    replaceFragment(gameListFragment, true)
                    setTitle(R.string.games)
                }
                R.id.menu_item_collections -> {
                    replaceFragment(collectionListFragment, true)
                    setTitle(R.string.collections)
                }
                R.id.menu_item_favorites -> {
                    if (isLoggedIn) {
                        replaceFragment(favoritesFragment, false)
                        setTitle(R.string.favorites)
                    } else {
                        Toast.makeText(this@MainActivity, "Sign in First", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.nav_about -> {
                    Snackbar.make(drawerLayout, "About this app", Snackbar.LENGTH_SHORT).show()
                    LibsBuilder()
                            //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                            .withActivityTitle(getString(R.string.about))
                            .withAboutAppName(getString(R.string.app_name))
                            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                            .withAboutIconShown(true)
                            .withAboutVersionShown(true)
                            .withAboutDescription(getString(R.string.app_description))
                            .start(this@MainActivity)
                }
            }
            navigationView.setCheckedItem(item.itemId)
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, isMenuActive: Boolean) {
        fm.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit()
        this.isMenuActive = isMenuActive
        invalidateOptionsMenu()
    }

}