package com.gmax.kotlin_one.mvp

import android.util.Log
import com.gmax.kotlin_one.base.BasePresenter
import com.gmax.kotlin_one.bean.OurCode
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.retrofit.MethodType
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Administrator on 2017\8\2 0002.
 */
class CodesPresenter
    @Inject constructor(private val mModel:CodesModel
            ,private val mView:CodesContract.View) : CodesContract.Presenter,BasePresenter(){
    override fun getData(map: Map<String, String>, type: Int) {
        addSubscription(mModel.getData(map,type).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res ->

                    Log.e("fafaf",(res.code).toString() + "--------code")
                    Log.e("fafaf",(res.msg) + "--------msg")
                    if (res.code == 200){
                        mView.successToDo()
                    }else{
                        //  TODO 最好是给个错误提示
                    }
                }))
    }
}

class CodesModel
@Inject constructor(private val api: IOurNewService) : CodesContract.Model {
    override fun getData(map: Map<String, String>, type: Int): Observable<OurCode> {
        when(type){
            MethodType.METHOD_TYPE_REQUEST_VER_CODE -> return api.getRequestVerCode(map)
            MethodType.METHOD_TYPE_RING_DELETE -> return api.deleteRing(map)
            MethodType.METHOD_TYPE_DOVE_DELETE -> return api.deleteDove(map)

            MethodType.METHOD_TYPE_DOVE_ADD -> return api.addDove(map)
            MethodType.METHOD_TYPE_RING_ADD -> return api.addRing(map)
            else -> return api.addDove(map)
        }
    }
}