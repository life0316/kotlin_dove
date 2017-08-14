package com.gmax.kotlin_one.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import java.util.ArrayList

/**
 * Created by Administrator on 2017\8\1 0001.
 */
class TabAdapter(fm: FragmentManager, mViewPager: ViewPager, mTabLayout: TabLayout, context: Context) : FragmentPagerAdapter(fm) {
    var mViewPager: ViewPager
    var context: Context
    var mTabLayout: TabLayout


    internal var fragments = ArrayList<Class<*>>()
    internal var mFragmentTitleList: MutableList<String> = ArrayList()
    internal var bundles = ArrayList<Bundle>()

    init {
        this.mTabLayout = mTabLayout
        this.mViewPager = mViewPager
        this.context = context
        //设置数据适配器
        this.mViewPager.adapter = this
        this.mTabLayout.setupWithViewPager(mViewPager)
    }

    fun addFragment(fragment: Class<*>, title: String, bundle: Bundle) {

        Log.e("fragments", fragments.size.toString() + "---")

        fragments.add(fragment)
        mFragmentTitleList.add(title)
        bundles.add(bundle)
        notifyDataSetChanged()
    }

    fun clearFragment() {
        fragments.clear()
        mFragmentTitleList.clear()
        bundles.clear()
        //    notifyDataSetChanged();
    }

    override fun getItem(position: Int): Fragment {
        val clazz = fragments[position]
        return Fragment.instantiate(context, clazz.name, bundles[position])
    }

    override fun getCount(): Int {

        return if (fragments.size == 0) 0 else fragments.size
    }


    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

}