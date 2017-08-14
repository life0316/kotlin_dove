package com.gmax.kotlin_one.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Administrator on 2017\8\1 0001.
 */
data class InnerDoveData(
        val age:Int,
        val ancestry:String,
        val color:String,
        val create_time:String,
        var gender : String,
        val playerid:String,
        val doveid:String,
        val eye:String,
        val foot_ring:String,
        val ringid:String,
        val ring_code:String,
        val fly_recordid:String,
        val fly_status:Boolean,
        val isSetMate:Boolean
        ):OpenBean(), Parcelable{

        constructor(source:Parcel):this(source.readInt(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readByte() == 0.toByte(),
                source.readByte() == 0.toByte()
        )

        companion object{
                @JvmField val CREATOR:Parcelable.Creator<InnerDoveData> = object :Parcelable.Creator<InnerDoveData>{
                        override fun newArray(size: Int): Array<InnerDoveData?> {
                                return arrayOfNulls(size)
                        }

                        override fun createFromParcel(source: Parcel): InnerDoveData {
                                return InnerDoveData(source)
                        }
                }
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
                dest!!.writeInt(this.age)
                dest.writeString(this.ancestry)
                dest.writeString(this.color)
                dest.writeString(this.create_time)
                dest.writeString(this.gender)
                dest.writeString(this.playerid)
                dest.writeString(this.doveid)
                dest.writeString(this.eye)
                dest.writeString(this.foot_ring)
                dest.writeString(this.ringid)
                dest.writeString(this.ring_code)
                dest.writeString(this.fly_recordid)
                dest.writeByte(if (this.fly_status) 0 else 1)
                dest.writeByte(if (this.isSetMate) 0 else 1)
        }

        override fun describeContents(): Int {
                return 0
        }

}