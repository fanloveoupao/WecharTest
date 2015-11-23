package com.example.testwechar;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyApplication extends Application {
	// 在清单文件里面注册使程序一开始就进行执行onCreate的方法
	public static MultiUserChat userChat = null;
	public static ConnectionConfiguration configuration;
	public static XMPPConnection xmppConnection;
	public static String state = "";
	public static String roomname = "";

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		new Thread() {
			public void run() {
				try {
					// 连接服务器
					configuration = new ConnectionConfiguration(
							"125.65.82.216", 5222, "125.65.82.216");
					xmppConnection = new XMPPConnection(configuration);
					xmppConnection.connect();
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();

	}

}
