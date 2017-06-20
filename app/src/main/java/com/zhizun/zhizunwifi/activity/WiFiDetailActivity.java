package com.zhizun.zhizunwifi.activity;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.zhizun.zhizunwifi.bean.FreeWifi;
import com.zhizun.zhizunwifi.dialog.AlertDialog;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.WifiUtils;

public class WiFiDetailActivity extends BaseActivity implements OnGetPoiSearchResultListener{

	@InjectView(id = R.id.headerTitle)
	TextView headerTitle;
	@InjectView(id = R.id.headerLeft, click="click")
	TextView headerLeft;
	@InjectView(id = R.id.wifi_signal_strength)
	TextView wifi_signal_strength;
	@InjectView(id = R.id.wifi_connect_times)
	TextView wifi_connect_times;
	@InjectView(id = R.id.wifi_security)
	TextView wifi_security;
	@InjectView(id = R.id.wifi_mac_address)
	TextView wifi_mac_address;
	@InjectView(id = R.id.btn_connect, click="click")
	Button btn_connect;
	@InjectView(id = R.id.btn_disconnect, click="click")
	Button btn_disconnect;
	@InjectView(id = R.id.add_to_blacklist, click="click")
	Button add_to_blacklist;

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private Marker mMarkerB;
	private Marker mMarkerC;
	private Marker mMarkerD;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bdA;
	BitmapDescriptor mCurrentMarker;
	private static final int accuracyCircleFillColor = 0xAAFCEBB4;
	private static final int accuracyCircleStrokeColor = 0xAAFCEBB4;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	boolean isFirstLoc = true; // 是否首次定位
	boolean isFirstGetData = true; // 是否首次定位
	private FreeWifi currentConnectWiFi;
	private WifiUtils localWifiUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.access_point_detail_fragment_layout);
		localWifiUtils = new WifiUtils(this);
		WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifi_service.getConnectionInfo();

		currentConnectWiFi = (FreeWifi) getIntent().getSerializableExtra("WiFiInfo");
		if(("\"" + currentConnectWiFi.getWifi_name()+ "\"").equals(wifiInfo.getSSID())){
			btn_connect.setVisibility(View.GONE);
			btn_disconnect.setVisibility(View.VISIBLE);
		}
		headerTitle.setText(currentConnectWiFi.getWifi_name());
		// 得到的值是一个0到-100的区间值，是一个int型数据，其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线。
		int rssi = currentConnectWiFi.getRSSI();
		if (Math.abs(rssi) <= 50) {
			rssi = 100;
		}else if (Math.abs(rssi) > 50 /*&& Math.abs(rssi) <= 70*/) {
//			rssi = rssi + 100;
			rssi = (rssi + 100) * 2;
		}
		wifi_signal_strength.setText(rssi +"%");
		wifi_mac_address.setText(currentConnectWiFi.getWifi_mac_address());

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {

				return true;
			}
		});
	}

	public void initOverlay(List<FreeWifi> wifiList, double latitude,
							double longitude) {

		// 自定义当前定位的图标
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.map_marker_mine_location);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker, accuracyCircleFillColor,
				accuracyCircleStrokeColor));

		/*View viewA = LayoutInflater.from(this).inflate(R.layout.view_map_mark, null);
		bdA = BitmapDescriptorFactory.fromView(viewA);

		for(Wifi wifi : wifiList){
//			wifi.getLatitude(); // 纬度
//			wifi.getLongitude(); // 经度

//			LatLng llA = new LatLng(Double.parseDouble(wifi.getLatitude()), Double.parseDouble(wifi.getLongitude()));
			LatLng llA = new LatLng(latitude - 0.020354, longitude - 0.031045);
			MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA).anchor(0.5f, 1.0f)
					.zIndex(9).draggable(false); // draggable: 设置 marker 是否允许拖拽，默认不可拖拽
			if (true) {
				// 掉下动画
//				ooA.animateType(MarkerAnimateType.drop);
			}
			mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			mMarkerA.setTitle(wifi.getWifi_name());

		}
		LatLng llA = new LatLng(latitude - 0.020354, longitude - 0.031045);
		MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA).anchor(0.5f, 1.0f)
				.zIndex(9).draggable(false); // draggable: 设置 marker 是否允许拖拽，默认不可拖拽
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		mMarkerA.setTitle(wifiList.get(0).getWifi_name());*/

		if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = new LatLng(latitude, longitude);
			MapStatus.Builder builder = new MapStatus.Builder();
			builder.target(ll).zoom(18.0f);
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory
					.newMapStatus(builder.build()));
		}

	}


	public void click(View v){
		Intent intent;
		switch (v.getId()) {
			case R.id.headerLeft:
				finish();
				break;

			case R.id.btn_connect:
				break;
			case R.id.btn_disconnect:
				localWifiUtils.disconnectWifi(null);
				break;
			case R.id.add_to_blacklist:
				showDialog("是否加入黑名单？");
				break;

		}
	}

	AlertDialog alertDialog;
	private void showDialog(String title) {
		alertDialog = new AlertDialog(WiFiDetailActivity.this, null).builder()
				.setTitle(title)
				.setMsg("加入黑名单后，该WiFi将不会在了表上显示~\n您可在WiFi设置里回复。")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("连接连接连接连接连接连接连接连接");
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
					}
				});
		alertDialog.show();
	}


	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
//		bdA.recycle();
//		bdB.recycle();
//		bdC.recycle();
//		bdD.recycle();
//		bd.recycle();
//		bdGround.recycle();

		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView = null;
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			// 定位完成，去查询后台数据，查询完后台数据，添加地图覆盖物，刷新地图
//            if(isFirstGetData){
//            	isFirstGetData = false;
////            	getPsw(location.getLatitude(),location.getLongitude());
//            	 initOverlay(null, location.getLatitude(), location.getLatitude());
//            }

			if (isFirstLoc) {
				searchNeayBy(location);

				isFirstLoc = false;
				// 自定义当前定位的图标
				mCurrentMarker = BitmapDescriptorFactory
						.fromResource(R.drawable.map_marker_mine_location);
				mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, false, mCurrentMarker));

				LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 搜索周边地理位置
	 */
	private void searchNeayBy(BDLocation location) {
		PoiSearch mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		int radius = 1000;
		PoiNearbySearchOption option = new PoiNearbySearchOption();
//		option.keyword("店铺");
		option.keyword("大厦");
		option.sortType(PoiSortType.distance_from_near_to_far);
		option.location(new LatLng(location.getLatitude(), location.getLongitude()));
		if (radius != 0) {
			option.radius(radius);
		} else {
			option.radius(1000);
		}

		option.pageCapacity(20);
		mPoiSearch.searchNearby(option);

	}



	@Override
	public void onGetPoiDetailResult(PoiDetailResult poiResult) {

	}

	private List<PoiInfo> nearList = new ArrayList<PoiInfo>();
	/*
     * 接受周边地理位置结果
     * @param poiResult
     */
	@Override
	public void onGetPoiResult(PoiResult poiResult) {
		if (poiResult != null) {
			if (poiResult.getAllPoi()!=null&&poiResult.getAllPoi().size()>0){
				nearList.addAll(poiResult.getAllPoi());
				if (nearList != null && nearList.size() > 0) {
					for (int i = 0; i < nearList.size(); i++) {
						System.out.println("poi所在城市= " + nearList.get(i).city);
						System.out.println("poi名称= " + nearList.get(i).name);
						System.out.println("poi地址信息= " + nearList.get(i).address);
					}
				}
//	          Message msg = new Message();
//	          msg.what = 0;
//	          handler.sendMessage(msg);
			}

		}
	}

}
