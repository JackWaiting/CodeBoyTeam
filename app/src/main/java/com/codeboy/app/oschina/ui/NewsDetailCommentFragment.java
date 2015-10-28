/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import net.oschina.app.adapter.ListViewCommentAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.core.AppException;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.core.Contanst;
import com.codeboy.app.oschina.modul.MessageData;
import com.codeboy.app.oschina.modul.UpdateDatasEvent;

/**
 * 类名 NewsDetailCommentFragment.java</br>
 * 创建日期 2014年4月29日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月29日 下午10:47:59</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 资讯详细界面-评论
 */
public class NewsDetailCommentFragment extends BaseSwipeRefreshFragment<Comment, CommentList>
	implements UpdateDatasEvent {

	public static NewsDetailCommentFragment newInstance(int newsid) {
		NewsDetailCommentFragment fragment = new NewsDetailCommentFragment();
		Bundle args = new Bundle();
		args.putInt(Contanst.NEWS_ID_KEY, newsid);
		fragment.setArguments(args);
		return fragment;
	}
	
	private int mNewsId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle argus = getArguments();
		if(argus != null) {
			mNewsId = argus.getInt(Contanst.NEWS_ID_KEY);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public BaseAdapter getAdapter(List<Comment> list) {
		return new ListViewCommentAdapter(getActivity(), list, R.layout.comment_listitem);
	}

	@Override
	protected MessageData<CommentList> asyncLoadList(int page, boolean reflash) {
		MessageData<CommentList> msg = null;
		try {
			CommentList list = mApplication.getCommentList(CommentList.CATALOG_NEWS, mNewsId, page, reflash);
			msg = new MessageData<CommentList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<CommentList>(e);
		}
		return msg;
	}

	@Override
	public void onNotifyUpdate(Object obj) {
		if(!isLoadding()) {
			update();
		}
	}

	@Override
	public void onItemClick(int position, Comment data) {
		
	}
}