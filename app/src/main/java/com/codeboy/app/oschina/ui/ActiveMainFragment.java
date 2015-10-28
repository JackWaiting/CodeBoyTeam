/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.adapter.TabsFragmentPagerAdapter;

/**
 * 类名 ActiveMainFragment.java</br>
 * 创建日期 2014年4月28日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月28日 上午1:05:15</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 我的空间
 */
public class ActiveMainFragment extends BaseMainFragment{
	
	public static ActiveMainFragment newInstance() {
		return new ActiveMainFragment();
	}

	@Override
	protected void onSetupTabAdapter(TabsFragmentPagerAdapter adapter) {
		adapter.addTab(getString(R.string.frame_title_active_lastest), 
				"active_lastest", ActiveLastestFragment.class, null);
		
		adapter.addTab(getString(R.string.frame_title_active_atme),
				"active_atme", ActiveAtMeFragment.class, null);
		
		adapter.addTab(getString(R.string.frame_title_active_comment),
				"active_comment", ActiveCommentFragment.class, null);
		
		adapter.addTab(getString(R.string.frame_title_active_myself),
				"active_myself", ActiveMySelfFragment.class, null);
		
		adapter.addTab(getString(R.string.frame_title_active_message),
				"active_message", ActiveMessageFragment.class, null);
	}
}