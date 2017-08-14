package com.gmax.kotlin_one.mvp

import android.util.Log
import com.gmax.kotlin_one.base.BasePresenter
import com.gmax.kotlin_one.bean.*
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.retrofit.MethodType
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class DovePresenter
@Inject constructor(private val mModel:DoveModel
                    , private val mView: DoveContract.View) : DoveContract.Presenter, BasePresenter(){
    override fun getData(map: Map<String, String>, type: Int) {
        addSubscription(mModel.getData(map,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    (code, msg, data) ->
                    Log.e("data",(code).toString() + "--------code")
                    Log.e("data",(msg) + "--------msg")
                    if (code == 200){
                        mView.setData(data)
                        Log.e("data",(data.toString()) + "--------msg")
                    }else{
                        //  TODO 最好是给个错误提示
                        mView.hideProgress()
                    }
                }))
    }
}

class DoveModel
@Inject   constructor(private val api: IOurNewService) : DoveContract.Model {
    override fun getData(map: Map<String, String>, type: Int): Observable<OurListBean<InnerDoveData>> {
        when (type) {
            MethodType.METHOD_TYPE_DOVE_SEARCH, MethodType.METHOD_TYPE_REGISTER -> return api.searchDove(map)
            else -> return api.searchDove(map)
        }
    }
}
