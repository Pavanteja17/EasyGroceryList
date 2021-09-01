package com.amreen.grocerylist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var tab:TabLayout
    lateinit var frameLayout: FrameLayout
    lateinit var viewPager: ViewPager
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tab=findViewById(R.id.tab_layout)
       frameLayout=findViewById(R.id.frame)
        toolbar=findViewById(R.id.toolbar)
        setUpToolbar()
        tab.addTab(tab.newTab().setText("PRIMARY LIST"))
        tab.addTab(tab.newTab().setText("SHOPPING LIST"))
        supportFragmentManager.beginTransaction().replace(R.id.frame,MasterList()).commit()
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }


            override fun onTabSelected(p0: TabLayout.Tab?) {
                when(p0?.position){
                    0->{
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame,MasterList()
                        ).commit()
                        println("in master list")
                    }
                    1->{
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame,ShoppingList()
                        ).commit()
                    }


                }



            }

        })
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title="Easy Grocery List"
    }

    override fun onBackPressed() {

        finishAffinity()
    }


}