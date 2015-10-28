/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package net.oschina.app.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.common.StringUtils;
import net.oschina.app.core.AppException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 搜索列表实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class SearchList extends Entity implements PageList<Search> {

	public final static String CATALOG_ALL = "all";
	public final static String CATALOG_NEWS = "news";
	public final static String CATALOG_POST = "post";
	public final static String CATALOG_SOFTWARE = "software";
	public final static String CATALOG_BLOG = "blog";
	public final static String CATALOG_CODE = "code";

	private int pageSize;
	private List<Search> resultlist = new ArrayList<Search>();

	public int getPageSize() {
		return pageSize;
	}
	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public List<Search> getList() {
		return resultlist;
	}

	public static SearchList parse(InputStream inputStream) throws IOException,
			AppException {
		SearchList searchList = new SearchList();
		Search res = null;
		// 获得XmlPullParser解析器
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(inputStream, UTF8);
			// 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (tag.equalsIgnoreCase("pageSize")) {
						searchList.pageSize = StringUtils.toInt(
								xmlParser.nextText(), 0);
					} else if (tag.equalsIgnoreCase("result")) {
						res = new Search();
					} else if (res != null) {
						if (tag.equalsIgnoreCase("objid")) {
							res.setObjid(StringUtils.toInt(xmlParser.nextText(),
									0));
						} else if (tag.equalsIgnoreCase("type")) {
							res.setType(StringUtils.toInt(xmlParser.nextText(),
									0));
						} else if (tag.equalsIgnoreCase("title")) {
							res.setTitle(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("url")) {
							res.setUrl(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pubDate")) {
							res.setPubDate(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("author")) {
							res.setAuthor(xmlParser.nextText());
						}
					}
					// 通知信息
					else if (tag.equalsIgnoreCase("notice")) {
						searchList.setNotice(new Notice());
					} else if (searchList.getNotice() != null) {
						if (tag.equalsIgnoreCase("atmeCount")) {
							searchList.getNotice().setAtmeCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						} else if (tag.equalsIgnoreCase("msgCount")) {
							searchList.getNotice().setMsgCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						} else if (tag.equalsIgnoreCase("reviewCount")) {
							searchList.getNotice().setReviewCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						} else if (tag.equalsIgnoreCase("newFansCount")) {
							searchList.getNotice().setNewFansCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					// 如果遇到标签结束，则把对象添加进集合中
					if (tag.equalsIgnoreCase("result") && res != null) {
						searchList.getList().add(res);
						res = null;
					}
					break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}
		} catch (XmlPullParserException e) {
			throw AppException.xml(e);
		} finally {
			inputStream.close();
		}
		return searchList;
	}
}