/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import android.os.Bundle;
import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.core.Contanst;
import com.codeboy.app.oschina.modul.MessageData;
import com.codeboy.app.oschina.modul.UpdateDatasEvent;

import net.oschina.app.adapter.ListViewCommentAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.core.AppException;

/**
 * 类名 QADetailCommentFragment.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 下午3:15:17</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public class QADetailCommentFragment extends BaseSwipeRefreshFragment<Comment, CommentList> 
	implements UpdateDatasEvent {

	public static QADetailCommentFragment newInstance(int postid) {
		QADetailCommentFragment fragment = new QADetailCommentFragment();
		Bundle args = new Bundle();
		args.putInt(Contanst.POST_ID_KEY, postid);
		fragment.setArguments(args);
		return fragment;
	}
	
	private int mPostId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle argus = getArguments();
		if(argus != null) {
			mPostId = argus.getInt(Contanst.POST_ID_KEY);
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
			CommentList list = mApplication.getCommentList(CommentList.CATALOG_POST, mPostId, page, reflash);
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
