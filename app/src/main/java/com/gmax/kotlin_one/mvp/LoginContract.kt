package com.gmax.kotlin_one.mvp

import com.gmax.kotlin_one.base.BaseView
import com.gmax.kotlin_one.bean.OurDataBean
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.bean.UserInnerData
import rx.Observable

interface LoginContract {

    interface View : BaseView {
        fun setData(data:UserInnerData)
        fun setUserData(data:UserInfoInner)
    }


    interface Model{
        fun getData(map:Map<String,String>,type:Int): Observable<OurDataBean<UserInnerData>>
        fun getInfoData(map:Map<String,String>,type:Int): Observable<OurDataBean<UserInfoInner>>
    }

    interface Presenter{
        fun getData(map:Map<String,String>,type:Int,preType:Int)

    }
}