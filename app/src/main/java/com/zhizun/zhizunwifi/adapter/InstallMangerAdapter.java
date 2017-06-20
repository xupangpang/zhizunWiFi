package com.zhizun.zhizunwifi.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.FileInfo;
import com.zhizun.zhizunwifi.bean.InstallManger;
import com.zhizun.zhizunwifi.utils.DowloadDbHelper;
import com.zhizun.zhizunwifi.utils.DownLoaderManger;
import com.zhizun.zhizunwifi.utils.OnProgressListener;
import com.zhizun.zhizunwifi.widget.DownloadProgressButton;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xupp
 * @date 2017/5/9
 */

public class InstallMangerAdapter extends RecyclerView.Adapter<InstallMangerAdapter.ViewHolder> implements OnProgressListener {
    private List<InstallManger>  mData;
    private Context mContext;
    private DownLoaderManger downLoader = null;
    private DowloadDbHelper helper;
    private List<FileInfo> infos = new ArrayList();

    public InstallMangerAdapter(Context context) {
        this.mContext = context;
        helper = new DowloadDbHelper(mContext);
        downLoader = DownLoaderManger.getInstance(helper,this);
    }

    public void  setList(List<InstallManger> list){
        this.mData = list;
        for (InstallManger installManger : mData) {
            FileInfo info = new FileInfo();
            info.setFileName(installManger.getAPPname()+".apk");
            info.setUrl(installManger.getAPPurl());
            infos.add(info);
            downLoader.addTask(info);
        }
    }
    public DownLoaderManger getDownLoader(){
        return downLoader;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.install_manage_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getAPPicon()).into(holder.app_icon);
        holder.app_name.setText(mData.get(position).getAPPname());
        holder.app_size.setText(mData.get(position).getAPPsize());

        if (mData.get(position).getAPPstag() == 0){
            holder.download_btn.setState(DownloadProgressButton.STATE_NORMAL);
            holder.download_btn.setCurrentText("下载");
        }else if (mData.get(position).getAPPstag() == 1){
            holder.download_btn.setState(DownloadProgressButton.STATE_NORMAL);
            holder.download_btn.setCurrentText("安装");
        }else if (mData.get(position).getAPPstag() == 2){
            holder.download_btn.setState(DownloadProgressButton.STATE_DOWNLOADING);
            holder.download_btn.setCurrentText("暂停");
        }else if (mData.get(position).getAPPstag() == 3){
            holder.download_btn.setState(DownloadProgressButton.STATE_PAUSE);
            holder.download_btn.setCurrentText("继续");
        }

        holder.download_btn.setMaxProgress(mData.get(position).getMaxprogress());
        holder.download_btn.setProgress(mData.get(position).getProgress());
        if (mData.get(position).getProgress() > 0){
            holder.download_btn.postInvalidate();
        }
        holder.download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getLayoutPosition();
                downLoader.setPos(pos);
                if (mData.get(pos).getAPPstag() == 0  || mData.get(pos).getAPPstag() == 3){
                    downLoader.start(infos.get(pos).getUrl());
                    mData.get(pos).setAPPstag(2);
                }else if (mData.get(pos).getAPPstag() == 2){
                    downLoader.stop(infos.get(pos).getUrl());
                    mData.get(pos).setAPPstag(3);
                }else if (mData.get(pos).getAPPstag() == 1){
                    File file = new File(DownLoaderManger.FILE_PATH, infos.get(pos).getFileName());
                    if (file.exists()) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");
                        mContext.startActivity(intent);
                    }
                }
                notifyItemChanged(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public void updateProgress(int max, int progress,int pos) {
        mData.get(pos).setMaxprogress(max);
        mData.get(pos).setProgress(progress);
        float speedK = progress / 1024;
        float speedmax = max / 1024;
        DecimalFormat decimalFormat =new DecimalFormat("0.00");
        String speedValue = speedK < 1024 ? decimalFormat.format(speedK) +" KB" : decimalFormat.format((speedK / 1024)) +" MB";
        String speedValuemax = speedmax < 1024 ? decimalFormat.format(speedmax) +" KB" : decimalFormat.format((speedmax / 1024)) +" MB";
        mData.get(pos).setAPPsize(speedValue+"/" + speedValuemax);

        if (progress == max){
            mData.get(pos).setAPPstag(1);
            File file = new File(DownLoaderManger.FILE_PATH, infos.get(pos).getFileName());
            if (file.exists()) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                mContext.startActivity(intent);
            }
        }
        notifyItemChanged(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView app_icon;
        TextView app_name;
        TextView app_size;
        DownloadProgressButton download_btn;
        public ViewHolder(View itemView) {
            super(itemView);
            app_icon = (ImageView)itemView.findViewById(R.id.app_icon);
            app_name = (TextView) itemView.findViewById(R.id.app_name);
            app_size = (TextView)itemView.findViewById(R.id.app_size);
            download_btn = (DownloadProgressButton)itemView.findViewById(R.id.download_btn);
        }
    }
}
