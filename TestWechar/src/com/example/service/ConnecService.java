package com.example.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * ������Ҫ��������������������еĽ���
 * 
 * @author jin
 * 
 */
public class ConnecService extends Service {
	private String Tag = "ConnecService";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * ������ʱ�� �������͵���Ϣ
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		getOnLineMsg();
		getOnLineFile();
	}

	/**
	 * �ı�
	 */
	private void getOnLineMsg() {
		ConnecMethod.getOnLine(this);
	}

	/**
	 * �ļ�
	 */
	private void getOnLineFile() {
		ConnecMethod.getOnLineFile(this);
	}
}
