/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.MessageData;

import net.oschina.app.adapter.ListViewActiveAdapter;
import net.oschina.app.bean.Active;
import net.oschina.app.bean.ActiveList;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;

/**
 * 类名 ActiveCommentFragment.java</br>
 * 创建日期 2014年4月28日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月28日 上午1:08:01</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 我的空间-评论
 */
public class ActiveCommentFragment extends BaseSwipeRefreshFragment<Active, ActiveList> {

	@Override
	public BaseAdapter getAdapter(List<Active> list) {
		return new ListViewActiveAdapter(getActivity(), list,
				R.layout.active_listitem);
	}

	@Override
	protected MessageData<ActiveList> asyncLoadList(int page, boolean reflash) {
		MessageData<ActiveList> msg = null;
		try {
			ActiveList list = mApplication.getActiveList(
					ActiveList.CATALOG_COMMENT, page, reflash);
			msg = new MessageData<ActiveList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<ActiveList>(e);
		}
		return msg;
	}

	@Override
	public void onItemClick(int position, Active data) {
		UIHelper.showActiveRedirect(getActivity(), data);
	}
}
