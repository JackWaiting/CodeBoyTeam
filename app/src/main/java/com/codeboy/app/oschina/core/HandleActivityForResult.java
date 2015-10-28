/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.core;

import android.content.Intent;

/**
 * 类名 HandleActivityForResult.java</br>
 * 创建日期 2014年5月10日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月10日 下午12:59:04</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 用于调用方法startActivityForResult
 */
public interface HandleActivityForResult {

	public void startActivityForResult(Intent intent, int requestCode);
}
