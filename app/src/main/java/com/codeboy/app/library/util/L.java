/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.library.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

/**
 * 类名 L.java</br>
 * 创建日期  2012-8-21</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月26日 下午12:16:53</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 调试类
 */
public class L {

	public static final String TAG = "CodeBoy";
	private static String sTagPre = null;

	public static boolean Debug = true;
	private static boolean enableWrite = false;

	public static final String DEBUG = "DEBUG";
	public static final String WARN = "WARN";
	public static final String ERROR = "ERROE";
	public static final String VERBOSE = "VERBOSE";
	public static final String INFO = "INFO";

	public static final String LOG_SUFFIX = "txt";
	private static final String BASE_DIR = android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "codeboy";
	private static final String DEFAULT_LOG_DIR = BASE_DIR + File.separator + "log";

	private static String mLogDir = DEFAULT_LOG_DIR;

	/**
	 * 是否设置调试模式
	 * */
	public static void setDebug(boolean isdebug) {
		Debug = isdebug;
	}

	/**
	 * 是否允许写入SD的日志
	 * */
	public static void setEnableWriteLog(boolean enable) {
		enableWrite = enable;
	}

	/**
	 * 设置日志的保存目录，默认为/sdcard/Immortal/.log/common
	 * @param dir_path
	 * */
	public static void setupLogDir(String dir_path) {
		mLogDir = dir_path;
	}

	/**
	 * 设置前缀TAG. Logcat的TAG标签将会以一下方式展示</br>
	 * <pre>
	 * pretag|class|method|line
	 * </pre>
	 * 使用preTag可以方便的与其他程序产生的日志进行分离
	 * @param preTag
	 */
	public static void setPreTag(String preTag) {
		sTagPre = preTag;
	}

	/**
	 * 获取日志保存目录
	 * */
	public static String getLogDir() {
		return mLogDir;
	}

	/**
	 * 调试级别info
	 * @param str
	 * @param tr
	 * */
	public static void i(String msg, Throwable tr) {
		if (!Debug) {
			return;
		}
		i(getLogTag(), msg, tr);
	}

	/**
	 * 调试级别info
	 * @param str
	 * */
	public static void i(String msg) {
		if (!Debug) {
			return;
		}
		i(getLogTag(), msg, (Throwable) null);
	}

	/**
	 * 调试级别info
	 * @param tag
	 * @param str
	 * */
	public static void i(Object tag, String msg) {
		i(tag, msg, (Throwable) null);
	}

	/**
	 * 调试级别info
	 * @param tag
	 * @param msg
	 * @param tr
	 * */
	public static void i(Object tag, String msg, Throwable tr) {
		log(INFO, tag, msg, tr);
	}

	/**
	 * 调试级别info
	 * @param tag
	 * @param format 格式化字符串
	 * @param objects 参数
	 * */
	public static void i(Object tag, String format, Object... objects) {
		log(INFO, tag, format, objects);
	}

	/**
	 * 调试级别 error
	 * @param str
	 * */
	public static void e(String str) {
		if (!Debug) {
			return;
		}
		e(getLogTag(), str, (Throwable) null);
	}

	/**
	 * 调试级别 error
	 * @param str
	 * @param tr
	 * */
	public static void e(String msg, Throwable tr) {
		if (!Debug) {
			return;
		}
		e(getLogTag(), msg, tr);
	}

	/**
	 * 调试级别 error
	 * @param tag
	 * @param str
	 * */
	public static void e(Object tag, String msg) {
		if (!Debug) {
			return;
		}
		e(tag, msg, (Throwable) null);
	}

	/**
	 * 调试级别 error
	 * @param tag
	 * @param msg
	 * @param tr
	 * */
	public static void e(Object tag, String msg, Throwable tr) {
		log(ERROR, tag, msg, tr);
	}

	/**
	 * 调试级别 error
	 * @param tag
	 * @param format 格式化字符串
	 * @param objects 参数
	 * */
	public static void e(Object tag, String format, Object... objects) {
		if (!Debug) {
			return;
		}
		log(ERROR, tag, format, objects);
	}

	/**
	 * 调试级别 verbose
	 * @param msg
	 * @param tr
	 * */
	public static void v(String msg, Throwable tr) {
		if (!Debug) {
			return;
		}
		v(getLogTag(), msg, tr);
	}

	/**
	 * 调试级别 verbose
	 * @param msg
	 * */
	public static void v(String msg) {
		if (!Debug) {
			return;
		}
		v(getLogTag(), msg, (Throwable) null);
	}

	/**
	 * 调试级别 verbose
	 * @param tag
	 * @param msg
	 * */
	public static void v(Object tag, String str) {
		v(tag, str, (Throwable) null);
	}

	/**
	 * 调试级别 verbose
	 * @param tag
	 * @param msg
	 * @param tr
	 * */
	public static void v(Object tag, String msg, Throwable tr) {
		log(VERBOSE, tag, msg, tr);
	}

