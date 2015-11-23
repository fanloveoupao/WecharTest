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
	 * 获取在线消息
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
	 * 设置广播进行接收
	 * 
	 * */
	private static void sendBroadcastMsg(Context context, String txt) {
		Intent intent = new Intent();
		intent.setAction("msg_receiver");
		intent.putExtra("msg", txt);
		context.sendBroadcast(intent);
	}

	/**
	 * 文件
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
	 * 发送文件
	 * 
	 * @param user
	 * @param filePath
	 */
	public static void sendFile(String user, String filePath) {
		if (MyApplication.xmppConnection == null)
			return;
		// 创建文件传输管理器
		FileTransferManager manager = new FileTransferManager(
				MyApplication.xmppConnection);

		// 创建输出的文件传输
		OutgoingFileTransfer transfer = manager
				.createOutgoingFileTransfer(user);

		// 发送文件
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
	 * 创建房间
	 * 
	 * 
	 * 房间名称
	 */
	public MultiUserChat createRoom(String user, String roomName,
			String password) {
		if (MyApplication.xmppConnection == null)
			return null;

		MultiUserChat muc = MyApplication.userChat;
		try {
			// 创建一个MultiUserChat
			muc = new MultiUserChat(MyApplication.xmppConnection, roomName
					+ "@conference." + "125.65.82.216");
			// 创建聊天室
			muc.create("user");
			// 获得聊天室的配置表单
			Form form = muc.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单。
			Form submitForm = form.createAnswerForm();
			// 向要提交的表单添加默认答复
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// 设置聊天室的新拥有者
			List<String> owners = new ArrayList<String>();
			owners.add(MyApplication.xmppConnection.getUser());// 用户JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// 房间仅对成员开放
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (!password.equals("")) {
				// 进入是否需要密码
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
						true);
				// 设置进入密码
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			}
			// 能够发现占有者真实 JID 的角色
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// 登录房间对话
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// 仅允许注册的昵称登录
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// 允许使用者修改昵称
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
			// 允许用户注册房间
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			muc.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
		return muc;
	}

	/**
	 * 加入房间
	 * 
	 * @param user
	 *            昵称
	 * @param password
	 *            会议室密码
	 * @param roomsName
	 *            会议室名
	 */
	public MultiUserChat joinMultiUserChat(String user, String roomsName,
			String password) {
		if (MyApplication.xmppConnection == null)
			return null;
		try {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			MultiUserChat muc = new MultiUserChat(MyApplication.xmppConnection,
					roomsName + "@conference."
							+ MyApplication.xmppConnection.getServiceName());
			// 聊天室服务将会决定要接受的历史记录数量
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxChars(5);
			// history.setSince(new Date());
			// 用户加入聊天室
			muc.join(user, password, history,
					SmackConfiguration.getPacketReplyTimeout());
			MyApplication.state = "会议室【" + roomsName + "】加入成功........";
			Log.i("MultiUserChat", "会议室【" + roomsName + "】加入成功........");
			return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			MyApplication.state = "会议室【" + roomsName + "】加入失败........";
			Log.i("MultiUserChat", "会议室【" + roomsName + "】加入失败........");
			return null;
		}
	}

	/**
	 * 初始化会议室列表
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
				Log.i("room=============", "名字：" + entry.getName() + " - ID:"
						+ entry.getJid());
			}
			Log.i("room=============", "服务会议数量:" + roominfos.size());

		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return roominfos;
	}

	/**
	 * 获取服务器上所有会议室
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
						friendrooms.setName(j.getName());// 聊天室的名称
						// friendrooms.setJid(j.getJid());//聊天室JID
						// friendrooms.setOccupants(info2.getOccupantsCount());//聊天室中占有者数量
						// friendrooms.setDescription(info2.getDescription());//聊天室的描述
						// friendrooms.setSubject(info2.getSubject());//聊天室的主题
						list.add(friendrooms);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 发送文件
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
	 * 上传文件到服务器
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
						testTextView.setText("开始上传");
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
						testTextView.setText("成功reply: " + responseInfo.result);
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
						testTextView.setText("失败" + error.getExceptionCode()
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
