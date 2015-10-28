/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import net.oschina.app.bean.FavoriteList;
import net.oschina.app.bean.Result;
import net.oschina.app.bean.Software;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.ApiClient;
import net.oschina.app.core.AppContext;
import net.oschina.app.core.AppException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeboy.app.oschina.core.Contanst;

/**
 * 类名 SoftwareDetailActivity.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 下午11:13:26</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 开源软件的详细页面
 */
public class SoftwareDetailActivity extends BaseActionBarActivity {
	
	private final static int REFLASH_ITEM_ID = 100;
    private final static int FAVORITE_ITEM_ID = 101;
	
	final static int STATUS_NONE = 0x0;
	final static int STATUS_LOADING = 0x01;
	final static int STATUS_LOADED = 0x11;
	
	private ImageView mLogo;
	private TextView mTitle;

	private TextView mLicense;
	private TextView mLanguage;
	private TextView mOS;
	private TextView mRecordtime;

	private LinearLayout ll_language;
	private LinearLayout ll_os;
	private View iv_language;
	private View iv_os;

	private Button mHomepage;
	private Button mDocment;
	private Button mDownload;
	
	private View mMainView;

	private WebView mWebView;
	
	private Software softwareDetail;
	private Bitmap logo;
	private String ident;
	
	private OSChinaApplication mApplication;
	
	private int mStatus = STATUS_NONE;
	
	private Menu optionsMenu;
    //是否正在处理收藏的事件
    private boolean isFavoriting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String id = getIntent().getStringExtra(Contanst.SOFTWARE_ID_KEY);
		if(id == null) {
			finish();
			return;
		}
		ident = id;
		
