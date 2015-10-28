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
import net.oschina.app.bean.BlogCommentList;
import net.oschina.app.bean.Comment;
import net.oschina.app.core.AppException;

/**
 * 类名 BlogDetailCommentFragment.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 下午1:13:35</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 博客评论界面
 */
public class BlogDetailCommentFragment extends BaseSwipeRefreshFragment<Comment, BlogCommentList> 
	implements UpdateDatasEvent {
	
	public static BlogDetailCommentFragment newInstance(int blog_id) {
		Bundle bundle = new Bundle();
		bundle.putInt(Contanst.BLOG_ID_KEY, blog_id);
		
		BlogDetailCommentFragment fragment = new BlogDetailCommentFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	int mBlogId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle argus = getArguments();
		if(argus != null) {
			mBlogId = argus.getInt(Contanst.BLOG_ID_KEY);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public BaseAdapter getAdapter(List<Comment> list) {
		return new ListViewCommentAdapter(getActivity(), list, R.layout.comment_listitem);
	}

	@Override
	protected MessageData<BlogCommentList> asyncLoadList(int page, boolean reflash) {
		MessageData<BlogCommentList> msg = null;
		try {
			BlogCommentList list = mApplication.getBlogCommentList(mBlogId, page, reflash);
			msg = new MessageData<BlogCommentList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<BlogCommentList>(e);
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