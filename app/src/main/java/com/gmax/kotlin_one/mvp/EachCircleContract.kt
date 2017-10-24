package com.gmax.kotlin_one.mvp

import com.gmax.kotlin_one.base.BaseView
import com.gmax.kotlin_one.bean.CircleBean
import com.gmax.kotlin_one.bean.OurDataBean
import rx.Observable

interface EachCircleContract {

    interface View : BaseView {
        fun setEachData(circleData: CircleBean)
    }
    interface Model{
        fun getData(map:Map<String,String>,type:Int): Observable<OurDataBean<CircleBean>>
    }
    interface Presenter{
        fun getData(map:Map<String,String>, type:Int)
    }
}