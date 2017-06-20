package com.zhizun.zhizunwifi.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.zhizun.zhizunwifi.R;

public class SearchNearInfoAdapter extends BaseAdapter {
	
	LayoutInflater inflater;
	List<PoiInfo> nearList;
	private Context context;
	ViewHolder holder;
	ListView listView;
	int currentSelectPosition = -1;
	
	public SearchNearInfoAdapter(Context context, List<PoiInfo> list, ListView listView) {
		this.inflater = LayoutInflater.from(context);
		this.nearList = list;
		this.context = context;
		this.listView = listView;
	}

	@Override
	public int getCount() {
		return nearList.size();
	}

	@Override
	public Object getItem(int position) {

		return nearList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_search_near, null);
			holder = new ViewHolder();
			holder.ll_layout = (LinearLayout) convertView.findViewById(R.id.ll_layout);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.cbx_address = (CheckBox) convertView.findViewById(R.id.cbx_address);
			holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			
			holder.cbx_address.setTag(position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.ll_layout.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				currentSelectPosition = position;
//				int visibleFirstPosi = listView.getFirstVisiblePosition();
//				int visibleLastPosi = listView.getLastVisiblePosition();
//				for(int i = visibleFirstPosi; i < visibleLastPosi+1; i++){
//					View view = listView.getChildAt(i);
//					if (view != null) {
//						CheckBox cbx = (CheckBox) view.findViewById(R.id.cbx_address);
//						if (cbx != null && i != position) { 
//							cbx.setChecked(false);
//						}else if (cbx != null && i == position) { 
//							cbx.setChecked(true);
//						}
//					}
//				}
//			}
//		});
		if(currentSelectPosition == position){
			holder.cbx_address.setChecked(true);
		}else {
			holder.cbx_address.setChecked(false);
		}
		PoiInfo poiInfo = nearList.get(position);
		holder.tv_name.setText(poiInfo.name);
		holder.tv_address.setText(poiInfo.address);
		return convertView;
	}
	
	
	class ViewHolder {
		LinearLayout ll_layout;
		CheckBox cbx_address;
		TextView tv_name;
		TextView tv_address;
	}
	
	public void updateCheckView(int postion){
		currentSelectPosition = postion;
		
//		int childCount = listView.getChildCount();
//		int count = listView.getCount();
//		int visibleFirstPosi = listView.getFirstVisiblePosition();
//		int visibleLastPosi = listView.getLastVisiblePosition();
//		int fetch;
//		if (visibleLastPosi >= childCount) {
//			fetch = childCount - 1
//					- (visibleLastPosi - postion);
//		} else {
//			fetch = postion;
//		}
//		
//		for(int i = 0; i < fetch+1; i++){
//			View view = listView.getChildAt(i);
//			if (view != null) {
//				CheckBox cbx = (CheckBox) view.findViewById(R.id.cbx_address);
//				if (cbx != null && i != postion) { 
//					cbx.setChecked(false);
//				}else if (cbx != null && i == postion) { 
//					cbx.setChecked(true);
//				}
//			}
//		}
		
	}
}
