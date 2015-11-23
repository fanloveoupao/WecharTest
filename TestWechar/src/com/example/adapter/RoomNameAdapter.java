package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bean.RoomNameBean;
import com.example.testwechar.R;

public class RoomNameAdapter extends BaseAdapter {
	private ArrayList<RoomNameBean> data;
	private Context context;
	private LayoutInflater inflater;

	public RoomNameAdapter(Context context, ArrayList<RoomNameBean> data) {
		// TODO �Զ����ɵĹ��캯�����
		this.data = data;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO �Զ����ɵķ������
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO �Զ����ɵķ������
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO �Զ����ɵķ������
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �Զ����ɵķ������
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.room_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.item_text_room);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(data.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		TextView name;
	}
}
