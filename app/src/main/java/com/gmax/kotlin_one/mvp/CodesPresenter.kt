package com.gmax.kotlin_one.mvp

import android.util.Log
import com.gmax.kotlin_one.base.BasePresenter
import com.gmax.kotlin_one.bean.OurCode
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.retrofit.MethodType
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class CodesPresenter
    @Inject constructor(private val mModel:CodesModel
            ,private val mView:CodesContract.View) : CodesContract.Presenter,BasePresenter(){
    override fun getData(map: Map<String, String>, type: Int) {
        addSubscription(mModel.getData(map,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    (code, msg) ->

                    Log.e("fafaf",(code).toString() + "--------code")
                    Log.e("fafaf",(msg) + "--------msg")
                    if (code == 200){
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
            MethodType.METHOD_TYPE_USER_UPDATE -> return api.getUpdateInfo(map)
            MethodType.METHOD_TYPE_ADD_ATTENTION -> return api.attentionFriend(map)
            MethodType.METHOD_TYPE_REMOVE_ATTENTION -> return api.removeAttention(map)
            MethodType.METHOD_TYPE_REMOVE_FAB -> return api.removeFab(map)
            MethodType.METHOD_TYPE_ADD_FAB -> return api.addFab(map)
            else -> return api.addDove(map)
        }
    }
}