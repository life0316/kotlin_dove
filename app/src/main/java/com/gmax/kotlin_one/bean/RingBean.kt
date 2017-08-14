package com.gmax.kotlin_one.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Administrator on 2017\8\1 0001.
 */
data class RingBean(
        val lfq:Int,
        val rfq:Int,
        val on_off_status:String,
        val off_time:String,
        val on_time:String,
        val ringid:String,
        val ring_code:String,
        val dove_code:String,
        val create_time:String,
        val doveid:String,
        val playerid:String
) : OpenBean(),Parcelable{

    constructor(source:Parcel):this(
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    companion object{
        @JvmField val CREATOR:Parcelable.Creator<RingBean> = object :Parcelable.Creator<RingBean>{
            override fun newArray(size: Int): Array<RingBean?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): RingBean {
                return RingBean(source)
            }
        }
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(this.lfq)
        dest.writeInt(this.rfq)
        dest.writeString(this.on_off_status)
        dest.writeString(this.off_time)
        dest.writeString(this.on_time)
        dest.writeString(this.ringid)
        dest.writeString(this.ring_code)
        dest.writeString(this.dove_code)
        dest.writeString(this.create_time)
        dest.writeString(this.doveid)
        dest.writeString(this.playerid)
    }

    override fun describeContents(): Int {
        return 0
    }
}