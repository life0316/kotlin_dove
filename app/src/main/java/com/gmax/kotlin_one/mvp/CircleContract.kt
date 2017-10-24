package com.gmax.kotlin_one.mvp

import com.gmax.kotlin_one.base.BaseView
import com.gmax.kotlin_one.bean.CircleBean
import com.gmax.kotlin_one.bean.OurListBean
import com.gmax.kotlin_one.common.DataLoadType
import com.gmax.kotlin_one.common.DataLoadType.TYPE_REFRESH
import rx.Observable

interface CircleContract {

    interface View : BaseView {
        fun setData(circleData: List<CircleBean>,loadType:DataLoadType = TYPE_REFRESH)
    }
    interface Model{
        fun getData(map:Map<String,String>,type:Int): Observable<OurListBean<CircleBean>>
    }
    interface Presenter{
        fun getData(map:Map<String,String>, type:Int, loadType: DataLoadType = TYPE_REFRESH)
    }
}