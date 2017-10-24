package com.gmax.kotlin_one.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gmax.kotlin_one.base.BaseTabFragment
import com.gmax.kotlin_one.base.TabAdapter
import com.gmax.kotlin_one.modules.circle.AllCircleFragment
import com.gmax.kotlin_one.modules.circle.FriendCircleFragment
import com.gmax.kotlin_one.modules.circle.MyCircleFragment
import com.gmax.kotlin_one.modules.circle.ReleaseCircleActivity
import kotlinx.android.synthetic.main.fragment_base_tab.*

class DoveCircleFragment : BaseTabFragment() {

    companion object{
        fun newInstance(content: String): DoveCircleFragment {
            val args = Bundle()
            args.putString("ARGS", content)
            val fragment = DoveCircleFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupAdapter(adapter: TabAdapter) {
        adapter.clearFragment()

        adapter.addFragment(AllCircleFragment::class.java, "鸽圈", getBundle("鸽圈"))
        adapter.addFragment(FriendCircleFragment::class.java, "好友圈", getBundle("好友圈"))
        adapter.addFragment(MyCircleFragment::class.java, "我的鸽圈", getBundle("我的鸽圈"))
    }

    override fun initView() {

        toolbar.title = ""
        title.text = "鸽圈"
        searchTv.visibility = View.GONE
        fragment_base_add.visibility = View.GONE
        base_add.visibility = View.VISIBLE
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        base_add.setOnClickListener {
            val intent = Intent(activity, ReleaseCircleActivity::class.java)
            startActivity(intent)
        }
    }
}