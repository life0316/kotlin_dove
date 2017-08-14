package com.gmax.kotlin_one.mvp

import com.gmax.kotlin_one.base.BaseView
import com.gmax.kotlin_one.bean.OurListBean
import com.gmax.kotlin_one.bean.RingBean
import rx.Observable

interface RingContract {

    interface View : BaseView {
        fun setData(ringData: List<RingBean>)
    }
    interface Model{
        fun getData(map:Map<String,String>,type:Int): Observable<OurListBean<RingBean>>
    }
    interface Presenter{
        fun getData(map:Map<String,String>,type:Int)
    }
}