	/**
	 * 调试级别 verbose
	 * @param tag
	 * @param format 格式化字符串
	 * @param objects 参数
	 * */
	public static void v(Object tag, String format, Object... objects) {
		log(VERBOSE, tag, format, objects);
	}

	/**
	 * 调试级别 debug
	 * @param msg
	 * @param tr
	 * */
	public static void d(String msg, Throwable tr) {
		if (!Debug) {
			return;
		}
		d(getLogTag(), msg, tr);
	}

	/**
	 * 调试级别 debug
	 * @param msg
	 * */
	public static void d(String msg) {
		if (!Debug) {
			return;
		}
		d(getLogTag(), msg, (Throwable) null);
	}

	/**
	 * 调试级别 debug
	 * @param tag
	 * @param msg
	 * */
	public static void d(Object tag, String msg) {
		d(tag, msg, (Throwable) null);
	}

	/**
	 * 调试级别 debug
	 * @param tag
	 * @param msg
	 * @param tr
	 * */
	public static void d(Object tag, String msg, Throwable tr) {
		log(DEBUG, tag, msg, tr);
	}

	/**
	 * 调试级别 debug
	 * @param tag
	 * @param format 格式化字符串
	 * @param objects 参数
	 * */
	public static void d(Object tag, String format, Object... objects) {
		log(DEBUG, tag, format, objects);
	}

	/**
	 * 调试级别 warning
	 * @param msg
	 * */
	public static void w(String msg) {
		if (!Debug) {
			return;
		}
		w(getLogTag(), msg, (Throwable) null);
	}

	/**
	 * 调试级别 warning
	 * @param msg
	 * @param tr
	 * */
	public static void w(String msg, Throwable tr) {
		if (!Debug) {
			return;
		}
		w(getLogTag(), msg, tr);
	}

	/**
	 * 调试级别 warning
	 * @param tag
	 * @param msg
	 * */
	public static void w(Object tag, String msg) {
		w(tag, msg, (Throwable) null);
	}

	/**
	 * 调试级别 warning
	 * @param tag
	 * @param msg
	 * @param tr
	 * */
	public static void w(Object tag, String msg, Throwable tr) {
		log(WARN, tag, msg, tr);
	}

	/**
	 * 调试级别 warning
	 * @param tag
	 * @param format 格式化字符串
	 * @param objects 参数
	 * */
	public static void w(Object tag, String format, Object... objects) {
		log(WARN, tag, format, objects);
	}

	private static void log(String type, Object obj, String msg, Throwable tr) {
		String tag = String.valueOf(obj);
		if (enableWrite && (WARN.equals(type) || ERROR.equals(type) || VERBOSE.equals(type))) {
			writeLogFile(getLogStyle(type, tag, msg, tr));
		}

		if (!Debug) {
			return;
		}

		if (WARN.equals(type)) {
			Log.w(tag, msg, tr);
		} else if (ERROR.equals(type)) {
			Log.e(tag, msg, tr);
		} else if (DEBUG.equals(type)) {
			Log.d(tag, msg, tr);
		} else if (INFO.equals(type)) {
			Log.i(tag, msg, tr);
		} else if (VERBOSE.equals(type)) {
			Log.v(tag, msg, tr);
		}
	}

	/**
	 * 获取类名与方法名
	 * */
	private static String getLogTag() {
		String tag = TAG;
		try {
			StackTraceElement stes[] = Thread.currentThread().getStackTrace();
			StackTraceElement ste = stes[4];
			String fileName = ste.getFileName();
			tag = (TextUtils.isEmpty(sTagPre) ? "" : sTagPre + "|")
			        + fileName.substring(0, fileName.lastIndexOf(".")) + "|" + ste.getMethodName()
			        + "|" + ste.getLineNumber();
		} catch (Exception e) {
		}
		return tag;
	}

	private static void log(String type, Object obj, String format, Object... objects) {
		String tag = String.valueOf(obj);
		String msg = null;
		try {
			msg = String.format(format, objects);
		} catch (Exception e) {
			msg = String.valueOf(objects);
		}

		/** 写入日志*/
		if (enableWrite && (WARN.equals(type) || ERROR.equals(type) || VERBOSE.equals(type))) {
			writeLogFile(getLogStyle(type, tag, msg, null));
		}

		if (!Debug) {
			return;
		}

		if (WARN.equals(type)) {
			Log.w(tag, msg);
		} else if (ERROR.equals(type)) {
			Log.e(tag, msg);
		} else if (DEBUG.equals(type)) {
			Log.d(tag, msg);
		} else if (INFO.equals(type)) {
			Log.i(tag, msg);
		} else if (VERBOSE.equals(type)) {
			Log.v(tag, msg);
		}
	}

