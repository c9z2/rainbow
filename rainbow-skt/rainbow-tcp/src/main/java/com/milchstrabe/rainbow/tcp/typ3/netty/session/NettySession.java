package com.milchstrabe.rainbow.tcp.typ3.netty.session;

import com.milchstrabe.rainbow.api.netty.codc.Data;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @Author ch3ng
 * @Date 2020/4/20 22:05
 * @Version 1.0
 * @Description netty session warp class
 **/
public class NettySession implements Session<SessionAttribute> {

	/**
	 * 绑定对象key
	 */
	public static AttributeKey<SessionAttribute> ATTACHMENT_KEY  = AttributeKey.valueOf("ATTACHMENT_KEY");

	/**
	 * 实际会话对象
	 */
	private Channel channel;


	public NettySession(Channel channel) {
		this.channel = channel;
	}

	@Override
	public SessionAttribute getAttachment() {
		return channel.attr(ATTACHMENT_KEY).get();
	}

	@Override
	public void setAttachment(SessionAttribute attachment) {
		channel.attr(ATTACHMENT_KEY).set(attachment);
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public void removeAttachment() {
		channel.attr(ATTACHMENT_KEY).remove();
	}

	@Override
	public void write(Data.Response response) {
		channel.writeAndFlush(response);
	}

	@Override
	public boolean isConnected() {
		return channel.isActive();
	}

	@Override
	public void close() {
		channel.close();
	}



}
