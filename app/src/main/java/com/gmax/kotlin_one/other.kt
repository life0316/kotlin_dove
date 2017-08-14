package com.gmax.kotlin_one

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import java.sql.Timestamp
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

private var lastClickTime: Long = 0

fun isFastDoubleClick(): Boolean {
    val time = System.currentTimeMillis()
    if (time - lastClickTime < 1000) {
        return true
    }
    lastClickTime = time
    return false
}

fun Context.showToast(msg:String,duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,msg,duration).show()
}
fun Context.getApiCompoent() = App.instance.apiCompoent
fun Context.isNetworkConnected(): Boolean {
    val manager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = manager.activeNetworkInfo
    return networkInfo.isAvailable
}

fun checkHanZi(needCheckStr: String): Boolean {
    try {
        val regex = "^[a-zA-Z0-9\u4E00-\u9FA5]+$"
        val pattern = Pattern.compile(regex)
        val match = pattern.matcher(needCheckStr)
        return match.matches()
    } catch (e: Exception) {
        return false
    }
}

fun isPhoneNumberValid(phoneNumber: String): Boolean {
    var isValid = false

    val expression = "((^(13|14|15|17|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))"

    val inputStr = phoneNumber
    val pattern = Pattern.compile(expression)
    val matcher = pattern.matcher(inputStr)
    if (matcher.matches()) {
        isValid = true
    }
    return isValid
}

fun Timestamp.tsToString():Long{

    val dateFormat:DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
    val str:String = dateFormat.format(this)
    val dFormat:DateFormat =  SimpleDateFormat("yyyyMMddhhmmss")
    return dFormat.parse(str).time
}

internal fun getMonth(startTime: String, endTime: String): Int {

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    try {
        var startDate = sdf.parse(startTime)
        var endDate = sdf.parse(endTime)
        if (startDate.after(endDate)) {
            val t = startDate
            startDate = endDate
            endDate = t
        }
        val startCalender = Calendar.getInstance()
        startCalender.time = startDate
        val endCalender = Calendar.getInstance()
        endCalender.time = endDate

        val temp = Calendar.getInstance()
        temp.time = endDate
        temp.add(Calendar.DATE, 1)

        val year = endCalender.get(Calendar.YEAR) - startCalender.get(Calendar.YEAR)
        val month = endCalender.get(Calendar.MONTH) - startCalender.get(Calendar.MONTH)

        if (startCalender.get(Calendar.DATE) == Calendar.SUNDAY && temp.get(Calendar.DATE) == Calendar.SUNDAY) {

            return year * 12 + month + 1
        } else if (startCalender.get(Calendar.DATE) != Calendar.SUNDAY && temp.get(Calendar.DATE) == Calendar.SUNDAY) {
            return year * 12 + month
        } else if (startCalender.get(Calendar.DATE) == Calendar.SUNDAY && temp.get(Calendar.DATE) != Calendar.SUNDAY) {
            return year * 12 + month
        } else {
            return if (year * 12 + month - 1 < 0) 0 else year * 12 + month
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0
}