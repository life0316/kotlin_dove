package com.gmax.kotlin_one.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Administrator on 2017\8\24 0024.
 */
data class CircleBean(
        val type:Int,
        var comment_count:Int,
        var fab_count:Int,
        var share_count:Int,
        val content:String,
        val playerid:String,
        val username:String,
        val create_time:String,
        val userid:String,
        val circleid:String,
        val headpic:String,
        val trans_username:String,
        val trans_userid:String,
        var is_friend:Boolean,
        val transfer:Boolean,
        var has_fab:Boolean,
        val pics:List<String>
) :Parcelable{

    constructor(source:Parcel):this(
            source.readInt(),
            source.readInt(),
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
            source.readString(),
            source.readByte() == 0.toByte(),
            source.readByte() == 0.toByte(),
            source.readByte() == 0.toByte(),
            source.createStringArrayList()
    )

    companion object{
        @JvmField val CREATOR: Parcelable.Creator<CircleBean> = object : Parcelable.Creator<CircleBean>{
            override fun newArray(size: Int): Array<CircleBean?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): CircleBean {
                return CircleBean(source)
            }
        }
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {

        dest!!.writeInt(this.type)
        dest.writeInt(this.comment_count)
        dest.writeInt(this.fab_count)
        dest.writeInt(this.share_count)

        dest.writeString(this.content)
        dest.writeString(this.playerid)
        dest.writeString(this.username)
        dest.writeString(this.create_time)
        dest.writeString(this.userid)
        dest.writeString(this.circleid)
        dest.writeString(this.headpic)
        dest.writeString(this.trans_username)
        dest.writeString(this.trans_userid)

        dest.writeByte(if (this.is_friend) 0 else 1)
        dest.writeByte(if (this.transfer) 0 else 1)
        dest.writeByte(if (this.has_fab) 0 else 1)
        dest.writeStringList(this.pics)
    }

    override fun describeContents(): Int {
        return 0
    }
}