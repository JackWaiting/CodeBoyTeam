/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import com.codeboy.app.oschina.core.HandleActivityForResult;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

/**
 * 类名 BaseActivity.java</br>
 * 创建日期 2014年4月20日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月20日 下午11:38:59</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public class BaseActivity extends Activity implements ActivityHelperInterface, HandleActivityForResult{

	ActivityHelper mHelper = new ActivityHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getSimpleName());
	    MobclickAgent.onResume(this);   
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		mHelper.onAttachedToWindow();
	}
	
	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mHelper.onDetachedFromWindow();
	}
	
	@Override
	public OSChinaApplication getOsChinaApplication() {
		return mHelper.getOsChinaApplication();
	}

	@Override
	public Activity getActivity() {
		return mHelper.getActivity();
	}
}
