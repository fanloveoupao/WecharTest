package com.example.utils;

/**
 * ������
 * 
 */
public class Utils {

	/**
	 * ����jid��ȡ�û���
	 * 
	 * @param jid
	 * @return
	 * @author by_wsc
	 */
	public static String getJidToUsername(String jid) {

		return jid.split("@")[0];
	}

	public static String getUserNameToJid(String username) {
		return username + "@" + "125.65.82.216";
	}
}