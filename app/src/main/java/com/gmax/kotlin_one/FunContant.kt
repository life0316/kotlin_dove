package com.gmax.kotlin_one

import android.app.Activity
import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.ViewGroup

fun AppCompatActivity.setupToolbar(toolbar : Toolbar){
    toolbar.title = ""
//        toolbar.setNavigationIcon()
//    toolbar.navigationIcon =
    this.setSupportActionBar(toolbar)
}

fun setDialogWindow(mDialog: Dialog,gravity:Int = Gravity.BOTTOM) {
    val window = mDialog.window
    val params = window!!.attributes
    params.gravity = gravity
    params.width = ViewGroup.LayoutParams.MATCH_PARENT
    window.attributes = params
}

fun setDialogWindowc(mDialog: Dialog) {
    val window = mDialog.window
    val params = window!!.attributes
    params.gravity = Gravity.CENTER
    window.attributes = params
}