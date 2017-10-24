package com.gmax.kotlin_one.retrofit


import com.gmax.kotlin_one.bean.*

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap
import retrofit2.http.Streaming
import retrofit2.http.Url
import rx.Observable
import java.util.HashMap

interface IOurNewService {

    @Streaming
    @GET
    fun downloadImage(@Url fileUrl: String): Observable<ResponseBody>

    //3.1用户申请验证码
    @POST("gehuan/")
    @FormUrlEncoded
    fun getRequestVerCode(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.2验证码验证
    @POST("gehuan/")
    @FormUrlEncoded
    fun getValidVerCode(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.3用户注册
    @POST("gehuan/")
    @FormUrlEncoded
    fun getRegister(@FieldMap map: Map<String, String>): Observable<OurDataBean<UserInnerData>>

    //3.4用户登录
    @POST("gehuan/")
    @FormUrlEncoded
    fun getOurLogin(@FieldMap map: Map<String, String>): Observable<OurDataBean<UserInnerData>>
    //    Observable<OurUser> getOurLogin(@PartMap Map<String,RequestBody> map);

    //3.5.用户退出登录
    @POST("gehuan/")
    @FormUrlEncoded
    fun exitApp(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.5.忘记密码
    @POST("gehuan/")
    @FormUrlEncoded
    fun getResetPwd(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.6获取用户信息
    @POST("gehuan/")
    @FormUrlEncoded
    fun getDetailInfo(@FieldMap map: Map<String, String>): Observable<OurDataBean<UserInfoInner>>

    //3.7上传图片
    @Multipart
    @POST("gehuan/")
    fun getUploadPic(@PartMap params: HashMap<String, RequestBody>): Observable<UploadImageBean>

    //3.8.修改用户信息
    @POST("gehuan/")
    @FormUrlEncoded
    fun getUpdateInfo(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.9.用户添加鸽子
    @POST("gehuan/")
    @FormUrlEncoded
    fun addDove(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.10用户修改鸽子信息
    @POST("gehuan/")
    @FormUrlEncoded
    fun updateDove(@FieldMap map: Map<String, String>): Observable<OurCode>


    //3.11用户删除鸽子
    @POST("gehuan/")
    @FormUrlEncoded
    fun deleteDove(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.12用户读取鸽子列表
    @POST("gehuan/")
    @FormUrlEncoded
    fun searchDove(@FieldMap map: Map<String, String>): Observable<OurListBean<InnerDoveData>>


    //3.13读取鸽环默认配置
    @POST("gehuan/")
    @FormUrlEncoded
    fun getRingConfig(@FieldMap map: Map<String, String>): Observable<OurDataBean<RingConfigBean>>

    //3.14 鸽子添加鸽环
    @POST("gehuan/")
    @FormUrlEncoded
    fun addRing(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.15.读取鸽环列表
    @POST("gehuan/")
    @FormUrlEncoded
    fun searchRing(@FieldMap map: Map<String, String>): Observable<OurListBean<RingBean>>

    //3.16 用户修改鸽环信息
    @POST("gehuan/")
    @FormUrlEncoded
    fun updateRing(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.17 用户删除鸽环
    @POST("gehuan/")
    @FormUrlEncoded
    fun deleteRing(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.18 匹配鸽环和鸽子
    @POST("gehuan/")
    @FormUrlEncoded
    fun matchRing(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.19 鸽环和鸽子解除匹配
    @POST("gehuan/")
    @FormUrlEncoded
    fun dematchRing(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.20 设置鸽子开始飞行
    //    @POST("gehuan/")
    //    @FormUrlEncoded
    //    Observable<StartFlyBean> startFly(@FieldMap Map<String, String> map);

    //3.21 设置鸽子结束飞行
    @POST("gehuan/")
    @FormUrlEncoded
    fun stopFly(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.22 获取信鸽当前位置信息
    @POST("gehuan/")
    @FormUrlEncoded
    fun curlocFly(@FieldMap map: Map<String, String>): Observable<*>

    //3.23 获取信鸽飞行记录
    //   根据单只信鸽 id 获取飞行记录
    //    @POST("gehuan/")
    //    @FormUrlEncoded
    //    Observable<OurRouteBean> searchFly(@FieldMap Map<String, String> map);

    //3.24 删除信鸽飞行记录
    @POST("gehuan/")
    @FormUrlEncoded
    fun deleteFly(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.25 获取最新版本信息
    //    @POST("gehuan/")
    //    @FormUrlEncoded
    //    Observable<OurVerBean> getCurrentVersion(@FieldMap Map<String, String> map);

    //3.26 关注好友
    @POST("gehuan/")
    @FormUrlEncoded
    fun attentionFriend(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.27 取消关注好友
    @POST("gehuan/")
    @FormUrlEncoded
    fun removeAttention(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.28 获取关注好友列表
    //    @POST("gehuan/")
    //    @FormUrlEncoded
    //    Observable<AttentionBean> searchAttentionFriend(@FieldMap Map<String, String> map);

    //3.29 发朋友圈消息
    @POST("gehuan/")
    @FormUrlEncoded
    fun addCircle(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.29 转发朋友圈消息
    @POST("gehuan/")
    @FormUrlEncoded
    fun shareCircle(@FieldMap map: Map<String, String>): Observable<OurCode>

//    3.30 获取指定好友的朋友圈消息
    @POST("gehuan/")
    @FormUrlEncoded
   fun getSingleFriendCircles(@FieldMap map:Map<String, String>): Observable<OurListBean<CircleBean>>

//    3.31 获取好友的朋友圈消息
    @POST("gehuan/")
    @FormUrlEncoded
    fun getFriendCircles(@FieldMap map:Map<String, String>) : Observable<OurListBean<CircleBean>>

    //    3.31 获取所有朋友圈消息
    @POST("gehuan/")
    @FormUrlEncoded
    fun getAllCircles(@FieldMap map:Map<String, String>) : Observable<OurListBean<CircleBean>>

    // 3.32.删除指定一条朋友圈消息
    @POST("gehuan/")
    @FormUrlEncoded
    fun deleteSingle(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.33.删除自己朋友圈所有消息
    @POST("gehuan/")
    @FormUrlEncoded
    fun deleteAll(@FieldMap map: Map<String, String>): Observable<OurCode>


    //3.34.帮助信息
    @GET("gehuan/app/html/app_helper")
    fun appHelper(): Observable<*>

    //3.35.App简介
    @GET("gehuan/app/html/app_intro")
    fun appIntro(): Observable<*>

    //3.36.用户协议
    @GET("gehuan/app/html/app_agreement")
    fun appAgreement(): Observable<*>

    //3.35 朋友圈添加评论
    @POST("gehuan/")
    @FormUrlEncoded
    fun addComment(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.35 回复朋友圈评论
    @POST("gehuan/")
    @FormUrlEncoded
    fun addReply(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.36 获取朋友圈评论
    //    @POST("gehuan/")
    //    @FormUrlEncoded
    //    Observable<OurCommentBean> getComment(@FieldMap Map<String, String> map);

    //3.37 删除朋友圈评论或回复
    @POST("gehuan/")
    @FormUrlEncoded
    fun removeComment(@FieldMap map: Map<String, String>): Observable<OurCode>

    //3.38 朋友圈点赞
    @POST("gehuan/")
    @FormUrlEncoded
    fun addFab(@FieldMap map: Map<String, String>): Observable<OurCode>


    //3.39 获取朋友圈点赞列表
    //    @POST("gehuan/")
    //    @FormUrlEncoded
    //    Observable<OurFabBean> getFab(@FieldMap Map<String, String> map);


    //3.40 取消朋友圈点赞
    @POST("gehuan/")
    @FormUrlEncoded
    fun removeFab(@FieldMap map: Map<String, String>): Observable<OurCode>


    //3.40 获取玩家信息
    //    @POST("gehuan/")
    //    @FormUrlEncoded
    //    Observable<PlayerBean> getPlayerInfo(@FieldMap Map<String, String> map);


//    3.40 获取单条朋友圈详情
        @POST("gehuan/")
        @FormUrlEncoded
       fun getCircleDetail(@FieldMap  map:Map<String, String>): Observable<OurDataBean<CircleBean>>

}
