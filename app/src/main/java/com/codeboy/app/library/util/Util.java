/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.library.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 类名 Util.java</br>
 * 创建日期 2013-4-3</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月26日 下午12:17:29</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 常用工具类
 */
public class Util {

	/**
	 * 根据文件名去获取文件后缀名
	 * 
	 * @param fileName
	 *            文件名
	 * */
	public static String getExtFromFileName(String fileName) {
		// 获取文件的扩展名
		String expName = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();
		return expName;
	}

	/**
	 * 转换文件大小的显示格式
	 * 
	 * @param file
	 *            文件的File对象
	 * */
	public static String formatSizeString(File file) {
		String fileSize = "";
		if (file.isFile()) {
			long length = file.length();
			fileSize = formatSizeString(length);
		}
		return fileSize;
	}

	/**
	 * 转换文件大小的显示格式
	 * 
	 * @param size
	 *            文件大小
	 * @return eg: 120KB
	 * */
	public static String formatSizeString(long size) {
		int sub_index = 0;
		String fileSize = "";
		long length = size;
		if (length >= 1073741824) {
			sub_index = (String.valueOf((float) length / 1073741824))
					.indexOf(".");
			fileSize = ((float) length / 1073741824 + "000").substring(0,
					sub_index + 3) + "GB";
		} else if (length >= 1048576) {
			sub_index = (String.valueOf((float) length / 1048576)).indexOf(".");
			fileSize = ((float) length / 1048576 + "000").substring(0,
					sub_index + 3) + "MB";
		} else if (length >= 1024) {
			sub_index = (String.valueOf((float) length / 1024)).indexOf(".");
			fileSize = ((float) length / 1024 + "000").substring(0,
					sub_index + 3) + "KB";
		} else {
			fileSize = String.valueOf(length) + "B";
		}
		return fileSize;
	}

