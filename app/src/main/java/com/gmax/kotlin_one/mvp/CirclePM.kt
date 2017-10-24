package com.gmax.kotlin_one.mvp

import android.util.Log
import com.gmax.kotlin_one.base.BasePresenter
import com.gmax.kotlin_one.bean.CircleBean
import com.gmax.kotlin_one.bean.OurListBean
import com.gmax.kotlin_one.common.DataLoadType
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.retrofit.MethodType
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class CirclePresenter
@Inject constructor(private val mModel:CircleModel
                    , private val mView: CircleContract.View) : CircleContract.Presenter, BasePresenter(){
    override fun getData(map: Map<String, String>, type: Int, loadType: DataLoadType) {
        addSubscription(mModel.getData(map,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    (code, msg, data) ->
                    Log.e("data",(code).toString() + "--------code")
                    Log.e("data",(msg) + "--------msg")
                    if (code == 200){
                        mView.setData(data,loadType)
                        Log.e("data",(data.toString()) + "--------msg")
                    }else{
                        //  TODO 最好是给个错误提示
                    }
                }))
    }
}

class CircleModel
@Inject   constructor(private val api: IOurNewService) : CircleContract.Model {
    override fun getData(map: Map<String, String>, type: Int): Observable<OurListBean<CircleBean>> {
        Log.e("data", map.toString() + "--------map")
        when (type) {
            MethodType.METHOD_TYPE_ALL_CIRCLES-> return api.getAllCircles(map)
            MethodType.METHOD_TYPE_SINGLE_CIRCLES-> return api.getSingleFriendCircles(map)
            MethodType.METHOD_TYPE_FRIENDS_CIRCLES-> return api.getFriendCircles(map)
            else -> return api.getAllCircles(map)
        }
    }
}
