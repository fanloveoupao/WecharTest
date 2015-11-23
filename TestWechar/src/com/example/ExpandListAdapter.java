package com.example;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.ListGroup;
import com.example.bean.ListitemChild;
import com.example.testwechar.R;

public class ExpandListAdapter extends BaseExpandableListAdapter {
	private ArrayList<ListGroup> group_data;
	private Map<String, ArrayList<ListitemChild>> map_data;
	private LayoutInflater inflater;
	private Context context;

	public ExpandListAdapter(Context context, ArrayList<ListGroup> group_data,
			Map<String, ArrayList<ListitemChild>> map_data) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.group_data = group_data;
		this.map_data = map_data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		String key = group_data.get(groupPosition).getName();
		return (map_data.get(key).get(childPosition).getName());
	}

	// 得到子item的ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
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
		String key = group_data.get(groupPosition).getName();
		holderChild.imageview.setImageResource(map_data.get(key)
				.get(childPosition).getImage());
		holderChild.name
				.setText(map_data.get(key).get(childPosition).getName());
		holderChild.signature.setText(map_data.get(key).get(childPosition)
				.getNickname());
		return convertView;
	}

	// 获取当前父item下的子item的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		String key = group_data.get(groupPosition).getName();
		int size = map_data.get(key).size();
		return size;
	}

	// 获取当前父item的数据
	@Override
	public Object getGroup(int groupPosition) {
		return group_data.get(groupPosition).getName();
	}

	@Override
	public int getGroupCount() {
		// TODO 自动生成的方法存根
		return group_data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO 自动生成的方法存根
		return groupPosition;
	}

	// 设置父item组件
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolderParent holderParent = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.parent_item, null);
			holderParent = new ViewHolderParent();
			holderParent.grou_name = (TextView) convertView
					.findViewById(R.id.grou_name);
			holderParent.num = (TextView) convertView
					.findViewById(R.id.item_num);
			convertView.setTag(holderParent);
		} else {
			holderParent = (ViewHolderParent) convertView.getTag();
		}
		holderParent.grou_name.setText(group_data.get(groupPosition).getName());
		holderParent.num.setText(group_data.get(groupPosition).getNum());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return true;
	}

	class ViewHolderChild {
		ImageView imageview;
		TextView name;
		TextView signature;
	}

	class ViewHolderParent {
		TextView grou_name;
		TextView num;
	}
}
