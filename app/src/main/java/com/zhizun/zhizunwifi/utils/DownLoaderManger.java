package com.zhizun.zhizunwifi.utils;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.zhizun.zhizunwifi.bean.FileInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xupp
 * @date 2017/5/9
 */

public class DownLoaderManger {
    public static String FILE_PATH = Environment.getExternalStorageDirectory() + "/zhizunwifi";//文件下载保存路径
    private DowloadDbHelper helper;//数据库帮助类
    private SQLiteDatabase db;
    private OnProgressListener listener;//进度回调监听
    private Map<String, FileInfo> map = new HashMap<>();//保存正在下载的任务信息
    private static DownLoaderManger manger;
    private int pos;

    private DownLoaderManger(DowloadDbHelper helper, OnProgressListener listener) {
        this.helper = helper;
        this.listener = listener;
        db = helper.getReadableDatabase();
    }

    /**
     * 单例模式
     *
     * @param helper   数据库帮助类
     * @param listener 下载进度回调接口
     * @return
     */
    public static synchronized DownLoaderManger getInstance(DowloadDbHelper helper, OnProgressListener listener) {
        if (manger == null) {
            synchronized (DownLoaderManger.class) {
                if (manger == null) {
                    manger = new DownLoaderManger(helper, listener);
                }
            }
        }
        return manger;
    }

    /**
     * shezhi
     */
    public void setPos(int pos) {
           this.pos = pos;
    }

    /**
     * 开始下载任务
     */
    public void start(String url) {
        db = helper.getReadableDatabase();
        FileInfo info = helper.queryData(db, url);
        map.put(url, info);

        //开始任务下载
        new DownLoadTask(map.get(url), helper, listener,pos).start();
    }

    /**
     * 停止下载任务
     */
    public void stop(String url) {
        map.get(url).setStop(true);
    }

    /**
     * 停止下载任务
     */
    public void stopAll(String url) {
        map.remove(url);
        if (!db.isOpen()){
            db = helper.getWritableDatabase();
        }
        helper.deleteData(db,url);
    }

    /**
     * 重新下载任务
     */
    public void restart(String url) {
        stop(url);
        try {
            File file = new File(FILE_PATH, map.get(url).getFileName());
            if (file.exists()) {
                file.delete();
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        db = helper.getWritableDatabase();
        helper.resetData(db, url);
        start(url);
    }

    /**
     * 获取当前任务状态
     */
    public boolean getCurrentState(String url) {
        return map.get(url).isDownLoading();
    }

    /**
     * 添加下载任务
     *
     * @param info 下载文件信息
     */
    public void addTask(FileInfo info) {
        //判断数据库是否已经存在这条下载信息
        if (!db.isOpen()){
            db = helper.getWritableDatabase();
        }
        if (!helper.isExist(db, info)) {
            db = helper.getWritableDatabase();
            helper.insertData(db, info);
            map.put(info.getUrl(), info);
        } else {
            //从数据库获取最新的下载信息
            db = helper.getWritableDatabase();
            helper.deleteData(db,info.getUrl());
            if (map.containsKey(info.getUrl())) {
                map.remove(info.getUrl());
            }
            db = helper.getWritableDatabase();
            helper.insertData(db, info);
            map.put(info.getUrl(), info);

        }
    }

}
