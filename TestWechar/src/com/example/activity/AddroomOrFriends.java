package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.testwechar.R;

public class AddroomOrFriends extends Activity {
	private Button btn_room;
	private Button btn_fid;
	private Button btn_joinroom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfriedsorroom);
		init();
	}

	private void init() {
		// TODO �Զ����ɵķ������
		btn_room = (Button) findViewById(R.id.create_room);
		btn_fid = (Button) findViewById(R.id.add_friends);
		btn_joinroom = (Button) findViewById(R.id.join_room);
		btn_fid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(getApplicationContext(),
						AddFriActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btn_room.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(getApplicationContext(),
						CreateRoomActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btn_joinroom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(getApplicationContext(),
						JoinActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}