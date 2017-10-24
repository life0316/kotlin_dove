package com.gmax.kotlin_one.mvp

import android.util.Log
import com.gmax.kotlin_one.base.BasePresenter
import com.gmax.kotlin_one.bean.CircleBean
import com.gmax.kotlin_one.bean.OurDataBean
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.retrofit.MethodType
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class EachCirclePresenter
@Inject constructor(private val mModel:EachCircleModel
                    , private val mView: EachCircleContract.View) : EachCircleContract.Presenter, BasePresenter(){
    override fun  getData(map: Map<String, String>, type: Int) {
        addSubscription(mModel.getData(map,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    (code, msg, data) ->
                    Log.e("data",(code).toString() + "--------code")
                    Log.e("data",(msg) + "--------msg")
                    if (code == 200){
                        mView.setEachData(data)
                    }else{
                        //  TODO 最好是给个错误提示
                        mView.hideProgress()
                    }
                }))
    }
}

class EachCircleModel
@Inject constructor(private val api: IOurNewService) : EachCircleContract.Model {
    override fun getData(map: Map<String, String>, type: Int): Observable<OurDataBean<CircleBean>> {
        when (type) {
            MethodType.METHOD_TYPE_CIRCLE_DETAIL -> return api.getCircleDetail(map)
            else -> return api.getCircleDetail(map)
        }
    }
}
