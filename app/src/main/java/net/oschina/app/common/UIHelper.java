/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package net.oschina.app.common;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.oschina.app.adapter.GridViewFaceAdapter;
import net.oschina.app.bean.Active;
import net.oschina.app.bean.News;
import net.oschina.app.bean.URLs;
import net.oschina.app.core.ApiClient;
import net.oschina.app.core.AppContext;
import net.oschina.app.core.AppException;
import net.oschina.app.widget.LinkView;
import net.oschina.app.widget.LinkView.MyURLSpan;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeboy.app.oschina.BlogDetailActivity;
import com.codeboy.app.oschina.FavoriteActivity;
import com.codeboy.app.oschina.FriendsActivity;
import com.codeboy.app.oschina.LoginActivity;
import com.codeboy.app.oschina.MessageDetailActivity;
import com.codeboy.app.oschina.NewsDetailActivity;
import com.codeboy.app.oschina.QADetailActivity;
import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.SettingActivity;
import com.codeboy.app.oschina.SoftwareDetailActivity;
import com.codeboy.app.oschina.TweetDetailActivity;
import com.codeboy.app.oschina.UserInfoActivity;
import com.codeboy.app.oschina.core.Contanst;
import com.codeboy.app.oschina.core.HandleActivityForResult;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UIHelper {
	
	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;

	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;

	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;

	/** 表情图片匹配 */
	private static Pattern facePattern = Pattern
			.compile("\\[{1}([0-9]\\d*)\\]{1}");

	/** 全局web样式 */
	// 链接样式文件，代码块高亮的处理
	public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
			+ "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>";
	
	public final static String WEB_STYLE = linkCss + "<style>* {font-size:14px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} "
			+ "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;overflow: auto;} "
			+ "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";

	/**
	 * 跳转登录
	 * @param context 上下文
	 * */
	public static void showLogin(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转登录，回调状态值
	 * @param context 上下文
	 * @param handle Activity或Fragment
	 * @param request 请求代号
	 * */
	public static void loginRequest(Context context, HandleActivityForResult handle, int request) {
		Intent intent = new Intent(context, LoginActivity.class);
		handle.startActivityForResult(intent, request);
	}
	
	/**
	 * 显示用户资料
	 * @param context 上下文
	 * */
	public static void showUserInfo(Context context) {
		Intent intent = new Intent(context, UserInfoActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示系统设置界面
	 * 
	 * @param context
	 */
	public static void showSetting(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示用户收藏夹
	 * 
	 * @param context
	 */
	public static void showUserFavorite(Context context) {
		Intent intent = new Intent(context, FavoriteActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示关于我们
	 * @param context
	 * TODO
	 * */
	public static void showAbout(Context context) {
	}
	
	/**
	 * 显示意见反馈
	 * @param context
	 * TODO
	 * */
	public static void showFeedBack(Context context) {
		
	}
	
	/**
	 * 显示可以缩放的图片
	 * @param context
	 * @param imgUrl 图片的链接
	 * */
	public static void showImageZoomDialog(Context context, String imgUrl) {
		
	}
	
	/**
	 * 显示用户好友
	 * @param context
	 */
	public static void showUserFriend(Context context, int friendType,
			int followers, int fans) {
		Intent intent = new Intent(context, FriendsActivity.class);
		intent.putExtra(Contanst.FRIENDS_TYPE, friendType);
		intent.putExtra(Contanst.FRIENDS_FOLLOWERS, followers);
		intent.putExtra(Contanst.FRIENDS_FANS, fans);
		context.startActivity(intent);
	}
	
	/**
	 * 显示用户动态
	 * 
	 * @param context
	 * @param uid
	 * @param hisuid
	 * @param hisname
	 * TODO
	 */
	public static void showUserCenter(Context context, int hisuid,
			String hisname) {
		/*Intent intent = new Intent(context, UserCenter.class);
		intent.putExtra("his_id", hisuid);
		intent.putExtra("his_name", hisname);
		context.startActivity(intent);*/
	}
	
	/**
	 * 点击链接时的处理
	 * @param context 上下文
	 * @param objType
	 * @param objId
	 * @param objKey
	 * */
	public static void showLinkRedirect(Context context, int objType,
			int objId, String objKey) {
		switch (objType) {
		case URLs.URL_OBJ_TYPE_NEWS:
			showNewsDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_QUESTION:
			showQuestionDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_QUESTION_TAG:
			//TODO
			//showQuestionListByTag(context, objKey);
			break;
		case URLs.URL_OBJ_TYPE_SOFTWARE:
			showSoftwareDetail(context, objKey);
			break;
		case URLs.URL_OBJ_TYPE_ZONE:
			showUserCenter(context, objId, objKey);
			break;
		case URLs.URL_OBJ_TYPE_TWEET:
			showTweetDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_BLOG:
			showBlogDetail(context, objId);
			break;
		case URLs.URL_OBJ_TYPE_OTHER:
			openBrowser(context, objKey);
			break;
		}
	}
	
	/**
	 * 新闻超链接点击跳转
	 * 
	 * @param context
	 * @param newsId
	 * @param newsType
	 * @param objId
	 */
	public static void showNewsRedirect(Context context, News news) {
		String url = news.getUrl();
		// url为空-旧方法
		if (StringUtils.isEmpty(url)) {
			int newsId = news.getId();
			int newsType = news.getNewType().type;
			String objId = news.getNewType().attachment;
			switch (newsType) {
			case News.NEWSTYPE_NEWS:
				showNewsDetail(context, newsId);
				break;
			case News.NEWSTYPE_SOFTWARE:
				showSoftwareDetail(context, objId);
				break;
			case News.NEWSTYPE_POST:
				showQuestionDetail(context, StringUtils.toInt(objId));
				break;
			case News.NEWSTYPE_BLOG:
				showBlogDetail(context, StringUtils.toInt(objId));
				break;
			}
		} else {
			showUrlRedirect(context, url);
		}
	}
	
	/**
	 * 动态点击跳转到相关新闻、帖子等
	 * 
	 * @param context
	 * @param id
	 * @param catalog
	 *            0其他 1新闻 2帖子 3动弹 4博客
	 */
	public static void showActiveRedirect(Context context, Active active) {
		String url = active.getUrl();
		// url为空-旧方法
		if (StringUtils.isEmpty(url)) {
			int id = active.getObjectId();
			int catalog = active.getActiveType();
			switch (catalog) {
			case Active.CATALOG_OTHER:
				// 其他-无跳转
				break;
			case Active.CATALOG_NEWS:
				showNewsDetail(context, id);
				break;
			case Active.CATALOG_POST:
				showQuestionDetail(context, id);
				break;
			case Active.CATALOG_TWEET:
				showTweetDetail(context, id);
				break;
			case Active.CATALOG_BLOG:
				showBlogDetail(context, id);
				break;
			}
		} else {
			showUrlRedirect(context, url);
		}
	}
	
	/**
	 * url跳转
	 * 
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url) {
		URLs urls = URLs.parseURL(url);
		if (urls != null) {
			showLinkRedirect(context, urls.getObjType(), urls.getObjId(),
					urls.getObjKey());
		} else {
			openBrowser(context, url);
		}
	}
	
	/**
	 * 显示新闻详情
	 * 
	 * @param context
	 * @param newsId
	 */
	public static void showNewsDetail(Context context, int newsId) {
		Intent intent = new Intent(context, NewsDetailActivity.class);
		intent.putExtra(Contanst.NEWS_ID_KEY, newsId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示动弹详情及评论
	 * 
	 * @param context
	 * @param tweetId
	 */
	public static void showTweetDetail(Context context, int tweetId) {
		Intent intent = new Intent(context, TweetDetailActivity.class);
		intent.putExtra(Contanst.TWEET_ID_KEY, tweetId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示博客详情
	 * 
	 * @param context
	 * @param blogId
	 */
	public static void showBlogDetail(Context context, int blogId) {
		Intent intent = new Intent(context, BlogDetailActivity.class);
		intent.putExtra(Contanst.BLOG_ID_KEY, blogId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示帖子详情
	 * 
	 * @param context
	 * @param postId
	 */
	public static void showQuestionDetail(Context context, int postId) {
		Intent intent = new Intent(context, QADetailActivity.class);
		intent.putExtra(Contanst.POST_ID_KEY, postId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示留言对话页面
	 * 
	 * @param context
	 * @param catalog
	 * @param friendid
	 */
	public static void showMessageDetail(Context context, int friendid,
			String friendname) {
		Intent intent = new Intent(context, MessageDetailActivity.class);
		intent.putExtra(Contanst.MESSAGE_FRIEND_NAME_KEY, friendname);
		intent.putExtra(Contanst.MESSAGE_FRIEND_ID_KEY, friendid);
		context.startActivity(intent);
	}
	
	/**
	 * 显示软件详情
	 * 
	 * @param context
	 * @param ident
	 */
	public static void showSoftwareDetail(Context context, String ident) {
		Intent intent = new Intent(context, SoftwareDetailActivity.class);
		intent.putExtra(Contanst.SOFTWARE_ID_KEY, ident);
		context.startActivity(intent);
	}
	
	/**
	 * 打开浏览器
	 * 
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			ToastMessage(context, "无法浏览此网页", 500);
		}
	}
	
	/**
	 * 调用系统安装了的应用分享
	 * 
	 * @param context
	 * @param title
	 * @param url
	 */
	public static void showShareMore(Activity context, final String title,
			final String url) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
		intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
		context.startActivity(Intent.createChooser(intent, "选择分享"));
	}

	

	/**
	 * 博客列表操作
	 * 
	 * @param context
	 * @param thread
	 */
	public static void showBlogOptionDialog(final Context context,
			final Thread thread) {
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(context.getString(R.string.delete_blog))
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (thread != null)
									thread.start();
								else
									ToastMessage(context,
											R.string.msg_noaccess_delete);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}

	/**
	 * 动弹操作选择框
	 * 
	 * @param context
	 * @param thread
	 */
	public static void showTweetOptionDialog(final Context context,
			final Thread thread) {
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(context.getString(R.string.delete_tweet))
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (thread != null)
									thread.start();
								else
									ToastMessage(context,
											R.string.msg_noaccess_delete);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}


	/**
	 * 加载显示用户头像
	 * 
	 * @param imgFace
	 * @param faceURL
	 */
	public static void showUserFace(final ImageView imgFace,
			final String faceURL) {
		showLoadImage(imgFace, faceURL,
				imgFace.getContext().getString(R.string.msg_load_userface_fail));
	}

	/**
	 * 加载显示图片
	 * 
	 * @param imgFace
	 * @param faceURL
	 * @param errMsg
	 */
	public static void showLoadImage(final ImageView imgView,
			final String imgURL, final String errMsg) {
		// 读取本地图片
		if (StringUtils.isEmpty(imgURL) || imgURL.endsWith("portrait.gif")) {
			Bitmap bmp = BitmapFactory.decodeResource(imgView.getResources(),
					R.drawable.default_avatar);
			imgView.setImageBitmap(bmp);
			return;
		}

		// 是否有缓存图片
		final String filename = FileUtils.getFileName(imgURL);
		// Environment.getExternalStorageDirectory();返回/sdcard
		String filepath = imgView.getContext().getFilesDir() + File.separator
				+ filename;
		File file = new File(filepath);
		if (file.exists()) {
			Bitmap bmp = ImageUtils.getBitmap(imgView.getContext(), filename);
			imgView.setImageBitmap(bmp);
			return;
		}

		// 从网络获取&写入图片缓存
		String _errMsg = imgView.getContext().getString(
				R.string.msg_load_image_fail);
		if (!StringUtils.isEmpty(errMsg))
			_errMsg = errMsg;
		final String ErrMsg = _errMsg;
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1 && msg.obj != null) {
					imgView.setImageBitmap((Bitmap) msg.obj);
					try {
						// 写图片缓存
						ImageUtils.saveImage(imgView.getContext(), filename,
								(Bitmap) msg.obj);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					ToastMessage(imgView.getContext(), ErrMsg);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					Bitmap bmp = ApiClient.getNetBitmap(imgURL);
					msg.what = 1;
					msg.obj = bmp;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * 获取webviewClient对象
	 * 
	 * @return
	 */
	public static WebViewClient getWebViewClient() {
		return new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}

	/**
	 * 获取TextWatcher对象
	 * 
	 * @param context
	 * @param tmlKey
	 * @return
	 */
	public static TextWatcher getTextWatcher(final Activity context,
			final String temlKey) {
		return new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 保存当前EditText正在编辑的内容
				((AppContext) context.getApplication()).setProperty(temlKey,
						s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		};
	}

	/**
	 * 编辑器显示保存的草稿
	 * 
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
	public static void showTempEditContent(Activity context, EditText editer,
			String temlKey) {
		String tempContent = ((AppContext) context.getApplication())
				.getProperty(temlKey);
		if (!StringUtils.isEmpty(tempContent)) {
			SpannableStringBuilder builder = parseFaceByText(context,
					tempContent);
			editer.setText(builder);
			editer.setSelection(tempContent.length());// 设置光标位置
		}
	}

	/**
	 * 将[12]之类的字符串替换为表情
	 * 
	 * @param context
	 * @param content
	 */
	public static SpannableStringBuilder parseFaceByText(Context context,
			String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = facePattern.matcher(content);
		while (matcher.find()) {
			// 使用正则表达式找出其中的数字
			int position = StringUtils.toInt(matcher.group(1));
			int resId = 0;
			try {
				if (position > 65 && position < 102)
					position = position - 1;
				else if (position > 102)
					position = position - 2;
				resId = GridViewFaceAdapter.getImageIds()[position];
				Drawable d = context.getResources().getDrawable(resId);
				d.setBounds(0, 0, 35, 35);// 设置表情图片的显示大小
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				builder.setSpan(span, matcher.start(), matcher.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {
			}
		}
		return builder;
	}

	/**
	 * 清除文字
	 * 
	 * @param cont
	 * @param editer
	 */
	public static void showClearWordsDialog(final Context cont,
			final EditText editer, final TextView numwords) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setTitle(R.string.clearwords);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 清除文字
						editer.setText("");
						numwords.setText("160");
					}
				});
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * 组合动态的动作文本
	 * 
	 * @param objecttype
	 * @param objectcatalog
	 * @param objecttitle
	 * @return
	 */
	@SuppressLint("NewApi")
	public static SpannableString parseActiveAction(String author,
			int objecttype, int objectcatalog, String objecttitle) {
		String title = "";
		int start = 0;
		int end = 0;
		if (objecttype == 32 && objectcatalog == 0) {
			title = "加入了开源中国";
		} else if (objecttype == 1 && objectcatalog == 0) {
			title = "添加了开源项目 " + objecttitle;
		} else if (objecttype == 2 && objectcatalog == 1) {
			title = "在讨论区提问：" + objecttitle;
		} else if (objecttype == 2 && objectcatalog == 2) {
			title = "发表了新话题：" + objecttitle;
		} else if (objecttype == 3 && objectcatalog == 0) {
			title = "发表了博客 " + objecttitle;
		} else if (objecttype == 4 && objectcatalog == 0) {
			title = "发表一篇新闻 " + objecttitle;
		} else if (objecttype == 5 && objectcatalog == 0) {
			title = "分享了一段代码 " + objecttitle;
		} else if (objecttype == 6 && objectcatalog == 0) {
			title = "发布了一个职位：" + objecttitle;
		} else if (objecttype == 16 && objectcatalog == 0) {
			title = "在新闻 " + objecttitle + " 发表评论";
		} else if (objecttype == 17 && objectcatalog == 1) {
			title = "回答了问题：" + objecttitle;
		} else if (objecttype == 17 && objectcatalog == 2) {
			title = "回复了话题：" + objecttitle;
		} else if (objecttype == 17 && objectcatalog == 3) {
			title = "在 " + objecttitle + " 对回帖发表评论";
		} else if (objecttype == 18 && objectcatalog == 0) {
			title = "在博客 " + objecttitle + " 发表评论";
		} else if (objecttype == 19 && objectcatalog == 0) {
			title = "在代码 " + objecttitle + " 发表评论";
		} else if (objecttype == 20 && objectcatalog == 0) {
			title = "在职位 " + objecttitle + " 发表评论";
		} else if (objecttype == 101 && objectcatalog == 0) {
			title = "回复了动态：" + objecttitle;
		} else if (objecttype == 100) {
			title = "更新了动态";
		}
		title = author + " " + title;
		SpannableString sp = new SpannableString(title);
		// 设置用户名字体大小、加粗、高亮
		sp.setSpan(new AbsoluteSizeSpan(14, true), 0, author.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0,
				author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置标题字体大小、高亮
		if (!StringUtils.isEmpty(objecttitle)) {
			start = title.indexOf(objecttitle);
			if (objecttitle.length() > 0 && start > 0) {
				end = start + objecttitle.length();
				sp.setSpan(new AbsoluteSizeSpan(14, true), start, end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				sp.setSpan(
						new ForegroundColorSpan(Color.parseColor("#0e5986")),
						start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return sp;
	}

	/**
	 * 组合动态的回复文本
	 * 
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseActiveReply(String name, String body) {
		SpannableString sp = new SpannableString(name + "：" + body);
		// 设置用户名字体加粗、高亮
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0,
				name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	/**
	 * 组合消息文本
	 * 
	 * @param name
	 * @param body
	 * @return
	 */
	public static void parseMessageSpan(LinkView view, String name,
			String body, String action) {
		Spanned span = null;
		SpannableStringBuilder style = null;
		int start = 0;
		int end = 0;
		String content = null;
		if (StringUtils.isEmpty(action)) {
			content = name + "：" + body;
			span = Html.fromHtml(content);
			view.setText(span);
			end = name.length();
		} else {
			content = action + name + "：" + body;
			span = Html.fromHtml(content);
			view.setText(span);
			start = action.length();
			end = start + name.length();
		}
		view.setMovementMethod(LinkMovementMethod.getInstance());

		Spannable sp = (Spannable) view.getText();
		URLSpan[] urls = span.getSpans(0, sp.length(), URLSpan.class);

		style = new SpannableStringBuilder(view.getText());
		// style.clearSpans();// 这里会清除之前所有的样式
		for (URLSpan url : urls) {
			 style.removeSpan(url);// 只需要移除之前的URL样式，再重新设置
			 MyURLSpan myURLSpan =  view.new MyURLSpan(url.getURL());
			 style.setSpan(myURLSpan, span.getSpanStart(url),
		    		span.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		// 设置用户名字体加粗、高亮
		style.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start,
				end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")),
				start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(style);
	}

	/**
	 * 组合回复引用文本
	 * 
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseQuoteSpan(String name, String body) {
		SpannableString sp = new SpannableString("回复：" + name + "\n" + body);
		// 设置用户名字体加粗、高亮
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 3,
				3 + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 3,
				3 + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}

	/**
	 * 点击返回监听事件
	 * 
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}

	/**
	 * 清除app缓存
	 * 
	 * @param activity
	 */
	public static void clearAppCache(Activity activity) {
		final AppContext ac = (AppContext) activity.getApplication();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					ToastMessage(ac, R.string.clean_cache_success);
				} else {
					ToastMessage(ac, R.string.clean_cache_fail);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/** 
	 * 设置评论的按钮
	 * @param commentButton
	 * @param commentCount 评论数
	 * */
	public static void setupCommentButton(Button commentButton, int commentCount) {
		String text = String.valueOf(commentCount);
		
		Drawable drable = null;
		Resources res = commentButton.getResources();
		if(commentCount > 100) {
			drable = res.getDrawable(R.drawable.comment_lajiao2_icon);
		} else if(commentCount > 50) {
			drable = res.getDrawable(R.drawable.comment_lajiao1_icon);
		} else if(commentCount == 0){
			text = "";
			drable = res.getDrawable(R.drawable.comment_sofa_icon);
		} else {
			drable = res.getDrawable(R.drawable.comment_simple_icon);
		}
		commentButton.setText(text);
		commentButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drable, null);
	}
}