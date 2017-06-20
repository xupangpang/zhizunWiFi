package com.zhizun.zhizunwifi.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.cache.CachePolicy;


/**
 * 閹恒儱褰涢崣鍌涙殶閸欏﹤鎮楅崣棰佹唉娴滐拷
 *
 * @author 濞粹晛鐡�
 * @date 2015-4-8
 * @since Version 1
 */
public class MarketAPI {


	/** host閿熸枻鎷峰潃 */
	public static final String API_BASE_URL =
			// real host
			"http://xiaohua.anzhuo.com/";
	/** API閿熸枻鎷峰潃 */
	public static final String API_HOST_JAVA = API_BASE_URL + "app/";
	public static String MD5_PASSWORD = "&RAOwx342i!#fke@!dfkiRSWS453LSBb6sdksSWfop";
	// 閿熸枻鎷风珯閿熸枻鎷烽〉
	public static String home_web = "http://xiaohua.anzhuo.com/web/index/index/1";
	//推广下载连接
	public static String QQ_web = "http://android.myapp.com/myapp/detail.htm?apkName=com.zhizun.zhizunwifi";

	// 寰敓鑴氬嚖鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 post閿熻娈碉綇鎷穒nfo閿熸枻鎷烽敓渚ユ唻鎷烽敓鏂ゆ嫹鎭敓鏂ゆ嫹 mp3path閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸帴锝忔嫹
	public static String weixin_sjfx = "http://xiaohua.anzhuo.com/web/index/sjfx/?mp3path=";
	// API URLS
	/* package */public static final String[] API_URLS = {

			// 1.1.1閼惧嘲褰囧▔銊ュ斀妤犲矁鐦夐惍锟�
			API_HOST_JAVA + "portal/user/sendValidateCode.do",
			// 1.1.2閻€劍鍩涢惂璇茬秿
			API_HOST_JAVA + "portal/user/login.do",
			// 1.1.3閻€劍鍩涘▔銊ュ斀
			API_HOST_JAVA + "portal/user/regist.do",

			// 1.1.1閼惧嘲褰囨い鍫曞劥鏉烆喗宕查崶锟�
			API_HOST_JAVA + "homepage/top_slide_img",
			// 1.1.2閼惧嘲褰囬弨璺哄弳閸掓銆�
			API_HOST_JAVA + "homepage/income_list",
			// 1.2.4娣囶喗鏁肩敮鎰娇娣団剝浼�
			API_HOST_JAVA + "member/change_pwd",
			// 1.2.5娣囶喗鏁奸幍瀣簚
			API_HOST_JAVA + "member/change_phone",
			// 1.2.6娣囶喗鏁奸崺鐑樻拱鐠у嫭鏋�
			API_HOST_JAVA + "member/modify",
			// 1.2.7閹垫儳娲栫�靛棛鐖�
			API_HOST_JAVA + "member/find_pwd",
			// 1.3.1濞茶濮╅崗顒�鎲�
			API_HOST_JAVA + "other/activity_list",
			// 1.3.2閹板繗顫嗛崣宥夘洯
			API_HOST_JAVA + "other/suggest",
			// 1.3.3濡拷閺屻儲娲块弬锟�
			API_HOST_JAVA + "other/update",
			// 1.3.4闁夸礁鐫嗘い鐢告桨閺佺増宓�
			API_HOST_JAVA + "other/webimage",
			// 1.2.8閼惧嘲褰囬悽銊﹀煕閸╃儤婀扮挧鍕灐
			API_HOST_JAVA + "member/get_data",
			// 1.3.5闁夸礁鐫嗘い鐢告桨閺佺増宓� (娴犺濮熺�瑰本鍨氶崣宥夘洯閹懎鍠�)
			API_HOST_JAVA + "other/tasked",
			// 1.3.6閸掑棝鍘d
			API_HOST_JAVA + "member/get_user_id",
			// 1.3.4闂傚綊鎸搈p3閹恒儱褰�
			API_HOST_JAVA + "other/webmp3",
			// 1.4.2鐠愵厺鎷遍崯鍡楁惂
			API_HOST_JAVA + "market/pay_goods",
			// 1.4.3鐠愵厺鎷遍崯鍡楁惂閸樺棗褰剁拋鏉跨秿
			API_HOST_JAVA + "market/history_goods",
			// 1.4.1閸忔垶宕� (閼惧嘲褰囬崯鍡楁惂)
			API_HOST_JAVA + "market/goods",
			// 1.3.5娑撳﹣绱堕柨娆掝嚖閺冦儱绻�
			API_HOST_JAVA + "other/createlog",
			// 1.4.4 閹﹤鏋╁Ο鈥崇础闂傚綊鎸揗P3閹恒儱褰�
			API_HOST_JAVA + "other/surprisewebmp3",
			// 1.4.5 閻ｆ瑥鐡ㄩ悳鍥ㄥ复閸欙拷
			API_HOST_JAVA + "other/retention",
			// 1.4.6 鏉╃偟鐢绘担璺ㄦ暏App娴犺濮熸總鏍уС閹恒儱褰�
			API_HOST_JAVA + "other/retention_task",
			// 1.4.8 閸掋倖鏌囬妴浣规纯閺傜檺EMI閸欓攱甯撮崣锟�
			API_HOST_JAVA + "other/check_update_imei",
			// 1.4.9 閺備即鏀ｇ仦蹇斈侀崸妤佹殶閹诡喗甯撮崣锟�
			API_HOST_JAVA + "other/new_webImage_2015_9_25",
			// 1.4.10 閺傞鎹㈤崝鈥崇暚閹存劕寮芥＃鍫熷复閸欙拷
			API_HOST_JAVA + "other/new_tasked_2015_9_25",
			// 鐠愵厺鎷遍崯鍡楁惂鐠囷妇绮忔い鐢告桨
			API_HOST_JAVA + "market/goodsDetailed",
			// 閾忔碍瀚欓崯鍡楁惂閸掓銆�
			API_HOST_JAVA + "market/goods_xuni",
			// 閹绘劗骞囬崯鍡楁惂閸掓銆�
			API_HOST_JAVA + "market/goods_tixian",
			// 鐎圭偟澧块崯鍡楁惂閸掓銆�
			API_HOST_JAVA + "market/goods_shiwu",
			// 1.4.1閺傛澘鍘幑锟� (閼惧嘲褰囬崯鍡楁惂)
			API_HOST_JAVA + "market/goods_new",
			// 1.4.11 閼惧嘲褰囬幎钘夘殯閺佺増宓侀幒銉ュ經
			API_HOST_JAVA + "market/lottery",
			// 1.4.12 閼惧嘲褰囧鍙夊▕婵傛牜鏁ら幋椋庢畱閺佺増宓�
			API_HOST_JAVA + "market/winning_user_data",};
	/** 閼惧嘲褰囧▔銊ュ斀妤犲矁鐦夐惍浣瑰复閸欙拷 */
	public static final int GET_VERI_CODE = 0;
	/** 閻€劍鍩涢惂璇茬秿閹恒儱褰� */
	public static final int LOGIN = 1;
	/** 閻€劍鍩涘▔銊ュ斀閹恒儱褰� */
	public static final int REGISTER = 2;

