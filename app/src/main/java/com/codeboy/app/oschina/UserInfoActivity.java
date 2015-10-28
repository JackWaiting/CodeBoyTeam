/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.oschina.app.bean.FriendList;
import net.oschina.app.bean.MyInformation;
import net.oschina.app.bean.Result;
import net.oschina.app.common.FileUtils;
import net.oschina.app.common.ImageUtils;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppConfig;
import net.oschina.app.core.AppException;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeboy.app.library.util.Util;

/**
 * 用户资料
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */

/**
 * 类名 UserInfoActivity.java</br>
 * 创建日期 2014年4月26日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月26日 上午10:02:03</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 用户资料界面
 */
public class UserInfoActivity extends BaseActionBarActivity implements OnClickListener {
	
	//头像截图的大小
	private final static int CROP_SIZE = 200;
	//头像保存目录
	private final static String FILE_SAVEPATH = 
			AppConfig.DEFAULT_IMAGE_PORTRAIT_PATH;
	
	private final static int REFLASH_ITEM_ID = 100;
	
	private final static int LOGIN_REQUEST = 2014;

	private ImageView face;
	private ImageView gender;
	private Button editer;
	private TextView name;
	private TextView jointime;
	private TextView from;
	private TextView devplatform;
	private TextView expertise;
	private TextView followers;
	private TextView fans;
	private TextView favorites;
	
	private MyInformation user;
	
	private ProgressDialog loadingDialog;
	//控制加载用户数据
	private boolean isLoaddingUserInfo = false;

	private Uri photoUri;
	private File protraitFile;
	private Bitmap protraitBitmap;
	
	private OSChinaApplication application;
	
	private Menu optionsMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//须要在setContentView 前调用
		supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
		
		ActionBar bar = getSupportActionBar();
		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
        bar.setDisplayOptions(change, flags);
        
        application = getOsChinaApplication();

