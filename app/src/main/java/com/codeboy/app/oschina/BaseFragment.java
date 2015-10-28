/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import com.codeboy.app.oschina.core.HandleActivityForResult;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 类名 BaseFragment.java</br>
 * 创建日期 2014年4月20日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月20日 下午11:47:28</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 碎片的基类
 */
public class BaseFragment extends Fragment implements HandleActivityForResult {

	
	public OSChinaApplication getOsChinaApplication() {
		return (OSChinaApplication) getActivity().getApplication();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getSimpleName());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
