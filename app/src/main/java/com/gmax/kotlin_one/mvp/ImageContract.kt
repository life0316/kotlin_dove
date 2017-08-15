package com.gmax.kotlin_one.mvp

import com.gmax.kotlin_one.bean.UploadImageBean
import okhttp3.RequestBody
import rx.Observable
import java.util.HashMap

interface ImageContract {

    interface ImageView{
        fun uploadImage(data:String)
    }
    interface ImageModel{
        fun getData(map:HashMap<String, RequestBody>, type:Int): Observable<UploadImageBean>
    }
    interface ImagePresenter{
        fun getData(map: HashMap<String, RequestBody>, type:Int)
    }
}