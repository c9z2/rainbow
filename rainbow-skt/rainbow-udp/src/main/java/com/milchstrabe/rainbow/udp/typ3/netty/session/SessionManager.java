package com.milchstrabe.rainbow.udp.typ3.netty.session;

import com.milchstrabe.rainbow.base.server.codc.Data;
import com.milchstrabe.rainbow.exception.LogicException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
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
	private static final ConcurrentHashMap<String, Map<String, Session>> onlineSessions = new ConcurrentHashMap<>();


	public static Integer countOnline(){
		Iterator<Map.Entry<String, Map<String, Session>>> iterator = onlineSessions.entrySet().iterator();
		int count = 0;
		while (iterator.hasNext()){
			Map.Entry<String, Map<String, Session>> next = iterator.next();
			Map<String, Session> value = next.getValue();
			count += value.size();
		}
		return count;
	}

	/**
	 * add session
	 * @return
	 */
	public static boolean putSession(String username,String cid, Session session){
		Map<String, Session> map = onlineSessions.get(username);
		map = Optional.ofNullable(map).orElse(new HashMap<>());
		if(!map.containsKey(cid)){
			boolean success = map.putIfAbsent(cid, session)== null? true : false;
			onlineSessions.put(username,map);
			return success;
		}
		return false;
	}

	/**
	 * remove session
	 * @param cid
	 * @param username
	 * @return
	 */
	public static boolean removeSession(String username ,String cid){
		Map<String, Session> map = onlineSessions.get(username);
		if(map.size() == 1){
			onlineSessions.remove(username);
		}else{
			map.remove(cid);
		}
		return true;
	}

	/**
	 * get online session
	 * @param cid
	 * @return
	 */
	public static Session getSession(String username, String cid) throws LogicException {
		Map<String, Session> map = onlineSessions.get(username);
		Optional.ofNullable(map).orElseThrow(()->new LogicException(5006,"user not sign in"));
		Session session = map.get(cid);
		Optional.ofNullable(session).orElseThrow(()->new LogicException(5007,"user not online"));
		return session;
	}

	/**
	 * get online session
	 * @return
	 */
	public static Map<String, Session> getSession(String username) throws LogicException {
		Map<String, Session> map = onlineSessions.get(username);
		Optional.ofNullable(map).orElseThrow(()->new LogicException(5002,"user not sign in"));
		return map;
	}

	/**
	 * send message
	 * @param cid
	 * @param username
	 * @param response
	 */
	public static void sendMessage(String username, String cid, Data.Response response) throws LogicException {
		Session session = getSession(username, cid);
		if(!session.isConnected()){
			throw new LogicException(5005,"connection err");
		}
		session.write(response);
		}
}