	/**
	 * 获取apk文件的图标
	 * 
	 * @param context
	 * @param path
	 *            apk文件路径
	 * */
	public static Drawable getApkIcon(Context context, String path) {
		PackageManager manager = context.getPackageManager();
		PackageInfo packageInfo = manager.getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
		if (null != packageInfo) {
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			try {
				return manager.getApplicationIcon(appInfo);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 获取apk文件的图标
	public static Bitmap getAPKIcon(Context context, String apkPath) {
		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		Drawable draw = null;
		Resources mResources = context.getResources();
		try {
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			// PackageParser packageParser = new PackageParser(apkPath);
			Class<?>[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor<?> pkgParserCt = Class.forName(PATH_PackageParser)
					.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();

			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = Class
					.forName(PATH_PackageParser).getDeclaredMethod(
							"parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = 0;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			// ApplicationInfo info = mPkgInfo.applicationInfo;
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);
			// uid 输出为"-1"，原因是未安装，系统未分配其Uid。

			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Constructor<?> assetMagCt = assetMagCls
					.getConstructor((Class[]) null);
			Object assetMag = assetMagCt.newInstance((Object[]) null);
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = mResources.getDisplayMetrics().getClass();
			typeArgs[2] = mResources.getConfiguration().getClass();
			Constructor<?> resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = mResources.getDisplayMetrics();
			valueArgs[2] = mResources.getConfiguration();
			mResources = (Resources) resCt.newInstance(valueArgs);

			// 这里就是读取一个apk程序的图标
			if (info.icon != 0) {
				draw = mResources.getDrawable(info.icon);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == draw)
			return null;
		return ((BitmapDrawable) draw).getBitmap();
	}

	/**
	 * 获取文件名，不加上扩展名
	 * 
	 * @param name
	 *            文件全名
	 * @return
	 * */
	public static String getNameFromFileName(String name) {
		int index = name.lastIndexOf(".");
		if (index != -1) {
			name = name.substring(0, index);
		}
		return name;
	}

	/**
	 * 根据文件路径获取文件名
	 * 
	 * @param path
	 * @return
	 * */
	public static String getNameFromFilepath(String path) {
		int index = path.lastIndexOf(File.separator);
		String name = "";
		if (index != -1) {
			name = path.substring(index + 1);
		} else {
			name = path;
		}
		return name;
	}

	/**
	 * 获取SD卡的信息 {@link com.codeboy.app.library.util.Util.SDCardInfo}
	 * */
	public static SDCardInfo getSDCardInfo() {
		if (isSDCardReady()) {
			StatFs statfs = new StatFs(getSdDirectory());
			long l1 = statfs.getBlockCount();
			long l2 = statfs.getBlockSize();
			long l3 = statfs.getAvailableBlocks();
			int free = statfs.getFreeBlocks();
			L.d("free blocks=" + free);
			SDCardInfo info = new SDCardInfo();
			info.total = l1 * l2;
			info.free = l3 * l2;
			return info;
		}
		return null;
	}

	/** 获取SD卡的目录路径 */
	public static String getSdDirectory() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/** 将字符串转为long */
	public static long getFormatMillisTime(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date d = format.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getSimPleDataFormatDataString(String f, long timeInMs) {
		SimpleDateFormat format = new SimpleDateFormat(f);
		Date date = new Date(timeInMs);
		try {
			return format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回格式化后的日期字符串
	 * 
	 * @param date
	 *            GMT时间,从1970.1.1开始的微秒
	 * */
	public static String getFormatDateString(long date) {
		return getFormatDateString("yyyy/MM/dd aa HH:mm:ss", date);
	}

	/**
	 * 自定义格式化后的日期字符串
	 * */
	public static String getFormatDateString(String formatstr, long time) {
		SimpleDateFormat format = new SimpleDateFormat(formatstr);
		Date d = new Date(time);
		return format.format(d);
	}

	/**
	 * 自定义格式化后的Date
	 * 
	 * @return {@link java.util.Date}
	 * */
	public static Date getFormatDate(String formatStr, String parse) {
		if (isEmpty(parse))
			return new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = format.parse(parse);
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date(System.currentTimeMillis());
		}
		return date;
	}

	/** 获取'时间'字符串 **/
	public static String getStrTime(int time) {
		int min = 0;
		int sec = 0;
		String strTime = "";
		time /= 1000;
		if (time < 60) {
			min = 0;
			sec = time;
		} else {
			min = time / 60;
			sec = time % 60;
		}
		if (min < 10) {
			strTime += "0" + min + ":";
		} else {
			strTime += min + ":";
		}
		if (sec < 10) {
			strTime += "0" + sec;
		} else {
			strTime += sec;
		}

		return strTime;
	}

	/**
	 * 拷贝文件
	 * */
	public static void copyFile(String fromFilePath, String toFilePath) {
		try {
			BufferedInputStream reader = new BufferedInputStream(
					new FileInputStream(new File(fromFilePath)));
			File saveFile = new File(toFilePath);
			int count = 0;
			while (saveFile.exists()) {
				saveFile = new File(toFilePath + "_" + (count++));
			}
			BufferedOutputStream writer = new BufferedOutputStream(
					new FileOutputStream(saveFile));
			try {
				byte buff[] = new byte[8192];
				int offset;
				while ((offset = reader.read(buff, 0, buff.length)) != -1) {
					writer.write(buff, 0, offset);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						writer.close();
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap decodeFile(File f, int maxSize) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > maxSize || o.outWidth > maxSize) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(maxSize
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 获取bitmap
	 * 
	 * @param path
	 *            本地图片的路径
	 * @param size
	 *            获取bitmap的大小
	 * @return
	 * */
	public static Bitmap getBitmap(String path, int size) {
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, size);
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(path, opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			return null;
		}
		return bmp;
	}

	/**
	 * 计算返回bitmap的大小
	 * */
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 格式化为秒
	 * */
	public static String getTimeWithSecond(long second) {
		long min = 0;
		long sec = 0;
		String str = "";
		if (second < 60) {
			min = 0;
			sec = second;
			str = sec + "秒";
		} else {
			min = second / 60;
			sec = second % 60;
			str = min + "分" + sec + "秒";
		}
		return str;
	}

	/**
	 * 简单判断是不是合法的手机号码
	 * */
	public static boolean isMobileNumber(String number) {
		if (number == null || number.length() != 11)
			return false;
		String reg = "(13|15|18)[0-9]{9}";
		CharSequence inputStr = number;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(inputStr);
		return m.matches();
	}

	/**
	 * 判断是不是正确的Email格式
	 * 
	 * @param email
	 *            电子邮箱的字符串
	 * @return
	 * */
	public static boolean isEmail(String email) {
		if (isEmpty(email))
			return false;
		Pattern emailPattern = Pattern
				.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		if (emailPattern.matcher(email).matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是不是手机号码
	 * */
	public static boolean isNumber(String n) {
		if (isEmpty(n))
			return false;
		return Pattern.compile("[0-9]*").matcher(n).matches();
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @return
	 * */
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 检查是否有sd卡
	 * 
	 * @param requireWriteAccess
	 *            检查是否需要可写权限
	 * @return
	 * */
	public static boolean hasStorage(boolean requireWriteAccess) {
		String state = Environment.getExternalStorageState();
		if(L.Debug){
			L.d("storage state is " + state);
		}

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			if (requireWriteAccess) {
				boolean writable = Environment.getExternalStorageDirectory()
						.canWrite();
				return writable;
			} else {
				return true;
			}
		} else if (!requireWriteAccess
				&& Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/** 判断Sd卡是否已经挂载 */
	public static boolean isSDCardReady() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 计算一个文件或文件夹下的总大小
	 * 
	 * @param file
	 *            要计算的文件或文件夹
	 * */
	public static long getFilesSize(File file) {
		long size = 0;
		if (file.isDirectory() == true) {
			File[] files = file.listFiles();
			for (File f : files) {
				size += getFilesSize(f);
			}
		} else {
			size = file.length();
		}
		return size;
	}

	/**
	 * 删除文件或文件夹下的所有文件
	 * 
	 * @param file要删除的文件或文件夹
	 * */
	public static boolean deleteFiles(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles())
				deleteFiles(f);
		}
		return file.delete();
	}

	/** 检查读写权限 */
	public static boolean checkCanWrite(String path) {
		File file = new File(path);
		return file.canWrite();
	}

	public static class SDCardInfo {
		public long free;
		public long total;
	}

	/**
	 * 判断字符串是否为空 {@linkplain isEmpty}
	 * */
	public static boolean isStringNull(String str) {
		return isEmpty(str);
	}

	/**
	 * 保存Bitmap到文件中
	 * 
	 * @param photobitmap 要保存的bitmap
	 * @param saveFile 要保存成的文件
	 * @param format 输出文件的格式
	 * @param quality 输出文件的质量
	 * */
	public static boolean saveBitmap(Bitmap photobitmap, File saveFile,
			CompressFormat format, int quality) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(saveFile);
			photobitmap.compress(format, quality, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 将输入流写出到输出流
	 * 
	 * @param is 输入流
	 * @param out 输出流
	 * @throws java.io.IOException
	 * */
	public static void copyStream(InputStream is, OutputStream out)
			throws IOException {
		copyStream(is, out, 1024 * 16);
	}

	/**
	 * 将输入流写出到输出流
	 * 
	 * @param is
	 *            输入流
	 * @param out
	 *            输出流
	 * @param buffer_size
	 *            缓存区大小
	 * @throws java.io.IOException
	 * */
	public static void copyStream(InputStream is, OutputStream out,
			int buffer_size) throws IOException {
		byte[] buffer = new byte[buffer_size];
		int offset = 0;
		while ((offset = is.read(buffer)) != -1) {
			out.write(buffer, 0, offset);
		}
		out.flush();
	}

	/**
	 * 获取文件的内容
	 * 
	 * @param file
	 *            要获取的文件
	 * @param charset
	 *            字符编码
	 * @throws java.io.IOException
	 * */
	public static String getFileStringBuffer(File file, String charset)
			throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,
				charset));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line).append("\n");
		}
		reader.close();
		return builder.toString();
	}

	/**
	 * 获取文件的内容
	 * 
	 * @param file
	 *            要获取的文件,默认编码为UTF-8
	 * @throws java.io.IOException
	 * */
	public static String getFileStringBuffer(File file) throws IOException {
		return getFileStringBuffer(file, "UTF-8");
	}

	/**
	 * sdk 4 android 1.6
	 * */
	public static boolean hasDonut() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
	}

	/**
	 * sdk 5 android 2.0
	 * */
	public static boolean hasEclair() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * sdk 7 android 2.1
	 * */
	public static boolean hasEclair_MR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1;
	}

	/**
	 * sdk 8 android 2.2
	 * */
	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * sdk 9 android 2.3
	 * */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * sdk 11 android 3.0
	 * */
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * sdk 12 android 3.1
	 * */
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * sdk 16 android 4.1
	 * */
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
	
	/**
	 * sdk 17 android 4.2
	 * */
	public static boolean hasJellyBean_MR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
	}
	
	/**
	 * sdk 18 android 4.3
	 * */
	public static boolean hasJellyBean_MR2() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	}
	
	/**
	 * sdk 19 android 4.4
	 * */
	public static boolean hasKitkat() {
		return Build.VERSION.SDK_INT >= 19;
	}

	/**
	 * 获取手机的IMEI值
	 * */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = null;
		try {
			imei = tm.getDeviceId();
		} catch (Exception e) {
		}
		return imei;
	}
	
	/**
	 * 获取手机的IMSI值
	 * */
	public static String getSubscriberId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = null;
		try {
			imsi = tm.getSubscriberId();
		} catch (Exception e) {
		}
		return imsi;
	}

	/**
	 * 获取手机型号
	 * */
	public static String getModel() {
		return Build.MODEL;
	}

	/***
	 * 以字母开头，长度在6~18之间，只能包含字符、数字和下划线
	 * @param str 要检测的字符串
	 * */
	public static boolean isSpecialChar(String str) {
		return str.matches("^[a-zA-Z]\\w{5,17}$");
	}
}