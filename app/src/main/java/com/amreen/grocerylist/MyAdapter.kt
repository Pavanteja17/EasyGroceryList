package com.amreen.grocerylist

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class MyAdapter(val myContext: Context, supportFragmentManager: FragmentManager, var tabCount: Int) : FragmentPagerAdapter(supportFragmentManager) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0->{
                return MasterList()

            }
            1->{
                return ShoppingList()
            }
            else->return MasterList()
        }

    }

    override fun getCount(): Int {
        return tabCount
    }

}
