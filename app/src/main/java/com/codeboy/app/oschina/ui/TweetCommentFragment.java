/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.util.List;

import net.oschina.app.adapter.ListViewCommentAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.Tweet;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeboy.app.library.util.L;
import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.core.Contanst;
import com.codeboy.app.oschina.modul.MessageData;
import com.codeboy.app.oschina.modul.OnLoadingListener;
import com.codeboy.app.oschina.modul.OnNotifyUpdateListener;

/**
 * 类名 TweetCommentFragment.java</br>
 * 创建日期 2014年5月3日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月3日 下午10:54:48</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 动弹评论界面
 */
public class TweetCommentFragment extends BaseSwipeRefreshFragment<Comment, CommentList> 
	implements OnNotifyUpdateListener {

	public static TweetCommentFragment newInstance(int tweetid) {
		TweetCommentFragment fragment = new TweetCommentFragment();
		Bundle args = new Bundle();
		args.putInt(Contanst.TWEET_ID_KEY, tweetid);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private Tweet tweetDetail;
	private int tweetId;
	
	private ImageView userface;
	private TextView username;
	private TextView date;
	private TextView commentCount;
	private WebView content;
	private ImageView image;
	private ImageView audio;
	private TextView audioTime;
	private MediaPlayer player;
	private LinearLayout audioLayout;
	
	private OnLoadingListener mOnLoadingListener;
	
	private int mStatus = OnLoadingListener.STATUS_NONE;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnLoadingListener) {
			mOnLoadingListener = (OnLoadingListener) activity;
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mOnLoadingListener = null;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if(args != null) {
			tweetId = args.getInt(Contanst.TWEET_ID_KEY);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		loadDatas(false);
	}

	@Override
	public BaseAdapter getAdapter(List<Comment> list) {
		return new ListViewCommentAdapter(getActivity(), list, R.layout.comment_listitem);
	}

	@Override
	protected MessageData<CommentList> asyncLoadList(int page,
			boolean reflash) {
		MessageData<CommentList> msg = null;
		try {
			CommentList list = mApplication.getCommentList(
					CommentList.CATALOG_TWEET, tweetId, page, reflash);
			msg = new MessageData<CommentList>(list);
		} catch (AppException e) {
			e.printStackTrace();
			msg = new MessageData<CommentList>(e);
		}
		return msg;
	}
	
	/** 收到通知去更新*/
	@Override
	public void onNotifyUpdate() {
		loadDatas(true);
		update();
	}

	@Override
	public void onItemClick(int position, Comment data) {
		L.d("--->评论内容" + data.getContent());
	}
	
	
	@Override
	protected View getHeaderView(LayoutInflater inflater) {
		View header = inflater.inflate(R.layout.tweet_detail_content, null);
		initHeaderView(header);
		return header;
	}

	/** 初始化界面*/
	private void initHeaderView(View view) {
		userface = (ImageView) view.findViewById(R.id.tweet_listitem_userface);
		username = (TextView) view.findViewById(R.id.tweet_listitem_username);
		date = (TextView) view.findViewById(R.id.tweet_listitem_date);
		commentCount = (TextView) view.findViewById(R.id.tweet_listitem_commentCount);
		image = (ImageView) view.findViewById(R.id.tweet_listitem_image);
		audio = (ImageView) view.findViewById(R.id.tweet_audio_controller);
		audioTime = (TextView) view.findViewById(R.id.tweet_audio_time);
		audioLayout = (LinearLayout) view.findViewById(R.id.tweet_audio_layout);
		content = (WebView) view.findViewById(R.id.tweet_listitem_content);
		
		content.getSettings().setJavaScriptEnabled(false);
		content.getSettings().setSupportZoom(true);
		content.getSettings().setBuiltInZoomControls(false);
		content.getSettings().setDefaultFontSize(12);
		
		setupHeaderView();
	}
	
	void setupHeaderView() {
		if(tweetDetail == null) {
			return;
		}
		username.setText(tweetDetail.getAuthor());
		username.setOnClickListener(faceClickListener);
		date.setText(StringUtils.friendly_time(tweetDetail
				.getPubDate()));
		commentCount.setText(tweetDetail.getCommentCount() + "");

		String body = UIHelper.WEB_STYLE + tweetDetail.getBody();
		body = body.replaceAll(
				"(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		body = body.replaceAll(
				"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");

		content.loadDataWithBaseURL(null, body, "text/html",
				"utf-8", null);
		content.setWebViewClient(UIHelper.getWebViewClient());

		// 加载用户头像
		String faceURL = tweetDetail.getFace();
		if (faceURL.endsWith("portrait.gif")
				|| StringUtils.isEmpty(faceURL)) {
			userface.setImageResource(R.drawable.mini_avatar);
		} else {
			UIHelper.showUserFace(userface, faceURL);
		}
		userface.setOnClickListener(faceClickListener);

		// 加载图片
		String imgSmall = tweetDetail.getImgSmall();
		if (!StringUtils.isEmpty(imgSmall)) {
			UIHelper.showLoadImage(image, imgSmall, null);
			image.setVisibility(View.VISIBLE);
			image.setOnClickListener(imageClickListener);
		}
		
		// 添加语音播放
		final String attach = tweetDetail.getAttach();
		if (!StringUtils.isEmpty(attach)) {
			player = new MediaPlayer();
			try {
				player.setDataSource(attach);
				try {
					player.prepare();
				} catch (Exception e) {
					UIHelper.ToastMessage(getActivity(),
							R.string.msg_load_audio_fail);
				}
				audio.setVisibility(View.VISIBLE);
				final AnimationDrawable anima = (AnimationDrawable) audio
						.getDrawable();
				anima.setOneShot(false);
                anima.stop();
				anima.selectDrawable(0);
				audioLayout.setVisibility(View.VISIBLE);
				audioLayout.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (player.isPlaying()) {
									player.pause();
									anima.stop();
								} else {
									player.start();
									anima.start();
								}
							}
				});
				
				player.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						int time = mp.getDuration() / 1000;
						audioTime.setText(time + "s");
						int width = 100 + time;
						width = (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, width,
								getResources().getDisplayMetrics());
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								width,
								ViewGroup.LayoutParams.MATCH_PARENT);
						audioLayout.setLayoutParams(params);
					}
				});
				
				player.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						try {
							mp.stop();
							mp.reset();
							mp.setDataSource(attach);
							mp.prepare();
							anima.stop();
							anima.selectDrawable(0);
						} catch (Exception e) {
							UIHelper.ToastMessage(getActivity(),
									R.string.msg_load_audio_fail);
						}
					}
				});
			} catch (Exception e) {
				UIHelper.ToastMessage(getActivity(),
						R.string.msg_load_audio_fail);
			}
		}
	}
	
	private OnClickListener faceClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (tweetDetail != null) {
				//TODO
				/*UIHelper.showUserCenter(v.getContext(),
						tweetDetail.getAuthorId(), tweetDetail.getAuthor());*/
			}
		}
	};

	private OnClickListener imageClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (tweetDetail != null) {
				//TODO
				/*UIHelper.showImageZoomDialog(v.getContext(),
						tweetDetail.getImgBig());*/
			}
		}
	};
	
	private void onStatus() {
		if(mOnLoadingListener != null) {
			mOnLoadingListener.onStatus(mStatus);
		}
	}
	
	private void onDataCallBack() {
		if(mOnLoadingListener != null) {
			mOnLoadingListener.OnDataCallBack(tweetDetail);
		}
	}
	
	/** 
	 * 加载数据
	 * @param isRefresh 是否刷新
	 * */
	private void loadDatas(final boolean isRefresh) {
		new AsyncTask<Void, Void, Message>() {

			@Override
			protected Message doInBackground(Void... params) {
				Message msg = new Message();
				try {
					Tweet tweetDetail = getOsChinaApplication().getTweet(tweetId, isRefresh);
					msg.what = (tweetDetail != null && tweetDetail.getId() > 0) ? 1 : 0;
					msg.obj = tweetDetail;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				return msg;
			}
			
			@Override
			protected void onPreExecute() {
				mStatus = OnLoadingListener.STATUS_LOADING;
				onStatus();
			}
			
			@Override
			protected void onPostExecute(Message msg) {
				mStatus = OnLoadingListener.STATUS_LOADED;
				if(isDetached()) {
					return;
				}
				onStatus();
				
				if (msg.what == 1) {
					tweetDetail = (Tweet) msg.obj;
					setupHeaderView();
					onDataCallBack();
				} else if (msg.what == 0) {
					UIHelper.ToastMessage(getActivity(),
							R.string.msg_load_is_null);
				} else {
					((AppException) msg.obj).makeToast(getActivity());
				}
			}
			
		}.execute();
	}
}