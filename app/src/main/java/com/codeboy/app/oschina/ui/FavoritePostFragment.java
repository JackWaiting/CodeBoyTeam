/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.MessageData;

import net.oschina.app.adapter.ListViewFavoriteAdapter;
import net.oschina.app.bean.Favorite;
import net.oschina.app.bean.FavoriteList;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;

/**
 * 类名 FavoritePostFragment.java</br>
 * 创建日期 2014年5月9日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月9日 下午10:20:59</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 收藏夹-话题
 */
public class FavoritePostFragment extends BaseSwipeRefreshFragment<Favorite, FavoriteList> {

	@Override
	public BaseAdapter getAdapter(List<Favorite> list) {
		return new ListViewFavoriteAdapter(getActivity(), list, R.layout.favorite_listitem);
	}

	@Override
	protected MessageData<FavoriteList> asyncLoadList(int page, boolean reflash) {
		MessageData<FavoriteList> msg = null;
		try {
			FavoriteList list = mApplication.getFavoriteList(
					FavoriteList.TYPE_POST, page, reflash);
			msg = new MessageData<FavoriteList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<FavoriteList>(e);
		}
		return msg;
	}
	
	@Override
	public void onItemClick(int position, Favorite data) {
		//跳转
		UIHelper.showUrlRedirect(getActivity(), data.url);
	}
}
