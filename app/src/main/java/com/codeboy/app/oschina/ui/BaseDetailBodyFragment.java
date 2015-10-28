/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import net.oschina.app.common.UIHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.codeboy.app.oschina.BaseFragment;
import com.codeboy.app.oschina.OSChinaApplication;
import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.modul.UpdateDatasEvent;


/**
 * 类名 BaseDetailBodyFragment.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 下午12:26:14</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 详细界面的基类
 */
public abstract class BaseDetailBodyFragment extends BaseFragment implements UpdateDatasEvent {
	
	protected OSChinaApplication mApplication;
	
	protected WebView mWebView;
	protected TextView mTitleTextView;
	protected TextView mAuthorTextView;
	protected TextView mDateTextView;
	protected TextView mCountTextView;
	protected View mMainView;
	protected View mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = getOsChinaApplication();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_basedetail_body, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mTitleTextView = (TextView) view.findViewById(R.id.news_detail_title);
		mAuthorTextView = (TextView) view.findViewById(R.id.news_detail_author);
		mDateTextView = (TextView) view.findViewById(R.id.news_detail_date);
		mCountTextView = (TextView) view.findViewById(R.id.news_detail_commentcount);
		mMainView = view.findViewById(R.id.news_detail_scrollview);
		mProgressBar = view.findViewById(R.id.news_detail_progressbar);
		
		mMainView.setVisibility(View.INVISIBLE);
		
		mWebView = (WebView) view.findViewById(R.id.news_detail_webview);
		mWebView.setWebViewClient(UIHelper.getWebViewClient());
		WebSettings settings = mWebView.getSettings();
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(false);
		
		//mWebView.getSettings().setDefaultFontSize(15);
		
		//TODO
        //UIHelper.addWebImageShow(getActivity(), mWebView);
		
		updateView();
		loadDatas();
	}
	
	protected void updateView() {
		if(!isDataLoaded()) {
			return;
		}
		mProgressBar.setVisibility(View.GONE);
		mTitleTextView.setText(getTitleText());
		mAuthorTextView.setText(getAuthorText());
		mDateTextView.setText(getDateText());
		mCountTextView.setText(getCountText());
	}
	
	protected abstract boolean isDataLoaded();
	protected abstract CharSequence getTitleText();
	protected abstract CharSequence getAuthorText();
	protected abstract CharSequence getDateText();
	protected abstract CharSequence getCountText();
	
	protected abstract void loadDatas();
	
	protected void showMainView() {
		if(mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}
		if(mMainView != null) {
			mMainView.setVisibility(View.VISIBLE);
		}
	}
}
