/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import java.io.File;

import net.oschina.app.bean.MyInformation;
import net.oschina.app.common.FileUtils;
import net.oschina.app.common.MethodsCompat;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;

import com.codeboy.app.library.util.L;
import com.codeboy.app.library.util.Util;
import com.codeboy.app.oschina.OSChinaApplication;
import com.codeboy.app.oschina.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 类名 SettingPreferenceFragment.java</br>
 * 创建日期 2014年5月10日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月10日 下午12:36:56</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 系统设置
 */
public class SettingPreferenceFragment extends PreferenceFragment{

	static final int LOGIN_REQUEST_CODE = 111;
	
	private Preference account;
	private Preference myinfo;
	private Preference cache;
	private Preference feedback;
	private Preference update;
	private Preference about;

	private EditTextPreference saveImagePath;

	private CheckBoxPreference httpslogin;
	private CheckBoxPreference loadimage;
	private CheckBoxPreference voice;
	private CheckBoxPreference checkup;
	
	private OSChinaApplication mApplication;
	
	//是否正在检查更新的标识
	private boolean isChecking = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mApplication = getOsChinaApplication();

		// 设置显示Preferences
		addPreferencesFromResource(R.xml.preferences);
		
		// 我的资料
		myinfo = (Preference) findPreference("myinfo");
		myinfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showUserInfo(getActivity());
				return true;
			}
		});

		// 登录、注销
		account = (Preference) findPreference("account");
		setupAccountPreference();
		account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if(mApplication.isLogin()) {
					mApplication.logout();
				} else {
					UIHelper.loginRequest(getActivity(), 
							SettingPreferenceFragment.this, LOGIN_REQUEST_CODE);
				}
				setupAccountPreference();
				return true;
			}
		});

		// 设置保存图片路径
		saveImagePath = (EditTextPreference) findPreference("saveimagepath");
		String path = mApplication.getSaveImagePath();
		saveImagePath.setText(path);
		setupImagePathPreference();
		saveImagePath.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				L.d("onPreferenceChange---->" + newValue);
				String path = (String) newValue;
				File file = new File(path);
				if(file.isFile()) {
					UIHelper.ToastMessage(getActivity(), R.string.settings_saveimage_path_error1);
					return false;
				}
				if(!file.exists()) {
					boolean result = file.mkdirs();
					if(!result) {
						UIHelper.ToastMessage(getActivity(), R.string.settings_saveimage_path_error2);
						return false;
					}
				}
				if(!file.canWrite()) {
					UIHelper.ToastMessage(getActivity(), R.string.settings_saveimage_path_error3);
					return false;
				}
				mApplication.setSaveImagePath(file.getAbsolutePath());
				setupImagePathPreference();
				return true;
			}
		});
		saveImagePath.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if (!FileUtils.checkSaveLocationExists() && !FileUtils.checkExternalSDExists()) {
    				UIHelper.ToastMessage(getActivity(), R.string.tips_check_sdcard);
					return false;
				}
				return true;
			}
		});
				

		// https登录
		httpslogin = (CheckBoxPreference) findPreference("httpslogin");
		httpslogin.setChecked(mApplication.isHttpsLogin());
		setupHttpsloginPreference();
		httpslogin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mApplication.setConfigHttpsLogin(httpslogin.isChecked());
				setupHttpsloginPreference();
				return true;
			}
		});

		// 加载图片loadimage
		loadimage = (CheckBoxPreference) findPreference("loadimage");
		loadimage.setChecked(mApplication.isLoadImage());
		setupLoadImagePreference();
		loadimage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mApplication.setConfigLoadimage(loadimage.isChecked());
				setupLoadImagePreference();
				return true;
			}
		});

		// 提示声音
		voice = (CheckBoxPreference) findPreference("voice");
		voice.setChecked(mApplication.isVoice());
		setupVoicePreference();
		voice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mApplication.setConfigVoice(voice.isChecked());
				setupVoicePreference();
				return true;
			}
		});

		// 启动检查更新
		checkup = (CheckBoxPreference) findPreference("checkup");
		checkup.setChecked(mApplication.isCheckUp());
		checkup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mApplication.setConfigCheckUp(checkup.isChecked());
				return true;
			}
		});

		// 计算缓存大小
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = getActivity().getFilesDir();
		File cacheDir = getActivity().getCacheDir();

		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (Util.hasFroyo()) {
			File externalCacheDir = MethodsCompat.getExternalCacheDir(getActivity());
			fileSize += FileUtils.getDirSize(externalCacheDir);
		}
		if (fileSize > 0)
			cacheSize = FileUtils.formatFileSize(fileSize);

		// 清除缓存
		cache = (Preference) findPreference("cache");
		cache.setSummary(cacheSize);
		cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.clearAppCache(getActivity());
				cache.setSummary("0KB");
				return true;
			}
		});

		// 意见反馈
		feedback = (Preference) findPreference("feedback");
		feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showFeedBack(getActivity());
				return true;
			}
		});

		// 版本更新
		update = (Preference) findPreference("update");
		update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				checkUpdate();
				return true;
			}
		});

		// 关于我们
		about = (Preference) findPreference("about");
		about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showAbout(getActivity());
				return true;
			}
		});

	}
	
	/**账号*/
	private void setupAccountPreference() {
		if (mApplication.isLogin()) {
			myinfo.setEnabled(true);
			
			account.setTitle(R.string.main_menu_logout);
			String summary = "";
			MyInformation info = null;
			try {
				info = mApplication.getMyInformation(false);
				if(info != null && info.getName() != null) {
					summary = info.getName();
				}
			} catch (AppException e) {
				e.printStackTrace();
			}
			account.setSummary(summary);
		} else {
			myinfo.setEnabled(false);
			
			account.setTitle(R.string.main_menu_login);
			account.setSummary(R.string.main_menu_login_tips);
		}
	}
	
	/** 图片保存路径*/
	private void setupImagePathPreference() {
		String path = mApplication.getSaveImagePath();
		String pathSummary = getString(R.string.settings_summary_imagepath, path);
		
		saveImagePath.setSummary(pathSummary);
	}
	
	/** https*/
	private void setupHttpsloginPreference() {
		if (mApplication.isHttpsLogin()) {
			httpslogin.setSummary(R.string.settings_summary_httpslogin_on);
		} else {
			httpslogin.setSummary(R.string.settings_summary_httpslogin_off);
		}
	}
	
	/** 加载图片*/
	private void setupLoadImagePreference() {
		if (mApplication.isLoadImage()) {
			loadimage.setSummary(R.string.settings_summary_loadimage_on);
		} else {
			loadimage.setSummary(R.string.settings_summary_loadimage_off);
		}
	}
	
	/** 声音*/
	private void setupVoicePreference() {
		if (mApplication.isVoice()) {
			voice.setSummary(R.string.settings_summary_voice_on);
		} else {
			voice.setSummary(R.string.settings_summary_voice_off);
		}
	}
	
	/** 检查更新*/
	private void checkUpdate() {
		if(isChecking) {
			return;
		}
		isChecking = true;
		UIHelper.ToastMessage(getActivity(), R.string.UMCheck_checking);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
		        case UpdateStatus.Yes: // has update
		        	if(L.Debug) {
		        		L.d("-->path:" + updateInfo.path);
		        	}
		        	UIHelper.ToastMessage(getActivity(), R.string.UMCheck_foundupdate);
		        	UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		        	UIHelper.ToastMessage(getActivity(), R.string.UMCheck_noupdate);
		            break;
		        case UpdateStatus.Timeout: // time out
		        	UIHelper.ToastMessage(getActivity(), R.string.UMCheck_timeout);
		            break;
		        }
				isChecking = false;
			}
		});
		UmengUpdateAgent.update(getActivity());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) {
			return;
		}
		setupAccountPreference();
	}
}