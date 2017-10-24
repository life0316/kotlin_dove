package com.gmax.kotlin_one.modules.circle

import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.*
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gmax.kotlin_one.*
import com.gmax.kotlin_one.adapter.CircleAdapter
import com.gmax.kotlin_one.adapter.CirclePhotoAdapter
import com.gmax.kotlin_one.base.BaseBindingFragment
import com.gmax.kotlin_one.bean.CircleBean
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.common.CommentListener
import com.gmax.kotlin_one.common.DataLoadType
import com.gmax.kotlin_one.common.OnHolderListener
import com.gmax.kotlin_one.databinding.FragmentMySmartBinding
import com.gmax.kotlin_one.inject.CircleModule
import com.gmax.kotlin_one.inject.DaggerCircleComponent
import com.gmax.kotlin_one.mvp.*
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.RxBus
import com.gmax.kotlin_one.widget.MyPhotoPreview
import kotlinx.android.synthetic.main.fragment_my_smart.*
import kotlinx.android.synthetic.main.item_friend_circle.view.*
import kotlinx.android.synthetic.main.personal_down_dialog.view.*
import kotlinx.android.synthetic.main.tran_fl.view.*
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class AllCircleFragment : BaseBindingFragment<FragmentMySmartBinding>(), CircleContract.View,CodesContract.View, OnHolderListener<CircleBean, CircleAdapter<CircleBean>.MyRefrashHolder>, EachCircleContract.View {


    @Inject lateinit var mCirclePresenter: CirclePresenter
    @Inject lateinit var mCodePresenter: CodesPresenter
    @Inject lateinit var mEachPresenter: EachCirclePresenter
    @Inject lateinit var mRxBus : RxBus

    internal var methodType = MethodType.METHOD_TYPE_ALL_CIRCLES
    private var PAGENUM = 1  //查询起始下标，默认为0
    private val PAGESIZE = 10//每页返回的数据，默认10

    private var isFriend = false
    private var isDao = false
    private var isLoad = true
    private val tag = 0
    private var headpic: String? = null

    private var circleid: String? = null
    private var friendId: String? = null
    private var commentContent: String? = null
    private var circleBean: CircleBean? = null
    private var currentPosition: Int = 0//当前操作的 position
    private val circleBeans = ArrayList<CircleBean>()

    private val pageNumMap = HashMap<String, Int>()

    lateinit var circleAdapter:CircleAdapter<CircleBean>

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentMySmartBinding {
        return FragmentMySmartBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        DaggerCircleComponent.builder()
                .apiCompoent(activity.getApiCompoent())
                .apiModule(ApiModule(activity))
                .circleModule(CircleModule(this,this,this))
                .build().inject(this)

        bsr_rv.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        circleAdapter = CircleAdapter(activity,0)
        bsr_rv.adapter = circleAdapter
        circleAdapter.onHolderListener = this
        refreshLayout.setOnRefreshListener {
            PAGENUM = 1
            pageNumMap.put("all", 1)
            getDatas()
        }
        refreshLayout.setOnLoadmoreListener {
            updateRecyclerView()
        }
    }
    // 上拉加载时调用的更新RecyclerView的方法
    private fun updateRecyclerView() {
        if (circleBeans.size % 10 != 0) PAGENUM = circleBeans.size / 10 + 2 else PAGENUM = circleBeans.size / 10 + 1

        pageNumMap.put("all", PAGENUM)
        methodType = MethodType.METHOD_TYPE_ALL_CIRCLES
        mCirclePresenter.getData(getParaMap(), methodType,DataLoadType.TYPE_LOAD_MORE)
    }

    override fun onResume() {
        super.onResume()
        if (isLoad) {
            PAGENUM = 1
            pageNumMap.put("all", 1)
            getDatas()
        }
        isLoad = false
    }

    private fun getDatas(loadType: DataLoadType = DataLoadType.TYPE_REFRESH) {
        if (!activity.isNetworkConnected()) {
        } else {
            methodType = MethodType.METHOD_TYPE_ALL_CIRCLES
            mCirclePresenter.getData(getParaMap(),methodType,loadType)
        }
    }
    override fun setData(circleData: List<CircleBean>,loadType: DataLoadType) {
        if (refreshLayout.isRefreshing) {
            refreshLayout.finishRefresh(false)
        } else if (refreshLayout.isLoading) {
            refreshLayout.finishLoadmore(false)
        }

        val tempList:MutableList<CircleBean> = ArrayList()
        circleData.forEachIndexed { _,circleBean ->
            tempList.add(circleBean)
        }
        when(loadType){
            DataLoadType.TYPE_REFRESH -> {
                if (circleData.isNotEmpty()) {
                    PAGENUM += 1
                    pageNumMap.put("all", PAGENUM)
                    circleAdapter.updateList(tempList)
                    circleBeans.clear()
                    circleBeans.addAll(tempList)
                }
            }
            DataLoadType.TYPE_LOAD_MORE -> {
                if (circleData.isNotEmpty()) {
                    PAGENUM += 1
                    pageNumMap.put("all", PAGENUM)

//                    isLoadMore = true
//                    nativeAd.loadAd("429")
                    circleAdapter.addData(tempList)
                    circleBeans.addAll(tempList)
                }
            }
        }
    }
    override fun toInitHolder(holder:  CircleAdapter<CircleBean>.MyRefrashHolder, position: Int, data: CircleBean) {
//转发的

        if (data.trans_userid != null && "" != data.trans_userid && "-1" != data.trans_userid) {
            holder.itemView.item_tran_fl.visibility = View.VISIBLE
            holder.itemView.item_isfriend_rv.visibility = View.GONE
            if (!TextUtils.isEmpty(data.content) && "" != data.content) {
                val content = data.content
                val contents = content.split("#".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                holder.itemView.tran_circle_text.text = contents[contents.size - 1]

                Log.e("mTranContentTv", content + "--------" + data.username + "--------" + data.trans_username)

                if (content.lastIndexOf("#") != -1) {
                    holder.itemView.friend_text.text = content.substring(0, content.lastIndexOf("#"))
                } else {
                    holder.itemView.friend_text.text = "转发动态"
                }
            }

            if ("" != data.trans_username && data.trans_username != null) {
                holder.itemView.tran_circle_name.text = "@" + data.trans_username
            }

            if (data.pics != null && data.pics.isNotEmpty()) {

                val selectedPhotos = ArrayList<String>()
                selectedPhotos.clear()
                val picCount = data.pics.size
                selectedPhotos.addAll(data.pics)
                holder.itemView.tran_circle_rv.removeAllViews()
                val photoAdapter = CirclePhotoAdapter(context, selectedPhotos)
                holder.itemView.tran_circle_rv.visibility = View.VISIBLE
                holder.itemView.tran_circle_rv.adapter = photoAdapter
                holder.itemView.tran_circle_rv.layoutManager = StaggeredGridLayoutManager(if (picCount < 3) 2 else 3, OrientationHelper.VERTICAL)
                (holder.itemView.tran_circle_rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                photoAdapter.setMyItemClickListener(object : CommentListener.MyItemClickListener {
                    override
                    fun onItemClick(view: View, position: Int) {
                        isLoad = false
                        MyPhotoPreview.builder()
                                .setPhotos(selectedPhotos)
                                .setCurrentItem(position)
                                .setShowDeleteButton(false)
                                .start(activity)
                    }
                })
            } else {
                holder.itemView.tran_circle_rv.removeAllViews()
                holder.itemView.tran_circle_rv.visibility = View.GONE
            }

            holder.itemView.item_dynamic_add_friend.visibility = View.GONE
            holder.itemView.itme_dynamic_down.visibility = View.GONE

        } else {

            holder.itemView.item_tran_fl.visibility = View.GONE
            if (data.content != null && data.content.isNotEmpty() ) {
                holder.itemView.friend_text.text = data.content
            }
            if (data.pics != null && data.pics.isNotEmpty()) {

                val selectedPhotos = ArrayList<String>()
                selectedPhotos.clear()
                val picCount = data.pics.size
                selectedPhotos.addAll(data.pics)
                holder.itemView.item_isfriend_rv.removeAllViews()
                val photoAdapter = CirclePhotoAdapter(context, selectedPhotos)
                holder.itemView.item_isfriend_rv.visibility = View.VISIBLE
                holder.itemView.item_isfriend_rv.adapter = photoAdapter
                holder.itemView.item_isfriend_rv.layoutManager = StaggeredGridLayoutManager(if (picCount < 3) 2 else 3, OrientationHelper.VERTICAL)
                (holder.itemView.item_isfriend_rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                photoAdapter.setMyItemClickListener(object : CommentListener.MyItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        isLoad = false
                        MyPhotoPreview.builder()
                                .setPhotos(selectedPhotos)
                                .setCurrentItem(position)
                                .setShowDeleteButton(false)
                                .start(activity)
                    }
                })
            } else {
                holder.itemView.item_isfriend_rv.removeAllViews()
                holder.itemView.item_isfriend_rv.visibility = View.GONE
            }
        }

        if (data.is_friend || tag == 1 || tag == 2) {
            holder.itemView.item_dynamic_add_friend.visibility = View.GONE
            holder.itemView.itme_dynamic_down.visibility = View.VISIBLE
        } else {
            holder.itemView.item_dynamic_add_friend.visibility = View.VISIBLE
            holder.itemView.itme_dynamic_down.visibility = View.GONE
        }
        if (data.has_fab) {
            val likeLift = resources.getDrawable(R.mipmap.like)
            likeLift.setBounds(0, 0, likeLift.minimumWidth, likeLift.minimumHeight)
            holder.itemView.friend_dislike.setCompoundDrawables(likeLift, null, null, null)
        } else {
            val likeLift = resources.getDrawable(R.mipmap.dislike)
            likeLift.setBounds(0, 0, likeLift.minimumWidth, likeLift.minimumHeight)
            holder.itemView.friend_dislike.setCompoundDrawables(likeLift, null, null, null)
        }

        if (data.share_count != 0) {
            holder.itemView.friend_share.text = getString(R.string.share_circle) + data.share_count
        } else {
            holder.itemView.friend_share.text = getString(R.string.share_circle)
        }

        if (data.comment_count != 0) {
            holder.itemView.friend_comments.text = getString(R.string.comment_circle) + data.comment_count
        } else {
            holder.itemView.friend_comments.text = getString(R.string.comment_circle)
        }
        if (data.fab_count != 0) {
            holder.itemView.friend_dislike.text = getString(R.string.fab_circle) + data.fab_count
        } else {
            holder.itemView.friend_dislike.text = getString(R.string.fab_circle)
        }

        if (data.content == null) {
            holder.itemView.friend_text.visibility = View.GONE
        } else {
            holder.itemView.friend_text.visibility = View.VISIBLE
        }

        if ("" != data.username && data.username != null) {
            holder.itemView.friend_name.text = data.username
        }

        headpic = Constant.HEADPIC
        if (data.headpic != null && "" != data.headpic && "-1" != data.headpic) {
            headpic += data.headpic

            val tag = holder.itemView.de_icon.getTag(R.id.imageid)

            if (tag != null &&!TextUtils.equals(headpic, tag.toString())) {
                holder.itemView.de_icon.setImageResource(R.mipmap.btn_img_photo_default)
            }

            holder.itemView.de_icon.setTag(R.id.imageid, headpic)
            Glide.with(context)
                    .load(headpic)
                    .dontAnimate()
                    .placeholder(R.mipmap.btn_img_photo_default)
                    .error(R.mipmap.btn_img_photo_default)
                    .into(holder.itemView.de_icon)

        } else {
            holder.itemView.de_icon.setTag(R.id.imageid, "")
            holder.itemView.de_icon.setImageResource(R.mipmap.btn_img_photo_default)
        }

        holder.itemView.de_icon.setOnClickListener{
            currentPosition = position
            if (data.is_friend || data.userid.equals(getUserObjId())) {
//                val intent = Intent(activity, CircleDetialActivity::class.java)
//                intent.putExtra("friendid", data.userid)
//                intent.putExtra("name", data.username)
//                intent.putExtra("innerCircleBean", data)
//                intent.putExtra("current_position", data)
//                intent.putExtra("circle_tag", tag)
//                startActivity(intent)
            }
        }

        if (!TextUtils.isEmpty(data.create_time) && "" != data.create_time) {
            holder.itemView.friend_date.text = data.create_time
        }
        holder.itemView.itme_dynamic_down.setOnClickListener{
            currentPosition = position
            circleid = data.circleid
            friendId = data.userid
            isFriend = data.is_friend
            showDownDialog()
        }
        holder.itemView.item_dynamic_add_friend.setOnClickListener {
            currentPosition = position
            circleid = data.circleid
            methodType = MethodType.METHOD_TYPE_ADD_ATTENTION
            friendId = data.userid
            mCodePresenter.getData(getParaMap(),methodType)
        }
        holder.itemView.friend_dislike.setOnClickListener{
            currentPosition = position
            circleid = data.circleid
            friendId = data.userid
            isFriend = data.is_friend
            if (data.has_fab) {
                methodType = MethodType.METHOD_TYPE_REMOVE_FAB
                mCodePresenter.getData(getParaMap(),methodType)
            } else {
                methodType = MethodType.METHOD_TYPE_ADD_FAB
                mCodePresenter.getData(getParaMap(),methodType)
            }
        }

        holder.itemView.friend_comments.setOnClickListener{
            currentPosition = position
            circleid = data.circleid
            methodType = MethodType.METHOD_TYPE_ADD_COMMENT
            if (data.comment_count != 0) {
//                val intent = Intent(activity, EarchCircleActivity::class.java)
//                intent.putExtra("innerCircleBean", innerCircleBeans.get(currentPosition))
//                intent.putExtra("circle_tag", tag)
//                startActivity(intent)
            } else {
//                showMyDialog()
            }
        }

        holder.itemView.friend_share.setOnClickListener{
            currentPosition = position
            circleid = data.circleid
//            val intent = Intent(activity, TransCircleActivity::class.java)
//            intent.putExtra("innerCircleBean", data)
//            intent.putExtra("circle_tag", tag)
//            startActivity(intent)
        }
    }

    override fun successToDo() {
        when (methodType) {
            MethodType.METHOD_TYPE_ADD_FAB,
            MethodType.METHOD_TYPE_REMOVE_FAB,
            MethodType.METHOD_TYPE_ADD_COMMENT -> {
                circleAdapter.getDatas().removeAt(currentPosition)
                methodType = MethodType.METHOD_TYPE_CIRCLE_DETAIL
                mEachPresenter.getData(getParaMap(),methodType)
            }
            MethodType.METHOD_TYPE_ADD_ATTENTION,
            MethodType.METHOD_TYPE_REMOVE_ATTENTION -> {
                circleAdapter.getDatas().removeAt(currentPosition)
                methodType = MethodType.METHOD_TYPE_CIRCLE_DETAIL
                mEachPresenter.getData(getParaMap(),methodType)
                mRxBus.post("isLoadF", true)
            }
        }
    }

    override fun setEachData(circleData: CircleBean) {
        val circleBean:CircleBean = circleBeans[currentPosition]
        circleBean.comment_count = circleData.comment_count
        circleBean.fab_count = circleData.fab_count
        circleBean.share_count = circleData.share_count
        circleBean.has_fab = circleData.has_fab
        circleBean.is_friend = circleData.is_friend

        circleAdapter.getDatas().add(currentPosition, circleBean)
        circleAdapter.notifyDataSetChanged()
    }
    override fun getMethod(): String {
        var method = ""
        when (methodType) {
            MethodType.METHOD_TYPE_ALL_CIRCLES -> method = MethodConstant.GET_ALL_CIRCLES
            MethodType.METHOD_TYPE_REMOVE_ATTENTION ->
                //取消关注好友
                method = MethodConstant.ATTENTTION_REMOVE
            MethodType.METHOD_TYPE_ADD_ATTENTION ->
                //关注好友
                method = MethodConstant.ATTENTION_ADD
            MethodType.METHOD_TYPE_ADD_FAB -> method = MethodConstant.ADD_FAB
            MethodType.METHOD_TYPE_REMOVE_FAB -> method = MethodConstant.REMOVE_FAB
            MethodType.METHOD_TYPE_ADD_COMMENT -> method = MethodConstant.ADD_COMMENT
            MethodType.METHOD_TYPE_SHARE_CIRCLE -> method = MethodConstant.SHARE_CIRCLE
            MethodType.METHOD_TYPE_CIRCLE_DETAIL -> method = MethodConstant.GET_CIRCLE_DETAIL
        }
        return method
    }

    fun getParaMap(): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())
        map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
        map.put(MethodParams.PARAMS_TOKEN, getToken())

        when (methodType) {
            MethodType.METHOD_TYPE_ADD_ATTENTION,
            MethodType.METHOD_TYPE_REMOVE_ATTENTION -> map.put(MethodParams.PARAMS_FRIEND_ID, friendId!!)
            MethodType.METHOD_TYPE_ALL_CIRCLES -> {
                map.put(MethodParams.PARAMS_CP, PAGENUM.toString())
                map.put(MethodParams.PARAMS_PS, PAGESIZE.toString())
            }
            MethodType.METHOD_TYPE_SHARE_CIRCLE,
            MethodType.METHOD_TYPE_ADD_FAB,
            MethodType.METHOD_TYPE_REMOVE_FAB,
            MethodType.METHOD_TYPE_CIRCLE_DETAIL -> map.put(MethodParams.PARAMS_CIRCLE_ID, circleid!!)
            MethodType.METHOD_TYPE_ADD_COMMENT -> {
                map.put(MethodParams.PARAMS_CIRCLE_ID, circleid!!)
                map.put(MethodParams.PARAMS_CONTENT, commentContent!!)
            }
        }

        Log.e("faaafee", map.toString())

        return map
    }

    private fun showDownDialog() {
        val mDialog = Dialog(activity, R.style.DialogTheme)
        mDialog.setCancelable(false)
        val view = activity.layoutInflater.inflate(R.layout.personal_down_dialog, null)
        mDialog.setContentView(view, LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        view.dialog_delete.visibility = View.GONE
        view.dialog_shou.visibility = View.GONE
        if (getUserObjId() == friendId) {
            view.remove_attention.visibility = View.GONE
            view.dialog_shou.visibility = View.GONE
        } else {
            view.remove_attention.visibility = View.VISIBLE
            view.dialog_shou.visibility = View.VISIBLE
        }
        if (isFriend) {
            view.remove_attention.text = "取消关注"
        } else {
            view.remove_attention.text = "添加关注"
        }
        view.dialog_cancle.setOnClickListener { mDialog.dismiss() }
        view.remove_attention.setOnClickListener {
            methodType = MethodType.METHOD_TYPE_REMOVE_ATTENTION
            mCodePresenter.getData(getParaMap(),methodType)
            mDialog.dismiss()
        }
        view.dialog_shou.setOnClickListener { mDialog.dismiss() }
        view.dialog_delete.setOnClickListener {
            methodType = MethodType.METHOD_TYPE_DELETE_SINGEL
            mCodePresenter.getData(getParaMap(),methodType)
            mDialog.dismiss()
        }
        setDialogWindow(mDialog)
        mDialog.show()
    }
}