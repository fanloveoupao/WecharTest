package com.example.bean;

public class Msg {
	public static final int TYPE_RECEIVE = 0;
	public static final int TYPE_SENT = 1;
	private String content;
	private int type;
	private int image;

	public Msg(String content, int type) {
		// TODO �Զ����ɵĹ��캯�����
		this.content = content;
		this.type = type;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public static int getTypeReceive() {
		return TYPE_RECEIVE;
	}

	public static int getTypeSent() {
		return TYPE_SENT;
	}

	public String getContent() {
		return content;
	}

	public int getType() {
		return type;
	}
}