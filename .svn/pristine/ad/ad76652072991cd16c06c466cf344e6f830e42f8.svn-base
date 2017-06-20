/*
 * Copyright 2013 Storm Zhang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhizun.zhizunwifi.view;

import com.zhizun.zhizunwifi.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class CustomLoading extends FrameLayout {

	private int mCurrentWidth = 0;
	private int screenWidth;
	private int time = 45; //
	private int addWidth; // 
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
//				mClipDrawable.setLevel(mProgress);
				
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
				lp.width = mCurrentWidth;
				setLayoutParams(lp);
				requestLayout();
			}
		}
	};

	
	public CustomLoading(Context context) {
		this(context, null, 0);
	}

	public CustomLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		 
	     screenWidth = wm.getDefaultDisplay().getWidth();
	     addWidth = screenWidth/time;
//	     int height = wm.getDefaultDisplay().getHeight();
	     
		View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_load, null);
		addView(view);
	}

	public void stop() {
		mCurrentWidth = 0;
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
		lp.width = mCurrentWidth;
		setLayoutParams(lp);
		requestLayout();
		if(mRunThread != null){
			mRunThread.setRun(false);
			mRunThread = null;
		}
	}
	
	public void start() {
		if(mRunThread != null){
			if(mRunThread.isRunning)
				return;
			mRunThread.setRun(false);
			mRunThread = null;
		}
		mRunThread = new RunThread();
		Thread s = new Thread(mRunThread);
		s.start();
	}

	RunThread mRunThread;
	class RunThread implements Runnable{

		boolean isRunning = true;
		public void setRun(boolean isRunning){
			this.isRunning = isRunning;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRunning) {
				handler.sendEmptyMessage(0x123);
				if (mCurrentWidth > screenWidth) {
					mCurrentWidth = 0;
				}
				mCurrentWidth += addWidth;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
