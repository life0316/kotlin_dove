package com.gmax.kotlin_one.modules

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gmax.kotlin_one.*
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.base.BaseLoginView
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.bean.UserInnerData
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.ActivityRegistBinding
import com.gmax.kotlin_one.inject.DaggerLoginComponent
import com.gmax.kotlin_one.inject.LoginModule
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.CodesPresenter
import com.gmax.kotlin_one.mvp.LoginContract
import com.gmax.kotlin_one.mvp.LoginPresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.MD5Tools
import com.gmax.kotlin_one.utils.SpUtils
import kotlinx.android.synthetic.main.activity_regist.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.personal_birth_dialog.view.*
import kotlinx.android.synthetic.main.personal_sex_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegistActivity : BaseLoginView<ActivityRegistBinding>(){

    internal var mHandler : Handler? = null
    var methodType:Int = MethodType.METHOD_TYPE_REGISTER
    @Inject lateinit var mCodePresenter: CodesPresenter
    @Inject lateinit var mLoginPresenter: LoginPresenter
//    var isChange = false
    private var tag = true
    private var i = 60
    internal var mThread: Thread? = null

    companion object{
        val SEX_TYPE_NAN:String = "1"
        val SEX_TYPE_NV:String = "2"
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityRegistBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_regist)
    }

    override fun initView() {
        mHandler = Handler()


        DaggerLoginComponent.builder()
                .apiCompoent(getApiCompoent())
                .apiModule(ApiModule(this))
                .loginModule(LoginModule(this,this)).build().inject(this)

        tl_custom.setNavigationIcon(R.mipmap.back_press)
        custom_toolbar_tv.text = getString(R.string.app_user_regist)
        setupToolbar(tl_custom)

        @SuppressLint("SimpleDateFormat")
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val strDate = format.format(date)
        regist_tv_date.text = strDate

        regist_sendcode.setOnClickListener{
            if (!isValidate()) return@setOnClickListener

            regist_sendcode.text = getString(R.string.get_code)
            regist_sendcode.isClickable = true
            changeBtnGetCode()
            getValidateCode()

        }

        regist_confirm.setOnClickListener {
            if (!isNetworkConnected()) {
                showToast(getString(R.string.net_conn_2))
                return@setOnClickListener
            }
            if (isValid()) {
                if (!regist_cb.isChecked) {
                    showToast(getString(R.string.disagree_user_protocol))
                    return@setOnClickListener
                }
            }
            methodType = MethodType.METHOD_TYPE_REGISTER
            mLoginPresenter.getData(getParaMap(),methodType,1)
        }
        regist_tv_date.setOnClickListener {
            setUserAge()
        }
        regist_tv_sex.setOnClickListener {
            setUserSex()
        }
    }
    fun setUserSex(){
        val mDialog = Dialog(this, R.style.DialogTheme)
        val nullVg:ViewGroup? = null
        val view = layoutInflater.inflate(R.layout.personal_sex_dialog, nullVg)
        view.dialog_sex_male.text = getString(R.string.user_sex_nan)
        view.dialog_sex_female.text = getString(R.string.user_sex_nv)

        view.dialog_sex_ll_male.setOnClickListener {
            regist_tv_sex.text = view.dialog_sex_male.text
            mDialog.dismiss()
        }
        view.dialog_sex_ll_female.setOnClickListener {
            regist_tv_sex.text = view.dialog_sex_female.text
            mDialog.dismiss()
        }
        mDialog.setContentView(view, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setDialogWindow(mDialog)
        mDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun setUserAge(){
        val mDialog = Dialog(this, R.style.DialogTheme)
        val nullVg:ViewGroup? = null
        val view =layoutInflater.inflate(R.layout.personal_birth_dialog, nullVg)

        val birthTime = regist_tv_date.text.toString().trim()
        val str = birthTime.split("-".toRegex()).toTypedArray()
        val timeYear = str[0]
        val timeMonth = str[1]
        val timeDay = str[2]
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        view.birth_dialog_np_year.maxValue = year
        view.birth_dialog_np_year.minValue = year - 100
        view.birth_dialog_np_year.value = timeYear.toInt()

        view.birth_dialog_np_month.maxValue = month
        view.birth_dialog_np_month.minValue = 1
        view.birth_dialog_np_month.value = timeMonth.toInt()

        when(timeMonth.toInt()){
            1,3,5,7,8,10,12 ->  {
                view.birth_dialog_np_day.maxValue = 31
            }
            4,6,9,11 ->  {
                view.birth_dialog_np_day.maxValue = 30
            }
            2 -> {
                val curYear = view.birth_dialog_np_year.value
                if (timeMonth.toInt() == 2 && (curYear%4==0&&curYear%100!=0)&&curYear%400==0){
                    view.birth_dialog_np_day.maxValue = 29
                }else{
                    view.birth_dialog_np_day.maxValue = 28
                }
            }
        }

        if (timeMonth.toInt() == day){
            view.birth_dialog_np_day.maxValue = day
        }
        view.birth_dialog_np_day.minValue = 1
        view.birth_dialog_np_day.value = timeDay.toInt()

        view.birth_dialog_np_year.setOnValueChangedListener { _, _, newVal ->

            if (newVal == year){
                view.birth_dialog_np_month.maxValue = month
            }else{
                view.birth_dialog_np_month.maxValue = 12
            }
            view.birth_dialog_np_month.minValue = 1
            view.birth_dialog_np_month.value = 1
        }
        view.birth_dialog_np_month.setOnValueChangedListener { _, _, newVal ->

            if (newVal == month){
                when(month){
                    1,3,5,7,8,10,12 ->  {
                        view.birth_dialog_np_day.maxValue = 31
                    }
                    4,6,9,11 ->  {
                        view.birth_dialog_np_day.maxValue = 30
                    }
                    2 -> {
                        val curYear = view.birth_dialog_np_year.value
                        if (newVal == 2 && (curYear%4==0&&curYear%100!=0||curYear%400==0)){
                            view.birth_dialog_np_day.maxValue = 29
                        }else{
                            view.birth_dialog_np_day.maxValue = 28
                        }
                    }
                }
                view.birth_dialog_np_day.minValue = 1
                view.birth_dialog_np_day.value = timeDay.toInt()
            }else{
                when(newVal){
                    1,3,5,7,8,10,12 ->  {
                        view.birth_dialog_np_day.maxValue = 31
                    }
                    4,6,9,11 ->  {
                        view.birth_dialog_np_day.maxValue = 30
                    }
                    2 -> {
                        val curYear = view.birth_dialog_np_year.value
                        if (newVal == 2 && (curYear%4==0&&curYear%100!=0)&&curYear%400==0){
                            view.birth_dialog_np_day.maxValue = 29
                        }else{
                            view.birth_dialog_np_day.maxValue = 28
                        }
                    }
                }
                view.birth_dialog_np_day.minValue = 1
                view.birth_dialog_np_day.value = 1
            }
        }

        view.birth_dialog_cancle.setOnClickListener{ mDialog.dismiss() }
        view.birth_dialog_confirm.setOnClickListener{

            val curYear = view.birth_dialog_np_year.value
            val curMonth = view.birth_dialog_np_month.value
            val curDay= view.birth_dialog_np_day.value

            var tempMonth = curMonth.toString()
            var tempDay = curDay.toString()
            if (curMonth < 10){
                tempMonth = "0"+ curMonth.toString()
            }
            if (curDay < 10){
                tempDay = "0"+ curDay.toString()
            }

            regist_tv_date.text = curYear.toString()+"-"+tempMonth+"-"+tempDay
            mDialog.dismiss()
        }
        mDialog.setContentView(view, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setDialogWindow(mDialog)
        mDialog.show()

    }
    private fun isValid(): Boolean {

        if ("" == getUserPhone()) {
            showToast(getString(R.string.user_phone))
            return false
        }
        if (!isPhoneNumberValid(getUserPhone())) {
            showToast(getString(R.string.user_phone_valid))
            return false
        }
        if ("" == getNickname()) {
            showToast(getString(R.string.user_name))
            return false
        }

        if ("" == getVerCode()) {
            showToast(getString(R.string.get_send_code))
            return false
        }

        if ("" == getUserPwd()) {
            showToast(getString(R.string.user_pwd))
            return false
        } else if (getUserPwd().length < 6) {
            showToast(getString(R.string.user_pwd_size))
            return false
        }

        if ("" == getUserRepwd()) {
            showToast(getString(R.string.user_pwd_re))
            return false
        } else if (getUserPwd() != getUserRepwd()) {
            showToast(getString(R.string.user_pwd_repwd))
            return false
        }

        if ("" == getUserSex()) {
            showToast(getString(R.string.user_sex_select))
            return false
        }
        return true
    }

    private fun changeBtnGetCode() {
        mThread = Thread{
                if (tag) {
                    while (i > 0) {
                        i--
                        this@RegistActivity.runOnUiThread {
                            regist_sendcode.text = "重新获取($i)"
                            regist_sendcode.isClickable = false
                        }
                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                    tag = false
                }
                i = 60
                tag = true
            this@RegistActivity.runOnUiThread {
                regist_sendcode.text = resources.getString(R.string.get_code)
                regist_sendcode.isClickable = true
            }
        }
        mThread?.start()
    }

    /**
     * 说明：获取验证码
     */
    private fun getValidateCode(): Boolean {

        if (getUserPhone() == "") {
            showToast(getString(R.string.user_phone))
            return false
        } else {
            methodType = MethodType.METHOD_TYPE_REQUEST_VER_CODE
            mCodePresenter.getData(getParaMap(),MethodType.METHOD_TYPE_REQUEST_VER_CODE)
        }
        return true
    }

    override fun successToDo() {

    }

    override fun setData(data: UserInnerData) {
        SpUtils.putString(this,Constant.USER_TOKEN,data.token)
        SpUtils.putString(this, Constant.USER_OBJ_ID,data.userid)

        val intent: Intent = Intent(this@RegistActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getMethod(): String {
        var method:String = ""
        when (methodType) {
            MethodType.METHOD_TYPE_REQUEST_VER_CODE -> method = MethodConstant.REQUEST_VER_CODE
            MethodType.METHOD_TYPE_REGISTER -> method = MethodConstant.REGISTER
            MethodType.METHOD_TYPE_USER_DETAIL -> method = MethodConstant.USER_INFO_DETAIL
        }
        return method
    }

    fun getParaMap(): Map<String, String> {

        val map = HashMap<String, String>()

        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())

        when (methodType) {
            MethodType.METHOD_TYPE_REGISTER -> {
                map.put(MethodParams.PARAMS_GENDER, getUserSex())
                map.put(MethodParams.PARAMS_NICKNAME, getNickname())
                map.put(MethodParams.PARAMS_TELEPHONE, getUserPhone())
                map.put(MethodParams.PARAMS_BIRTHDAY, getUserBirth())
                map.put(MethodParams.PARAMS_PASSWORD, MD5Tools.MD5(getUserPwd()))
                map.put(MethodParams.PARAMS_VER_CODE, getVerCode())
            }
            MethodType.METHOD_TYPE_USER_DETAIL -> {
                map.put(MethodParams.PARAMS_TOKEN, getToken())
                map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
            }
            MethodType.METHOD_TYPE_REQUEST_VER_CODE -> map.put(MethodParams.PARAMS_TELEPHONE, getUserPhone())
        }

        Log.e("fafaf",map.toString() + "--------map")
        return map
    }

    fun getUserPhone(): String {

        var mUserPhone = regist_phone.text.toString().trim()

        mUserPhone = isEmptyStr(mUserPhone, resources.getString(R.string.user_phone))

        if (!isPhoneNumberValid(mUserPhone)) {
            showToast(resources.getString(R.string.user_phone_valid))
            return ""
        }
        return mUserPhone
    }

    fun getNickname(): String {
        var mUserMail = regist_nickname.text.toString().trim()
        mUserMail = isEmptyStr(mUserMail, resources.getString(R.string.user_name))
        return mUserMail
    }

    fun getUserPwd(): String {
        var mUserPwd = regist_password.text.toString().trim()
        mUserPwd = isEmptyStr(mUserPwd, resources.getString(R.string.user_pwd))
        return mUserPwd
    }
    fun getUserRepwd(): String {
        val mUserRepwd = regist_repassword.text.toString().trim()
        return mUserRepwd
    }

    fun getVerCode():String = regist_vercode.text.toString().trim()

    fun getUserSex() : String {
        var mUserSex = regist_tv_sex.text.toString().trim()
        when (mUserSex) {
            getString(R.string.user_sex_nan) -> mUserSex = SEX_TYPE_NAN
            getString(R.string.user_sex_nv) -> mUserSex = SEX_TYPE_NV
        }

        return mUserSex
    }
    fun getUserBirth() : String = regist_tv_date.text.toString().trim()
    fun isEmptyStr(checkStr: String?, showMsg: String): String {
        if ("" == checkStr || checkStr == null || checkStr.isEmpty()) {
            showToast(showMsg)
            return ""
        }
        return checkStr
    }

    private fun isValidate(): Boolean {

        val mUserPhone = regist_phone.text.toString().trim()

        if (!isNetworkConnected()) {
            showToast(getString(R.string.net_conn_2))
            return false
        }

        //获取手机号
        if (mUserPhone.isEmpty()) {
            showToast( getString(R.string.user_phone))
            return false
        }
        if (!isPhoneNumberValid(mUserPhone)) {
            showToast(resources.getString(R.string.user_phone_valid))
            return false
        }
        return true
    }
}