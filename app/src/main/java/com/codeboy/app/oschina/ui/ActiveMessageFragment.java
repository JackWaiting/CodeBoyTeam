/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import android.widget.BaseAdapter;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.MessageData;

import net.oschina.app.adapter.ListViewMessageAdapter;
import net.oschina.app.bean.MessageList;
import net.oschina.app.bean.Messages;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;

/**
 * 类名 ActiveMessageFragment.java</br>
 * 创建日期 2014年4月28日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月28日 上午1:09:51</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 我的空间-留言
 */
public class ActiveMessageFragment extends BaseSwipeRefreshFragment<Messages, MessageList> {

	@Override
	public BaseAdapter getAdapter(List<Messages> list) {
		return new ListViewMessageAdapter(getActivity(), list,
				R.layout.message_listitem);
	}

	@Override
	protected MessageData<MessageList> asyncLoadList(int page, boolean reflash) {
		MessageData<MessageList> msg = null;
		try {
			MessageList list = mApplication.getMessageList(page, reflash);
			msg = new MessageData<MessageList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<MessageList>(e);
		}
		return msg;
	}

	@Override
	public void onItemClick(int position, Messages msg) {
		UIHelper.showMessageDetail(getActivity(), 
				msg.getFriendId(), msg.getFriendName());
	}
}
