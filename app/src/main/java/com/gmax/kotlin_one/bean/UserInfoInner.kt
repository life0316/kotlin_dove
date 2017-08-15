package com.gmax.kotlin_one.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Administrator on 2017\8\1 0001.
 */
data class UserInfoInner(
        var age:Int = 0,
        val userid:String,
        var nickname:String = "",
        val headpic:String,
        var gender:String = "1",
        var experience:String = "1",
        var loftname:String = "",
        val telephone:String,
        var user_birth:String="2017-08-14") : Parcelable {

    constructor(source:Parcel):this(source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString())

    companion object{
        @JvmField val CREATOR:Parcelable.Creator<UserInfoInner> = object : Parcelable.Creator<UserInfoInner>{
            override fun newArray(size: Int): Array<UserInfoInner?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): UserInfoInner {
                return UserInfoInner(source)
            }
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest!!.writeInt(this.age)
            dest.writeString(this.userid)
            dest.writeString(this.nickname)
            dest.writeString(this.headpic)
            dest.writeString(this.gender)
            dest.writeString(this.experience)
            dest.writeString(this.loftname)
            dest.writeString(this.telephone)
            dest.writeString(this.user_birth)
    }

    override fun describeContents(): Int {
        return 0
    }
}