package com.milchstrabe.rainbow.skt.server.session;

import com.milchstrabe.rainbow.skt.server.codc.Data;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ch3ng
 * @Date 2020/4/20 22:12
 * @Version 1.0
 * @Description
 **/
public class SessionManager {

	/**
	 * 在线会话
	 */
	private static final ConcurrentHashMap<String, Session> onlineSessions = new ConcurrentHashMap<>();
	
	/**
	 * 加入
	 * @return
	 */
	public static boolean putSession(String cid, Session session){
		if(!onlineSessions.containsKey(cid)){
			boolean success = onlineSessions.putIfAbsent(cid, session)== null? true : false;
			return success;
		}
		return false;
	}

	/**
	 * 移除
	 * @param cid
	 * @return
	 */
	public static Session removeSession(String cid){
		return onlineSessions.remove(cid);
	}

	/**
	 * 获取当前设备的在线会话
	 * @param cid
	 * @return
	 */
	public static Session getSession(String cid){
		Session session = onlineSessions.get(cid);
		return session;
	}

	/**
	 * 发送消息
	 */
	public static  void sendMessage(String cid,Object object){
		Session session = onlineSessions.get(cid);
		if (session != null && session.isConnected()) {
			Data.Response resp = Data.Response.newBuilder().build();
			session.write(resp);
		}
	}


}
