package com.zhizun.zhizunwifi.utils;


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wbb on 2016/4/6.
 */
public class JmTools {

    public final static String DKAETYA = "@#-*￥cxmx^&+#@*";

    /**
     * 输入加密 T
     * @param str
     * @return
     */
    public static String DKAETYA16(String str){

        String a=str.substring(0, 16);

        return a;
    }

    /**
     * json 解密
     * @param object
     * @return
     */
    public static String DecryptKey(JSONObject object){

        String result = object.optString("result");

        String t = object.optString("T");

        String keyString= Security.md5(t + DKAETYA);

        String key=DKAETYA16(keyString);

        //解密
        String data= Security.decrypt(key, result);

        return data;
    }

    /**
     * 字符串 解密
     * @param object
     * @return
     */
    public static String DecryptString(String object,String T){

        String keyString= Security.md5(T + DKAETYA);

        String key=DKAETYA16(keyString);

        //解密
        String data= Security.decrypt(key, object);

        return data;
    }


    /**
     * 字符串 加密
     * @param time
     * @param mString
     * @return
     */
    public static String encryptionEnhanced(String time,String mString){

        String key= Security.md5(time+DKAETYA);

        String key16=DKAETYA16(key);

        String date= Security.encrypt(key16,mString);

        return date;
    }

    /**
     * 加密 url signkey
     *
     * @param hashMap
     * @return
     */
    public static String NRJM(Map<String, String> hashMap) {
        List<String> listKey = new ArrayList();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        List<String> listSort = Sorting.listSort(listKey);
        String key = "";
        StringBuilder sb = new StringBuilder();
        for (String string : listSort) {
            String name = hashMap.get(string);
            key = sb.append(name).toString();
        }
        return Security.md5(key + "hfqhjy1sbm8icc23");
    }


    /**
     * 用户加密
     *
     * @param hashMap
     * @param methond
    * @return
     */
//    public static String USER_NRJM(HashMap<String, String> hashMap, String methond) {
//        hashMap.put("service", methond);
//        List<String> listKey = new ArrayList();
//        Iterator it = hashMap.keySet().iterator();
//        while (it.hasNext()) {
//            String key = it.next().toString();
//            listKey.add(key);
//        }
//        List<String> listSort = Sorting.listSort(listKey);
//        String key = "";
//        StringBuilder sb = new StringBuilder();
//        for (String string : listSort) {
//            String name = hashMap.get(string);
//            key = sb.append(name).toString();
//        }
//        return Security.md5(key +  ConnectionConstants.DJCCUSERKEY);
//    }

    /**
     * 短信验证码加密方法
     *
     * @return
     */
    public static HashMap<String, String> JM(Context context,String phone) throws JSONException {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name", phone);//账号：手机号码/邮箱
        hashMap.put("service", "User.SendMark");
        hashMap.put("api", "3");//接口的版本号
        hashMap.put("condition","0");
        hashMap.put("c_appid","12");//客户端应用平台标识ID
        hashMap.put("c_os","1");//客户端操作系统：1是安卓，2是IOS，3是WAP，4是PC，5是其它
        hashMap.put("c_version",DeviceUtil.getVersionName(context));//客户端应用版本号
        String s = JmTools.NRJM(hashMap);
        hashMap.put("s", s);
        Iterator iter = hashMap.keySet().iterator();
        JSONObject jsonObject = new JSONObject();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            jsonObject.put(key, hashMap.get(key));
        }
        long time = System.currentTimeMillis() / 1000;
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("data", JmTools.encryptionEnhanced(time + "", jsonObject.toString()));
        hashMap1.put("T", time + "");
        return hashMap1;
    }


    /**
     * 短信验证码加密方法
     *
     * @return
     */
    public static HashMap<String, String> JM_check(Context context,String phone,String mark) throws JSONException {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name", phone);//账号：手机号码/邮箱
        hashMap.put("mark", mark);//账号：手机号码/邮箱
        hashMap.put("service", "User.CheckMark");
        hashMap.put("api", "3");//接口的版本号
        hashMap.put("c_appid","12");//客户端应用平台标识ID
        hashMap.put("c_os","1");//客户端操作系统：1是安卓，2是IOS，3是WAP，4是PC，5是其它
        hashMap.put("c_version",DeviceUtil.getVersionName(context));//客户端应用版本号
        String s = JmTools.NRJM(hashMap);
        hashMap.put("s", s);
        Iterator iter = hashMap.keySet().iterator();
        JSONObject jsonObject = new JSONObject();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            jsonObject.put(key, hashMap.get(key));
        }
        long time = System.currentTimeMillis() / 1000;
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("data", JmTools.encryptionEnhanced(time + "", jsonObject.toString()));
        hashMap1.put("T", time + "");
        return hashMap1;
    }

}
