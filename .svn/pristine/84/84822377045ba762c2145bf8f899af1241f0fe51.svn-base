package com.zhizun.zhizunwifi.activity;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;

public class AboutActivity extends BaseActivity implements OnItemClickListener {
	
	@InjectView(id = R.id.act_about_list)
	ListView act_about_list;
	private aboutAdapter adapter;
	String[] aboutStr = new String[]{"产品介绍","常见问题","用户服务协议"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about);
		initView();
		adapter = new aboutAdapter();
		act_about_list.setAdapter(adapter);
	}

	private void initView() {
		act_about_list.setOnItemClickListener(this);
		// 设置左侧自定义图标不可点击，出现系统自带的返回箭头，点击可finish
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 隐藏左侧自定义logo和icon
		getActionBar().setDisplayShowHomeEnabled(false);
		
		// 设置左侧自定义图标可点击
//		getActionBar().setHomeButtonEnabled(true);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
//			intent.setClass(AboutActivity.this, FeedBackActivity.class);
			break;
		case 1:
//			intent.setClass(AboutActivity.this, FeedBackActivity.class);
			break;
		case 2:
//			intent.setClass(AboutActivity.this, FeedBackActivity.class);
			break;
		}
//		startActivity(intent);
		
	}
	
	class aboutAdapter extends BaseAdapter {
		LayoutInflater inflater;
		private Context context;
		ViewHolder holder;
		
		public void aboutAdapter(Context context){
			this.context = context;
		}

		@Override
		public int getCount() {
			return aboutStr.length;
		}

		@Override
		public Object getItem(int position) {
			return aboutStr[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				inflater = LayoutInflater.from(AboutActivity.this);
				convertView = inflater.inflate(R.layout.act_about_item, null);
				holder = new ViewHolder();
				holder.tv_about = (TextView) convertView.findViewById(R.id.tv_about);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.tv_about.setText(aboutStr[position]);
			return convertView;
		}
		
	}
	
	class ViewHolder {
		TextView tv_about;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		}
		return false;
	}
	
}
