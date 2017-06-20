package com.zhizun.zhizunwifi.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.activity.WiFiDetailsActivity;
import com.zhizun.zhizunwifi.bean.FreeWifi;
import com.zhizun.zhizunwifi.bean.JsonWifi;
import com.zhizun.zhizunwifi.bean.StoreMsg;
import com.zhizun.zhizunwifi.bean.WiFiDetailEntity;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.SystemUtil;
import com.zhizun.zhizunwifi.utils.WifiUtils;

import java.util.ArrayList;
import java.util.List;

public class WiFiListLayout extends LinearLayout implements OnClickListener,OnLongClickListener{

	Context context;

	//freeWiFi-View
	/**
	 * 存放免费WiFi的模块
	 */
	LinearLayout ViewFreeWifi;
	TextView tv_freeWiFi;
	/**
	 * 存放免费WiFi的列表
	 */
	LinearLayout LayoutFreeWifi;

	//localWiFi-View
	/**
	 * 存放免费WiFi的模块
	 */
	LinearLayout ViewLocalWifi;
	/**
	 * 存放免费WiFi的列表
	 */
	LinearLayout LayoutLocalWifi;

	int lineColor = Color.WHITE;
	boolean isPaintLine = true;

	LayoutParams params;
	LinearLayout.LayoutParams LineParams;

	//存放不需要密码的wifi
	LinearLayout OpenWifi;
	LinearLayout OpenWifilay;

	private WifiUtils mwifiutils;

	private enum WiFiState{
		Free,NoFree
	}

	private OnItemListener onItemListener;

	public WiFiListLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public WiFiListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.line);
		isPaintLine = ta.getBoolean(R.styleable.line_isPaint, true);
		lineColor = ta.getInt(R.styleable.line_color_base, 0);
		ta.recycle();

	}

	public WiFiListLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void initView(View view){

		mwifiutils = new WifiUtils(context);

		//freeWiFi-View
		ViewFreeWifi = (LinearLayout) view.findViewById(R.id.ViewFreeWifi);
		tv_freeWiFi = (TextView) view.findViewById(R.id.tv_freeWiFi);
		LayoutFreeWifi = (LinearLayout) view.findViewById(R.id.LayoutFreeWifi);

		//localWiFi-View
		ViewLocalWifi = (LinearLayout) view.findViewById(R.id.ViewLocalWifi);
		LayoutLocalWifi = (LinearLayout) view.findViewById(R.id.LayoutLocalWifi);

		OpenWifi = (LinearLayout)view.findViewById(R.id.OpenWifi);
		OpenWifilay = (LinearLayout)view.findViewById(R.id.OpenWifilay);

		LineParams = new LayoutParams(LayoutParams.MATCH_PARENT, SystemUtil.dpTurnPx(context, 1));
	}

	/**
	 * 更新刷新WiFi列表
	 * @param mFreeWifiList
	 * @param scanWiFiResults
	 */
