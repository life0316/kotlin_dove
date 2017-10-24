package com.gmax.kotlin_one.modules.circle

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.databinding.ActivityReleaseBinding
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.CodesPresenter
import kotlinx.android.synthetic.main.custom_toolbar.*
import javax.inject.Inject

class ReleaseCircleActivity : BaseBindingActivity<ActivityReleaseBinding>(), CodesContract.View{


//    @Inject lateinit var mCodePresenter: CodesPresenter

    override fun getMethod(): String? {
        return ""
    }

    override fun initView() {
        custom_toolbar_left_tv.visibility = View.VISIBLE
        custom_toolbar_tv.text = getString(R.string.release_circle)
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityReleaseBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_release)
    }

    override fun onBackPressed() {

    }

    override fun successToDo() {

    }

}