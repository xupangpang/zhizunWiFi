package com.zhizun.zhizunwifi.utils;

/**
 * @author xupp
 * @date 2017/5/9
 */

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.zhizun.zhizunwifi.bean.FileInfo;
import com.zhizun.zhizunwifi.widget.DownloadProgressButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载文件线程
 * 从服务器获取需要下载的文件大小
 */
public class DownLoadTask extends Thread {
    private FileInfo info;
    private SQLiteDatabase db;
    private DowloadDbHelper helper;//数据库帮助类
    private int finished = 0;//当前已下载完成的进度
    private OnProgressListener listener;//进度回调监听
    private int pos;

    public DownLoadTask(FileInfo info, DowloadDbHelper helper, OnProgressListener listener,int pos) {
        this.info = info;
        this.helper = helper;
        this.db = helper.getReadableDatabase();
        this.listener = listener;
        this.pos = pos;
        info.setDownLoading(true);
    }

    @Override
    public void run() {
        getLength();
        HttpURLConnection connection = null;
        RandomAccessFile rwd = null;
        try {
            URL url = new URL(info.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            //从上次下载完成的地方下载
            int start = info.getFinished();
            //设置下载位置(从服务器上取要下载文件的某一段)
            connection.setRequestProperty("Range", "bytes=" + start + "-" + info.getLength());//设置下载范围
            //设置文件写入位置
            File file = new File(DownLoaderManger.FILE_PATH, info.getFileName());
            rwd = new RandomAccessFile(file, "rwd");
            //从文件的某一位置开始写入
            rwd.seek(start);
            finished += info.getFinished();
            if (connection.getResponseCode() == 206) {//文件部分下载，返回码为206
                InputStream is = connection.getInputStream();
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    //写入文件
                    rwd.write(buffer, 0, len);
                    finished += len;
                    info.setFinished(finished);
                    //更新界面显示
                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.arg1 = info.getLength();
                    msg.arg2 = info.getFinished();
                    handler.sendMessage(msg);
                    //停止下载
                    if (info.isStop()) {
                        info.setDownLoading(false);
                        //保存此次下载的进度
                        helper.updateData(db, info);
                        return;
                    }
                }
                //下载完成
                info.setDownLoading(false);
                helper.updateData(db, info);
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (rwd != null) {
                    rwd.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 首先开启一个线程去获取要下载文件的大小（长度）
     */
    private void getLength() {
        HttpURLConnection connection = null;
        try {
            //连接网络
            URL url = new URL(info.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            int length = -1;
            if (connection.getResponseCode() == 200) {//网络连接成功
                //获得文件长度
                length = connection.getContentLength();
            }
            if (length <= 0) {
                //连接失败
                return;
            }
            //创建文件保存路径
            File dir = new File(DownLoaderManger.FILE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            info.setLength(length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            try {
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新进度
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    if (listener != null) {
                        listener.updateProgress(msg.arg1, msg.arg2,pos);
                    }
                    break;
            }
        }
    };
}