//	public void refreshData(List<FreeWifi> mFreeWifiList ,List<ScanResult> scanWiFiResults){
//		LayoutFreeWifi.removeAllViews();
//		if(mFreeWifiList.size() > 0){
//			ViewFreeWifi.setVisibility(View.VISIBLE);
//			tv_freeWiFi.setText(mFreeWifiList.size() + "个可免费连接WiFi");
//
//			for(int i = 0; i < mFreeWifiList.size(); i++){
//				addFreeWiFiView(LayoutFreeWifi, mFreeWifiList.get(i));
//			}
//
//		} else{
//			ViewFreeWifi.setVisibility(View.GONE);
//		}
//
//		LayoutLocalWifi.removeAllViews();
//		for(int i = 0; i < scanWiFiResults.size(); i++){
//			addLocalWiFiView(LayoutLocalWifi, scanWiFiResults.get(i));
//		}
//	}

	public void refreshData(List<FreeWifi> mFreeWifiList , List<ScanResult> scanWiFiResults, String curConnSSID, List<JsonWifi> jsonWifiList, List<StoreMsg> storeMsgList){
//		LayoutFreeWifi.removeAllViews();
		int changeItemEnd = 0;
		int addItemStart = mFreeWifiList.size();
		List<FreeWifi> mOpenWifiList = new ArrayList<>();

		if(mFreeWifiList.size() > 0){

			//删除重复数据
			for (int i = 0; i < mFreeWifiList.size(); i++)  //外循环是循环的次数
			{
				for (int j = mFreeWifiList.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
				{
					if (TextUtils.isEmpty(mFreeWifiList.get(j).wifi_name) || mFreeWifiList.get(i).wifi_name.equals(mFreeWifiList.get(j).wifi_name))
					{
						mFreeWifiList.remove(j);
					}
				}
			}

			for (int i = 0; i < mFreeWifiList.size(); i++) {
				if (mFreeWifiList.get(i).isNoPsw()){
					mOpenWifiList.add(mFreeWifiList.get(i));
					mFreeWifiList.remove(mFreeWifiList.get(i));
				}
			}

			ViewFreeWifi.setVisibility(View.VISIBLE);

			int displayValue = 0;

			if(LayoutFreeWifi.getChildCount() > mFreeWifiList.size()){
				LayoutFreeWifi.removeViews(mFreeWifiList.size(), LayoutFreeWifi.getChildCount() - mFreeWifiList.size());
				changeItemEnd = mFreeWifiList.size();
			} else{
				addItemStart = changeItemEnd = LayoutFreeWifi.getChildCount();
				for(; addItemStart < mFreeWifiList.size(); addItemStart++){
					displayValue += addFreeWiFiView(LayoutFreeWifi, mFreeWifiList.get(addItemStart), curConnSSID,storeMsgList);
				}
			}
			for(int i = 0; i < changeItemEnd; i++){
				displayValue += updateFreeWiFiView(LayoutFreeWifi.getChildAt(i), mFreeWifiList.get(i), curConnSSID,storeMsgList);
			}
			if(displayValue > 0){
				ViewFreeWifi.setVisibility(View.VISIBLE);
				tv_freeWiFi.setText("免费安全的WiFi");
			} else{
				ViewFreeWifi.setVisibility(View.GONE);
			}
		} else{
			ViewFreeWifi.setVisibility(View.GONE);
		}

		//不需要密码连接的wifi
		if (mOpenWifiList.size() > 0){
			OpenWifi.setVisibility(VISIBLE);

			int displayValue = 0;

			if(OpenWifilay.getChildCount() > mOpenWifiList.size()){
				OpenWifilay.removeViews(mOpenWifiList.size(), OpenWifilay.getChildCount() - mOpenWifiList.size());
				changeItemEnd = mOpenWifiList.size();
			} else{
				addItemStart = changeItemEnd = OpenWifilay.getChildCount();
				for(; addItemStart < mOpenWifiList.size(); addItemStart++){
					displayValue += addFreeWiFiView(OpenWifilay, mOpenWifiList.get(addItemStart), curConnSSID,storeMsgList);
				}
			}
			for(int i = 0; i < changeItemEnd; i++){
				displayValue += updateFreeWiFiView(OpenWifilay.getChildAt(i), mOpenWifiList.get(i), curConnSSID,storeMsgList);
			}
			if(displayValue > 0){
				OpenWifi.setVisibility(View.VISIBLE);
			} else{
				OpenWifi.setVisibility(View.GONE);
			}

		}else {
			OpenWifi.setVisibility(GONE);
		}

		//可破解的
		List<ScanResult> scanWiFiResults_begin = new ArrayList<>();
		//不可破解的
		List<ScanResult> scanWiFiResults_befor = new ArrayList<>();
		scanWiFiResults_befor.addAll(scanWiFiResults);
		for (int i = 0; i < scanWiFiResults.size(); i++) {
			for (int i1 = 0; i1 < jsonWifiList.size(); i1++) {
				if (scanWiFiResults.get(i).SSID.equals(jsonWifiList.get(i1).getSsid())){
					scanWiFiResults_befor.remove(scanWiFiResults.get(i));
					scanWiFiResults_begin.add(scanWiFiResults.get(i));
				}
			}

		}
		scanWiFiResults.clear();
		scanWiFiResults.addAll(scanWiFiResults_begin);
		scanWiFiResults.addAll(scanWiFiResults_befor);

        //删除重复数据
		for (int i = 0; i < scanWiFiResults.size(); i++)  //外循环是循环的次数
		{
			for (int j = scanWiFiResults.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
			{
				if (TextUtils.isEmpty(scanWiFiResults.get(j).SSID) || scanWiFiResults.get(i).SSID.equals(scanWiFiResults.get(j).SSID))
				{
					scanWiFiResults.remove(j);
				}

			}

		}


		if(LayoutLocalWifi.getChildCount() > scanWiFiResults.size()){
			LayoutLocalWifi.removeViews(scanWiFiResults.size(), LayoutLocalWifi.getChildCount() - scanWiFiResults.size());
			changeItemEnd = scanWiFiResults.size();
		} else{
			addItemStart = changeItemEnd = LayoutLocalWifi.getChildCount();
			for(; addItemStart < scanWiFiResults.size(); addItemStart++){
				addLocalWiFiView(LayoutLocalWifi, scanWiFiResults.get(addItemStart),jsonWifiList,storeMsgList);
			}
		}
		for(int i = 0; i < changeItemEnd; i++){

			updateLocalWiFiView(LayoutLocalWifi.getChildAt(i), scanWiFiResults.get(i),jsonWifiList,storeMsgList);
		}
	}

	Thread thread = null;
	private boolean isRunning_Visibility = false;
	private String preSSID;
	public void setVisibility(final String SSID){
		if(!isRunning_Visibility || !preSSID.equals(SSID)){
			isRunning_Visibility = true;
			preSSID = SSID;
			if(thread != null){
				try{
					thread.stop();
				}catch(UnsupportedOperationException uoe){}
				thread = null;
			}
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int freeWifiDisplayCount = 0;
					for(int i = 0; i < LayoutFreeWifi.getChildCount(); i++){
						View view = LayoutFreeWifi.getChildAt(i);
						FreeWifi mFreeWifi = (FreeWifi) view.getTag(R.id.dataClass);
						if(!mFreeWifi.getWifiName().equals(SSID.replaceAll("\"", ""))){
							if(view.getVisibility() != View.VISIBLE){
								//handler处理显示
								if(ViewFreeWifi.getVisibility() != View.VISIBLE){
									sendHandle_display(ViewFreeWifi, View.VISIBLE);
								}
								sendHandle_display(view, View.VISIBLE);
							}
							freeWifiDisplayCount++;
						} else if(mFreeWifi.getWifiName().equals(SSID.replaceAll("\"", ""))){
							if(LayoutFreeWifi.getChildCount() == 1){
								sendHandle_display(ViewFreeWifi, View.GONE);
							} else if(view.getVisibility() != View.GONE){
								//handler处理隐藏
								sendHandle_display(view, View.GONE);
							}
						}
					}
					sendHandle_setText(tv_freeWiFi, freeWifiDisplayCount + "个可免费连接WiFi");

					for(int i = 0; i < LayoutLocalWifi.getChildCount(); i++){
						View view = LayoutLocalWifi.getChildAt(i);
						ScanResult mScanResult = (ScanResult) view.getTag(R.id.dataClass);
						if(!mScanResult.SSID.replaceAll("\"", "").equals(SSID) &&
								view.getVisibility() != View.VISIBLE){
							//handler处理显示
							sendHandle_display(view, View.VISIBLE);
						} else if(mScanResult.SSID.replaceAll("\"", "").equals(SSID) &&
								view.getVisibility() != View.GONE){
							//handler处理隐藏
							sendHandle_display(view, View.GONE);
						}
					}
					isRunning_Visibility = false;
				}
			});
			thread.start();
		}
	}

	public void sendHandle_display(View view, int Visibility){
		Message msg = new Message();
		msg.what = displayHandler;
		msg.obj = view;
		msg.arg1 = Visibility;
		handler.sendMessage(msg);
	}

	public void sendHandle_setText(View view, String textStr){
		Message msg = new Message();
		Object[] obj = new Object[2];
		obj[0] = view;
		obj[1] = textStr;
		msg.what = setTextHandler;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	private final static int displayHandler = 0x1;
	private final static int setTextHandler = 0x100;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case displayHandler:
					if(msg.obj != null){
						View view = (View)msg.obj;
						if (msg.arg1 == 0){
							view.setVisibility(VISIBLE);
						}else {
							view.setVisibility(GONE);
						}
					}
					break;
				case setTextHandler:
					if(msg.obj != null){
						Object[] obj = (Object[]) msg.obj;
						((TextView) obj[0]).setText(obj[1].toString());
					}
					break;

				default:
					break;
			}
		}
	};

	/**
	 * 在免费WiFi列表添加新免费WiFi-View
	 * @param LayoutFreeWifi
	 * @param freeWifi
	 */
	public int addFreeWiFiView(LinearLayout LayoutFreeWifi, FreeWifi freeWifi, String curConnSSID,List<StoreMsg> storeMsgList){
		View freeWiFiView = initWiFiView();
		int displayValue = updateFreeWiFiView(freeWiFiView, freeWifi, curConnSSID,storeMsgList);
		LayoutFreeWifi.addView(freeWiFiView);

		addItemLine(LayoutFreeWifi);
		return displayValue;
	}

	/**
	 * 对免费WiFi-itemView初始化数据
	 * @param freeWifi
	 */
	public int updateFreeWiFiView(View freeWiFiView, final FreeWifi freeWifi, final String curConnSSID,final List<StoreMsg> storeMsgList){
		WiFiViewHolder holder = (WiFiViewHolder) freeWiFiView.getTag();

		holder.tv_ap_ssid.setText(freeWifi.getWifiName().replaceAll("\"", ""));
		holder.ap_state_value.setVisibility(View.GONE);
		if(freeWifi.getIs_store() == 0){
			//普通免费WiFI-网络--不是店铺
			holder.ap_state_value.setCompoundDrawables(null, null, null, null);//画在右边
			holder.ap_state_value.setText("已分享，可免费连接");
		} else if(freeWifi.getIs_store() == -1){
			//普通免费WiFI-本地
			holder.ap_state_value.setCompoundDrawables(null, null, null, null);//画在右边
			holder.ap_state_value.setText("我连接过已保存");
			if (freeWifi.isNoPsw()){
				holder.ap_state_value.setText("开放网络");
			}
		} else{
			//普通免费WiFI-网络--店铺
			holder.ap_state_value.setText(freeWifi.getStore_name() + " / " + "已分享，可免费连接");
			Drawable drawable = context.getResources().getDrawable(R.drawable.icon_check_wait);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
			holder.ap_state_value.setCompoundDrawables(drawable, null, null, null);//画在右边
		}
		holder.signal.setImageLevel(freeWifi.getRSSI());
		setImageLevel(holder, WiFiState.Free, freeWifi.getRSSI(),freeWifi.isNoPsw());
		freeWiFiView.setTag(R.id.dataClass, freeWifi);
		if(curConnSSID != null && freeWifi.getWifiName().equals(curConnSSID)){
			freeWiFiView.setVisibility(View.GONE);
			return 0;
		}
		freeWiFiView.setVisibility(View.VISIBLE);
		holder.connected.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(context, WiFiDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("WiFiDetail", new WiFiDetailEntity(freeWifi.getWifiName().replaceAll("\"", ""),
						Math.abs(freeWifi.getRSSI()),freeWifi.isNoPsw(),BaseUtils.getSharedPreferences("address",context),0,freeWifi.netId,freeWifi.getWifi_mac_address(),freeWifi.wifi_psw,false));

				if (storeMsgList != null) {
					for (int i = 0; i < storeMsgList.size(); i++) {
						if (storeMsgList.get(i).ssid.equals(freeWifi.getWifiName().replaceAll("\"", ""))) {
							bundle.putSerializable("storemsg", storeMsgList.get(i));
						}
					}
				}

				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});

		holder.tv_store_lay.setVisibility(GONE);