	/** 閼惧嘲褰囨い鍫曞劥鏉烆喗宕查崶鐐复閸欙拷 */
	public static final int TOP_SLIDE_IMG = 0;
	/** 閼惧嘲褰囬弨璺哄弳閸掓銆冮幒銉ュ經 */
	public static final int INCOME_LIST = 1;
	/** 娣囶喗鏁肩敮鎰娇娣団剝浼呴幒銉ュ經 */
	public static final int CHANGE_PWD = 5;
	/** 娣囶喗鏁奸幍瀣簚閹恒儱褰� */
	public static final int CHANGE_PHONE = 6;
	/** 娣囶喗鏁奸崺鐑樻拱鐠у嫭鏋￠幒銉ュ經 */
	public static final int CHANGE_DATA = 7;
	/** 閹垫儳娲栫�靛棛鐖滈幒銉ュ經 */
	public static final int FIND_PWD = 8;
	/** 濞茶濮╅崗顒�鎲￠幒銉ュ經 */
	public static final int ACTIVITY_LIST = 9;
	/** 閹板繗顫嗛崣宥夘洯閹恒儱褰� */
	public static final int SUGGEST = 10;
	/** 濡拷閺屻儲娲块弬鐗堝复閸欙拷 */
	public static final int UPDATE = 11;
	/** 闁夸礁鐫嗘い鐢告桨閺佺増宓� */
	public static final int WEB_IMAGE = 12;
	/** 閼惧嘲褰囬悽銊﹀煕閸╃儤婀扮挧鍕灐 */
	public static final int GET_DATE = 13;
	/** 娴犺濮熺�瑰本鍨氶崣宥夘洯閹懎鍠� */
	public static final int FEEDBACK_TASK = 14;
	/** 閸掑棝鍘d */
	public static final int GET_USER_ID = 15;
	/** 闂傚綊鎸揗P3 */
	public static final int CLOCK_URL = 16;
	/** 鐠愵厺鎷遍崯鍡楁惂 */
	public static final int GET_BUY = 17;
	/** 鐠愵厺鎷遍崯鍡楁惂閸樺棗褰� */
	public static final int GET_HISTORY_GOODS = 18;
	/** 閸忔垶宕� */
	public static final int GET_EXCHANGE = 19;
	/** 娑撳﹣绱堕柨娆掝嚖閺冦儱绻� */
	public static final int UPLOADLOG = 20;
	/** 閹﹤鏋╁Ο鈥崇础闂傚綊鎸揗P3閹恒儱褰� */
	public static final int SURPRISE_WEB_MP3 = 21;
	/** 閻ｆ瑥鐡ㄩ悳鍥ㄥ复閸欙拷 */
	public static final int APP_RETENTION = 22;
	/** 鏉╃偟鐢绘担璺ㄦ暏App娴犺濮熸總鏍уС閹恒儱褰� */
	public static final int APP_RETENTION_TASK = 23;
	/** 濡拷濞村娲块弬鐧怣EI閸欓攱甯撮崣锟� */
	public static final int CHE_AND_UPDA_IMEI_TASK = 24;
	/** 閺備即鏀ｇ仦蹇斈侀崸妤佹殶閹诡喗甯撮崣锟� */
	public static final int NEW_WEB_IMAGE_2015_9_25 = 25;
	/** 閺傞鎹㈤崝鈥崇暚閹存劕寮芥＃鍫熷复閸欙拷 */
	public static final int NEW_FEEDBACK_TASK_2015_9_25 = 26;
	/** 鐠愵厺鎷遍崯鍡楁惂鐠囷妇绮� */
	public static final int goodsdetailed = 27;
	/** 閾忔碍瀚欓崯鍡楁惂閸掓銆� */
	public static final int GET_EXCHANGE_XUNI = 28;
	/** 閹绘劗骞囬崯鍡楁惂閸掓銆� */
	public static final int GET_EXCHANGE_TIXIAN = 29;
	/** 鐎圭偟澧块崯鍡楁惂閸掓銆� */
	public static final int GET_EXCHANGE_SHIWU = 30;
	/** 閺傜増甯归懡鎰櫌閸濅礁鍨悰锟� */
	public static final int GET_EXCHANGE_NEW = 31;
	/** 閼惧嘲褰囬幎钘夘殯閺佺増宓侀幒銉ュ經 */
	public static final int GET_LOTTERY_DATA = 32;
	/** 閼惧嘲褰囧鍙夊▕婵傛牜鏁ら幋椋庢畱閺佺増宓侀幒銉ュ經 */
	public static final int GET_LOTTERY_USER_DATA = 33;
	private static final int TIME_OUT = 10 * 1000; // 鐡掑懏妞傞弮鍫曟？
	public static String shareTitle = "至尊免费WiFi";
	public static String shareContents = "这是一款可以免费连接附近WiFi的APP,超好用！";
	public static void dhnetUrl(String url, NetTask netTask) {
		DhNet net = new DhNet();
		net.setUrl(url);
		// 娴ｈ法鏁ょ紓鎾崇摠缁涙牜鏆�
		net.useCache(CachePolicy.POLICY_ON_NET_ERROR);
		net.doGet(netTask);
	}

