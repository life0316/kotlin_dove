package com.gmax.kotlin_one

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.databinding.ActivityMainBinding
import com.gmax.kotlin_one.fragments.DoveCircleFragment
import com.gmax.kotlin_one.fragments.DoveFragment
import com.gmax.kotlin_one.fragments.HomeFragment
import com.gmax.kotlin_one.fragments.PigeonFragment
import com.gmax.kotlin_one.modules.trail.TrailFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseBindingActivity<ActivityMainBinding>(){

    internal var mTab1: DoveFragment? = null
    internal var mTab2: TrailFragment? = null
    internal var mTab3: DoveCircleFragment? = null
    internal var mTab4: HomeFragment? = null

    override fun getMethod(): String? {
        return ""
    }

    override fun initView() {
        setSelection(0)

        act_main_rg.setOnCheckedChangeListener { _, checkedId ->
            var tab = 0
            when(checkedId){
                R.id.act_main_rb_tab1 -> tab = 0
                R.id.act_main_rb_tab2 -> tab = 1
                R.id.act_main_rb_tab3 -> tab = 2
                R.id.act_main_rb_tab4 -> tab = 3
            }
            setSelection(tab)
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMainBinding {

        return DataBindingUtil.setContentView(this,R.layout.activity_main)
    }


    private fun setSelection(position: Int) {

//        mRxBus.post("clickRadio", position)
//
//        currentPos = position
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        hideAllFragments(ft)

        when (position) {
            0 -> if (mTab1 == null) {
                mTab1 = DoveFragment.newInstance("信鸽")
                ft.add(R.id.act_main_cvp, mTab1)
            } else {
                ft.show(mTab1)
            }
            1 -> if (mTab2 == null) {
                mTab2 = TrailFragment.newInstance("轨迹")
                ft.add(R.id.act_main_cvp, mTab2)
            } else {
                ft.show(mTab2)
            }
            2 -> if (mTab3 == null) {
                mTab3 = DoveCircleFragment.newInstance("鸽圈")
                ft.add(R.id.act_main_cvp, mTab3)
            } else {
                ft.show(mTab3)
            }
            3 -> if (mTab4 == null) {
                mTab4 = HomeFragment.newInstance("我的")
                ft.add(R.id.act_main_cvp, mTab4)
            } else {
                ft.show(mTab4)
            }
        }
        ft.commitAllowingStateLoss()
    }

    private fun hideAllFragments(ft: FragmentTransaction) {

        if (mTab1 != null) {
            ft.hide(mTab1)
        }
        if (mTab2 != null) {
            ft.hide(mTab2)
        }
        if (mTab3 != null) {
            ft.hide(mTab3)
        }
        if (mTab4 != null) {
            ft.hide(mTab4)
        }
    }
}