package com.example.testwechar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.ChooseActivity;
import com.example.bean.Msg;
import com.example.bean.MsgListadapter;
import com.example.service.ConnecMethod;
import com.example.utils.ImageTools;
import com.example.utils.Utils;
import com.lidroid.xutils.BitmapUtils;

public class MainActivity extends Activity {
	// �㲥������
	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if ("msg_receiver".equals(intent.getAction())) {
				// Log.d(Tag, "msg");
				if (null != intent.getExtras()) {
					Msg msg = new Msg(intent.getExtras().getString("msg"),
							Msg.TYPE_RECEIVE);
					msg.setImage(R.drawable.icon_pwd);
					arrayList.add(msg);
					// ˢ��ListView����ʾ
					adapter.notifyDataSetChanged();
					listview.setSelection(arrayList.size());
				}
			} else if ("file_receiver".equals(intent.getAction())) {

			}
		}
	};

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;

	private static final int SCALE = 5;
	private BitmapUtils bitmapUtils;
	public EditText edit_content;
	public TextView text_send;
	public static MainActivity instance;
	public ListView listview;
	public MsgListadapter adapter;
	public ArrayList<Msg> arrayList;
	public String content = "";
	private String user;
	private String user_two;
	private TextView text_friends;
	private static TextView text_loading;
	private TextView rightsg;
	private TextView leftmsg;
	private ImageView imageView;
	private ImageView img_photo;
	private ImageView img_file;
	private TextView text_send_file;
	private View view_send;
	private ImageView cancel_send;
	// ��Ƭ������
	private View popview_photo;
	private PopupWindow popupWindow_photo;
	private View char_layout;
	private Button btn_choose;
	private ImageView show_choose;
	private LinearLayout layout;
	private Button btn_photograph;
	private Button btn_native;
	private Button btn_send_photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interfce_char);
		instance = this;
		regisBroadcast();
		init();
		init_click();
		init_sendFile();
		init_clickfile();
		init_sendphoto();
	}

	/**
	 * ע��㲥
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("msg_receiver");
		filter.addAction("file_receiver");
		this.registerReceiver(this.receiver, filter);
	}

	private void init() {
		// TODO �Զ����ɵķ������
		bitmapUtils = new BitmapUtils(this);
		edit_content = (EditText) findViewById(R.id.input_text);
		text_send = (TextView) findViewById(R.id.send);
		listview = (ListView) findViewById(R.id.msg_list_view);
		text_friends = (TextView) findViewById(R.id.title);
		text_loading = (TextView) findViewById(R.id.loading);
		rightsg = (TextView) findViewById(R.id.right_msg);
		leftmsg = (TextView) findViewById(R.id.left_msg);
		imageView = (ImageView) findViewById(R.id.right_img);
		text_send_file = (TextView) findViewById(R.id.send_file);
		view_send = findViewById(R.id.include_send);
		cancel_send = (ImageView) findViewById(R.id.send_cancle);
		// ��ʼ����Ƭ
		img_photo = (ImageView) findViewById(R.id.send_photo);
		char_layout = findViewById(R.id.char_home);
		// ��Ƭѡ��ĵ�����
		popview_photo = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.chhose_photos, null);
		btn_choose = (Button) popview_photo.findViewById(R.id.btn_choose);
		show_choose = (ImageView) popview_photo.findViewById(R.id.img_photo);
		layout = (LinearLayout) popview_photo.findViewById(R.id.choose_layout);
		btn_photograph = (Button) popview_photo.findViewById(R.id.btn_photo);
		btn_native = (Button) popview_photo.findViewById(R.id.btn_native);
		btn_send_photo = (Button) popview_photo.findViewById(R.id.btn_send);
		view_send.setVisibility(View.GONE);
		arrayList = new ArrayList<Msg>();
		adapter = new MsgListadapter(getApplicationContext(), arrayList);
		listview.setAdapter(adapter);
		Intent intent = getIntent();
		user = intent.getStringExtra("user");
		if (user.equals("����")) {
			text_friends.setText(MyApplication.roomname + "����");
			user_two = MyApplication.roomname + "@conference.125.65.82.216";
		} else {
			text_friends.setText(user);
		}

	}

	/**
	 * ������Ϣ
	 * 
	 * @param to
	 * @param msg
	 */
	public static void sendTalkMsg(String to, String msg) {
		Chat chat = MyApplication.xmppConnection.getChatManager().createChat(
				to, null);
		try {
			chat.sendMessage(msg);
			ConnecMethod.postFiletoService("130560779417728750.png",
					text_loading);

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init_click() {
		// TODO �Զ����ɵķ������

		text_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				final String body = edit_content.getText().toString();
				if (!body.equals("")) {
					Msg msg = new Msg(body, Msg.TYPE_SENT);
					msg.setImage(R.drawable.h091);
					arrayList.add(msg);
					// ˢ��ListView����ʾ
					adapter.notifyDataSetChanged();
					listview.setSelection(arrayList.size());
					edit_content.setText("");
					// MyApplication.xmppCon
					// nection.getRoster().createGroup("123");

				}
				// ������Ϣ
				new Thread() {
					public void run() {
						try {

							if (user.equals("����")) {
								Message message = new Message();
								// ���͵�����
								message.setTo(user_two);
								// �û�����������
								message.setFrom(MyApplication.xmppConnection
										.getUser().toString());
								// ������Ϣ������ΪȺ��
								message.setType(Type.groupchat);
								// ������Ϣ������
								message.setBody(body);
								content = body;
								// ��ʼ����
								MyApplication.userChat.sendMessage(message);
							} else {
								sendTalkMsg(Utils.getUserNameToJid(user), body);
								// ConnecMethod.sendTalkFile(user,
								// "/sdcard/rl02.jpg");
								// bitmapUtils
								// .display(imageView,
								// "http://oa.redmany.com:888/document/130560779417728750.png");
								imageView.setVisibility(View.VISIBLE);
							}

						} catch (Exception e) {
							// TODO: handle exception
						}
					};
				}.start();

			}
		});
	}

	/**
	 * 
	 * �����ļ��ĳ�ʼ��
	 * */
	private void init_sendFile() {
		// TODO �Զ����ɵķ������
		text_send_file.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				view_send.setVisibility(View.VISIBLE);
			}
		});
		// �������
		cancel_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				view_send.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 
	 * ������Ƭ
	 * 
	 * */

	private void init_clickfile() {
		// TODO �Զ����ɵķ������
		// �����Ƭ����
		img_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				if (popupWindow_photo != null && popupWindow_photo.isShowing()) {
					popupWindow_photo.dismiss();
				} else {
					initPopupwindowForphoto();
					popupWindow_photo.showAtLocation(char_layout,
							Gravity.CENTER, 0, 0);
					//
				}
				// Intent intent = new Intent(getApplicationContext(),
				// ChooseActivity.class);
				// startActivity(intent);
			}

		});
	}

	private void initPopupwindowForphoto() {
		// TODO �Զ����ɵķ������
		int[] position = new int[2];
		popupWindow_photo = new PopupWindow(popview_photo,
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		popview_photo.getLocationOnScreen(position);
		popupWindow_photo.setFocusable(true);
		// ��������ط���ʧ
		popview_photo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO �Զ����ɵķ������
				// TODO �Զ����ɵķ������
				if (popupWindow_photo != null && popupWindow_photo.isShowing()) {
					popupWindow_photo.dismiss();
					popupWindow_photo = null;
				}

				return false;
			}
		});
	}

	private void init_sendphoto() {
		// TODO �Զ����ɵķ������
		btn_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				init_choose();
			}

		});
		btn_send_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				// bitmapUtils
				// .display(show_choose,
				// "http://oa.redmany.com:888/document/130560779417728750.png");
			}
		});
	}

	private void init_choose() {
		// TODO �Զ����ɵķ������
		layout.setVisibility(View.VISIBLE);
		btn_photograph.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				Uri imageUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "image.jpg"));
				Toast.makeText(getApplicationContext(),
						"�������յ�·��" + imageUri.getPath(), 1).show();
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
				layout.setVisibility(View.GONE);
			}
		});
		btn_native.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/*");
				startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
				layout.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				bitmap.recycle();

				// ���д�����Ƭ
				show_choose.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				Uri originalUri = data.getData();
				Toast.makeText(getApplicationContext(),
						"ѡ���·��" + originalUri.getPath(), 1).show();
				try {
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						photo.recycle();

						show_choose.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 
	 * ��Ƭѡ�������
	 * 
	 * */
	public void showPicturePicker(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ѡ��");
		builder.setNegativeButton("ȡ��", null);
		builder.setItems(new String[] { "���", "ͼ��" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							Intent openCameraIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							Uri imageUri = Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(), "image.jpg"));
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									imageUri);
							startActivityForResult(openCameraIntent,
									TAKE_PICTURE);
							break;

						case CHOOSE_PICTURE:
							Intent openAlbumIntent = new Intent(
									Intent.ACTION_GET_CONTENT);
							openAlbumIntent.setType("image/*");
							startActivityForResult(openAlbumIntent,
									CHOOSE_PICTURE);
							break;

						default:
							break;
						}
					}
				});
		builder.create().show();
	}

}