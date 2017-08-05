package com.codeblooded.gamedb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.codeblooded.gamedb.ui.fragments.CollectionListFragment
import com.codeblooded.gamedb.ui.fragments.GameListFragment

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {

    //lateinit var navigationView:NavigationView
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var fm : FragmentManager

    lateinit var gameListFragment : GameListFragment
    lateinit var collectionListFragment : CollectionListFragment

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


        fm = supportFragmentManager
        if (savedInstanceState == null){
            val ft = fm.beginTransaction() as FragmentTransaction
            ft.add(R.id.frame,gameListFragment)
            ft.commit()
        }


        setupDrawer()
        navigationView.setCheckedItem(R.id.menu_item_games)
    }


    private fun setupDrawer() {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_games -> {
                    replaceFragment(gameListFragment)
                }
                R.id.menu_item_collections ->{
                    replaceFragment(collectionListFragment)
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