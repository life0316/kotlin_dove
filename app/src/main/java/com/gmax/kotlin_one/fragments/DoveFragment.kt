package com.gmax.kotlin_one.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseTabFragment
import com.gmax.kotlin_one.base.TabAdapter
import com.gmax.kotlin_one.modules.dove.AddDoveActivity
import com.gmax.kotlin_one.modules.dove.AddRingActivity
import kotlinx.android.synthetic.main.add_popup.view.*
import kotlinx.android.synthetic.main.fragment_base_tab.*

@Suppress("DEPRECATION")
/**
 * Created by Administrator on 2017\8\1 0001.
 */
class DoveFragment : BaseTabFragment() {

    private val TAG = "DoveFragment"
    companion object{
        fun newInstance(content: String): DoveFragment {
            val args = Bundle()
            args.putString("ARGS", content)
            val fragment = DoveFragment()
            fragment.arguments = args
            return fragment
        }
    }
    var mPopupWindow: PopupWindow? = null

    override fun setupAdapter(adapter: TabAdapter) {
        adapter.clearFragment()

        adapter.addFragment(RingListFragment::class.java, "我的鸽环", getBundle("我的鸽环"))
        adapter.addFragment(DoveListFragment::class.java, "我的信鸽", getBundle("我的信鸽"))
    }

    override fun initView() {

        toolbar.title = ""
        title.text = "信鸽"
        searchTv.visibility = View.GONE
        fragment_base_add.visibility = View.GONE
        base_add.visibility = View.VISIBLE
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        base_add.setOnClickListener {
            v ->  showWindow(v)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("dfaf","Dove-onResume")
    }

    private fun showWindow(v:View) {
        if (mPopupWindow == null) {
            val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.add_popup, null)

            mPopupWindow = PopupWindow(view, resources.getDimension(R.dimen.DIP_150_DP).toInt(), resources.getDimension(R.dimen.DIP_180_DP).toInt())

            view.add_pigeon_ll.setOnClickListener {
                Log.e(TAG, "点击添加信鸽")

                val intent = Intent(activity, AddDoveActivity::class.java)
                startActivity(intent)

                if (mPopupWindow != null) {
                    mPopupWindow!!.dismiss()
                }
            }
            view.add_circle_ll.setOnClickListener {
                Log.e(TAG, "点击添加鸽环")

                val intent = Intent(activity, AddRingActivity::class.java)
                startActivity(intent)

                if (mPopupWindow != null) {
                    mPopupWindow!!.dismiss()
                }
            }

            view.add_mate_ll.setOnClickListener {
//                val intent = Intent(activity, MateActivity::class.java)
//                intent.putExtra("matelist_size", mateList.size())
//                startActivity(intent)

                if (mPopupWindow != null) {
                    mPopupWindow!!.dismiss()
                }
            }
        }
        mPopupWindow!!.isFocusable = true
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.setBackgroundDrawable(BitmapDrawable())

        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val xPos = windowManager.defaultDisplay.width * 1 / 5 - mPopupWindow!!.width / 2

        mPopupWindow!!.showAsDropDown(v, xPos, 40)

        val params = activity.window.attributes
        params.alpha = 1f

        activity.window.attributes = params


        mPopupWindow!!.setOnDismissListener(PopupWindow.OnDismissListener {
            val params1 = activity.window.attributes
            params1.alpha = 1f
            activity.window.attributes = params1
        })
    }
}