package com.zhizun.zhizunwifi.bean;

import com.zhizun.zhizunwifi.utils.L;

public class BaseHttpInfo<T> {

	/**
	 * ok
	 * error
	 */
	public String result;

	/**
	 * 描述
	 */
	public String msg;

	/**
	 * 错误码
	 */
	public String err_code;

	public T data;

	public T getData(){
		if(result.equals("ok"))
			return data;
		return null;
	}

	public boolean isSuccess(){
		if(result.equals("ok")){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		L.d("tag", "result="+result+" , "+"msg="+msg+" , "+"err_code="+err_code+" , "+"data="+data);
		return super.toString();
	}
}
