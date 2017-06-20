package com.zhizun.zhizunwifi.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.WifiUtils;

public class WiFiAdapter extends BaseAdapter {

	// 状态码
	int wifiItemId;
	private WifiUtils localWifiUtils;
	LayoutInflater inflater;
	ListView mListView;
	List<ScanResult> list;
	private Context context;
	NearWiFiViewHolder nearWiFiViewHolder;
	FreeWiFiViewHolder freeWiFiViewHolder;
	ViewHolder holder;
	View nearWiFiView = null;
	View freeWiFiView = null;
	View holderView = null;
	//	private ListView listView;
	private int CurrentConnetPos;
	private String CurrentConnetSSID;
	private int knowPswCount;

	private boolean updateView = false;

	public WiFiAdapter(Context context, List<ScanResult> list, int knowPswCount, WifiUtils localWifiUtils) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.localWifiUtils = localWifiUtils;
		this.context = context;
		this.knowPswCount = knowPswCount-1;
		System.out.println("knowPswCount========== " + knowPswCount);
//		this.listView = listView;
	}

	public void setMainListView(ListView mListView){
		this.mListView = mListView;
	}

	public void refreshData(List<ScanResult> list, int knowPswCount, WifiUtils localWifiUtils){
		this.list = list;
		this.localWifiUtils = localWifiUtils;
		this.knowPswCount = knowPswCount-1;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		Log.i("WifiUtils", "附近免费wifi = " + knowPswCount);
		String ss = list.get(position).SSID;
		return ss;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected View initGetView(ViewGroup parent, int type){
		View convertView = null;
		switch(type){
			case TYPE_FREE:
				inflater = LayoutInflater.from(context);
				freeWiFiView = inflater.inflate(R.layout.layout_free_wifi, null);
				freeWiFiViewHolder = new FreeWiFiViewHolder();

				freeWiFiViewHolder.tv_freeWiFi = (TextView) freeWiFiView.findViewById(R.id.tv_freeWiFi);

				convertView = freeWiFiView;
				convertView.setTag(freeWiFiViewHolder);
				break;
			case TYPE_NEAR:
				inflater = LayoutInflater.from(context);
				nearWiFiView = inflater.inflate(R.layout.layout_near_wifi, null);
				nearWiFiViewHolder = new NearWiFiViewHolder();

				convertView = nearWiFiView;
				convertView.setTag(nearWiFiViewHolder);
				break;
			case TYPE_WIFI_INFO:
				inflater = LayoutInflater.from(context);
				holderView = inflater.inflate(R.layout.act_deep_unlock_list_item, null);
				holder = new ViewHolder();
				holder.tv_ap_ssid = (TextView) holderView.findViewById(R.id.tv_ap_ssid);
				holder.signal = (ImageView) holderView.findViewById(R.id.signal);
				holder.connected = (ImageView) holderView.findViewById(R.id.connected);
				holder.cb_ap_check = (CheckBox) holderView.findViewById(R.id.cb_ap_check);
				holder.tv_state_safe = (TextView) holderView.findViewById(R.id.tv_state_safe);

				convertView = holderView;
				convertView.setTag(holder);
				break;
		}
		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		if (convertView == null) {
			convertView = initGetView(parent, type);
		}else {
			switch(type){
				case TYPE_FREE:
					freeWiFiViewHolder = (FreeWiFiViewHolder) convertView.getTag();
					break;
				case TYPE_NEAR:
					if(!(convertView.getTag() instanceof NearWiFiViewHolder))
						convertView = initGetView(parent, type);
					nearWiFiViewHolder = (NearWiFiViewHolder) convertView.getTag();
					break;
				case TYPE_WIFI_INFO:
					if(!(convertView.getTag() instanceof ViewHolder))
						convertView = initGetView(parent, type);
					holder = (ViewHolder) convertView.getTag();
					break;
			}
		}

		switch(type){
			case TYPE_FREE:
				freeWiFiViewHolder.tv_freeWiFi.setText((knowPswCount + 1)+ "个可免费连接WiFi");
				break;
			case TYPE_NEAR:
				break;
			case TYPE_WIFI_INFO:
				ScanResult scanResult = list.get(position);
//			Log.v("WiFiAdapter","scanResult.SSID= " + scanResult.SSID);
				if(scanResult == null){
					System.out.println("getView null position= " + position);
					convertView = getView(position, convertView, parent);
					return convertView;
				}
				if(holder.tv_ap_ssid != null){
					holder.tv_ap_ssid.setText(scanResult.SSID);
				}
//			TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
				// 返回所有的SSID值
				wifiItemId = localWifiUtils.IsConfiguration("\"" + scanResult.SSID+ "\"");

				WifiInfo mWifiInfo = localWifiUtils.getConnectionInfo();
//			Log.v("WiFiAdapter","mWifiInfo.getMacAddress()= " + mWifiInfo.getMacAddress()); // 手机 mac
//			Log.v("WiFiAdapter","mWifiInfo.getIpAddress()= " + mWifiInfo.getIpAddress()); // Wifi ip
//			Log.v("WiFiAdapter","mWifiInfo.getBSSID()= " + mWifiInfo.getBSSID());   // 已连接wifi mac
//			Log.v("WiFiAdapter","scanResult.BSSID= " +scanResult.BSSID); // 扫描到的wifi mac

				// 得到连接的名称SSID
//			String SSIDs = mWifiInfo.getSSID();
//
//			if(localWifiUtils.isWifiConnected(context) && SSIDs.equals("\"" + scanResult.SSID + "\"")){
//
//				// 删除已连接上的wifi
//				list.remove(position);
//
//				if(knowPswCount >= 0 && position < (knowPswCount+2)){
//					knowPswCount--;
//				}
//				notifyDataSetChanged();
//			}else {
//				holder.connected.setVisibility(View.GONE);
//			}


//			imageView.setImageState((accessPoint.security != AccessPoint.SECURITY_NONE) ? AccessPoint.STATE_SECURED : AccessPoint.STATE_NONE, true);
				holder.signal.setImageLevel(scanResult.level);
//			imageView.setImageState((scanResult.security != AccessPoint.SECURITY_NONE) ? scanResult.STATE_SECURED : scanResult.STATE_NONE, true);
				// 判断信号强度，显示对应的指示图标

				if(knowPswCount <= -1 || position > (knowPswCount+2)){//非免费的
					holder.tv_state_safe.setVisibility(View.GONE);
					if (Math.abs(scanResult.level) > 100) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ic_wifi_lock_signal_1));
					} else if (Math.abs(scanResult.level) > 80) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ic_wifi_lock_signal_1));
					} else if (Math.abs(scanResult.level) > 70) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ic_wifi_lock_normal_2));
					} else if (Math.abs(scanResult.level) > 60) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ic_wifi_lock_normal_2));
					} else if (Math.abs(scanResult.level) > 50) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ic_wifi_lock_normal_3));
					} else {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ic_wifi_lock_normal_3));
					}
				} else{//免费的
					holder.tv_state_safe.setVisibility(View.VISIBLE);
					if (Math.abs(scanResult.level) > 100) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ico_wifi_blue_1));
					} else if (Math.abs(scanResult.level) > 80) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ico_wifi_blue_1));
					} else if (Math.abs(scanResult.level) > 70) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ico_wifi_blue_2));
					} else if (Math.abs(scanResult.level) > 60) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ico_wifi_blue_2));
					} else if (Math.abs(scanResult.level) > 50) {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ico_wifi_blue_3));
					} else {
						holder.signal.setImageDrawable(context.getResources().getDrawable(
								R.drawable.ico_wifi_blue_3));
					}
				}

				// 是否连接
			/*if(updateView && scanResult.SSID.equals(CurrentConnetSSID)){
				holder.connected.setVisibility(View.VISIBLE);
				updateView = !updateView;
				Log.v("WifiUtils","updateView-------------");
			}*/

				if(position == (list.size() - 1)){

				}
				break;
		}




		return convertView;
	}

	public void updateView(String wifiSSID) {
		CurrentConnetSSID = wifiSSID;
		updateView = true;
		Log.v("WifiUtils","wifiSSID= " + wifiSSID);
		this.notifyDataSetChanged();
		//得到第一个可显示控件的位置，
		/*int visiblePosition = listView.getFirstVisiblePosition();
		//只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
		if (itemIndex - visiblePosition >= 0) {
			//得到要更新的item的view
			View view = listView.getChildAt(itemIndex - visiblePosition);
			//从view中取得holder
			ViewHolder holder = (ViewHolder) view.getTag();
//			HashMap<String, Object> item = data.get(itemIndex);

			holder.signal = (ImageView) view.findViewById(R.id.signal);
//			updateData(itemIndex, holder, item);
		}		*/
	}

	private static final int TYPE_COUNT = 3;// item类型的总数
	private static final int TYPE_FREE = 0;// 免费的wifi
	private static final int TYPE_NEAR = 1;// 附近的wifi
	private static final int TYPE_WIFI_INFO = 2;// wifi的信息

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if(knowPswCount > -1 && position == 0){
			return TYPE_FREE; // 免费的wifi
		} else if(position == 0 || (position == (knowPswCount+2) && knowPswCount != -1)){ // 第一个item添加了免费的wifi的布局
			return TYPE_NEAR;// 附近的wifi
		} else {
			return TYPE_WIFI_INFO;
		}
	}

	class NearWiFiViewHolder {
		ImageView signal;
		TextView tv_ap_ssid;
		ImageView connected;
		CheckBox cb_ap_check;
	}

	class ViewHolder {
		ImageView signal;
		TextView tv_ap_ssid;
		ImageView connected;
		CheckBox cb_ap_check;
		TextView tv_state_safe;
	}

	class FreeWiFiViewHolder {
		TextView tv_freeWiFi;
	}
}
