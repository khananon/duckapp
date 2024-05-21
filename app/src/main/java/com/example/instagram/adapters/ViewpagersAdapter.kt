package com.example.instagram.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class ViewpagersAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm , BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val fragmenList = mutableListOf<Fragment>()
    val titleList = mutableListOf<String>()
    override fun getCount(): Int {
   return  fragmenList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmenList.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList.get(position)
    }
    fun addFragments(fragment: Fragment,title:String){
        fragmenList.add(fragment)
        titleList.add(title)
    }
}