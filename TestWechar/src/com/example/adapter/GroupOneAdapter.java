package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bean.ListitemChild;
import com.example.testwechar.R;

public class GroupOneAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ListitemChild> data;
	private LayoutInflater inflater;

	public GroupOneAdapter(Context context, ArrayList<ListitemChild> data) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolderChild holderChild = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_item, null);
			holderChild = new ViewHolderChild();
			holderChild.imageview = (ImageView) convertView
					.findViewById(R.id.friend_icon);
			holderChild.name = (TextView) convertView
					.findViewById(R.id.friend_nickname);
			holderChild.signature = (TextView) convertView
					.findViewById(R.id.friend_signature);
			convertView.setTag(holderChild);
		} else {
			holderChild = (ViewHolderChild) convertView.getTag();
		}
		holderChild.imageview.setImageResource(data.get(position).getImage());
		holderChild.name.setText(data.get(position).getName());
		holderChild.signature.setText(data.get(position).getNickname());
		return convertView;
	}

	class ViewHolderChild {
		ImageView imageview;
		TextView name;
		TextView signature;
	}
}
