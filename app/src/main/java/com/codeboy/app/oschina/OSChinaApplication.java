/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import net.oschina.app.core.AppContext;

import com.codeboy.app.library.util.L;
import com.umeng.analytics.MobclickAgent;

/**
 * 类名 OSChinaApplication.java</br>
 * 创建日期 2014年4月22日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月22日 上午12:33:15</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 application
 */
public class OSChinaApplication extends AppContext{

	@Override
	public void onCreate() {
		MobclickAgent.openActivityDurationTrack(false);
		//设置是否为调试模式
		L.setDebug(BuildConfig.DEBUG);
		super.onCreate();
	}
}
