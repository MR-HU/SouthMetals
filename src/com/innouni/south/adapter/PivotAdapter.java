package com.innouni.south.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.PivotIndex;

/**
 * 计算公式适配器
 * 
 * @author HuGuojun
 * @date 2013-12-3 下午3:20:29
 * @modify
 * @version 1.0.0
 */
public class PivotAdapter extends ArrayListAdapter<Object> {

	private LayoutInflater inflater;
	
	public PivotAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_pivot_listview, null);
			holder.view1 = (TextView) convertView.findViewById(R.id.text_1);
			holder.view2 = (TextView) convertView.findViewById(R.id.text_2);
			holder.view3 = (TextView) convertView.findViewById(R.id.text_3);
			holder.view4 = (TextView) convertView.findViewById(R.id.text_4);
			holder.view5 = (TextView) convertView.findViewById(R.id.text_5);
			holder.view6 = (TextView) convertView.findViewById(R.id.text_6);
			holder.view7 = (TextView) convertView.findViewById(R.id.text_7);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PivotIndex pivot = (PivotIndex) mList.get(position);
		holder.view1.setText(pivot.getS3());
		holder.view2.setText(pivot.getS2());
		holder.view3.setText(pivot.getS1());
		holder.view4.setText(pivot.getSr());
		holder.view5.setText(pivot.getR1());
		holder.view6.setText(pivot.getR2());
		holder.view7.setText(pivot.getR3());
		return convertView;
	}

	private static final class ViewHolder {
		TextView view1;
		TextView view2;
		TextView view3;
		TextView view4;
		TextView view5;
		TextView view6;
		TextView view7;
	}
}
