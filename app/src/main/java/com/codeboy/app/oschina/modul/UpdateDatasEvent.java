/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.modul;

/**
 * 类名 UpdateDatasEvent.java</br>
 * 创建日期 2014年4月30日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月30日 下午8:19:49</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 更新数据事件
 */
public interface UpdateDatasEvent {
	/** 收到通知去更新*/
	public void onNotifyUpdate(Object obj);
}