	/**
	 * 娴滃本顐糓D5閸旂姴鐦� sign=md5(md5(user_id=&session_id=1&goods_id=1&RAOwx342i!#fke@!
	 * dfkiRSWS453LSBb6sdksSWfop))
	 *
	 * @return
	 */
	/*
	 * public static String MD5_TWO(String sign) { String md5 = ""; Log.d("lxf",
	 * "閸旂姴鐦戦崢鐔风�� = " + sign + MD5_PASSWORD); try { md5 =
	 * MD5.encryptMD5(MD5.encryptMD5(sign + MD5_PASSWORD)); } catch (Exception
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } return
	 * md5; }
	 */

	public static void getIncomelist(NetTask netTask, String url,Map<String, Object> params) {
		DhNet incomeNet = new DhNet();
		incomeNet.setUrl(url);
		// 娴ｈ法鏁ょ紓鎾崇摠缁涙牜鏆�
		incomeNet.useCache(CachePolicy.POLICY_CACHE_AndRefresh);
		incomeNet.addParams(params);
		incomeNet.doPost(netTask);
	}

	// 娴ｈ法鏁ょ紓鎾崇摠
	public static void getIncomelistDB(NetTask netTask, String url,
									   Map<String, Object> params) {
		DhNet incomeNet = new DhNet();
		incomeNet.setUrl(url);
		// 娴ｈ法鏁ょ紓鎾崇摠缁涙牜鏆�
		incomeNet.useCache(CachePolicy.POLICY_CACHE_AndRefresh);
		incomeNet.addParams(params);
		incomeNet.doPost(netTask);
	}

