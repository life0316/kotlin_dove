package com.gmax.kotlin_one.modules

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import com.gmax.kotlin_one.Constant
import com.gmax.kotlin_one.MainActivity
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.base.BaseLoginView
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.bean.UserInnerData
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.ActivityLoginBinding
import com.gmax.kotlin_one.getApiCompoent
import com.gmax.kotlin_one.inject.DaggerLoginComponent
import com.gmax.kotlin_one.inject.LoginModule
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.LoginContract
import com.gmax.kotlin_one.mvp.LoginPresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.MD5Tools
import com.gmax.kotlin_one.utils.SpUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.backgroundResource
import java.util.HashMap
import javax.inject.Inject

class LoginActivity : BaseLoginView<ActivityLoginBinding>(){

    var methodType:Int = MethodType.METHOD_TYPE_LOGIN
    internal var editPwd: String? = null

    internal var mHandler : Handler? = null
    @Inject lateinit var mPresenter: LoginPresenter

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityLoginBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun initView() {
        mHandler = Handler()
        DaggerLoginComponent.builder()
            .apiCompoent(getApiCompoent())
                .apiModule(ApiModule(this))
            .loginModule(LoginModule(this,this)).build().inject(this)

        val userName : String = SpUtils.getString(this,Constant.USER_PHONE)
        val userPwd :String = SpUtils.getString(this,Constant.USER_PWD)
        val isRemCb : Boolean = SpUtils.getBoolean(this,Constant.REMEBER_CB)
        val isAutoCb : Boolean = SpUtils.getBoolean(this,Constant.AUTO_CB)

        login_remenber.isChecked = isRemCb
        login_auto.isChecked = isAutoCb

        login_username.setText(userName)
        login_username.setSelection(userName.length)

        login_password.setText(userPwd)
        login_password.setSelection(userPwd.length)

        login_remenber.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                login_auto.isChecked = false
            }
        }
        login_auto.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                login_remenber.isChecked = isChecked
            }
        }
        login_cb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                login_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }else{
                login_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        login_regist.setOnClickListener {
            val intent : Intent = Intent(this, RegistActivity::class.java)
            startActivity(intent) }
        loginbtn.setOnClickListener{
            val mUserName : String = login_username.text.toString().trim()
            val mPwd : String = login_password.text.toString().trim()

            if ("" == mUserName  && "" == mPwd ) {
            } else {
                SpUtils.putString(this,Constant.USER_PHONE, mUserName)
                SpUtils.putBoolean(this,Constant.REMEBER_CB, login_remenber.isChecked)
                SpUtils.putBoolean(this,Constant.AUTO_CB, login_auto.isChecked)
                if (login_remenber.isChecked) {
                    SpUtils.putString(this,Constant.USER_PWD, mPwd)
                } else {
                    SpUtils.putString(this,Constant.USER_PWD, "")
                }
                methodType =  MethodType.METHOD_TYPE_LOGIN
                mPresenter.getData(getParaMap(),MethodType.METHOD_TYPE_LOGIN,1)
                showProgress()
            }
        }

        val mUserName : String = login_username.text.toString().trim()
        val mPwd : String = login_password.text.toString().trim()

        if ("" == mUserName  && "" == mPwd ) {
            loginbtn.backgroundResource = R.drawable.btn_pigeon_bg2
            loginbtn.isEnabled = false
        } else {
            loginbtn.backgroundResource = R.drawable.btn_pigeon_bg
            loginbtn.isEnabled = true
        }

        login_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                mHandler!!.removeCallbacks(delayRun)
                editPwd = s.toString()
                mHandler!!.postDelayed(delayRun, 500)
            }
        })
    }

    private val delayRun = Runnable {
        if (editPwd!!.isEmpty() || "" == editPwd || editPwd == null) {
            loginbtn.backgroundResource = R.drawable.btn_pigeon_bg2
            loginbtn.isEnabled = false
        } else {
            loginbtn.backgroundResource = R.drawable.btn_pigeon_bg
            loginbtn.isEnabled = true
        }
    }

    override fun setData(data: UserInnerData) {
        SpUtils.putString(this,Constant.USER_TOKEN,data.token)
        SpUtils.putString(this, Constant.USER_OBJ_ID,data.userid)
        hideProgress()
        val intent:Intent = Intent(this@LoginActivity, MainActivity::class.java)
        SpUtils.putBoolean(this,Constant.MAIN_EXIT,true)
        startActivity(intent)
        finish()
    }

    override fun getMethod(): String {
        var method:String? = MethodConstant.LOGIN
        when(methodType){
            MethodType.METHOD_TYPE_LOGIN -> method = MethodConstant.LOGIN
            MethodType.METHOD_TYPE_USER_DETAIL -> method = MethodConstant.USER_INFO_DETAIL
        }
        return method!!
    }

    fun getParaMap(): Map<String, String> {

        val map = HashMap<String, String>()
        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())
        when (methodType) {
            MethodType.METHOD_TYPE_LOGIN -> {
                map.put(MethodParams.PARAMS_TELEPHONE, login_username.text.toString().trim())
                map.put(MethodParams.PARAMS_PASSWORD,MD5Tools.MD5(login_password.text.toString().trim()))
            }
            MethodType.METHOD_TYPE_USER_DETAIL -> {
                map.put(MethodParams.PARAMS_TOKEN, getToken())
                map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
            }
        }
        return map
    }
}