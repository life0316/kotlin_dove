package com.gmax.kotlin_one

import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.ViewGroup

fun AppCompatActivity.setupToolbar(toolbar : Toolbar){
    toolbar.title = ""
    this.setSupportActionBar(toolbar)
}

fun checkPermissions(activity: Activity, requestCode: Int, needPermissions: Array<String>): Boolean {
    val needRequestPermissonList = findDeniedPermissions(activity, needPermissions)
    if (needRequestPermissonList.size > 0) {
        ActivityCompat.requestPermissions(activity, needRequestPermissonList.toTypedArray(), requestCode)
        return true
    }
    return false
}

fun findDeniedPermissions(context: Activity, needPermissions:Array<String>): List<String> {
    val needRequestPermissonList = needPermissions.filter { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(context, it) }
    return needRequestPermissonList
}

internal fun verifyPermissions(grantResults: IntArray): Boolean {
    return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
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