package com.example.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.testwechar.R;
import com.example.utils.ImageTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ChooseActivity extends Activity {
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;

	private static final int SCALE = 5;
	private ImageView iv_image = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chhose_photos);
		setTitle("照片的上传");

		iv_image = (ImageView) this.findViewById(R.id.img_photo);

		this.findViewById(R.id.btn_choose).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// showPicturePicker(getApplicationContext());
						Intent openAlbumIntent = new Intent(
								Intent.ACTION_GET_CONTENT);
						openAlbumIntent.setType("image/*");
						startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
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

				// 进行传递照片
				iv_image.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				Uri originalUri = data.getData();
				try {
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						photo.recycle();

						iv_image.setImageBitmap(smallBitmap);
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

	public void showPicturePicker(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("选择");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "相机", "图库" },
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
