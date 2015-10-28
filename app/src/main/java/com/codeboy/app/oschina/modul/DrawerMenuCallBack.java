/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.modul;

/**
 * 类名 DrawerMenuCallBack.java</br>
 * 创建日期 2014年4月27日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月27日 下午1:10:39</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 菜单事件回调
 */
public interface DrawerMenuCallBack {

	/** 点击资讯*/
	public void onClickNews();
	/** 点击问答*/
	public void onClickQuestionAsk();
	/** 点击动弹*/
	public void onClickTweet();
	/** 点击开源软件库*/
	public void onClickSoftware();
	/** 点击我的空间*/
	public void onClickAvtive();
}
