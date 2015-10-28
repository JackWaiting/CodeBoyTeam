/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 类名 CBSwipeRefreshLayout.java</br>
 * 创建日期 2014年4月23日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月23日 下午11:58:00</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public class CBSwipeRefreshLayout extends SwipeRefreshLayout {

	public CBSwipeRefreshLayout(Context context) {
		super(context);
	}
	
	public CBSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(!isEnabled()) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(!isEnabled()) {
			return false;
		}
		return super.onTouchEvent(arg0);
	}
}