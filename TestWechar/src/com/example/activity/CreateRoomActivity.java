package com.example.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.service.ConnecMethod;
import com.example.testwechar.R;

public class CreateRoomActivity extends Activity {
	private EditText user;
	private EditText room;
	private Button btn_create;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_room);
		init();
		init_click();
	}

	private void init() {
		// TODO 自动生成的方法存根
		user = (EditText) findViewById(R.id.room_user);
		room = (EditText) findViewById(R.id.room_name);
		btn_create = (Button) findViewById(R.id.btn_createroom);
	}

	private void init_click() {
		// TODO 自动生成的方法存根
		btn_create.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (!user.getText().toString().equals("")
						&& !room.getText().toString().equals("")) {
					ConnecMethod connecMethod = new ConnecMethod();
					connecMethod.createRoom(user.getText().toString(), room
							.getText().toString(), "");
					Toast.makeText(getApplicationContext(), "创建成功", 1).show();
					// 创建后加入房间
					connecMethod.joinMultiUserChat(user.getText().toString(),
							room.getText().toString(), "");
				}

			}
		});
	}

}
