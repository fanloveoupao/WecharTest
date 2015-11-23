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
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
		receive();
		clicklogin();
		click_registrer();
	}

	private void init() {
		// TODO �Զ����ɵķ������
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
	}

	private void click_registrer() {
		// TODO �Զ����ɵķ������
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				if (MyApplication.xmppConnection.isConnected()) {
					try {
						MyApplication.xmppConnection.getAccountManager()
								.createAccount(username.getText().toString(),
										password.getText().toString());
						login();
					} catch (XMPPException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "ע��ʧ�� ", 1)
								.show();
					}
				}
			}
		});
	}

	private void clicklogin() {
		// TODO �Զ����ɵķ������
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				// ��¼������
				login();
			}

		});
	}

	private void login() {
		// TODO �Զ����ɵķ������
		if (MyApplication.xmppConnection.isConnected()) {
			try {
				MyApplication.xmppConnection.login(username.getText()
						.toString(), password.getText().toString());
				MyApplication.userChat = new MultiUserChat(
						MyApplication.xmppConnection,
						"bruse@conference.125.65.82.216");
				MyApplication.userChat.join(username.getText().toString(),
						password.getText().toString());
				// ���н�����Ϣ
				// receive();
				ConnecMethod.getOnLine(this);
				addNewGroup("�ҵĺ���");
				addNewGroup("����");
				addNewGroup("����");
				addNewGroup("����");
				Intent intent = new Intent(getApplicationContext(),
						HomeChar.class);
				startActivity(intent);
				finish();
				//
			} catch (XMPPException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"��¼ʧ��\n" + e.getMessage(), 1).show();
			}
		}
	}

	/*
	 * 
	 * ������
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
		// TODO �Զ����ɵķ������
		MyApplication.xmppConnection.addPacketListener(new PacketListener() {
			// ����������Ϣ��android�ͻ��˵�ʱ��ִ��
			@Override
			public void processPacket(Packet packet) {
				// TODO �Զ����ɵķ������
				// �ж���Ϣ�ǲ���Ⱥ�ĵ���Ϣ,��Ϣ��Ⱥ�ĺ�˽��
				if (packet instanceof Message) {
					// ��packetת��Ϊ����
					Message message = (Message) packet;
					Type type = message.getType();
					// message.getFrom();
					if (type == Type.groupchat) {
						// Ⱥ��
						// �õ���Ϣ������
						final String body = message.getBody();
						// �ڹ����߳����治�ܸĶ�UI
						if (MainActivity.instance != null) {
							MainActivity.instance.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO
									// �Զ����ɵķ������
									try {
										if (!MainActivity.instance.content
												.equals(body)) {
											Msg msg = new Msg(body,
													Msg.TYPE_RECEIVE);
											MainActivity.instance.arrayList
													.add(msg);
											// ˢ��ListView����ʾ
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
