/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.MessageData;

import android.widget.BaseAdapter;
import net.oschina.app.adapter.ListViewSoftwareAdapter;
import net.oschina.app.bean.SimpleSoftware;
import net.oschina.app.bean.SoftwareList;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;

/**
 * 类名 SofewareRecommonFragment.java</br>
 * 创建日期 2014年4月25日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月25日 上午12:17:08</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 开源软件库-推荐
 */
public class SoftwareRecommonFragment extends BaseSwipeRefreshFragment<SimpleSoftware, SoftwareList>{

	@Override
	public BaseAdapter getAdapter(List<SimpleSoftware> list) {
		return new ListViewSoftwareAdapter(getActivity(), list, R.layout.software_listitem);
	}

	@Override
	protected MessageData<SoftwareList> asyncLoadList(int page, boolean reflash) {
		MessageData<SoftwareList> msg = null;
		try {
			SoftwareList list = mApplication.getSoftwareList(SoftwareList.TAG_RECOMMEND, page, reflash);
			msg = new MessageData<SoftwareList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<SoftwareList>(e);
		}
		return msg;
	}
	
	@Override
	public void onItemClick(int position, SimpleSoftware sw) {
		//跳转
		UIHelper.showUrlRedirect(getActivity(), sw.getUrl());
	}
}