	public static String getLogStyle(String type, Object tag, String msg, Throwable tr) {
		StringBuilder log = new StringBuilder();
		String date = DateFormat.format("yyyy-MM-dd aa hh:mm:ss", System.currentTimeMillis())
		                        .toString();
		log.append("[");
		log.append(date);
		log.append("]");
		log.append("[");
		log.append(type);
		log.append("]");
		log.append("[");
		log.append(tag);
		log.append("]");
		log.append(msg);
		if (tr != null) {
			log.append(" ");
			log.append(getStackTraceString(tr));
		}
		return log.toString();
	}

	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		return sw.toString();
	}

	public static void Toast(String str, Context context) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void Toast(int resId, Context context) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 字符集的测试,在乱码状态下使用
	 * @param datastr
	 * <p>传入需要测试的字符串</p>
	 * */
	public static void testCharset(String datastr) {
		try {
			String temp = new String(datastr.getBytes(), "GBK");
			L.v("****** getBytes() -> GBK ******\n" + temp);

			temp = new String(datastr.getBytes("GBK"), "UTF-8");
			L.v("****** GBK -> UTF-8 *******\n" + temp);

			temp = new String(datastr.getBytes("GBK"), "ISO-8859-1");
			L.v("****** GBK -> ISO-8859-1 *******\n" + temp);

			temp = new String(datastr.getBytes("ISO-8859-1"), "UTF-8");
			L.v("****** ISO-8859-1 -> UTF-8 *******\n" + temp);

			temp = new String(datastr.getBytes("ISO-8859-1"), "GBK");
			L.v("****** ISO-8859-1 -> GBK *******\n" + temp);

			temp = new String(datastr.getBytes("UTF-8"), "GBK");
			L.v("****** UTF-8 -> GBK *******\n" + temp);

			temp = new String(datastr.getBytes("UTF-8"), "ISO-8859-1");
			L.v("****** UTF-8 -> ISO-8859-1 *******\n" + temp);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将byte数组以16进制形式输出
	 * */
	public synchronized static void writeLog(byte log[]) {
		String slog = "";
		for (int i = 0; i < log.length; i++) {
			slog += " " + L.toHex(log[i]);
		}
		L.writeLogFile(slog);
	}

	/**
	 * 写入调试日志
	 * @param log 要写入的调试内容
	 * */
	public synchronized static void writeLogFile(String log) {
		long currentTime = System.currentTimeMillis();
		//保存同一小时内的日志
		String logFileName = DateFormat.format("yyyy-MM-dd_hh", currentTime).toString();
		writeLogFile(logFileName + "." + LOG_SUFFIX, log);
	}

	/**
	 * 写入调试日志
	 * @param filename 要保存的文件名
	 * @param content 要写入的调试内容
	 * */
	public synchronized static void writeLogFile(String filename, String content) {
		try {
			File saveLogDir = new File(mLogDir);
			if (!saveLogDir.exists()) {
				saveLogDir.mkdirs();
			}
			File fileName = new File(saveLogDir, filename);
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content + "\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取设备与应用的详细信息
	 * @param context 上下文
	 * */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String getDeviceInfo(Context context) {
		StringBuilder builder = new StringBuilder();
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			builder.append("----- 应用程序信息 ------").append("\n");
			builder.append("应用程序包名:").append(info.packageName).append("\n");
			builder.append("版本信息:").append(info.versionName).append("\n");
			builder.append("版本号:").append(info.versionCode).append("\n");
			if (Util.hasGingerbread()) {
				builder.append("安装时间:").append(Util.getFormatDateString(info.firstInstallTime))
				       .append("\n");
			}
		} catch (Exception e1) {
		}
		try {
			builder.append("\n\n----- 设备信息 ----\n");
			builder.append("DEVICE ").append(Build.DEVICE).append("\n");
			builder.append("ID ").append(Build.ID).append("\n");
			builder.append("MANUFACTURER ").append(Build.MANUFACTURER).append("\n");
			builder.append("MODEL ").append(Build.MODEL).append("\n");
			builder.append("PRODUCT ").append(Build.PRODUCT).append("\n");
			builder.append("VERSION_CODES.BASE ").append(Build.VERSION_CODES.BASE)
			       .append("\n");
			builder.append("VERSION.RELEASE ").append(Build.VERSION.RELEASE)
			       .append("\n");
			builder.append("SDK").append(Build.VERSION.SDK_INT).append("\n");
		} catch (Exception e) {
		}
		return builder.toString();
	}

	/**
	 * 将byte转为16进制
	 * @param b
	 * @return
	 * */
	public static final String toHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF".charAt(b & 0xf));
	}

	public synchronized static void buildLogMessage(Throwable ex) {
		StringBuilder builder = new StringBuilder();
		if (ex != null) {
			builder.append("\n\n").append("------ 程序错误信息 -------").append("\n")
			       .append(L.getLogStyle(L.ERROR, TAG, "", ex));
		}
		long currentTime = System.currentTimeMillis();
		String logFileName = DateFormat.format("yyyy-MM-dd_hh_mm", currentTime).toString();
		writeLogFile(logFileName + "." + LOG_SUFFIX, builder.toString());
	}
}
