/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.modul;

/**
 * 类名 OnStatusListener.java</br>
 * 创建日期 2014年5月3日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月3日 下午1:14:26</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 回调加载状态
 */
public interface OnStatusListener {
	
	public final static int STATUS_NONE = 0x0;
	public final static int STATUS_LOADING = 0x01;
	public final static int STATUS_LOADED = 0x11;

	public void onStatus(int status);
}
