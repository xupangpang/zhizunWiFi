package com.zhizun.zhizunwifi.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.WifiUtils;

public class CrackAdapter extends BaseAdapter {

	// 状态码
	int wifiItemId;
	LayoutInflater inflater;
	List<ScanResult> list;
	private Context context;
	ViewHolder holder;
	//	private ListView listView;
	private int CurrentConnetPos;
	private String CurrentConnetSSID;

	private boolean updateView = false;

	public CrackAdapter(Context context, List<ScanResult> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
//		this.listView = listView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		String ss = list.get(position).SSID;
		return ss;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.act_deep_unlock_list_item, null);
			holder = new ViewHolder();
			holder.tv_ap_ssid = (TextView) convertView.findViewById(R.id.tv_ap_ssid);
			holder.signal = (ImageView) convertView.findViewById(R.id.signal);
			holder.connected = (ImageView) convertView.findViewById(R.id.connected);
			holder.cb_ap_check = (CheckBox) convertView.findViewById(R.id.cb_ap_check);
			holder.tv_crackStatus = (TextView) convertView.findViewById(R.id.tv_crackStatus);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cb_ap_check.setVisibility(View.GONE);

		ScanResult scanResult = list.get(position);
		Log.v("WiFiAdapter","scanResult.SSID= " + scanResult.SSID);

		holder.tv_ap_ssid.setText(scanResult.SSID);
//		TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);

		holder.connected.setVisibility(View.GONE);
		holder.tv_crackStatus.setVisibility(View.VISIBLE);
		if(position != 0){
			holder.tv_crackStatus.setTextColor(Color.parseColor("#FF8A21"));
			holder.tv_crackStatus.setText("待挖掘");
		}

		// 返回值 为 -1 2 3 返回的状态值 -1表示有密码 2表示无密码 3表示有密码 已保存过密码
//		if (scanResult.SSID.equals(SSIDs)) {
//			signalStrenth.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.ka));
//		} else {
//			signalStrenth.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.guan));
//		}


//		imageView.setImageState((accessPoint.security != AccessPoint.SECURITY_NONE) ? AccessPoint.STATE_SECURED : AccessPoint.STATE_NONE, true);
		holder.signal.setImageLevel(scanResult.level);
//		imageView.setImageState((scanResult.security != AccessPoint.SECURITY_NONE) ? scanResult.STATE_SECURED : scanResult.STATE_NONE, true);
		// 判断信号强度，显示对应的指示图标
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

		// 是否连接
		/*if(updateView && scanResult.SSID.equals(CurrentConnetSSID)){
			holder.connected.setVisibility(View.VISIBLE);
			updateView = !updateView;
			Log.v("WifiUtils","updateView-------------");
		}*/

		if(position == (list.size() - 1)){

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

	class ViewHolder {
		ImageView signal;
		TextView tv_ap_ssid;
		ImageView connected;
		CheckBox cb_ap_check;
		TextView tv_crackStatus;
	}
}
