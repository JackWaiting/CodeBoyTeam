/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.codeboy.app.library.util.L;
import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.core.Contanst;
import com.codeboy.app.oschina.modul.MessageData;
import com.codeboy.app.oschina.modul.OnNotifyUpdateListener;
import com.codeboy.app.oschina.modul.OnStatusListener;

import net.oschina.app.adapter.ListViewMessageDetailAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.core.AppException;

/**
 * 类名 MessageDetailFragment.java</br>
 * 创建日期 2014年5月3日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月3日 下午11:42:55</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 留言详情
 */
public class MessageDetailFragment extends BaseSwipeRefreshFragment<Comment, CommentList> 
	implements OnNotifyUpdateListener {

	public static MessageDetailFragment newInstance(int friendId) {
		MessageDetailFragment fragment = new MessageDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Contanst.MESSAGE_FRIEND_ID_KEY, friendId);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private int friendId;
	
	private OnStatusListener mOnStatusListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnStatusListener) {
			mOnStatusListener = (OnStatusListener) activity;
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mOnStatusListener = null;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if(args != null) {
			friendId = args.getInt(Contanst.MESSAGE_FRIEND_ID_KEY);
		}
		if(L.Debug) {
			L.d("留言:" + friendId);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public BaseAdapter getAdapter(List<Comment> list) {
		return new ListViewMessageDetailAdapter(getActivity(), list, 
				R.layout.message_detail_listitem);
	}

	@Override
	protected MessageData<CommentList> asyncLoadList(int page, boolean reflash) {
		MessageData<CommentList> msg = null;
		try {
			CommentList list = mApplication.getCommentList(
					CommentList.CATALOG_MESSAGE, friendId, page, reflash);
			msg = new MessageData<CommentList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<CommentList>(e);
		}
		return msg;
	}

	@Override
	public void onNotifyUpdate() {
		update();
	}

	@Override
	public void onRefreshLoadingStatus() {
		if(mOnStatusListener != null) {
			mOnStatusListener.onStatus(OnStatusListener.STATUS_LOADING);
		}
	}

	@Override
	public void onRefreshLoadedStatus() {
		if(mOnStatusListener != null) {
			mOnStatusListener.onStatus(OnStatusListener.STATUS_LOADED);
		}
	}
	
	
}
