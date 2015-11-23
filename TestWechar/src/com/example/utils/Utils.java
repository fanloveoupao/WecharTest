package com.example.utils;

/**
 * 工具类
 * 
 */
public class Utils {

	/**
	 * 根据jid获取用户名
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