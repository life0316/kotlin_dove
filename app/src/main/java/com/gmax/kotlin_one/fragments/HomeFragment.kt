package com.gmax.kotlin_one.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.gmax.kotlin_one.Constant
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseBindingFragment
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.bean.UserInnerData
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.FragmentHomeBinding
import com.gmax.kotlin_one.getApiCompoent
import com.gmax.kotlin_one.inject.DaggerLoginComponent
import com.gmax.kotlin_one.inject.LoginModule
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.LoginContract
import com.gmax.kotlin_one.mvp.LoginPresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.SpUtils
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.HashMap
import javax.inject.Inject

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(),LoginContract.View,CodesContract.View {


    companion object{
        fun newInstance(content: String): HomeFragment {
            val args = Bundle()
            args.putString("ARGS", content)
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
    @Inject lateinit var mPresenter: LoginPresenter
    var isLoad:Boolean = true
    var userInfo:UserInfoInner?=null

    override fun onResume() {
        super.onResume()

        if (isLoad) {
            mPresenter.getData(getParaMap(),MethodType.METHOD_TYPE_USER_DETAIL,2)
            isLoad = false
        }
    }

    override fun initView() {
        DaggerLoginComponent.builder()
                .apiCompoent(activity.getApiCompoent())
                .apiModule(ApiModule(activity))
                .loginModule(LoginModule(this,this)).build().inject(this)

        home_civ.setOnClickListener {
//            val intent = Intent(activity, PersonalActivity::class.java)
//            intent.putExtra(Constant.USER_INFO, userInfo)
//            startActivityForResult(intent,100)

        }
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            if (data!!.getBooleanExtra(Constant.CHANGE_USER,false)) {
                data.getParcelableExtra<UserInfoInner>(Constant.USER_INFO)
            }
        }
    }

    override fun getMethod(): String {
        return MethodConstant.USER_INFO_DETAIL
    }

    fun getParaMap(): Map<String, String> {

        val map = HashMap<String, String>()
        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())

        map.put(MethodParams.PARAMS_TOKEN, getToken())
        map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
        return map
    }

    override fun setUserData(data: UserInfoInner) {
        SpUtils.putString(activity, Constant.USER_PHONE,data.telephone)
        SpUtils.putString(activity, Constant.NICK_NAME,data.nickname)
        SpUtils.putString(activity, Constant.USER_DOVECOTE,data.loftname)
        SpUtils.putString(activity, Constant.USER_CODE,data.userid)
        SpUtils.putString(activity, Constant.USER_HEADPIC,data.headpic)
        SpUtils.putString(activity, Constant.USER_AGE,data.age.toString())
        SpUtils.putString(activity, Constant.USER_BIRTH,data.user_birth)
        SpUtils.putString(activity, Constant.USER_SEX,data.gender)
        SpUtils.putString(activity, Constant.USER_YEAR,data.experience)

        hideProgress()
        home_username.text = data.nickname
        home_userid.text = "用户编号:${data.userid}"

        if (data.headpic.startsWith("http")) {
            Glide.with(context)
                    .load(data.headpic)
                    .asBitmap()
                    .error(R.mipmap.btn_img_photo_default)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                            home_civ.setImageBitmap(resource)
                        }
                    })
        }
    }

    override fun setData(data: UserInnerData) {

    }
    override fun successToDo() {

    }
}