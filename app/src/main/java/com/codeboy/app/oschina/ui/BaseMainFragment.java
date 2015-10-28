/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.codeboy.app.oschina.BaseFragment;
import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.adapter.TabsFragmentPagerAdapter;

/**
 * 类名 BaseMainFragment.java</br>
 * 创建日期 2014年4月27日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月27日 下午4:29:16</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 各类主界面的基类
 */
public abstract class BaseMainFragment extends BaseFragment{

	protected PagerSlidingTabStrip mTabStrip;
	protected ViewPager  mViewPager;
	protected TabsFragmentPagerAdapter mTabsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_viewpager_tabs, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
        mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabstrip);
        mTabStrip.setIndicatorColorResource(R.color.theme_green);
        
        mTabStrip.setTextSize((int)TypedValue.applyDimension(
        		TypedValue.COMPLEX_UNIT_SP, 16, dm));
        mTabStrip.setIndicatorHeight((int)TypedValue.applyDimension(
        		TypedValue.COMPLEX_UNIT_DIP, 5, dm));
        mTabStrip.setShouldExpand(true);
        
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);

        mTabsAdapter = new TabsFragmentPagerAdapter(
        		getChildFragmentManager(), mTabStrip, mViewPager);

        onSetupTabAdapter(mTabsAdapter);
        mTabsAdapter.notifyDataSetChanged();

        if (savedInstanceState != null) {
        	int pos = savedInstanceState.getInt("position");
        	mViewPager.setCurrentItem(pos, false);
        }
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mViewPager.getCurrentItem());
    }
	
	protected abstract void onSetupTabAdapter(TabsFragmentPagerAdapter adapter);
}