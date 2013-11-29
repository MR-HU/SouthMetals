package com.innouni.south.adapter;

import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.ETFEntity;

/**
 * ETF数据适配器
 * 
 * @author HuGuojun
 * @date 2013-11-29 上午10:58:15
 * @modify
 * @version 1.0.0
 */
public class ETFAdapter extends ArrayListAdapter<Object> {
	
	private LayoutInflater inflater;

	public ETFAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_etf_list, null);
			holder.allView = (TextView) convertView.findViewById(R.id.txt_etf_all);
			holder.leftView = (TextView) convertView.findViewById(R.id.txt_etf_left);
			holder.timeView = (TextView) convertView.findViewById(R.id.txt_etf_time);
			holder.upView = (TextView) convertView.findViewById(R.id.txt_etf_up);
			convertView.setTag(holder);
		} else {
			 holder = (ViewHolder) convertView.getTag(); 
		}
		ETFEntity bean = (ETFEntity) mList.get(position);
		holder.allView.setText("总价值:" + bean.getPrice());
		holder.leftView.setText(bean.getTotal() + "顿");
		holder.timeView.setText(bean.getTime());
		if (bean.getUp().equals("0.00")) {
			holder.upView.setText("持平");
			holder.upView.setBackgroundColor(Color.GRAY);
		} else if (Float.parseFloat(bean.getUp()) < 0) {
			holder.upView.setText(bean.getUp() + "顿");
			holder.upView.setBackgroundColor(Color.GREEN);
		} else {
			holder.upView.setText(bean.getUp() + "顿");
			holder.upView.setBackgroundColor(Color.RED);
		}
		return convertView;
	}

	private static final class ViewHolder {
		TextView timeView;
		TextView leftView;
		TextView upView;
		TextView allView;
	}
}