		//先判断用户是否已经登录了
		if(!application.isLogin()) {
			//跳转登录
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, LOGIN_REQUEST);
		} else {
			setupView();
		}
	}
	
	private void setupView() {
		setContentView(R.layout.activity_user_info);
		// 初始化视图控件
		initView();
		// 加载用户数据
		loadUserInfo(false);
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		optionsMenu = menu;
		//刷新按钮
		MenuItem reflashItem = menu.add(0, REFLASH_ITEM_ID, 
				100, R.string.footbar_refresh);
        reflashItem.setIcon(R.drawable.ic_menu_refresh);
        MenuItemCompat.setShowAsAction(reflashItem, 
        		MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == REFLASH_ITEM_ID) {
			//刷新
			loadUserInfo(true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** 设置刷新状态*/
	private void setRefreshActionButtonState(final boolean refreshing) {
	    if (optionsMenu != null) {
	        final MenuItem refreshItem = optionsMenu.findItem(REFLASH_ITEM_ID);
	        if (refreshItem != null) {
	            if (refreshing) {
	            	MenuItemCompat.setActionView(refreshItem, R.layout.actionbar_indeterminate_progress);
	            } else {
	                MenuItemCompat.setActionView(refreshItem, null);
	            }
	        }
	    }
	}

	/** 初始化界面*/
	private void initView() {
		editer = (Button) findViewById(R.id.user_info_editer);
		editer.setOnClickListener(this);

		face = (ImageView) findViewById(R.id.user_info_userface);
		gender = (ImageView) findViewById(R.id.user_info_gender);
		name = (TextView) findViewById(R.id.user_info_username);
		jointime = (TextView) findViewById(R.id.user_info_jointime);
		from = (TextView) findViewById(R.id.user_info_from);
		devplatform = (TextView) findViewById(R.id.user_info_devplatform);
		expertise = (TextView) findViewById(R.id.user_info_expertise);
		followers = (TextView) findViewById(R.id.user_info_followers);
		fans = (TextView) findViewById(R.id.user_info_fans);
		favorites = (TextView) findViewById(R.id.user_info_favorites);
		
		findViewById(R.id.user_info_favorites_ll).setOnClickListener(this);
		findViewById(R.id.user_info_followers_ll).setOnClickListener(this);
		findViewById(R.id.user_info_fans_ll).setOnClickListener(this);
		
		findViewById(R.id.user_info_logout).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.user_info_logout) {
			onLogoutClick();
			return;
		} else if(id == R.id.user_info_editer) {
			onEditerClick();
			return;
		}
		
		if(!application.isLogin()) {
			return;
		}
		
		if(id == R.id.user_info_favorites_ll) {
			onFavoritesClick();
		} else if(id == R.id.user_info_followers_ll) {
			onFollowersClick();
		} else if(id == R.id.user_info_fans_ll) {
			onFansClick();
		}
	}
	
	/** 
	 * 异步加载用户数据
	 * @param isRefresh 是否刷新，false则表示加载本地的缓存
	 * */
	private void loadUserInfo(final boolean isRefresh) {
		//如果已经正在刷新,则不处理,避免重复加载
		if(isLoaddingUserInfo) {
			return;
		}
		new AsyncTask<Void, Void, Message>() {

			@Override
			protected Message doInBackground(Void... params) {
				Message msg = new Message();
				try {
					MyInformation user = application.getMyInformation(isRefresh);
					msg.what = 1;
					msg.obj = user;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				return msg;
			}

			@Override
			protected void onPreExecute() {
				if(isRefresh) {
					isLoaddingUserInfo = true;
					setRefreshActionButtonState(true);
				}
			}

			@Override
			protected void onPostExecute(Message msg) {
				if(isRefresh) {
					isLoaddingUserInfo = false;
				}
				//如果当前界面已经退出，则不执行以下操作
				if(isFinishing()) {
					return;
				}
				setRefreshActionButtonState(false);
				if (msg.what == 1 && msg.obj != null) {
					user = (MyInformation) msg.obj;

					// 加载用户头像
					UIHelper.showUserFace(face, user.getFace());

					// 用户性别
					if (user.getGender() == 1) {
						gender.setImageResource(R.drawable.widget_gender_man);
					} else {
						gender.setImageResource(R.drawable.widget_gender_woman);
					}

					// 其他资料
					name.setText(user.getName());
					jointime.setText(StringUtils.friendly_time(user
							.getJointime()));
					from.setText(user.getFrom());
					devplatform.setText(user.getDevplatform());
					expertise.setText(user.getExpertise());
					followers.setText(user.getFollowerscount() + "");
					fans.setText(user.getFanscount() + "");
					favorites.setText(user.getFavoritecount() + "");
					
					if(isRefresh) {
						UIHelper.ToastMessage(UserInfoActivity.this, R.string.loaded_userinfo);
					}
				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(UserInfoActivity.this);
				}
			}
			
		}.execute();
	}
	
	private void onLogoutClick() {
		application.logout();
		finish();
	}

	/** 点击了编辑头像*/
	private void onEditerClick() {
		CharSequence[] items = { getString(R.string.img_from_album),
				getString(R.string.img_from_camera) };
		imageChooseItem(items);
	}

	/**点击了收藏 */
	private void onFavoritesClick() {
		UIHelper.showUserFavorite(this);
	}
	
	/** 点击了粉丝*/
	private void onFansClick() {
		int followers = user != null ? user.getFollowerscount() : 0;
		int fans = user != null ? user.getFanscount() : 0;
		UIHelper.showUserFriend(getActivity(), FriendList.TYPE_FANS,
				followers, fans);
	}
	
	/** 点击了关注*/
	private void onFollowersClick() {
		int followers = user != null ? user.getFollowerscount() : 0;
		int fans = user != null ? user.getFanscount() : 0;
		UIHelper.showUserFriend(getActivity(), FriendList.TYPE_FOLLOWER,
				followers, fans);
	}

	/**
	 * 生成输出图片的文件
	 * @return
	 */
	@SuppressLint("NewApi")
	private File getOutputMediaFile() {
		File mediaStorageDir = null;
		if(Util.hasFroyo()) {
			mediaStorageDir = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES);
		} else {
			mediaStorageDir = new File(FILE_SAVEPATH);
		}
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
				.format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}

	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.userinfo_upload_avatar_title)
		.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// 相册选图
				if (item == 0) {
					startImagePick();
				}
				// 手机拍照
				else if (item == 1) {
					startActionCamera();
				}
			}
		});
		 builder.create().show();
	}

	/**
	 * 选择图片裁剪
	 * 
	 * @param output
	 */
	private void startImagePick() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		try {
			startActivityForResult(Intent.createChooser(intent, 
					getString(R.string.userinfo_upload_avatar_choose)),
					ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 相机拍照
	 * 
	 * @param output
	 */
	private void startActionCamera() {
		File file = getOutputMediaFile();
		if(file == null) {
			UIHelper.ToastMessage(this, R.string.tips_check_sdcard);
			return;
		}
		photoUri = Uri.fromFile(file);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(intent,
				ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
	}

	/**
	 * 拍照后裁剪
	 * 
	 * @param data 原始图片
	 */
	private void startActionCrop(Uri data) {
		//截图后保存的文件
		File file = getOutputMediaFile();
		if(file == null) {
			UIHelper.ToastMessage(this, R.string.tips_check_sdcard);
			return;
		}
		protraitFile = file;
		photoUri = Uri.fromFile(protraitFile);
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", photoUri);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP_SIZE);// 输出图片大小
		intent.putExtra("outputY", CROP_SIZE);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		try {
			startActivityForResult(intent,
					ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** 上传新照片 */
	private void uploadNewPhoto() {
		// 获取头像缩略图
		if (protraitFile == null || !protraitFile.exists()) {
			UIHelper.ToastMessage(this, R.string.upload_user_avatar_error2);
			return;
		}
		new AsyncTask<Void, Void, Message>() {

			@Override
			protected Message doInBackground(Void... params) {
				Message msg = new Message();
				// 获取头像缩略图
				protraitBitmap = ImageUtils.loadImgThumbnail(
						protraitFile.getAbsolutePath(), 200, 200);

				if (protraitBitmap != null) {
					try {
						Result res = getOsChinaApplication()
								.updatePortrait(protraitFile);
						if (res != null && res.OK()) {
							// 保存新头像到缓存
							String filename = FileUtils.getFileName(user.getFace());
							ImageUtils.saveImage(UserInfoActivity.this, filename,
									protraitBitmap);
						}
						msg.what = 1;
						msg.obj = res;
					} catch (AppException e) {
						msg.what = -1;
						msg.obj = e;
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					msg.what = -2;
				}
				return msg;
			}
			
			@Override
			protected void onPreExecute() {
				if(loadingDialog == null) {
					loadingDialog = new ProgressDialog(getActivity());
					loadingDialog.setMessage(getString(R.string.upload_user_avatar));
					//设置为不可以关闭的对话框
					loadingDialog.setCancelable(false);
				}
				loadingDialog.show();
			}
			
			@Override
			protected void onPostExecute(Message msg) {
				//如果当前界面已经退出，则不执行以下操作
				if(isFinishing()) {
					return;
				}
				if(loadingDialog != null) {
					loadingDialog.dismiss();
				}
				if (msg.what == 1 && msg.obj != null) {
					Result res = (Result) msg.obj;
					if (res.OK()) {
						// 提示信息
						UIHelper.ToastMessage(UserInfoActivity.this, 
								R.string.upload_user_avatar_success);
						// 显示新头像
						face.setImageBitmap(protraitBitmap);
					} else {
						UIHelper.ToastMessage(UserInfoActivity.this, 
								res.getErrorMessage());
					}
				} else if (msg.what == -1 && msg.obj != null) {
					((AppException) msg.obj).makeToast(UserInfoActivity.this);
				} else if(msg.what == -2) {
					UIHelper.ToastMessage(UserInfoActivity.this, 
							R.string.upload_user_avatar_error2);
				}
			}
			
		}.execute();
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		//如果是从登录回来的
		if(requestCode == LOGIN_REQUEST) {
			if(resultCode == RESULT_OK) {
				//登录成功，初始化界面
				setupView();
			} else {
				//登录取消，结束当前页面
				finish();
			}
			return;
		}
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
			// 拍照后裁剪
			startActionCrop(photoUri);
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
			// 选图后裁剪
			startActionCrop(data.getData());
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
			// 上传新照片
			uploadNewPhoto();
			break;
		}
	}
}