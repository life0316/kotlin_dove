package com.gmax.kotlin_one.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.gmax.kotlin_one.Constant
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseUserinfoF
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.FragmentHomeBinding
import com.gmax.kotlin_one.getApiCompoent
import com.gmax.kotlin_one.inject.DaggerLoginComponent
import com.gmax.kotlin_one.inject.LoginModule
import com.gmax.kotlin_one.modules.home.PersonalActivity
import com.gmax.kotlin_one.mvp.LoginPresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.SpUtils
import kotlinx.android.synthetic.main.activity_personal.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayInputStream
import java.util.HashMap
import javax.inject.Inject

class HomeFragment : BaseUserinfoF<FragmentHomeBinding>(){

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

        Log.e("dfaf","home-onResume")

        val currentNum = SpUtils.getInt(activity,Constant.CLICK_NUM)
        if (isLoad and (currentNum == 3)) {
            mPresenter.getData(getParaMap(),MethodType.METHOD_TYPE_USER_DETAIL,2)
            isLoad = false
        }else{
            if (SpUtils.getBoolean(activity,Constant.CHANGE_USER)){
                changInfo()
            }
        }
        SpUtils.putBoolean(activity,Constant.CHANGE_USER,false)
    }

    override fun initView() {
        DaggerLoginComponent.builder()
                .apiCompoent(activity.getApiCompoent())
                .apiModule(ApiModule(activity))
                .loginModule(LoginModule(this,this)).build().inject(this)
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
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

    @SuppressLint("SetTextI18n")
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
        home_userid.text = getString(R.string.user_code_home)+data.userid
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
        if(data.nickname == null) data.nickname = ""
        if(data.gender == null) data.gender = "1"
        if(data.experience == null) data.experience = "1"
        if(data.loftname == null) data.loftname = ""

        userInfo = UserInfoInner(data.age,data.userid,data.nickname,data.headpic,data.gender,data.experience,data.loftname,data.telephone,"2017-08-14")

        home_civ.setOnClickListener {
            if (userInfo != null) {
                val intent = Intent(activity, PersonalActivity::class.java)
                intent.putExtra(Constant.USER_INFO, userInfo)
                startActivity(intent)
            }
        }
    }

    fun changInfo(){
        userInfo!!.nickname = SpUtils.getString(activity, Constant.NICK_NAME)
        userInfo!!.loftname = SpUtils.getString(activity, Constant.USER_DOVECOTE)
        val userHeadpic = SpUtils.getString(activity, Constant.USER_HEADPIC)
        SpUtils.getString(activity, Constant.USER_AGE)
        SpUtils.getString(activity, Constant.USER_BIRTH)
        userInfo!!.gender = SpUtils.getString(activity, Constant.USER_SEX)
        userInfo!!.experience = SpUtils.getString(activity, Constant.USER_YEAR)

        if ("" != userHeadpic) {

            if (userHeadpic.startsWith("http")) {
                Glide.with(this)
                        .load(userHeadpic)
                        .asBitmap()
                        .placeholder(R.mipmap.btn_img_photo_default)
                        .error(R.mipmap.btn_img_photo_default)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                home_civ.setImageBitmap(resource)
                            }
                        })
            } else {
                if (userHeadpic != "") {
                    val byteArray = Base64.decode(userHeadpic, Base64.DEFAULT)
                    val byteArrayInputStream = ByteArrayInputStream(byteArray)
                    //第三步:利用ByteArrayInputStream生成Bitmap
                    val bitmap = BitmapFactory.decodeStream(byteArrayInputStream)
                    home_civ.setImageBitmap(bitmap)
                }
            }
        }
    }
}