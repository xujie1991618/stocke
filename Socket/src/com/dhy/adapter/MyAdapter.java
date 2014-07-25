package com.dhy.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dhy.socket.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<Map<String, String>> mData = new ArrayList<Map<String, String>>();

	public MyAdapter(Context context, List<Map<String, String>> mData2) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.item, null);

			holder.txCen = (TextView) convertView.findViewById(R.id.tx_center);

			holder.lyLt = (LinearLayout) convertView.findViewById(R.id.ly_lt);
			holder.imgLt = (ImageView) convertView.findViewById(R.id.img_lt);
			holder.txNameLt = (TextView) convertView
					.findViewById(R.id.tx_name_lt);
			holder.txLt = (TextView) convertView.findViewById(R.id.tx_lt);

			holder.lyRt = (LinearLayout) convertView.findViewById(R.id.ly_rt);
			holder.imgRt = (ImageView) convertView.findViewById(R.id.img_rt);
			holder.txNameRt = (TextView) convertView
					.findViewById(R.id.tx_name_rt);
			holder.txRt = (TextView) convertView.findViewById(R.id.tx_rt);

			// holder.info = (TextView)convertView.findViewById(R.id.info);
			// holder.viewBtn = (Button)convertView.findViewById(R.id.view_btn);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		if (mData.get(position).get("name").toString().equals("me")) {

			holder.txCen.setVisibility(View.GONE);
			holder.lyLt.setVisibility(View.GONE);

			holder.lyRt.setVisibility(View.VISIBLE);
			holder.imgRt.setVisibility(View.GONE);
			if (mData.get(position).containsKey("name")) {
				holder.txNameRt.setText(mData.get(position).get("name")+":");
			} else {
				holder.txNameRt.setText("who");
			}
			if (mData.get(position).containsKey("content")) {
				holder.txRt.setText(mData.get(position).get("content")
						.toString());
			} else {

				holder.txLt.setText("nothing");
			}
		}

		if (mData.get(position).get("name").toString().equals("him")) {
			holder.txCen.setVisibility(View.GONE);
			holder.lyRt.setVisibility(View.GONE);

			holder.lyLt.setVisibility(View.VISIBLE);
			holder.imgLt.setVisibility(View.GONE);
			if (mData.get(position).containsKey("name")) {
				holder.txNameLt.setText(mData.get(position).get("name") + ":");
			} else {
				holder.txNameLt.setText("who");

			}
			if (mData.get(position).containsKey("content")) {
				holder.txLt.setText(mData.get(position).get("content")
						.toString());
			} else {

				holder.txLt.setText("nothing");
			}
		}

		if (mData.get(position).get("name").toString().equals("sys")) {
			holder.lyLt.setVisibility(View.GONE);
			holder.lyRt.setVisibility(View.GONE);

			holder.txCen.setVisibility(View.VISIBLE);
			if (mData.get(position).containsKey("content")) {
				holder.txCen.setText(mData.get(position).get("content")
						.toString());
			} else {

				holder.txLt.setText("nothing");
			}
		}

		// holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
		// holder.title.setText((String)mData.get(position).get("title"));
		// holder.info.setText((String)mData.get(position).get("info"));

		// holder.viewBtn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// showInfo();
		// }
		// });

		return convertView;
	}

	public final class ViewHolder {

		public TextView txCen;

		public LinearLayout lyLt, lyRt;
		public ImageView imgLt, imgRt;
		public TextView txLt, txRt;
		public TextView txNameLt, txNameRt;
	}

}
