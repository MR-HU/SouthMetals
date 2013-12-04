package com.innouni.south.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.AnalyseIndex;

/**
 * 移动平均指标适配器
 * 
 * @author HuGuojun
 * @date 2013-12-3 下午3:34:54
 * @modify
 * @version 1.0.0
 */
public class AvgIndexAdapter extends ArrayListAdapter<Object> {

	private LayoutInflater inflater;
	
	public AvgIndexAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_avg_listview, null);
			holder.nameView = (TextView) convertView.findViewById(R.id.txt_avg_name);
			holder.price1View = (TextView) convertView.findViewById(R.id.txt_avg_price1);
			holder.suggest1View = (TextView) convertView.findViewById(R.id.txt_avg_suggest1);
			holder.price2View = (TextView) convertView.findViewById(R.id.txt_avg_price2);
			holder.suggest2View = (TextView) convertView.findViewById(R.id.txt_avg_suggest2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AnalyseIndex index = (AnalyseIndex) mList.get(position);
		holder.nameView.setText(index.getName());
		holder.price1View.setText(index.getPrice1());
		holder.suggest1View.setText(index.getSuggest1());
		holder.price2View.setText(index.getPrice2());
		holder.suggest2View.setText(index.getSuggest2());
		return convertView;
	}

	private static final class ViewHolder {
		TextView nameView;
		TextView price1View;
		TextView suggest1View;
		TextView price2View;
		TextView suggest2View;
	}
}
