package com.gmax.kotlin_one.mvp

import com.gmax.kotlin_one.bean.OurCode
import com.gmax.kotlin_one.bean.UploadImageBean
import okhttp3.RequestBody
import rx.Observable

interface CodesContract {

    interface View{
        fun successToDo()
    }
    interface Model{
        fun getData(map:Map<String,String>,type:Int): Observable<OurCode>
    }
    interface Presenter{
        fun getData(map:Map<String,String>,type:Int)
    }
}