	/**
	 * 妫ｆ牠銆夐弨璺哄弳閸掓銆�
	 *
	 */
	public static void moneyction(NetTask netTask, String url,Map<String, Object> params) {
		// dhnetUrl(url, netTask);
		DhNet loginNet = new DhNet();
		loginNet.setUrl(url);
		loginNet.useCache(CachePolicy.POLICY_CACHE_AndRefresh);
		loginNet.addParams(params);
		loginNet.doPost(netTask);
	}

	/**
	 * 鐢箑寮弫鎵畱閸氬骸褰存禍銈勭鞍 濞夈劌鍞介妴浣烘暏閹撮娅ヨぐ鏇橈拷浣藉箯閸欐牠鏀ｇ仦蹇撴禈閻楀洤鎷版禒璇插 瀹告彃鍘幑銏犳櫌閸濅浇顔囪ぐ鏇橈拷浣瑰絹娴溿倛顓归崡锟� 鐎瑰顥婇幋鎰閻╂垵鎯夐妴浣锋崲閸斺�冲冀妫ｏ拷
	 * 鐢妇绱︾�涳拷
	 *
	 */
	public static void paramsInteraction(NetTask netTask, String url, Map<String, Object> params) {
		// dhnetUrl(url, netTask);
		DhNet loginNet = new DhNet();
		loginNet.setUrl(url);
		loginNet.useCache(CachePolicy.POLICY_CACHE_AndRefresh);
		loginNet.addParams(params);
		loginNet.doPost(netTask);
	}

	/**
	 * 鐢箑寮弫鎵畱閸氬骸褰存禍銈勭鞍 濞夈劌鍞介妴浣烘暏閹撮娅ヨぐ鏇橈拷浣藉箯閸欐牠鏀ｇ仦蹇撴禈閻楀洤鎷版禒璇插 瀹告彃鍘幑銏犳櫌閸濅浇顔囪ぐ鏇橈拷浣瑰絹娴溿倛顓归崡锟� 鐎瑰顥婇幋鎰閻╂垵鎯夐妴浣锋崲閸斺�冲冀妫ｏ拷
	 * 娑撳秴鐢紓鎾崇摠
	 *
	 */
	public static void paramsInteraction_POLICY_NOCACHE(NetTask netTask, String url,
														Map<String, Object> params) {
		// dhnetUrl(url, netTask);
		DhNet loginNet = new DhNet();
		loginNet.setUrl(url);
		loginNet.useCache(CachePolicy.POLICY_NOCACHE);
		loginNet.addParams(params);
		loginNet.doPost(netTask);
	}

	/**
	 * 娑撳秴鐢崣鍌涙殶閻ㄥ嫬鎮楅崣棰佹唉娴滐拷 閼惧嘲褰囬崗鎴炲床閸熷棗鎼ф穱鈩冧紖
	 *
	 */
	public static void noParamsInteraction(NetTask netTask, String url) {
		DhNet lockImgNet = new DhNet();
		lockImgNet.setUrl(url);
		lockImgNet.useCache(CachePolicy.POLICY_CACHE_AndRefresh);
		// lockImgNet.addParams(params);
		lockImgNet.doPost(netTask);
	}

	/**
	 * 閸旂姾娴囨潪顔款嚄閸ュ墽澧�<br>
	 *
	 */
	public static void getTopSlideImg(NetTask netTask, String url) {
		DhNet topSlideImgNet = new DhNet();
		topSlideImgNet.setUrl(url);
		topSlideImgNet.useCache(CachePolicy.POLICY_CACHE_AndRefresh);
		topSlideImgNet.doPost(netTask);
	}

