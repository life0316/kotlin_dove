package com.gmax.kotlin_one.base

/**
 * Created by Administrator on 2017\8\1 0001.
 */
abstract interface BaseView {

    //显示进度条
    abstract fun showProgress()

    //隐藏进度条或对话框
    abstract fun hideProgress()

    //显示错误信息
    abstract fun showErrorMsg(errorMsg: String)

    abstract fun getMethod(): String?
    abstract fun getTime(): String?
    abstract fun getSign(): String
    abstract fun getVersion(): String
}