/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import net.oschina.app.adapter.ListViewNewsAdapter;
import net.oschina.app.bean.News;
import net.oschina.app.bean.NewsList;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.MessageData;

/**
 * 类名 NewsLatestNewsFragment.java</br>
 * 创建日期 2014年4月20日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月20日 下午11:34:05</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 最新资讯
 */
public class NewsLatestNewsFragment extends BaseSwipeRefreshFragment<News, NewsList> {
	
	public static NewsLatestNewsFragment newInstance() {
		return new NewsLatestNewsFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public BaseAdapter getAdapter(List<News> list) {
		return new ListViewNewsAdapter(getActivity(), list, R.layout.news_listitem);
	}

	@Override
	protected MessageData<NewsList> asyncLoadList(int page, boolean reflash) {
		MessageData<NewsList> msg = null;
		try {
			NewsList list = mApplication.getNewsList(NewsList.CATALOG_ALL, page, reflash);
			msg = new MessageData<NewsList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<NewsList>(e);
		}
		return msg;
	}

	@Override
	public void onItemClick(int position, News news) {
		UIHelper.showNewsRedirect(getActivity(), news);
	}
}
