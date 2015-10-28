/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.MessageData;

import net.oschina.app.adapter.ListViewFriendAdapter;
import net.oschina.app.bean.Friend;
import net.oschina.app.bean.FriendList;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;

/**
 * 类名 FriendsFansFragment.java</br>
 * 创建日期 2014年5月9日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月9日 下午11:41:04</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 我的好友-粉丝
 */
public class FriendsFansFragment extends BaseSwipeRefreshFragment<Friend, FriendList>{

	@Override
	public BaseAdapter getAdapter(List<Friend> list) {
		return new ListViewFriendAdapter(getActivity(), list, R.layout.friend_listitem);
	}

	@Override
	protected MessageData<FriendList> asyncLoadList(int page, boolean reflash) {
		MessageData<FriendList> msg = null;
		try {
			FriendList list = mApplication.getFriendList(
					FriendList.TYPE_FANS, page, reflash);
			msg = new MessageData<FriendList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<FriendList>(e);
		}
		return msg;
	}
	
	@Override
	public void onItemClick(int position, Friend friend) {
		//跳转
		UIHelper.showUserCenter(getActivity(), friend.getUserid(), friend.getName());
	}

}
