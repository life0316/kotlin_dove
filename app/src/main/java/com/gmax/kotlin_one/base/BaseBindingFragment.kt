package com.gmax.kotlin_one.base

import android.app.Dialog
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmax.kotlin_one.Constant
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.showToast
import com.gmax.kotlin_one.tsToString
import com.gmax.kotlin_one.utils.MD5Tools
import com.gmax.kotlin_one.utils.SpUtils
import java.sql.Timestamp

/**
 * Created by Administrator on 2017\8\1 0001.
 */
abstract class BaseBindingFragment<VDB : ViewDataBinding> : Fragment(),BaseView{

    lateinit var mBinding : VDB
    lateinit var mDialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDialog()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = createDataBinding(inflater,container,savedInstanceState)

        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    fun setDialog(){
        mDialog = Dialog(activity, R.style.AppTheme)
        mDialog.setCancelable(false)

        var view = activity.layoutInflater.inflate(R.layout.progressdialog,null)
        mDialog.setContentView(view, LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT))
    }


    override fun showProgress() {

        mDialog.show()
    }

    override fun hideProgress() {
        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
    }
    override fun showErrorMsg(errorMsg: String) {

        activity.showToast(errorMsg)
    }
    override fun getTime(): String {
        var time:Long = Timestamp(System.currentTimeMillis()).tsToString()
        return time.toString()
    }
    override fun getSign(): String {
        var sign:String = ""
        if (getMethod() != null) {
            sign = MD5Tools.MD5(getMethod() + getTime() + getVersion() + Constant.APP_SECRET)
        }
        return sign
    }

    override fun getVersion(): String {
        return "1.0"
    }

    fun getUserObjId(): String {

        return SpUtils.getString(activity,Constant.USER_OBJ_ID)
    }

    fun getToken(): String {
        return SpUtils.getString(activity,Constant.USER_TOKEN)
    }

    override fun getMethod(): String? {
        return ""
    }

    abstract fun initView()

    abstract fun  createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): VDB
}