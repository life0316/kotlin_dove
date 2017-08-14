package com.gmax.kotlin_one.modules.dove

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.ActivityAddringBinding
import com.gmax.kotlin_one.getApiCompoent
import com.gmax.kotlin_one.inject.CodeModule
import com.gmax.kotlin_one.inject.DaggerCodeComponent
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.CodesPresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.util.HashMap
import javax.inject.Inject

class AddRingActivity:BaseBindingActivity<ActivityAddringBinding>(), CodesContract.View{
    @Inject lateinit var mCodePresenter: CodesPresenter
    override fun initView() {
        tl_custom.setNavigationIcon(R.mipmap.back_press)
        custom_toolbar_tv.text = getString(R.string.add_ring)
        setupToolbar(tl_custom)

        DaggerCodeComponent.builder()
                .apiCompoent(getApiCompoent())
                .apiModule(ApiModule(this))
                .codeModule(CodeModule(this))
                .build().inject(this)
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityAddringBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_addring)
    }

    override fun successToDo() {

    }

    override fun getMethod(): String {
        return MethodConstant.RING_ADD
    }

    fun getParaMap(): Map<String, String> {

        val map = HashMap<String, String>()

        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())
        map.put(MethodParams.PARAMS_TOKEN, getToken())
        map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
        map.put(MethodParams.PARAMS_PLAYER_ID, getUserObjId())
//        map.put(MethodParams.PARAMS_FOOT_RING, getRingCode())
//        map.put(MethodParams.PARAMS_GENDER, getPigeonSex())
//        map.put(MethodParams.PARAMS_AGE, "1")
//        map.put(MethodParams.PARAMS_COLOR, getPigeonColor())
//        map.put(MethodParams.PARAMS_EYE, getPigeonEyes())
//        map.put(MethodParams.PARAMS_ANCESTRY, getPigeonBlood())

        return map
    }
}