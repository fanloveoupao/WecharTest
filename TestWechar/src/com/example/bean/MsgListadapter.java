package com.example.bean;

import java.util.ArrayList;

import com.example.testwechar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MsgListadapter extends BaseAdapter {
	private ArrayList<Msg> data;
	private Context context;
	private LayoutInflater inflater;

	public MsgListadapter(Context context, ArrayList<Msg> data) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list, null);
			holder = new ViewHolder();
			holder.leftLayout = (LinearLayout) convertView
					.findViewById(R.id.left_layout);
			holder.rightLayout = (LinearLayout) convertView
					.findViewById(R.id.right_layout);
			holder.leftMsg = (TextView) convertView.findViewById(R.id.left_msg);
			holder.rightMsg = (TextView) convertView
					.findViewById(R.id.right_msg);
			holder.lefticon = (ImageView) convertView
					.findViewById(R.id.icon_left);
			holder.righticon = (ImageView) convertView
					.findViewById(R.id.icon_right);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (data.get(position).getType() == Msg.TYPE_SENT) {
			holder.rightLayout.setVisibility(View.VISIBLE);
			holder.leftLayout.setVisibility(View.GONE);
			holder.rightMsg.setText(data.get(position).getContent());
			holder.righticon.setImageResource(data.get(position).getImage());
		} else if (data.get(position).getType() == Msg.TYPE_RECEIVE) {
			holder.leftLayout.setVisibility(View.VISIBLE);
			holder.rightLayout.setVisibility(View.GONE);
			holder.lefticon.setImageResource(data.get(position).getImage());
			holder.leftMsg.setText(data.get(position).getContent());
		}
		return convertView;
	}

	class ViewHolder {
		LinearLayout leftLayout;
		LinearLayout rightLayout;
		TextView leftMsg;
		TextView rightMsg;
		ImageView lefticon;
		ImageView righticon;
	}
}
