package com.example.activity;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bean.Msg;
import com.example.service.ConnecMethod;
import com.example.testwechar.MainActivity;
import com.example.testwechar.MyApplication;
import com.example.testwechar.R;

public class LoginActivity extends Activity {

	private EditText username;
	private EditText password;
	private Button login;
	private Button register;
	public static final int DIALOG_SHOW = 0;
	public static final int DIALOG_CANCLE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
		receive();
		clicklogin();
		click_registrer();
	}

	private void init() {
		// TODO 自动生成的方法存根
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
	}

	private void click_registrer() {
		// TODO 自动生成的方法存根
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (MyApplication.xmppConnection.isConnected()) {
					try {
						MyApplication.xmppConnection.getAccountManager()
								.createAccount(username.getText().toString(),
										password.getText().toString());
						login();
					} catch (XMPPException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "注册失败 ", 1)
								.show();
					}
				}
			}
		});
	}

	private void clicklogin() {
		// TODO 自动生成的方法存根
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				// 登录服务器
				login();
			}

		});
	}

	private void login() {
		// TODO 自动生成的方法存根
		if (MyApplication.xmppConnection.isConnected()) {
			try {
				MyApplication.xmppConnection.login(username.getText()
						.toString(), password.getText().toString());
				MyApplication.userChat = new MultiUserChat(
						MyApplication.xmppConnection,
						"bruse@conference.125.65.82.216");
				MyApplication.userChat.join(username.getText().toString(),
						password.getText().toString());
				// 进行接收消息
				// receive();
				ConnecMethod.getOnLine(this);
				addNewGroup("我的好友");
				addNewGroup("朋友");
				addNewGroup("工作");
				addNewGroup("房间");
				Intent intent = new Intent(getApplicationContext(),
						HomeChar.class);
				startActivity(intent);
				finish();
				//
			} catch (XMPPException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"登录失败\n" + e.getMessage(), 1).show();
			}
		}
	}

	/*
	 * 
	 * 创建组
	 */
	public static boolean addNewGroup(String group) {
		try {
			MyApplication.xmppConnection.getRoster().createGroup(group);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void receive() {
		// TODO 自动生成的方法存根
		MyApplication.xmppConnection.addPacketListener(new PacketListener() {
			// 服务器发消息给android客户端的时候执行
			@Override
			public void processPacket(Packet packet) {
				// TODO 自动生成的方法存根
				// 判断信息是不是群聊的信息,消息分群聊和私聊
				if (packet instanceof Message) {
					// 将packet转换为对象
					Message message = (Message) packet;
					Type type = message.getType();
					// message.getFrom();
					if (type == Type.groupchat) {
						// 群聊
						// 拿到消息的内容
						final String body = message.getBody();
						// 在工作线程里面不能改动UI
						if (MainActivity.instance != null) {
							MainActivity.instance.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO
									// 自动生成的方法存根
									try {
										if (!MainActivity.instance.content
												.equals(body)) {
											Msg msg = new Msg(body,
													Msg.TYPE_RECEIVE);
											MainActivity.instance.arrayList
													.add(msg);
											// 刷新ListView的显示
											MainActivity.instance.adapter
													.notifyDataSetChanged();
											MainActivity.instance.listview
													.setSelection(MainActivity.instance.arrayList
															.size());
										}
									} catch (Exception e) {
										// TODO:
										// handle
										// exception
									}
								}
							});
						}
					}
				}
			}
		}, null);
	}
}
