package com.sahil.cocoontest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sahil.cocoontest.fragment.BookmarkFragment
import com.sahil.cocoontest.fragment.NewsFragment

class PagerAdapter(fm: FragmentManager?, var tabCount: Int) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        //Returning the current tabs
        return when (position) {
            0 -> {
                NewsFragment()
            }
            else -> BookmarkFragment()
        }
    }

    //get the number of tabs
    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "Top Stories"
        } else if (position == 1) {
            title = "Bookmarks"
        }
        return title
    }
}