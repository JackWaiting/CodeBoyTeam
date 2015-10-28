/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.adapter;

import android.os.Bundle;

/**
 * 类名 TabInfo.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 上午10:57:35</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public final class TabInfo {

	public final String tag;
    public final Class<?> clss;
    public final Bundle args;
    public final String title;

    public TabInfo(String _title, String _tag, Class<?> _class, Bundle _args) {
    	title = _title;
        tag = _tag;
        clss = _class;
        args = _args;
    }
}
