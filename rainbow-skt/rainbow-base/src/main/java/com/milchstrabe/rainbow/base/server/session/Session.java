package com.milchstrabe.rainbow.base.server.session;


import com.milchstrabe.rainbow.base.server.codc.Data;
import io.netty.channel.Channel;

/**
 * @Author ch3ng
 * @Date 2020/4/20 22:01
 * @Version 1.0
 * @Description session abstract interface
 **/
public interface Session<T> {
	
	/**
	 * 会话绑定对象
	 * @return
	 */
	T getAttachment();
	
	/**
	 * 绑定对象
	 * @return
	 */
	void setAttachment(T attachment);

	/**
	 * 获得当前的通道对象
	 * @return
	 */
	Channel getChannel();
	
	/**
	 * 移除绑定对象
	 * @return
	 */
	void removeAttachment();
	
	/**
	 * 向会话中写入消息
	 * @param response
	 */
	void write(Data.Response response);
	
	/**
	 * 判断会话是否在连接中
	 * @return
	 */
	boolean isConnected();
	
	/**
	 * 关闭
	 * @return
	 */
	void close();
}
