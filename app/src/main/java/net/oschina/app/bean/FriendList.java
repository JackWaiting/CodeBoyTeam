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
 * 好友列表实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FriendList extends Entity implements PageList<Friend> {

	public final static int TYPE_FANS = 0x00;
	public final static int TYPE_FOLLOWER = 0x01;

	private List<Friend> friendlist = new ArrayList<Friend>();
	
	@Override
	public int getPageSize() {
		return friendlist.size();
	}

	@Override
	public int getCount() {
		return friendlist.size();
	}

	@Override
	public List<Friend> getList() {
		return friendlist;
	}

	public static FriendList parse(InputStream inputStream) throws IOException,
			AppException {
		FriendList friendlist = new FriendList();
		Friend friend = null;
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
					if (tag.equalsIgnoreCase("friend")) {
						friend = new Friend();
					} else if (friend != null) {
						if (tag.equalsIgnoreCase("userid")) {
							friend.setUserid(StringUtils.toInt(
									xmlParser.nextText(), 0));
						} else if (tag.equalsIgnoreCase("name")) {
							friend.setName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("portrait")) {
							friend.setFace(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("expertise")) {
							friend.setExpertise(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("gender")) {
							friend.setGender(StringUtils.toInt(
									xmlParser.nextText(), 0));
						}
					}
					// 通知信息
					else if (tag.equalsIgnoreCase("notice")) {
						friendlist.setNotice(new Notice());
					} else if (friendlist.getNotice() != null) {
						if (tag.equalsIgnoreCase("atmeCount")) {
							friendlist.getNotice().setAtmeCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						} else if (tag.equalsIgnoreCase("msgCount")) {
							friendlist.getNotice().setMsgCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						} else if (tag.equalsIgnoreCase("reviewCount")) {
							friendlist.getNotice().setReviewCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						} else if (tag.equalsIgnoreCase("newFansCount")) {
							friendlist.getNotice().setNewFansCount(
									StringUtils.toInt(xmlParser.nextText(), 0));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					// 如果遇到标签结束，则把对象添加进集合中
					if (tag.equalsIgnoreCase("friend") && friend != null) {
						friendlist.friendlist.add(friend);
						friend = null;
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
		return friendlist;
	}
}
