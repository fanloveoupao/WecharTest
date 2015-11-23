package com.example.utils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.OfflineMessageManager;

/**
 * asmack���صľ�̬����
 * 
 */
public class XmppTool {
	private static ConnectionConfiguration connConfig;
	private static XMPPConnection con;
	private static OfflineMessageManager offlineManager;

	// ��̬����ReconnectionManager ,��������������
	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static OfflineMessageManager getOffLineMessageManager() {
		if (offlineManager == null) {
			offlineManager = new OfflineMessageManager(con);
		}
		return offlineManager;
	}

	private static void openConnection() {
		try {
			connConfig = new ConnectionConfiguration("192.168.1.113", 5222);

			// ���õ�¼״̬Ϊ����
			connConfig.setSendPresence(false);
			// ��������
			connConfig.setReconnectionAllowed(true);
			con = new XMPPConnection(connConfig);
			con.connect();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static XMPPConnection getConnection() {
		if (con == null || !con.isConnected()) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		con.disconnect();
	}
}