		ActionBar bar = getSupportActionBar();
		int flags = ActionBar.DISPLAY_HOME_AS_UP;
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, flags);
        
		mApplication = getOsChinaApplication();
		
		setContentView(R.layout.activity_software_detail);
		
		initView();
		loadDatas(false);
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		optionsMenu = menu;
        //收藏按钮
        MenuItem favoriteItem = menu.add(0, FAVORITE_ITEM_ID,
                100, R.string.footbar_favorite);
        favoriteItem.setIcon(R.drawable.ic_menu_favorite_n);
        MenuItemCompat.setShowAsAction(favoriteItem,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		//刷新按钮
		MenuItem reflashItem = menu.add(0, REFLASH_ITEM_ID, 
				101, R.string.footbar_refresh);
        reflashItem.setIcon(R.drawable.ic_menu_refresh);
        MenuItemCompat.setShowAsAction(reflashItem, 
        		MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == REFLASH_ITEM_ID) {
			//刷新
			loadDatas(true);
			return true;
		} else if(item.getItemId() == FAVORITE_ITEM_ID) {
            favoriteClick();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	/** 更新菜单的状态*/
	private void updateMenu() {
		if(optionsMenu == null) {
			return;
		}
        final MenuItem favoriteItem = optionsMenu.findItem(FAVORITE_ITEM_ID);
        //收藏状态
        if(softwareDetail != null && favoriteItem != null) {
            if (softwareDetail.getFavorite() == 1) {
                favoriteItem.setIcon(R.drawable.ic_menu_favorite_y);
            } else {
                favoriteItem.setIcon(R.drawable.ic_menu_favorite_n);
            }
        }
		final MenuItem refreshItem = optionsMenu.findItem(REFLASH_ITEM_ID);
        if (refreshItem == null) {
            return;
        }
		if(mStatus == STATUS_LOADED) {
			MenuItemCompat.setActionView(refreshItem, null);
		} else {
			MenuItemCompat.setActionView(refreshItem, 
					R.layout.actionbar_indeterminate_progress);
		}
	}
	
	private void initView() {
		mMainView = findViewById(R.id.software_detail_scrollview);
		mMainView.setVisibility(View.GONE);
		
		mLogo = (ImageView) findViewById(R.id.software_detail_logo);
		mTitle = (TextView) findViewById(R.id.software_detail_title);

		mLicense = (TextView) findViewById(R.id.software_detail_license);
		mLanguage = (TextView) findViewById(R.id.software_detail_language);
		mOS = (TextView) findViewById(R.id.software_detail_os);
		mRecordtime = (TextView) findViewById(R.id.software_detail_recordtime);

		mHomepage = (Button) findViewById(R.id.software_detail_homepage);
		mDocment = (Button) findViewById(R.id.software_detail_document);
		mDownload = (Button) findViewById(R.id.software_detail_download);

		ll_language = (LinearLayout) findViewById(R.id.software_detail_language_ll);
		ll_os = (LinearLayout) findViewById(R.id.software_detail_os_ll);
		iv_language = findViewById(R.id.software_detail_language_iv);
		iv_os = findViewById(R.id.software_detail_os_iv);
		
		mWebView = (WebView) findViewById(R.id.software_detail_webview);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(false);
		//TODO
		//mWebView.getSettings().setDefaultFontSize(15);
		//UIHelper.addWebImageShow(this, mWebView);
	}
	
	private void loadDatas(final boolean isRefresh){
		new AsyncTask<Void, Void, Message> () {

			@Override
			protected Message doInBackground(Void... params) {
				Message msg = new Message();
				try {
					softwareDetail = mApplication.getSoftware(ident, isRefresh);
					if (softwareDetail != null
							&& !StringUtils.isEmpty(softwareDetail.getLogo())) {
						/*
						 * 软件logo格式为gif，保存后再读取图片透明效果消失 //先加载SD卡中的图片缓存 String
						 * filename =
						 * FileUtils.getFileName(softwareDetail.getLogo());
						 * String filepath = getFilesDir() + File.separator +
						 * filename; File file = new File(filepath);
						 * if(file.exists()){ logo =
						 * ImageUtils.getBitmap(SoftwareDetail.this, filename);
						 * }else{ //加载网络图片 logo =
						 * ApiClient.getNetBitmap(softwareDetail.getLogo());
						 * if(logo != null){ //向SD卡中写入图片缓存 try{
						 * ImageUtils.saveImage(SoftwareDetail.this, filename,
						 * logo); } catch (IOException e) { e.printStackTrace();
						 * } } }
						 */
						// 加载网络图片
						logo = ApiClient.getNetBitmap(softwareDetail.getLogo());
					}
					
					String body = UIHelper.WEB_STYLE + softwareDetail.getBody();
					// 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
					boolean isLoadImage;
					if (AppContext.NETTYPE_WIFI == mApplication.getNetworkType()) {
						isLoadImage = true;
					} else {
						isLoadImage = mApplication.isLoadImage();
					}
					if (isLoadImage) {
						body = body.replaceAll(
								"(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
						body = body.replaceAll(
								"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
						// 添加点击图片放大支持
						body = body
								.replaceAll("(<img[^>]+src=\")(\\S+)\"",
										"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
					} else {
						body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
					}
					
					msg.what = (softwareDetail != null && softwareDetail.getId() > 0) ? 1 : 0;
					msg.obj = body;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				return msg;
			}
			
			@Override
			protected void onPreExecute() {
				mStatus = STATUS_LOADING;
				updateMenu();
			}
			
			@Override
			protected void onPostExecute(Message msg) {
				if(isFinishing()) {
					return;
				}
				mStatus = STATUS_LOADED;
				updateMenu();
				
				if(softwareDetail != null) {
					mMainView.setVisibility(View.VISIBLE);
				}
				if (msg.what == 1) {
					mLogo.setImageBitmap(logo);

					String title = softwareDetail.getExtensionTitle() + " "
							+ softwareDetail.getTitle();
					String body = (String)msg.obj;
					
					mTitle.setText(title);

					mWebView.loadDataWithBaseURL(null, body, 
							"text/html", "utf-8", null);
					mWebView.setWebViewClient(UIHelper.getWebViewClient());

					mLicense.setText(softwareDetail.getLicense());
					mRecordtime.setText(softwareDetail.getRecordtime());
					String language = softwareDetail.getLanguage();
					String os = softwareDetail.getOs();
					if (StringUtils.isEmpty(language)) {
						ll_language.setVisibility(View.GONE);
						iv_language.setVisibility(View.GONE);
					} else {
						mLanguage.setText(language);
					}
					if (StringUtils.isEmpty(os)) {
						ll_os.setVisibility(View.GONE);
						iv_os.setVisibility(View.GONE);
					} else {
						mOS.setText(os);
					}

					if (StringUtils.isEmpty(softwareDetail.getHomepage())) {
						mHomepage.setVisibility(View.GONE);
					} else {
						mHomepage.setOnClickListener(homepageClickListener);
					}
					
					if (StringUtils.isEmpty(softwareDetail.getDocument())) {
						mDocment.setVisibility(View.GONE);
					} else {
						mDocment.setOnClickListener(docmentClickListener);
					}
					
					if (StringUtils.isEmpty(softwareDetail.getDownload())) {
						mDownload.setVisibility(View.GONE);
					} else {
						mDownload.setOnClickListener(downloadClickListener);
					}
				} else if (msg.what == 0) {
					UIHelper.ToastMessage(getActivity(),
							R.string.msg_load_is_null);
				} else if (msg.what == -1 && msg.obj != null) {
					((AppException) msg.obj).makeToast(getActivity());
				}
			}
			
		}.execute();
	}

    private void favoriteClick() {
        if(isFavoriting) {
            return;
        }

        if (ident == "" || softwareDetail == null) {
            return;
        }

        if(!mApplication.isLogin()) {
            Toast.makeText(this, R.string.msg_login_request, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return;
        }
        isFavoriting = true;
        final int uid = mApplication.getLoginUid();
        new AsyncTask<Void, Void, Message>(){

            @Override
            protected Message doInBackground(Void... params) {
                Message msg = new Message();
                try {
                    Result res = null;
                    if (softwareDetail.getFavorite() == 1) {
                        res = mApplication.delFavorite(uid, softwareDetail.getId(),
                                FavoriteList.TYPE_SOFTWARE);
                    } else {
                        res = mApplication.addFavorite(uid, softwareDetail.getId(),
                                FavoriteList.TYPE_SOFTWARE);
                    }
                    msg.what = 1;
                    msg.obj = res;
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Message msg) {
                if (msg.what == 1) {
                    Result res = (Result) msg.obj;
                    if (res.OK()) {
                        if (softwareDetail.getFavorite() == 1) {
                            softwareDetail.setFavorite(0);
                        } else {
                            softwareDetail.setFavorite(1);
                        }
                        // 重新保存缓存
                        mApplication.saveObject(softwareDetail,
                                softwareDetail.getCacheKey());
                    }
                    UIHelper.ToastMessage(getActivity(), res.getErrorMessage());
                    updateMenu();
                } else {
                    ((AppException) msg.obj).makeToast(getActivity());
                }
                isFavoriting = false;
            }
        }.execute();
    }
	
	private View.OnClickListener homepageClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			UIHelper.openBrowser(v.getContext(), softwareDetail.getHomepage());
		}
	};

	private View.OnClickListener docmentClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			UIHelper.openBrowser(v.getContext(), softwareDetail.getDocument());
		}
	};

	private View.OnClickListener downloadClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			UIHelper.openBrowser(v.getContext(), softwareDetail.getDownload());
		}
	};
}