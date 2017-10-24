package com.gmax.kotlin_one

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.databinding.ActivityMainBinding
import com.gmax.kotlin_one.fragments.DoveCircleFragment
import com.gmax.kotlin_one.fragments.DoveFragment
import com.gmax.kotlin_one.fragments.HomeFragment
import com.gmax.kotlin_one.modules.trail.TrailFragment
import com.gmax.kotlin_one.utils.RxBus
import com.gmax.kotlin_one.utils.SpUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseBindingActivity<ActivityMainBinding>(){

    internal var mTab1: DoveFragment? = null
    internal var mTab2: TrailFragment? = null
    internal var mTab3: DoveCircleFragment? = null
    internal var mTab4: HomeFragment? = null
    internal var exitTime:Long = 0

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

        SpUtils.putInt(this,Constant.CLICK_NUM,position)
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        hideAllFragments(ft)
        when (position) {
            0 -> if (mTab1 == null) {
                mTab1 = DoveFragment.newInstance(getString(R.string.main_dove))
                ft.add(R.id.act_main_cvp, mTab1)
            } else {
                ft.show(mTab1)
            }
            1 -> if (mTab2 == null) {
                mTab2 = TrailFragment.newInstance(getString(R.string.main_trail))
                ft.add(R.id.act_main_cvp, mTab2)
            } else {
                ft.show(mTab2)
            }
            2 -> if (mTab3 == null) {
                mTab3 = DoveCircleFragment.newInstance(getString(R.string.main_circle))
                ft.add(R.id.act_main_cvp, mTab3)
            } else {
                ft.show(mTab3)
            }
            3 -> if (mTab4 == null) {
                mTab4 = HomeFragment.newInstance(getString(R.string.main_mine))
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        val isExit = SpUtils.getBoolean(this,Constant.MAIN_EXIT,true)
        if (isExit) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    showToast(getString(R.string.once_exit))
                    exitTime = System.currentTimeMillis()
                } else {
                    finish()
                    System.exit(0)
                }
                return true
            }
        }else{
            val other:String = SpUtils.getString(this,Constant.OTHER_NOT_EXIT)
            if (other != ""){
                RxBus.getInstance().post(Constant.MAIN_EXIT,other)
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}