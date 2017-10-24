package com.gmax.kotlin_one.mvp

import android.util.Log
import com.gmax.kotlin_one.base.BasePresenter
import com.gmax.kotlin_one.bean.OurDataBean
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.bean.UserInnerData
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.retrofit.MethodType
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter
@Inject constructor(private val mModel:LoginModel
                        , private val mView: LoginContract.View) : LoginContract.Presenter, BasePresenter(){
    override fun getData(map: Map<String, String>, type: Int,preType:Int) {
        when(preType){
            1 -> {
                addSubscription(mModel.getData(map,type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            res ->

                            Log.e("fafaf",res.toString() + "--------login---1")
                            if (res.code == 200){
                                mView.setData(res.data)
                            }else{
                                //  TODO 最好是给个错误提示
                                //82:EF:89:3D:AB:B9:4E:CC:B8:73:86:FD:2E:35:12:7A:AD:85:3A:CA
                                //55:FE:90:9C:FB:CF:C0:5C:8A:DD:33:AA:13:4E:01:F8:2F:C8:C5:BF
                            }
                        },
                        {
                            error ->
                            mView.hideProgress()
                            Log.e("fafaf",error.message + "--------login---1")
                        }))
            }
             2 -> {
                 addSubscription(mModel.getInfoData(map,type)
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe({
                             res ->
                             Log.e("fafaf",res.toString() + "--------login---2")
                             if (res.code == 200){
                                 mView.setUserData(res.data)
                             }else{
                                 //  TODO 最好是给个错误提示
                             }
                         },
                                 {
                                    error ->
                                    mView.hideProgress()
                                     Log.e("fafaf",error.message + "--------login---1")
                                 }
                         ))
             }
        }
    }
}
class LoginModel
@Inject   constructor(private val api: IOurNewService) : LoginContract.Model
{
    override fun getData(map: Map<String, String>, type: Int): Observable<OurDataBean<UserInnerData>> {
        when(type){
            MethodType.METHOD_TYPE_LOGIN -> return api.getOurLogin(map)
            MethodType.METHOD_TYPE_REGISTER -> return api.getRegister(map)
            else -> return api.getOurLogin(map)
        }
    }

    override fun getInfoData(map: Map<String, String>, type: Int): Observable<OurDataBean<UserInfoInner>> {
        when(type){
            MethodType.METHOD_TYPE_USER_DETAIL,MethodType.METHOD_TYPE_REGISTER -> return api.getDetailInfo(map)
            else -> return api.getDetailInfo(map)
        }
    }
}


