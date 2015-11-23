package com.example.adapter;

import java.util.ArrayList;

import com.example.adapter.GroupTwoAdapter.ViewHolderChild;
import com.example.bean.ListitemChildThree;
import com.example.bean.ListitemChildTwo;
import com.example.testwechar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupThreeAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ListitemChildThree> data;
	private LayoutInflater inflater;

	public GroupThreeAdapter(Context context, ArrayList<ListitemChildThree> data) {
		// TODO �Զ����ɵĹ��캯�����
		this.context = context;
		this.data = data;
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
		ViewHolderChild holderChild = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_three, null);
			holderChild = new ViewHolderChild();
			holderChild.imageview = (ImageView) convertView
					.findViewById(R.id.friend_icon_three);
			holderChild.name = (TextView) convertView
					.findViewById(R.id.friend_nickname_three);
			holderChild.signature = (TextView) convertView
					.findViewById(R.id.friend_signature_three);
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
