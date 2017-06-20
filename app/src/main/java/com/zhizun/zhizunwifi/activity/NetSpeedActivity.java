package com.zhizun.zhizunwifi.activity;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhizun.zhizunwifi.bean.NetWorkSpeedInfo;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.ReadFile;
import com.zhizun.zhizunwifi.view.RoundProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NetSpeedActivity extends BaseActivity {
	private String url = "http://dp.anzhuo.com/zzwifi/zzwifi.apk ";

	byte[] imageData = null;
	NetWorkSpeedInfo netWorkSpeedInfo = null;
	private final int UPDATE_SPEED = 1;// 进行中
	private final int UPDATE_DNOE = 0;// 完成下载
	private final int INIT_DNOE = -1;// 初始化状态

	private TextView tv_network_speed;
	private Button startButton;
	private RoundProgressBar roundProgressBar;
	//private TextView netSpeedDes;

	private int testTime = 10 *1000; // 测试时间

	ReadFile mReadFile;
	boolean isStart = false;
	private ImageView headerLeft;
	private TextView headerTitle;
	private TextView wifi_name;
	private String Delay;
	private TextView wifi_speed_test_ys,wifi_speed_test_xz,wifi_speed_test_sc;
	private LinearLayout wifi_speed_finsh;
	private String speedValue;
    private int speedK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifitools_speed_test);
		initView();

		netWorkSpeedInfo = new NetWorkSpeedInfo();
		handler.sendEmptyMessage(INIT_DNOE);

		wifi_name.setText(this.getIntent().getStringExtra("wifiname"));

		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isStart = !isStart;
				if(!isStart){
					if (mReadFile != null)
					mReadFile.isStop = true;

					startButton.setText("停止测速");
				}else{
					startButton.setText("开始测速");
					startTest();

				}
			}
		});
	}

	private void startTest() {
		final long clickTime = System.currentTimeMillis();

		new Thread() {
			@Override
			public void run() {
				Log.i("开始", "**********开始  ReadFile*******");
				mReadFile = new ReadFile();
				imageData = mReadFile.getFileFromUrl(url, netWorkSpeedInfo, testTime);
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				int i = 0;
				while (isStart && (System.currentTimeMillis() - clickTime) <= testTime) { // 测试10秒
					i++;
					Log.i("NetSpeedActivity","测试第" + i + "次");
					handler.sendEmptyMessage(UPDATE_SPEED);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Log.i("NetSpeedActivity","完成测试");
				handler.sendEmptyMessage(UPDATE_DNOE);

			}
		}.start();
	}

	private void initView() {
		headerLeft = (ImageView)findViewById(R.id.headerLeft);
		headerLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		headerTitle = (TextView)findViewById(R.id.headerTitle);
		headerTitle.setText("测速");
		tv_network_speed = (TextView) findViewById(R.id.tv_network_speed);
		startButton = (Button) findViewById(R.id.startButton);
		roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
		wifi_name = (TextView)findViewById(R.id.wifi_name);

		wifi_speed_test_ys = (TextView)findViewById(R.id.wifi_speed_test_ys);
		wifi_speed_test_xz = (TextView)findViewById(R.id.wifi_speed_test_xz);
		wifi_speed_test_sc = (TextView)findViewById(R.id.wifi_speed_test_sc);

		wifi_speed_finsh = (LinearLayout)findViewById(R.id.wifi_speed_finsh);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int value = msg.what;
			switch (value) {
				case UPDATE_SPEED:

					startButton.setText("停止测速");
					roundProgressBar.setCurNetSpeedValue(netWorkSpeedInfo.speed);
					speedK = (int)netWorkSpeedInfo.speed / 1024;
					speedValue = speedK < 1024 ? speedK +" Kb/s" : (speedK / 1024) +" Mb/s";
					tv_network_speed.setText(speedValue);

					break;

				case UPDATE_DNOE:

				    startButton.setText("重新测速");
					isStart = false;
					roundProgressBar.setCurNetSpeedValue(0);

					try {
						Delay	= getDelay();
					} catch (IOException e) {
						Log.e("@@@@@@@@@@",e.getMessage());
						e.printStackTrace();
					}


					//完成后显示布局
					wifi_speed_finsh.setVisibility(View.VISIBLE);
					wifi_speed_test_ys.setText(Delay);
					wifi_speed_test_xz.setText(speedValue);
					int speedXZ = speedK / 2;
					String speedValuexz = speedXZ < 1024 ? speedXZ +" Kb/s" : (speedXZ / 1024) +" Mb/s";
					wifi_speed_test_sc.setText(speedValuexz);

					break;
				case INIT_DNOE:
				startButton.setText("开始测速");
					isStart = false;

			}
		}
	};



	private String getDelay() throws IOException {
		String lost = new String();
		String delay = new String();
		Process p = Runtime.getRuntime().exec("ping -c 4 " + "www.baidu.com");
		BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String str = new String();
		while((str=buf.readLine())!=null){
			/*if(str.contains("packet loss")){
				int i= str.indexOf("received");
				int j= str.indexOf("%");
				Log.e("丢包率:",str.substring(i+10, j+1));
//                  System.out.println("丢包率:"+str.substring(j-3, j+1));
				lost = str.substring(i+10, j+1);
			}*/
			if(str.contains("avg")){
				int i=str.indexOf("/", 20);
				int j=str.indexOf(".", i);
				Log.e("延迟:",str.substring(i+1, j));
				delay =str.substring(i+1, j);
			}
		}
		return  delay = delay+"ms";
	}
}
