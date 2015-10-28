/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.MessageData;

import net.oschina.app.adapter.ListViewTweetAdapter;
import net.oschina.app.bean.Tweet;
import net.oschina.app.bean.TweetList;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;

/**
 * 类名 TweetMyFragment.java</br>
 * 创建日期 2014年4月24日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月24日 下午10:20:27</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 动弹-我的动弹
 */
public class TweetMyFragment extends BaseSwipeRefreshFragment<Tweet, TweetList>{

	@Override
	public BaseAdapter getAdapter(List<Tweet> list) {
		return new ListViewTweetAdapter(getActivity(), list, R.layout.tweet_listitem);
	}

	@Override
	protected MessageData<TweetList> asyncLoadList(int page, boolean reflash) {
		MessageData<TweetList> msg = null;
		//用户ID,需要登录获取
		int uid = mApplication.getLoginUid();
		//等于0表示还没登录
		if(uid == 0) {
			msg = new MessageData<TweetList>(MessageData.MESSAGE_STATE_EMPTY);
			return msg;
		} else {
			try {
				TweetList list = mApplication.getTweetList(uid, page, reflash);
				msg = new MessageData<TweetList>(list);
			} catch (AppException e) {
				e.printStackTrace();
				msg = new MessageData<TweetList>(e);
			}
		}
		return msg;
	}
	
	@Override
	public void onItemClick(int position, Tweet data) {
		Tweet tweet = getData(position);
		// 跳转到动弹详情&评论页面
		UIHelper.showTweetDetail(getActivity(), tweet.getId());
	}
}