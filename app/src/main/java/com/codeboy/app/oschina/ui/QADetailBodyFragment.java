/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.net.URLEncoder;
import java.util.List;

import net.oschina.app.bean.Post;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppContext;
import android.os.AsyncTask;
import android.os.Bundle;

import com.codeboy.app.library.util.L;
import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.core.Contanst;

/**
 * 类名 QADetailBodyFragment.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 下午3:13:45</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public class QADetailBodyFragment extends BaseDetailBodyFragment {

	public static QADetailBodyFragment newInstance(Post post) {
		QADetailBodyFragment fragment = new QADetailBodyFragment();
		Bundle args = new Bundle();
		args.putSerializable(Contanst.POST_DATA_KEY, post);
		fragment.setArguments(args);
		return fragment;
	}
	
	private Post postDetail;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle argus = getArguments();
		if(argus != null) {
			postDetail = (Post) argus.getSerializable(Contanst.POST_DATA_KEY);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected boolean isDataLoaded() {
		return postDetail != null;
	}

	@Override
	protected CharSequence getTitleText() {
		if(postDetail != null) {
			return postDetail.getTitle();
		}
		return null;
	}

	@Override
	protected CharSequence getAuthorText() {
		if(postDetail != null) {
			return postDetail.getAuthor();
		}
		return null;
	}

	@Override
	protected CharSequence getDateText() {
		if(postDetail != null) {
			return StringUtils.friendly_time(postDetail.getPubDate());
		}
		return null;
	}

	@Override
	protected CharSequence getCountText() {
		if(postDetail != null) {
			return getString(R.string.comment_answer_view_count, 
					postDetail.getAnswerCount(), postDetail.getViewCount());
		}
		return null;
	}
	
	private String getPostTags(List<String> taglist) {
    	if(taglist == null)
    		return "";
    	String tags = "";
    	for(String tag : taglist) {
    		tags += String.format(
    				"<a class='tag' href='http://www.oschina.net/question/tag/%s' >&nbsp;%s&nbsp;</a>&nbsp;&nbsp;", 
    				URLEncoder.encode(tag), tag);
    	}
    	return String.format("<div style='margin-top:10px;'>%s</div>", tags);
    }
	
	/**
	 * 加载资讯数据
	 * */
	@Override
	protected void loadDatas() {
		if(postDetail == null || !isAdded()) {
			if(L.Debug) {
				L.d("newsDetail == null || !isAdded()-->" + isAdded());
			}
			return;
		}
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				//显示标签
				String tags = getPostTags(postDetail.getTags());
				
				String body = UIHelper.WEB_STYLE + postDetail.getBody() + tags + "<div style=\"margin-bottom: 80px\" />";
				//读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
				boolean isLoadImage;
				if(AppContext.NETTYPE_WIFI == mApplication.getNetworkType()){
					isLoadImage = true;
				}else{
					isLoadImage = mApplication.isLoadImage();
				}
				if(isLoadImage){
					body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
					body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
					// 添加点击图片放大支持
					body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
							"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
				}else{
					body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>","");
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
		if(obj == null || !(obj instanceof Post)) {
			return;
		}
		postDetail = (Post)obj;
		updateView();
		loadDatas();
	}
}