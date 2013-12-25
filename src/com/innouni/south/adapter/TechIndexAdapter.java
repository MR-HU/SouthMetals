package com.innouni.south.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.AnalyseIndex;

/**
 * 技术指标适配器
 * 
 * @author HuGuojun
 * @date 2013-12-3 下午3:34:54
 * @modify
 * @version 1.0.0
 */
public class TechIndexAdapter extends ArrayListAdapter<Object> {

	private LayoutInflater inflater;
	
	public TechIndexAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_tech_listview, null);
			holder.nameView = (TextView) convertView.findViewById(R.id.txt_tech_name);
			holder.priceView = (TextView) convertView.findViewById(R.id.txt_tech_price);
			holder.suggestView = (TextView) convertView.findViewById(R.id.txt_tech_suggest);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AnalyseIndex index = (AnalyseIndex) mList.get(position);
		holder.nameView.setText(index.getName());
		holder.priceView.setText(index.getPrice1());
		holder.suggestView.setText(index.getSuggest1());
		if (index.getSuggest1().equals("购买")) {
			holder.suggestView.setTextColor(Color.RED);
		} else if (index.getSuggest1().equals("出售")) {
			holder.suggestView.setTextColor(Color.GREEN);
		} else {
			holder.suggestView.setTextColor(Color.WHITE);
		}
		return convertView;
	}

	private static final class ViewHolder {
		TextView nameView;
		TextView priceView;
		TextView suggestView;
	}
}
