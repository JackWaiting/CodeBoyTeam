/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CheckedInputStream;

/**
 * 类名 CRC32.java</br>
 * 创建日期 2014年4月26日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月26日 下午12:16:38</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 说明 CRC32校验算法
 */
public class CRC32 {
	
	/**
	 * 获取校验值的16进制字符串
	 * @param file 要校验的文件
	 * */
	public static String getFileCheckSumValue(File file) throws IOException {
		return Long.toHexString(getCheckSumValue(file));
	}
	
	/**
	 * 获取校验值的16进制字符串
	 * @param path 要校验的文件的路径
	 * */
	public static String getFileCheckSumValue(String path) throws IOException {
		return getFileCheckSumValue(new File(path));
	}
	
	/**
	 * 获取校验值
	 * @param is 要校验的流
	 * */
	public static long getStringCheckSum(String str) throws IOException {
		return getCheckSumValue(str.getBytes());
	}
	
	/**
	 * 获取校验值
	 * @param is 要校验的流
	 * */
	public static long getCheckSumValue(InputStream is) throws IOException {
		CheckedInputStream cis = new CheckedInputStream(is, new java.util.zip.CRC32());
		byte[] buffer = new byte[1024];
		while (cis.read(buffer) >= 0) {}
		return cis.getChecksum().getValue();
	}
	
	/**
	 * 获取校验值
	 * @param file 要校验的文件
	 * */
	public static long getCheckSumValue(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		long value = getCheckSumValue(fis);
		fis.close();
		return value;
	}
	
	/**
	 * 获取校验值
	 * @param path 要校验的文件路径
	 * */
	public static long getCheckSumValue(String path) throws IOException {
		return getCheckSumValue(new File(path));
	}
	
	/**
	 * 获取校验值
	 * @param buffer 要校验的数据
	 * */
	public static long getCheckSumValue(byte buffer[]) throws IOException {
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		crc32.update(buffer);
		return crc32.getValue();
	}
	
	/**
	 * 获取校验值的16进制字符串
	 * @param buffer 要校验的数据
	 * */
	public static String getCheckSumData(byte[] buffer) throws IOException {
		return Long.toHexString(getCheckSumValue(buffer));
	}
	
	/**
	 * 获取校验值的16进制字符串
	 * @param is 要校验的流
	 * */
	public static String getCheckSumData(InputStream is) throws IOException {
		return Long.toHexString(getCheckSumValue(is));
	}
}
