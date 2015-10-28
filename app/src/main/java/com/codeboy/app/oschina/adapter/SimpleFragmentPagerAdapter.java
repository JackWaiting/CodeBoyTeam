/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 类名 SimpleFragmentPagerAdapter.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 上午10:57:10</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	private Context mContext;
	
	public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}
	
	public void addTab(String tag, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(null, tag, clss, args);
        mTabs.add(info);
        notifyDataSetChanged();
    }
	
	public void addTab(TabInfo info) {
		mTabs.add(info);
		notifyDataSetChanged();
	}
	
	public TabInfo getTab(int position) {
		return mTabs.get(position);
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}
}
