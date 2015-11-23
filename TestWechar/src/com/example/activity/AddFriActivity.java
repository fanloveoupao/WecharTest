package com.example.activity;

import java.util.ArrayList;
import java.util.Iterator;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testwechar.MyApplication;
import com.example.testwechar.R;
import com.example.utils.Utils;

public class AddFriActivity extends Activity {
	private EditText user;
	private EditText group;
	private Button btn_add;
	private static TextView sear_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_activity);
		init();
		init_clickadd();
	}

	private void init() {
		// TODO �Զ����ɵķ������
		user = (EditText) findViewById(R.id.search_frs);
		group = (EditText) findViewById(R.id.search_zu);
		btn_add = (Button) findViewById(R.id.btn_add);
		sear_result = (TextView) findViewById(R.id.news);
	}

	private void init_clickadd() {
		// TODO �Զ����ɵķ������
		btn_add.setOnClickListener(new OnClickListener() {
			// ���������
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				addGroupFriend(group.getText().toString(), user.getText()
						.toString());
			}
		});
	}

	public static void addGroupFriend(String group, String friend) {
		Roster roster = MyApplication.xmppConnection.getRoster();
		try {
			roster.createEntry(Utils.getUserNameToJid(friend), null,
					new String[] { group });
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private void searchUserNmae() {
		new Thread() {
			public void run() {
				ArrayList<String> users = searchUsers(user.getText().toString());
			};
		}.start();
	}

	// ���������û�
	public static ArrayList<String> searchUsers(String user) {
		ArrayList<String> users = new ArrayList<String>();
		UserSearchManager usm = new UserSearchManager(
				MyApplication.xmppConnection);
		Form searchForm = null;
		try {
			searchForm = usm.getSearchForm(MyApplication.xmppConnection
					.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", user);
			ReportedData data = usm.getSearchResults(answerForm, "search"
					+ MyApplication.xmppConnection.getServiceName());
			// column:jid,Username,Name,Email
			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				Log.i("UserName", row.getValues("Username").next().toString());
				sear_result.setText(row.getValues("Username").next().toString()
						+ "�ҵ���Ϣ");
				// Toast.makeText(get, text, duration)
				// Log.d("Name", row.getValues("Name").next().toString());
				// Log.d("Email", row.getValues("Email").next().toString());
				// �����ڣ����з���,UserNameһ���ǿգ����������������裬һ���ǿ�
				users.add(row.getValues("Username").next().toString());
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

}