	/** 闁挎瑨顕ら弮銉ョ箶娑撳﹣绱堕幒銉ュ經 */
	public static void getErrorLog(NetTask netTask, String url,
								   Map<String, Object> params) {
		DhNet loginNet = new DhNet();
		loginNet.setUrl(url);
		loginNet.useCache(CachePolicy.POLICY_ON_NET_ERROR);
		loginNet.addParams(params);
		loginNet.doPost(netTask);
	}

	/**
	 * 閺�璺哄弳閸掓銆�<br>
	 *
	 */
	public static void getMoneyList(NetTask netTask, String url,
									Map<String, Object> params) {
		DhNet loginNet = new DhNet();
		loginNet.setUrl(url);
		loginNet.useCache(CachePolicy.POLICY_CACHE_AndRefresh);
		loginNet.addParams(params);
		loginNet.doPost(netTask);
	}

	/**
	 * 閼惧嘲褰囬悽銊﹀煕閸╃儤婀扮挧鍕灐
	 *
	 */
	public static void getDate(NetTask netTask, String url,
							   Map<String, Object> params) {
	}

	/**
	 * 濡拷閺屻儲娲块弬甯礄鎼存梻鏁ら敍锟�
	 */
	// public static void checkUpgrade(final Context context) {
	//
	// final HashMap<String, Object> params = new HashMap<String, Object>(1);
	// params.put("upgradeList", Utils.getInstalledApps(context));
	//
	// new ApiAsyncTask(context, ACTION_CHECK_UPGRADE, new ApiRequestListener()
	// {
	// @Override
	// public void onSuccess(int method, Object obj) {
	// // do nothing
	// }
	//
	// @Override
	// public void onError(int method, int statusCode) {
	// // do nothing
	// Utils.D("check upgrade fail : " + statusCode);
	// }
	// }, params).execute();
	// }
	/**
	 * android娑撳﹣绱堕弬鍥︽閸掔増婀囬崝鈥虫珤
	 *
	 * @param file
	 *            闂囷拷鐟曚椒绗傛导鐘垫畱閺傚洣娆�
	 *            鐠囬攱鐪伴惃鍓卽l
	 * @return 鏉╂柨娲栭崫宥呯安閻ㄥ嫬鍞寸�癸拷
	 */
	public static boolean uploadLogFile(File file) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 鏉堝湱鏅弽鍥槕 闂呭繑婧�閻㈢喐鍨�
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 閸愬懎顔愮猾璇茬��

		try {
			URL url = new URL(API_URLS[UPLOADLOG]);
			// URL url = new URL("http://192.168.2.213/uploadlog.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 閸忎浇顔忔潏鎾冲弳濞达拷
			conn.setDoOutput(true); // 閸忎浇顔忔潏鎾冲毉濞达拷
			conn.setUseCaches(false); // 娑撳秴鍘戠拋闀愬▏閻€劎绱︾�涳拷
			conn.setRequestMethod("POST"); // 鐠囬攱鐪伴弬鐟扮础
			conn.setRequestProperty("ENCODING", "UTF-8"); // 鐠佸墽鐤嗙紓鏍垳
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				/**
				 * 瑜版挻鏋冩禒鏈电瑝娑撹櫣鈹栭敍灞惧Ω閺傚洣娆㈤崠鍛邦棅楠炴湹绗栨稉濠佺炊
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 鏉╂瑩鍣烽柌宥囧仯濞夈劍鍓伴敍锟� name闁插矂娼伴惃鍕拷闂磋礋閺堝秴濮熼崳銊ь伂闂囷拷鐟曚共ey 閸欘亝婀佹潻娆庨嚋key 閹靛秴褰叉禒銉ョ繁閸掓澘顕惔鏃傛畱閺傚洣娆�
				 * filename閺勵垱鏋冩禒鍓佹畱閸氬秴鐡ч敍灞藉瘶閸氼偄鎮楃紓锟介崥宥囨畱 濮ｆ柨顩�:abc.png
				 */

				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; ENCODING="
						+ "UTF-8" + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024 * 1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();

				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				// LogManager.i(tag, result);
//				Gson gson = new Gson();
				// RequestResult rs = gson.fromJson(result,
				// RequestResult.class);
				// if ("ok".equals(rs.getResult())) {
				// return true;

				// }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}