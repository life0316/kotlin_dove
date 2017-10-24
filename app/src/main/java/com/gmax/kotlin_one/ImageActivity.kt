package com.gmax.kotlin_one

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.gmax.kotlin_one.adapter.PopAdapter
import kotlinx.android.synthetic.main.activity_image.*
import android.util.Pair


class ImageActivity : AppCompatActivity() {
    lateinit var adapter: PopAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        initview()
    }

    private fun initview() {
        var list = intent.extras.getStringArrayList(STRLIST)
        viewpage.transitionName = "img" + intent.getIntExtra(IMGPOS, 0)
        adapter = PopAdapter(list)
        viewpage.adapter = adapter
        viewpage.currentItem = intent.getIntExtra(IMGPOS, 0)
        //滑动监听改变transitionName
        viewpage.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                viewpage.transitionName = "img" + position
            }
            override fun onPageSelected(position: Int) {}
        })
    }

    companion object {
        val IMGPOS = "IMG"
        val STRLIST = "STRLIST"
        fun startActivity(context: Context, imageView: Array<Pair<View, String>?>, postion: Int, list: ArrayList<String>) {
            val intent = Intent(context, ImageActivity::class.java)
            val bundle = Bundle()
            bundle.putStringArrayList(STRLIST, list)
            intent.putExtra(IMGPOS, postion)
            intent.putExtras(bundle)
            if (Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, *imageView).toBundle())
            } else {
                context.startActivity(intent)
            }
        }
    }
}
