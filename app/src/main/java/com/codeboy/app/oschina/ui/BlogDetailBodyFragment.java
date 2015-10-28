/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import com.codeboy.app.library.util.L;
import com.codeboy.app.oschina.core.Contanst;

import android.os.AsyncTask;
import android.os.Bundle;
import net.oschina.app.bean.Blog;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppContext;

/**
 * 类名 BlogDetailBodyFragment.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 下午1:00:19</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 博客详细界面
 */
public class BlogDetailBodyFragment extends BaseDetailBodyFragment{
	
	public static BlogDetailBodyFragment newInstance(Blog blog) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(Contanst.BLOG_DATA_KEY, blog);
		BlogDetailBodyFragment fragment = new BlogDetailBodyFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private Blog mBlog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle argus = getArguments();
		if(argus != null) {
			mBlog = (Blog) argus.getSerializable(Contanst.BLOG_DATA_KEY);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected boolean isDataLoaded() {
		return mBlog != null;
	}

	@Override
	protected CharSequence getTitleText() {
		if(mBlog != null) {
			return mBlog.getTitle();
		}
		return null;
	}

	@Override
	protected CharSequence getAuthorText() {
		if(mBlog != null) {
			return mBlog.getAuthor();
		}
		return null;
	}

	@Override
	protected CharSequence getDateText() {
		if(mBlog != null) {
			return StringUtils.friendly_time(mBlog.getPubDate());
		}
		return null;
	}

	@Override
	protected CharSequence getCountText() {
		if(mBlog != null) {
			return String.valueOf(mBlog.getCommentCount());
		}
		return null;
	}

	@Override
	protected void loadDatas() {
		if(mBlog == null || !isAdded()) {
			if(L.Debug) {
				L.d("mBlog == null || !isAdded()-->" + isAdded());
			}
			return;
		}
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				//异步更新数据
				final Blog blogDetail = mBlog;
				
				String body = UIHelper.WEB_STYLE + blogDetail.getBody();
				
				//读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
				boolean isLoadImage;
				if(AppContext.NETTYPE_WIFI == mApplication.getNetworkType()){
					isLoadImage = true;
				}else{
					isLoadImage = mApplication.isLoadImage();
				}
				if(isLoadImage){
					body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
					body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
					
					// 添加点击图片放大支持
					body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
							"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
				}else{
					body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
				}
				
				if(L.Debug) {
					L.d(body);
				}
				
				return body;
			}
			
			@Override
			protected void onPreExecute() {
				
			}
			
			@Override
			protected void onPostExecute(String body) {
				if(isDetached()) {
					return;
				}
				showMainView();
					
				mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
			}
		}.execute();
	}

	@Override
	public void onNotifyUpdate(Object obj) {
		if(obj == null || !(obj instanceof Blog)) {
			return;
		}
		mBlog = (Blog)obj;
		updateView();
		loadDatas();
	}
}
