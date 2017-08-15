package com.gmax.kotlin_one.mvp

import android.util.Log
import com.gmax.kotlin_one.base.BasePresenter
import com.gmax.kotlin_one.bean.UploadImageBean
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.retrofit.MethodType
import okhttp3.RequestBody
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by Administrator on 2017\8\14 0014.
 */
class ImagePresenter
@Inject constructor(private val mModel:ImageModel
                                         , private val mView:ImageContract.ImageView) : ImageContract.ImagePresenter, BasePresenter(){
    override fun getData(map: HashMap<String, RequestBody>, type: Int) {
        addSubscription(mModel.getData(map,type).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    (data, msg, code) ->

                    Log.e("fafaf",(code).toString() + "--------code")
                    Log.e("fafaf",(msg) + "--------msg")
                    if (code == 200){
                        mView.uploadImage(data)
                    }else{
                        //  TODO 最好是给个错误提示
                    }
                }))
    }
}

class ImageModel
@Inject constructor(private val api: IOurNewService) : ImageContract.ImageModel {
    override fun getData(map: HashMap<String, RequestBody>, type: Int): Observable<UploadImageBean> {
        when(type){
            MethodType.METHOD_TYPE_IMAGE_UPLOAD -> return api.getUploadPic(map)
            else -> return api.getUploadPic(map)
        }
    }
}