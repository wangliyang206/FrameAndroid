package com.cj.mobile.common.ui.multiimageselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cj.mobile.common.R;
import com.cj.mobile.common.ui.multiimageselector.bean.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹Adapter
 * Created by wly on 2015/4/7.
 * Updated by wly on 2016/1/19.
 */

public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Folder> mFolders = new ArrayList<>();

    int mImageSize;

    int lastSelected = 0;

    public FolderAdapter(Context context){
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.mis_folder_cover_size);
    }

    /**
     * 设置数据集
     * @param folders
     */
    public void setData(List<Folder> folders) {
        if(folders != null && folders.size()>0){
            mFolders = folders;
        }else{
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size()+1;
    }

    @Override
    public Folder getItem(int i) {
        if(i == 0) return null;
        return mFolders.get(i-1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = mInflater.inflate(R.layout.mis_list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if(i == 0){
                holder.name.setText(R.string.mis_folder_all);
                holder.path.setText("/sdcard");
                holder.size.setText(String.format("%d%s",
                        getTotalImageSize(), mContext.getResources().getString(R.string.mis_photo_unit)));
                if(mFolders.size()>0){
                    Folder f = mFolders.get(0);
                    if (f != null) {
                        Glide.with(mContext)
                                .load(new File(f.cover.path))
                                .asBitmap()                                  //静态图，asGif()为动态图
                                .error(R.drawable.mis_default_error)            //请求图片错误后显示的图
                                .centerCrop()
                                .override(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)            //设置图片大小
                                .into(holder.cover);
                    }else{
                        holder.cover.setImageResource(R.drawable.mis_default_error);
                    }
                }
            }else {
                holder.bindData(getItem(i));
            }
            if(lastSelected == i){
                holder.indicator.setVisibility(View.VISIBLE);
            }else{
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize(){
        int result = 0;
        if(mFolders != null && mFolders.size()>0){
            for (Folder f: mFolders){
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if(lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex(){
        return lastSelected;
    }

    class ViewHolder{
        ImageView cover;
        TextView name;
        TextView path;
        TextView size;
        ImageView indicator;
        ViewHolder(View view){
            cover = (ImageView)view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            path = (TextView) view.findViewById(R.id.path);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            if(data == null){
                return;
            }
            name.setText(data.name);
            path.setText(data.path);
            if (data.images != null) {
                size.setText(String.format("%d%s", data.images.size(), mContext.getResources().getString(R.string.mis_photo_unit)));
            }else{
                size.setText("*"+mContext.getResources().getString(R.string.mis_photo_unit));
            }
            if (data.cover != null) {
                // 显示图片
                Glide.with(mContext)
                        .load(new File(data.cover.path))
                        .asBitmap()                                  //静态图，asGif()为动态图
                        .placeholder(R.drawable.mis_default_error)
                        .error(R.drawable.mis_default_error)            //请求图片错误后显示的图
                        .centerCrop()
                        .override(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)            //设置图片大小
                        .into(cover);
            }else{
                cover.setImageResource(R.drawable.mis_default_error);
            }
        }
    }

}