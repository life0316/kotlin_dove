package com.gmax.kotlin_one.bean

/**
 * Created by Administrator on 2017\8\1 0001.
 */
data class RingConfigBean(
        val on_off_status:Int,
        val lfq:Int,
        val rfq:Int,
        val off_time:String,
        val on_time:String,
        val configid:String
) {
}