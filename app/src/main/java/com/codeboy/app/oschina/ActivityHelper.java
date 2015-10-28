/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import android.app.Activity;
import android.os.Bundle;

/**
 * 类名 ActivityHelper.java</br>
 * 创建日期 2014年4月22日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月22日 上午12:45:23</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public class ActivityHelper implements ActivityHelperInterface{
	
	Activity mActivity;
	
	public ActivityHelper(Activity activity) {
		mActivity = activity;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		
	}
	
	public void onAttachedToWindow() {
		
	}
	
	public void onDetachedFromWindow() {
		
	}

	@Override
	public OSChinaApplication getOsChinaApplication() {
		return (OSChinaApplication) mActivity.getApplication();
	}

	@Override
	public Activity getActivity() {
		return mActivity;
	}
}
