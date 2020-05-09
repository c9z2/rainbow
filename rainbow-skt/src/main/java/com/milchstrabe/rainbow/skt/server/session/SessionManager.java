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
	 * online session
	 */
	private static final ConcurrentHashMap<String, Session> onlineSessions = new ConcurrentHashMap<>();


	public static Integer countOnline(){
		 return onlineSessions.size();
	}

	/**
	 * add session
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
	 * remove session
	 * @param cid
	 * @return
	 */
	public static Session removeSession(String cid){
		return onlineSessions.remove(cid);
	}

	/**
	 * get online session
	 * @param cid
	 * @return
	 */
	public static Session getSession(String cid){
		Session session = onlineSessions.get(cid);
		return session;
	}

	/**
	 * send messate
	 * @param cid
	 * @param object
	 */
	public static  void sendMessage(String cid,Object object){
		Session session = onlineSessions.get(cid);
		if (session != null && session.isConnected()) {
			Data.Response resp = Data.Response.newBuilder().build();
			session.write(resp);
		}
	}


}
