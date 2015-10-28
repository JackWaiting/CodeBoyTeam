/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import net.oschina.app.bean.FriendList;
import android.os.Bundle;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.adapter.TabsFragmentPagerAdapter;
import com.codeboy.app.oschina.core.Contanst;

/**
 * 类名 FriendsMainFragment.java</br>
 * 创建日期 2014年5月9日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月9日 下午11:38:41</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明  用户关注、粉丝界面
 */
public class FriendsMainFragment extends BaseMainFragment {

	public static FriendsMainFragment newInstance(int followers, int fans, int type) {
		Bundle args = new Bundle();
		args.putInt(Contanst.FRIENDS_FOLLOWERS, followers);
		args.putInt(Contanst.FRIENDS_FANS, fans);
		args.putInt(Contanst.FRIENDS_TYPE, type);
		FriendsMainFragment fragment = new FriendsMainFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	protected void onSetupTabAdapter(TabsFragmentPagerAdapter adapter) {
		int followers = 0;
		int fans = 0;
		int pos = 0;
		Bundle args = getArguments();
		if(args != null) {
			followers = args.getInt(Contanst.FRIENDS_FOLLOWERS, 0);
			fans = args.getInt(Contanst.FRIENDS_FANS, 0);
			int type = args.getInt(Contanst.FRIENDS_TYPE, 0);
			if(type == FriendList.TYPE_FANS) {
				pos = 1;
			}
		}
		
		String followerTitle = getString(R.string.friends_follower) + "(" + followers + ")";
		String fansTitle = getString(R.string.friends_fans) + "(" + fans + ")";
		
		adapter.addTab(followerTitle, "follower", FriendsFollowerFragment.class, null);
		adapter.addTab(fansTitle, "fans", FriendsFansFragment.class, null);
		
		mViewPager.setCurrentItem(pos);
	}
}