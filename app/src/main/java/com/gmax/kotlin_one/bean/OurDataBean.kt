package com.gmax.kotlin_one.bean

/**
 * Created by Administrator on 2017\8\1 0001.
 */
data class OurDataBean<T>(val code : Int,val msg:String,val data:T) {
}