package com.gmax.kotlin_one.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class InnerCircleBean implements Parcelable {

    private String content;

    private List<String> pics;
    private String playerid;

    private String username;


    private String create_time;
    private String userid;
    private int type;

    private String circleid;

    private String type_name;
    private String headpic;

    private int comment_count;
    private int fab_count;
    private int share_count;


    private boolean is_friend = true;
    private boolean transfer;  //是否是转发其他信鸽玩家的朋友圈
    private boolean has_fab;   // 是否已经点过赞的标识，true代表该朋友圈已经被当前userid点赞

    private String trans_username;   //被转发者 昵称
    private String trans_userid; //被转发者 id

    public String getTrans_name() {
        return trans_username;
    }

    public void setTrans_name(String trans_name) {
        this.trans_username = trans_name;
    }

    public String getTrans_userid() {
        return trans_userid;
    }

    public void setTrans_userid(String trans_userid) {
        this.trans_userid = trans_userid;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public boolean isHas_fab() {
        return has_fab;
    }

    public void setHas_fab(boolean has_fab) {
        this.has_fab = has_fab;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getFab_count() {
        return fab_count;
    }

    public void setFab_count(int fab_count) {
        this.fab_count = fab_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public boolean isIs_friend() {
        return is_friend;
    }

    public void setIs_friend(boolean is_friend) {
        this.is_friend = is_friend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCircleid() {
        return circleid;
    }

    public void setCircleid(String circleid) {
        this.circleid = circleid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeByte((byte)(is_friend ? 1 : 0));
        dest.writeByte((byte)(transfer ? 1 : 0));
        dest.writeByte((byte)(has_fab ? 1 : 0));

        dest.writeInt(type);
        dest.writeInt(share_count);
        dest.writeInt(comment_count);
        dest.writeInt(fab_count);

        dest.writeString(content);
        dest.writeString(username);
        dest.writeString(create_time);
        dest.writeString(userid);
        dest.writeString(circleid);
        dest.writeString(type_name);
        dest.writeString(headpic);
        dest.writeString(playerid);
        dest.writeString(trans_username);
        dest.writeString(trans_userid);
        dest.writeStringList(pics);

    }
    public boolean getIs_friend() {
        return this.is_friend;
    }

    public boolean getTransfer() {
        return this.transfer;
    }

    public boolean getHas_fab() {
        return this.has_fab;
    }

    public String getTrans_username() {
        return this.trans_username;
    }

    public void setTrans_username(String trans_username) {
        this.trans_username = trans_username;
    }

    public static final Creator<InnerCircleBean> CREATOR = new Creator<InnerCircleBean>() {
        @Override
        public InnerCircleBean createFromParcel(Parcel source) {

            InnerCircleBean innerCircleBean = new InnerCircleBean();

            innerCircleBean.is_friend = source.readByte() != 0;
            innerCircleBean.transfer = source.readByte() != 0;
            innerCircleBean.has_fab = source.readByte() != 0;

            innerCircleBean.type = source.readInt();
            innerCircleBean.share_count = source.readInt();
            innerCircleBean.comment_count = source.readInt();
            innerCircleBean.fab_count = source.readInt();

            innerCircleBean.content = source.readString();
            innerCircleBean.username = source.readString();
            innerCircleBean.create_time = source.readString();
            innerCircleBean.userid = source.readString();
            innerCircleBean.circleid = source.readString();
            innerCircleBean.type_name = source.readString();
            innerCircleBean.headpic = source.readString();
            innerCircleBean.playerid = source.readString();
            innerCircleBean.trans_username = source.readString();
            innerCircleBean.trans_userid = source.readString();
            innerCircleBean.pics = source.createStringArrayList();

            return innerCircleBean;
        }

        @Override
        public InnerCircleBean[] newArray(int size) {
            return new InnerCircleBean[size];
        }
    };
}
