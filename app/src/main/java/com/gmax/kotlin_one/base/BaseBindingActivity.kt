package com.gmax.kotlin_one.base

import android.app.Dialog
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import com.gmax.kotlin_one.*
import com.gmax.kotlin_one.utils.MD5Tools
import com.gmax.kotlin_one.utils.SpUtils
import java.sql.Timestamp


/**
 * Created by Administrator on 2017\8\1 0001.
 */
abstract class BaseBindingActivity<VDB : ViewDataBinding> : AppCompatActivity(),BaseView{

    lateinit var mBinding:VDB

    var mDialog:Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = createDataBinding(savedInstanceState)

        initView()
        setDialog()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun setDialog(){
        mDialog = Dialog(this, R.style.DialogTheme)
        mDialog?.setCancelable(false)

        var view = layoutInflater.inflate(R.layout.progressdialog,null)
        mDialog?.setContentView(view, LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    fun setupToolbar(toolbar : Toolbar){
        toolbar.title = ""
//        toolbar.setNavigationIcon()
        setSupportActionBar(toolbar)
    }

    abstract fun initView()

    abstract fun createDataBinding(savedInstanceState: Bundle?): VDB

    override fun showProgress() {

        mDialog?.show()
        setDialogWindowc(mDialog!!)
    }

    override fun hideProgress() {
        if (mDialog?.isShowing!!) {
            mDialog?.dismiss()
        }
    }

    override fun showErrorMsg(errorMsg: String) {

        showToast(errorMsg)
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

    fun getToken():String{
        return SpUtils.getString(this,Constant.USER_TOKEN)
    }

    fun getUserObjId():String{
        return SpUtils.getString(this,Constant.USER_OBJ_ID)
    }
}