package com.zhizun.zhizunwifi.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.FreeWifi;
import com.zhizun.zhizunwifi.bean.WiFiMap;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;

import net.duohuo.dhroid.ioc.annotation.InjectView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class WiFiMapActivity extends BaseActivity {

	@InjectView(id = R.id.iv_ml_show)
	LinearLayout iv_ml_show;
	@InjectView(id = R.id.map_loading)
	RelativeLayout map_loading;
	@InjectView(id = R.id.map_location)
	Button map_location;

	protected CompositeSubscription mCompositeSubscription;
	private HttpService apiService;
	/*@InjectView(id = R.id.headerTitle)
	TextView headerTitle;
	@InjectView(id = R.id.headerLeft, click="click")
	ImageView headerLeft;*/
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private TextView headerTitle;
	private ImageView headerLeft;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
	private SeekBar alphaSeekBar = null;
//	private CheckBox animationBox = null;

	// 初始化全局 bitmap 信息，不用时及时 recycle
//	BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
	BitmapDescriptor bdA;
	BitmapDescriptor bdB;
	BitmapDescriptor bdC;
	BitmapDescriptor bdD;
	BitmapDescriptor bd;
	BitmapDescriptor bdGround;
	BitmapDescriptor mCurrentMarker;
	private static final int accuracyCircleFillColor = 0xAAFCEBB4;
	private static final int accuracyCircleStrokeColor = 0xAAFCEBB4;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	boolean isFirstLoc = true; // 是否首次定位
	boolean isFirstGetData = true; // 是否首次定位
	private List<FreeWifi> wifiList = new ArrayList<FreeWifi>();
	/** 数据库查询的wif信息集合 **/
	private List<FreeWifi> queryList;
	private ImageView map_refresh_btn;
    private List<Marker> markers = new ArrayList<>();
	private double Latitude;
	private double Longitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		//headerTitle.setText("附近的免费WiFi");

		apiService =  HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();

//		alphaSeekBar = (SeekBar) findViewById(R.id.alphaBar);
//		alphaSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
//		animationBox = (CheckBox) findViewById(R.id.animation);
		headerTitle = (TextView)findViewById(R.id.headerTitle);
		headerTitle.setText("附近的免费WiFi");

		headerLeft = (ImageView)findViewById(R.id.headerLeft);
		headerLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		map_refresh_btn = (ImageView)findViewById(R.id.map_refresh_btn);
		map_refresh_btn.setOnClickListener(this);
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
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


		map_location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//定位
				addMyLocation();
			}
		});


		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				View mInfoView = LayoutInflater.from(WiFiMapActivity.this).inflate(R.layout.map_marker_info_window_layout, null);
				TextView ap_name = (TextView) mInfoView.findViewById(R.id.ap_name);
				TextView ap_meta = (TextView) mInfoView.findViewById(R.id.ap_meta);
				TextView ap_tip = (TextView) mInfoView.findViewById(R.id.ap_tip);

					ap_name.setText(marker.getTitle());
					ap_meta.setText("已分享，免密码连接");
					ap_tip.setText("前往此处，就可在WiFi列表里免费连接了!");

				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory
						.fromView(mInfoView), ll, -100, null);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}

	public void initOverlay(double latitude,  double longitude) {
		// 自定义当前定位的图标
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.map_marker_mine_location);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker,
				accuracyCircleFillColor, accuracyCircleStrokeColor));

		View viewA = LayoutInflater.from(this).inflate(R.layout.view_map_mark, null);
		bdA = BitmapDescriptorFactory.fromView(viewA);


		getmapFreeWiFi(String.valueOf(longitude),String.valueOf(latitude));


		if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = new LatLng(latitude,longitude);
			MapStatus.Builder builder = new MapStatus.Builder();
			builder.target(ll).zoom(18.0f);
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
		}


		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				Toast.makeText(
						WiFiMapActivity.this,
						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
								+ marker.getPosition().longitude,
						Toast.LENGTH_LONG).show();
			}

			public void onMarkerDragStart(Marker marker) {
			}
		});

		// 加载完成
		iv_ml_show.setVisibility(View.GONE);
		map_loading.setVisibility(View.GONE);
	}

	/**
	 * 清除所有Overlay
	 *
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
        for (Marker marker : markers) {
            markers.clear();
        }
    }

	/**
	 * 重新添加Overlay
	 *
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
//		initOverlay();
	}

	private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
									  boolean fromUser) {
			// TODO Auto-generated method stub
			float alpha = ((float) seekBar.getProgress()) / 10;
			if (markers.size() > 0){
				for (Marker marker : markers) {
					marker.setAlpha(alpha);
				}
			}

        }

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

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

		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView = null;

		if (mCompositeSubscription != null){
			mCompositeSubscription.unsubscribe();
		}
	}

	/**
	 * 定位并添加标注
	 */
	private void addMyLocation() {
		/*LatLng ll = new LatLng(Latitude,Longitude);
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(ll).zoom(18.0f);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/

		LatLng loc = new LatLng(Latitude,Longitude);
		//MapStatusUpdate描述地图将要发生的变化
		//MapStatusUpdateFactory生成地图将要反生的变化
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
		mBaiduMap.animateMapStatus(msu);
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

			Latitude = location.getLatitude();
			Longitude = location.getLongitude();

			// 定位完成，去查询后台数据，查询完后台数据，添加地图覆盖物，刷新地图
			if(isFirstGetData){
				isFirstGetData = false;
				getPsw(location.getLatitude(),location.getLongitude());
			}
		}
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public void getPsw(final double latitude, final double longitude) {
		initOverlay(latitude, longitude);
	}

	public void getmapFreeWiFi(String lon,String lat) {
		Subscription subscription = apiService.getSsidFromPos(lat,lon).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<List<WiFiMap>>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
							}

							@Override
							public void onNext(BaseResultEntity<List<WiFiMap>> baseResultEntity) {
								basehideProgressDialog();
								if (baseResultEntity.ret == 200){
									if (baseResultEntity.data.size() > 0){
										for (WiFiMap stringStringHashMap : baseResultEntity.data) {
											LatLng lat = new LatLng(Double.valueOf(stringStringHashMap.lat), Double.valueOf(stringStringHashMap.lon));
											MarkerOptions ooB = new MarkerOptions().position(lat).icon(bdA).anchor(0.5f, 1.0f)
													.zIndex(9).draggable(false); // draggable: 设置 marker 是否允许拖拽，默认不可拖拽
											Marker  mMarker = (Marker) (mBaiduMap.addOverlay(ooB));
											mMarker.setTitle(stringStringHashMap.ssid);
											markers.add(mMarker);
										}
									}
								}

							}

							@Override
							public void onStart() {
								super.onStart();
							}
						}

				);
		mCompositeSubscription.add(subscription);

       /* //设置传递的参数
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("lon",lon);
        paramsMap.put("lat",lat);
        paramsMap.put("gtype","1");
        paramsMap.put("key","4f5a272d0e493f5e320903ff967fe434");


        HttpConnections register = new HttpConnections() {

            @Override
            public void requestSuccess(String json) {
                Log.e("WifiUtils","json:;   "+json);
                try{
                    JSONObject resul = new JSONObject(json);
                    JSONObject result = resul.getJSONObject("result");
                    JSONArray jsonarray = result.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length();i++){
                        JSONObject temp = jsonarray.getJSONObject(i);
                        HashMap<String,String> map = new HashMap<>();
                        map.put("name",temp.getString("name"));
                        map.put("baidu_lat",temp.getString("baidu_lat"));
                        map.put("baidu_lon",temp.getString("baidu_lon"));
                        mapwifi.add(map);
                    }

                    if (mapwifi.size() > 0){
                        for (HashMap<String, String> stringStringHashMap : mapwifi) {
                            LatLng lat = new LatLng(Double.valueOf(stringStringHashMap.get("baidu_lat")), Double.valueOf(stringStringHashMap.get("baidu_lon")));
                            MarkerOptions ooB = new MarkerOptions().position(lat).icon(bdA).anchor(0.5f, 1.0f)
                                    .zIndex(9).draggable(false); // draggable: 设置 marker 是否允许拖拽，默认不可拖拽
                            Marker  mMarker = (Marker) (mBaiduMap.addOverlay(ooB));
                            mMarker.setTitle(stringStringHashMap.get("name"));
                            markers.add(mMarker);
                        }
                    }

                }catch (Exception e){
                 e.printStackTrace();
                }
			}
			@Override
            public void requestFailure(HttpException arg0, String arg1) {
            }
        };
        register.DoPostRequest("http://apis.juhe.cn/wifi/local", paramsMap);*/
    }


}