if (storeMsgList != null) {
	for (int i = 0; i < storeMsgList.size(); i++) {
		if (storeMsgList.get(i).ssid.equals(freeWifi.getWifiName().replaceAll("\"", ""))) {
			holder.tv_store_lay.setVisibility(VISIBLE);
			holder.tv_store_name.setText(storeMsgList.get(i).name);
			holder.tv_store_type.setText(storeMsgList.get(i).type);
		}
	}
}

		return 1;
	}

	/**
	 * 在本地WiFi列表添加新本地WiFi-View

	 */
	public void addLocalWiFiView(LinearLayout LayoutLocalWifi, ScanResult scanResult,List<JsonWifi> jsonWifiList,List<StoreMsg> storeMsgList){
		View localWiFiView = initWiFiView();
		updateLocalWiFiView(localWiFiView, scanResult,jsonWifiList,storeMsgList);
		LayoutLocalWifi.addView(localWiFiView);
		addItemLine(LayoutLocalWifi);
	}

	/**
	 * 对本地WiFi-itemView初始化数据
	 */
	public void updateLocalWiFiView(View localWiFiView,final ScanResult scanResult,final List<JsonWifi> jsonWifiList,final List<StoreMsg> storeMsgList){
		boolean isPj = false;
		WiFiViewHolder holder = (WiFiViewHolder) localWiFiView.getTag();

		holder.tv_ap_ssid.setText(scanResult.SSID.replaceAll("\"", ""));
		holder.ap_state_value.setVisibility(View.GONE);
		holder.signal.setImageLevel(scanResult.level);

		holder.canconn_wifi_im.setVisibility(GONE);
		if (jsonWifiList != null && jsonWifiList.size() > 0){
			for (int i = 0; i < jsonWifiList.size(); i++) {
				if (scanResult.SSID.equals(jsonWifiList.get(i).getSsid())){
					holder.canconn_wifi_im.setVisibility(VISIBLE);
					holder.canconn_wifi_im.setImageResource(R.drawable.ic_menu_ap_manucon_p);
					isPj = true;
				}else {
					//holder.connected.setVisibility(GONE);
				}
			}
		}else {
			holder.connected.setVisibility(GONE);
		}

		holder.tv_store_lay.setVisibility(GONE);
		if (storeMsgList != null) {
			for (int i = 0; i < storeMsgList.size(); i++) {
				if (storeMsgList.get(i).ssid.equals(scanResult.SSID)) {
					holder.tv_store_lay.setVisibility(VISIBLE);
					holder.tv_store_name.setText(storeMsgList.get(i).name);
					holder.tv_store_type.setText(storeMsgList.get(i).type);
				}
			}
		}
		setImageLevel(holder, WiFiState.NoFree, scanResult.level, TextUtils.isEmpty(scanResult.capabilities));
		localWiFiView.setTag(R.id.dataClass, scanResult);
		localWiFiView.setVisibility(View.VISIBLE);
		holder.connected.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent();
				intent.setClass(context, WiFiDetailsActivity.class);
				Bundle bundle = new Bundle();

				bundle.putSerializable("WiFiDetail", new WiFiDetailEntity(scanResult.SSID.replaceAll("\"", ""),
						Math.abs(scanResult.level),false,BaseUtils.getSharedPreferences("address",context),1,-1,scanResult.BSSID,"",false));

				if (storeMsgList != null) {
					for (int i = 0; i < storeMsgList.size(); i++) {
						if (storeMsgList.get(i).ssid.equals(scanResult.SSID)) {
							bundle.putSerializable("storemsg", storeMsgList.get(i));
						}
					}
				}

				if (jsonWifiList != null && jsonWifiList.size() > 0){
					for (int i = 0; i < jsonWifiList.size(); i++) {
						if (scanResult.SSID.equals(jsonWifiList.get(i).getSsid())){
							bundle.putSerializable("WiFiDetail", new WiFiDetailEntity(scanResult.SSID.replaceAll("\"", ""),
									Math.abs(scanResult.level),false,BaseUtils.getSharedPreferences("address",context),1,-1,scanResult.BSSID,jsonWifiList.get(i).getPasswd(),true));
						}
					}
				}


				intent.putExtras(bundle);
				context.startActivity(intent);

			}
		});
	}

	/**
	 * 初始化-加载WiFi通用View
	 * @return
	 */
	public View initWiFiView(){
		View holderView = LayoutInflater.from(context).inflate(R.layout.act_deep_unlock_list_item, null);
		WiFiViewHolder holder = new WiFiViewHolder();
		holder.tv_ap_ssid = (TextView) holderView.findViewById(R.id.tv_ap_ssid);
		holder.ap_state_value = (TextView) holderView.findViewById(R.id.ap_state_value);
		holder.signal = (ImageView) holderView.findViewById(R.id.signal);
		holder.connected = (ImageView) holderView.findViewById(R.id.connected);
		holder.cb_ap_check = (CheckBox) holderView.findViewById(R.id.cb_ap_check);
		holder.tv_state_safe = (TextView) holderView.findViewById(R.id.tv_state_safe);
		holder.canconn_wifi_im = (ImageView)holderView.findViewById(R.id.canconn_wifi_im);
		holder.tv_store_lay = (LinearLayout)holderView.findViewById(R.id.tv_store_lay);
		holder.tv_store_name = (TextView)holderView.findViewById(R.id.tv_store_name);
		holder.tv_store_type = (TextView)holderView.findViewById(R.id.tv_store_type);
		holderView.setTag(holder);

		holderView.setOnClickListener(this);
		holderView.setOnLongClickListener(this);

		return holderView;
	}

	/**
	 * 根据信号值设置相关数据，信号源图片，安全ICON显隐
	 * @param holder
	 * @param mWiFiState
	 * @param level
	 */
	public void setImageLevel(WiFiViewHolder holder, WiFiState mWiFiState, int level,boolean isnopsw){
		level = Math.abs(level);
		if(mWiFiState == WiFiState.NoFree){//非免费的
			holder.tv_state_safe.setVisibility(View.GONE);
			if (level > 100) {
				holder.signal.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_wifi_lock_signal_1));
			} else if (level > 80) {
				holder.signal.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_wifi_lock_signal_1));
			} else if (level > 70) {
				holder.signal.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_wifi_lock_normal_2));
			} else if (level > 60) {
				holder.signal.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_wifi_lock_normal_2));
			} else if (level > 50) {
				holder.signal.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_wifi_lock_normal_3));
			} else {
				holder.signal.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_wifi_lock_normal_3));
			}
		} else{//免费的
			if (isnopsw){
				holder.tv_state_safe.setVisibility(View.GONE);
				if (level > 100) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_wifi_open_signal_1));
				} else if (level > 80) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_wifi_open_signal_1));
				} else if (level > 70) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_wifi_open_signal_2));
				} else if (level > 60) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_wifi_open_signal_2));
				} else if (level > 50) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_wifi_open_signal_3));
				} else {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_wifi_open_signal_3));
				}

			}else {
				holder.tv_state_safe.setVisibility(View.GONE);
				if (level > 100) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ico_wifi_blue_1));
				} else if (level > 80) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ico_wifi_blue_1));
				} else if (level > 70) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ico_wifi_blue_2));
				} else if (level > 60) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ico_wifi_blue_2));
				} else if (level > 50) {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ico_wifi_blue_3));
				} else {
					holder.signal.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ico_wifi_blue_3));
				}
			}
		}
	}

	class WiFiViewHolder {
		ImageView signal;
		TextView tv_ap_ssid;
		TextView ap_state_value;
		ImageView connected;
		CheckBox cb_ap_check;
		TextView tv_state_safe;
		ImageView canconn_wifi_im;
		LinearLayout tv_store_lay;
		TextView tv_store_name;
		TextView tv_store_type;
	}

	/**
	 * 添加view-line线-给每个item
	 * @param layoutView
	 */
	public void addItemLine(LinearLayout layoutView){
		if(!isPaintLine)
			return;
		ImageView lineView = new ImageView(context);
		lineView.setLayoutParams(LineParams);
		lineView.setBackgroundColor(lineColor);
		layoutView.addView(lineView);
	}

	public int getColor(int resId){
		return context.getResources().getColor(resId);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int measuredWidth = getMeasuredWidth();
//		int measuredHeight = getMeasuredHeight();
//		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public void setOnItemListener(OnItemListener onItemListener) {
		this.onItemListener = onItemListener;
	}

	/**
	 * 监听 item单击，item长按事件
	 * @author Administrator
	 *
	 */
	public interface OnItemListener{
		/**
		 *
		 * @param parentView--父view
		 * @param view--响应事件的view
		 * @param mFreeWifi--!null时，响应事件免费WiFi事件
		 * @param mScanResult--!null时，响应事件本地WiFi事件
		 */
		public void onItemClick(View parentView, View view, FreeWifi mFreeWifi, ScanResult mScanResult);
		/**
		 *
		 * @param parentView--父view
		 * @param view--响应事件的view
		 * @param mFreeWifi--!null时，响应事件免费WiFi事件
		 * @param mScanResult--!null时，响应事件本地WiFi事件
		 */
		public void onItemLongClick(View parentView, View view, FreeWifi mFreeWifi, ScanResult mScanResult);
	}

	@Override
	public void onClick(View view) {
		Object dataClass = view.getTag(R.id.dataClass);
		if(onItemListener != null && dataClass != null){
			if(view.getTag(R.id.dataClass) instanceof FreeWifi){
				onItemListener.onItemClick(LayoutFreeWifi, view, (FreeWifi) dataClass, null);
			} else if(dataClass instanceof ScanResult){
				onItemListener.onItemClick(LayoutLocalWifi, view, null, (ScanResult) dataClass);
			}
		}
	}

	@Override
	public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		Object dataClass = view.getTag(R.id.dataClass);
		if(onItemListener != null && dataClass != null){
			if(view.getTag(R.id.dataClass) instanceof FreeWifi){
				onItemListener.onItemLongClick(LayoutFreeWifi, view, (FreeWifi) dataClass, null);
			} else if(dataClass instanceof ScanResult){
				onItemListener.onItemLongClick(LayoutLocalWifi, view, null, (ScanResult) dataClass);
			}
		}
		return true;
	}

}
