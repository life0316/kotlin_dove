package com.gmax.kotlin_one.modules.dove

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.databinding.ActivityPigeonBinding
import kotlinx.android.synthetic.main.custom_toolbar.*

/**
 * Created by Administrator on 2017\8\9 0009.
 */
class DoveInfoActivity : BaseBindingActivity<ActivityPigeonBinding>(){
    override fun getMethod(): String? {
        return ""
    }

    override fun initView() {
        tl_custom.setNavigationIcon(R.mipmap.back_press)
        custom_toolbar_tv.text = getResources().getString(R.string.pigeon_info)
        setupToolbar(tl_custom)
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityPigeonBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_pigeon)
    }
}