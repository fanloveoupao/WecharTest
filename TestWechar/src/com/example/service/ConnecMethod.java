package com.example.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.RoomNameBean;
import com.example.testwechar.MyApplication;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class ConnecMethod {
	/**
	 * ��ȡ������Ϣ
	 * 
	 */
	public static void getOnLine(final Context context) {
		ChatManager cm = MyApplication.xmppConnection.getChatManager();
		cm.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able) {
				chat.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Chat chat2, Message message) {
						Log.d("connectMethod:getOnLine()", message.getBody()
								+ message.getFrom());
						if (!message.getBody().equals("")) {
							sendBroadcastMsg(context, message.getBody());
						}

					}
				});
			}
		});
	}

	/**
	 * 
	 * ���ù㲥���н���
	 * 
	 * */
	private static void sendBroadcastMsg(Context context, String txt) {
		Intent intent = new Intent();
		intent.setAction("msg_receiver");
		intent.putExtra("msg", txt);
		context.sendBroadcast(intent);
	}

	/**
	 * �ļ�
	 * 
	 * @param context
	 */
	public static void getOnLineFile(Context context) {
		FileTransferManager fileTransferManagernew = new FileTransferManager(
				MyApplication.xmppConnection);
		FileTransferListener filter = new ChatFileTransferListener(context);
		fileTransferManagernew.addFileTransferListener(filter);
	}

	/**
	 * �����ļ�
	 * 
	 * @param user
	 * @param filePath
	 */
	public static void sendFile(String user, String filePath) {
		if (MyApplication.xmppConnection == null)
			return;
		// �����ļ����������
		FileTransferManager manager = new FileTransferManager(
				MyApplication.xmppConnection);

		// ����������ļ�����
		OutgoingFileTransfer transfer = manager
				.createOutgoingFileTransfer(user);

		// �����ļ�
		try {
			transfer.sendFile(new File(filePath), "You won't believe this!");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	static class ChatFileTransferListener implements FileTransferListener {
		Context context;

		public ChatFileTransferListener(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public void fileTransferRequest(FileTransferRequest request) {
			// TODO Auto-generated method stub
			try {
				File insFile = new File(
						Environment.getExternalStorageDirectory() + "/"
								+ request.getFileName());
				IncomingFileTransfer infiletransfer = request.accept();
				infiletransfer.recieveFile(insFile);
				sendBroadcastFile(context, insFile.getAbsolutePath());
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void sendBroadcastFile(Context context, String filepath) {
		Intent intent = new Intent();
		intent.setAction("file_receiver");
		intent.putExtra("path", filepath);
		context.sendBroadcast(intent);
	}

	/**
	 * ��������
	 * 
	 * 
	 * ��������
	 */
	public MultiUserChat createRoom(String user, String roomName,
			String password) {
		if (MyApplication.xmppConnection == null)
			return null;

		MultiUserChat muc = MyApplication.userChat;
		try {
			// ����һ��MultiUserChat
			muc = new MultiUserChat(MyApplication.xmppConnection, roomName
					+ "@conference." + "125.65.82.216");
			// ����������
			muc.create("user");
			// ��������ҵ����ñ���
			Form form = muc.getConfigurationForm();
			// ����ԭʼ��������һ��Ҫ�ύ���±�����
			Form submitForm = form.createAnswerForm();
			// ��Ҫ�ύ�ı�������Ĭ�ϴ�
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// ����Ĭ��ֵ��Ϊ��
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// ���������ҵ���ӵ����
			List<String> owners = new ArrayList<String>();
			owners.add(MyApplication.xmppConnection.getUser());// �û�JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// �����������ǳ־������ң�����Ҫ����������
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// ������Գ�Ա����
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// ����ռ��������������
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (!password.equals("")) {
				// �����Ƿ���Ҫ����
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
						true);
				// ���ý�������
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			}
			// �ܹ�����ռ������ʵ JID �Ľ�ɫ
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// ��¼����Ի�
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// ������ע����ǳƵ�¼
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// ����ʹ�����޸��ǳ�
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
			// �����û�ע�᷿��
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// ��������ɵı�������Ĭ��ֵ����������������������
			muc.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
		return muc;
	}

	/**
	 * ���뷿��
	 * 
	 * @param user
	 *            �ǳ�
	 * @param password
	 *            ����������
	 * @param roomsName
	 *            ��������
	 */
	public MultiUserChat joinMultiUserChat(String user, String roomsName,
			String password) {
		if (MyApplication.xmppConnection == null)
			return null;
		try {
			// ʹ��XMPPConnection����һ��MultiUserChat����
			MultiUserChat muc = new MultiUserChat(MyApplication.xmppConnection,
					roomsName + "@conference."
							+ MyApplication.xmppConnection.getServiceName());
			// �����ҷ��񽫻����Ҫ���ܵ���ʷ��¼����
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxChars(5);
			// history.setSince(new Date());
			// �û�����������
			muc.join(user, password, history,
					SmackConfiguration.getPacketReplyTimeout());
			MyApplication.state = "�����ҡ�" + roomsName + "������ɹ�........";
			Log.i("MultiUserChat", "�����ҡ�" + roomsName + "������ɹ�........");
			return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			MyApplication.state = "�����ҡ�" + roomsName + "������ʧ��........";
			Log.i("MultiUserChat", "�����ҡ�" + roomsName + "������ʧ��........");
			return null;
		}
	}

	/**
	 * ��ʼ���������б�
	 */
	public List<HostedRoom> getHostRooms() {
		if (MyApplication.xmppConnection == null)
			return null;
		Collection<HostedRoom> hostrooms = null;
		List<HostedRoom> roominfos = new ArrayList<HostedRoom>();
		try {
			new ServiceDiscoveryManager(MyApplication.xmppConnection);
			hostrooms = MultiUserChat.getHostedRooms(
					MyApplication.xmppConnection,
					MyApplication.xmppConnection.getServiceName());
			for (HostedRoom entry : hostrooms) {
				roominfos.add(entry);
				Log.i("room=============", "���֣�" + entry.getName() + " - ID:"
						+ entry.getJid());
			}
			Log.i("room=============", "�����������:" + roominfos.size());

		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return roominfos;
	}

	/**
	 * ��ȡ�����������л�����
	 * 
	 * @return
	 * @throws XMPPException
	 */
	public static ArrayList<RoomNameBean> getConferenceRoom()
			throws XMPPException {
		ArrayList<RoomNameBean> list = new ArrayList<RoomNameBean>();
		new ServiceDiscoveryManager(MyApplication.xmppConnection);
		if (!MultiUserChat.getHostedRooms(MyApplication.xmppConnection,
				MyApplication.xmppConnection.getServiceName()).isEmpty()) {

			for (HostedRoom k : MultiUserChat.getHostedRooms(
					MyApplication.xmppConnection,
					MyApplication.xmppConnection.getServiceName())) {

				for (HostedRoom j : MultiUserChat.getHostedRooms(
						MyApplication.xmppConnection, k.getJid())) {
					RoomInfo info2 = MultiUserChat.getRoomInfo(
							MyApplication.xmppConnection, j.getJid());
					if (j.getJid().indexOf("@") > 0) {
						RoomNameBean friendrooms = new RoomNameBean();
						friendrooms.setName(j.getName());// �����ҵ�����
						// friendrooms.setJid(j.getJid());//������JID
						// friendrooms.setOccupants(info2.getOccupantsCount());//��������ռ��������
						// friendrooms.setDescription(info2.getDescription());//�����ҵ�����
						// friendrooms.setSubject(info2.getSubject());//�����ҵ�����
						list.add(friendrooms);
					}
				}
			}
		}
		return list;
	}

	/**
	 * �����ļ�
	 * 
	 * @param to
	 * @param filepath
	 */
	public static void sendTalkFile(String to, String filepath) {
		FileTransferManager fileTransferManager = new FileTransferManager(
				MyApplication.xmppConnection);
		OutgoingFileTransfer outgoingFileTransfer = fileTransferManager
				.createOutgoingFileTransfer(to + "/Spark 2.7.1");
		File insfile = new File(filepath);
		try {
			outgoingFileTransfer.sendFile(insfile, "descr");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * �ϴ��ļ���������
	 * 
	 * */
	public static void postFiletoService(String name,
			final TextView testTextView) {
		HttpUtils http = new HttpUtils();
		
		http.send(HttpRequest.HttpMethod.POST,
				"http://oa.redmany.com:888/document/" + name,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						testTextView.setVisibility(View.VISIBLE);
						testTextView.setText("��ʼ�ϴ�");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							testTextView.setText("upload: " + current + "/"
									+ total);
						} else {
							testTextView.setText("reply: " + current + "/"
									+ total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						testTextView.setText("�ɹ�reply: " + responseInfo.result);
						new Thread() {
							public void run() {
								try {
									Thread.sleep(2000);
									testTextView.setVisibility(View.GONE);
								} catch (Exception e) {
									// TODO: handle exception
								}
							};
						}.start();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						testTextView.setText("ʧ��" + error.getExceptionCode()
								+ ":" + msg);
						new Thread() {
							public void run() {
								try {
									Thread.sleep(2000);
									testTextView.setVisibility(View.GONE);
								} catch (Exception e) {
									// TODO: handle exception
								}
							};
						}.start();

					}
				});
	}
}