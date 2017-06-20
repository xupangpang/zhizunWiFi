package com.zhizun.zhizunwifi.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 */
public class ProgressBarWithText extends ProgressBar {

	private final Timer timer = new Timer(); 
	private TimerTask task; 
	
	Handler handler = new Handler() { 
	    @Override
	    public void handleMessage(Message msg) { 
	        // TODO Auto-generated method stub 
	        // Ҫ�������� 
	        super.handleMessage(msg); 
	    }
	};

	public ProgressBarWithText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ProgressBarWithText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		task = new TimerTask() { 
		    @Override
		    public void run() { 
		        // TODO Auto-generated method stub 
		        Message message = new Message(); 
		        message.what = 1; 
		        handler.sendMessage(message); 
		    } 
		};
		
		
		timer.schedule(task, 2000, 2000);
	}
	
	public ProgressBarWithText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	

	
	
	
	
	
}
