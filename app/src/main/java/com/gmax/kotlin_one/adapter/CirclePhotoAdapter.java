package com.gmax.kotlin_one.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gmax.kotlin_one.common.CommentListener;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

public class CirclePhotoAdapter extends RecyclerView.Adapter<CirclePhotoAdapter.PhotoViewHolder> {

  private ArrayList<String> photoPaths = new ArrayList<>();
  private LayoutInflater inflater;


  private CommentListener.MyItemClickListener myItemClickListener;

  public void setMyItemClickListener(CommentListener.MyItemClickListener myItemClickListener) {
    this.myItemClickListener = myItemClickListener;
  }


  private Context mContext;

//  public final static int TYPE_ADD = 1;
  private final static int TYPE_PHOTO = 2;

//  public final static int MAX = 9;
  private final static int MAX = 3;

  public CirclePhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
    this.photoPaths = photoPaths;
    this.mContext = mContext;
    inflater = LayoutInflater.from(mContext);

  }

  public void addPhotos(ArrayList<String> photoPaths){
    this.photoPaths = photoPaths;
  }


  @Override public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = null;
    switch (viewType) {
      case TYPE_PHOTO:
        itemView = inflater.inflate(me.iwf.photopicker.R.layout.__picker_item_photo, parent, false);
        break;
    }
    return new PhotoViewHolder(itemView,myItemClickListener);
  }


  @Override
  public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

    if (getItemViewType(position) == TYPE_PHOTO) {

      String path = photoPaths.get(position);
      final Uri uri;

      if (path.startsWith("http")) {
        uri = Uri.parse(path);
      } else {
        uri = Uri.fromFile(new File(path));
      }

      //Uri uri = Uri.fromFile(new File(photoPaths.get(position)));

//      holder.ivPhoto.setTag(uri.toString());
      Log.e("fadfebdqqqqqq",uri+"-------1");


      boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

      if (canLoadImage) {

        Glide.with(mContext)
                .load(uri)
                .asBitmap()
                .placeholder(com.gmax.kotlin_one.R.mipmap.default_pic)
                .error(com.gmax.kotlin_one.R.mipmap.default_pic)
                .into(holder.ivPhoto);
//                .into(new SimpleTarget<Bitmap>() {
//                  @Override
//                  public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    if (uri.toString().equals(holder.ivPhoto.getTag())){
//                      holder.ivPhoto.setImageBitmap(resource);
//                    }
//                  }
//                });
      }
    }
  }


  @Override public int getItemCount() {
    int count = photoPaths.size();
    if (count > MAX) {
      count = MAX;
    }
    return count;
  }

  @Override
  public int getItemViewType(int position) {
//    return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    return TYPE_PHOTO;
  }

  public static class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CommentListener.MyItemClickListener mItemClickListener;
    private ImageView ivPhoto;
    private View vSelected;
    private PhotoViewHolder(View itemView, CommentListener.MyItemClickListener myItemClickListener) {
      super(itemView);
      ivPhoto   = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
      vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);
      if (vSelected != null) vSelected.setVisibility(View.GONE);
      this.mItemClickListener = myItemClickListener;
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (mItemClickListener != null){
        //noinspection deprecation
        mItemClickListener.onItemClick(v,getAdapterPosition());
      }
    }
  }
}
