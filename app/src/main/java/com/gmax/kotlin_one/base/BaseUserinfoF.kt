package com.gmax.kotlin_one.base

import android.databinding.ViewDataBinding
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.bean.UserInnerData
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.LoginContract

/**
 * Created by Administrator on 2017\8\14 0014.
 */
abstract class BaseUserinfoF<VDB : ViewDataBinding>:BaseBindingFragment<VDB>(),  LoginContract.View, CodesContract.View  {
    override fun successToDo() {

    }

    override fun setData(data: UserInnerData) {

    }

    override fun setUserData(data: UserInfoInner) {

    }

}