package com.gmax.kotlin_one.mvp

import com.gmax.kotlin_one.base.BaseView
import com.gmax.kotlin_one.bean.InnerDoveData
import com.gmax.kotlin_one.bean.OurListBean
import rx.Observable

interface DoveContract {

    interface View : BaseView {
        fun setData(doveData: List<InnerDoveData>)
    }
    interface Model{
        fun getData(map:Map<String,String>,type:Int): Observable<OurListBean<InnerDoveData>>
    }
    interface Presenter{
        fun getData(map:Map<String,String>,type:Int)
    }
}