package com.innouni.south.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.EconomicCalendar;

/**
 * 财经日历数据适配器
 * 
 * @author HuGuojun
 * @date 2013-11-21 下午6:57:25
 * @modify
 * @version 1.0.0
 */
public class EconomicCalendaAdapter extends ArrayListAdapter<Object> {

	private LayoutInflater inflater = null;
	
	public EconomicCalendaAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_economic_calenda_index, null);
			holder.timeView = (TextView) convertView.findViewById(R.id.txt_economic_time_index);
			holder.titleView = (TextView) convertView.findViewById(R.id.txt_economic_title_index);
			holder.effectView = (TextView) convertView.findViewById(R.id.txt_economic_effect_index);
			holder.previewView = (TextView) convertView.findViewById(R.id.txt_economic_preview_index);
			holder.predictView = (TextView) convertView.findViewById(R.id.txt_economic_predict_index);
			holder.actualView = (TextView) convertView.findViewById(R.id.txt_economic_actual_index);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		EconomicCalendar entity = (EconomicCalendar) mList.get(position);
		switch (Integer.parseInt(entity.getFlag().toString())) {
		case 0:
		case 3:
		case 6:
		case 1:
		case 4:
		case 7:
			holder.titleView.setText(entity.getTitle().toString());
			holder.titleView.setGravity(Gravity.CENTER);
			holder.timeView.setText("");
			holder.effectView.setText("");
			holder.previewView.setText("");
			holder.predictView.setText("");
			holder.actualView.setText("");
			break;
		case 2:
		case 5:
			holder.timeView.setText(entity.getTime().toString());
			holder.titleView.setText(entity.getTitle().toString());
			holder.titleView.setGravity(Gravity.LEFT);
			holder.effectView.setText(entity.getEffect().toString());
			holder.previewView.setText("");
			holder.predictView.setText("");
			holder.actualView.setText("");
			break;
		case 8:
			holder.timeView.setText(entity.getTime().toString());
			holder.titleView.setText(entity.getTitle().toString());
			holder.titleView.setGravity(Gravity.LEFT);
			holder.effectView.setText(entity.getEffect().toString());
			holder.previewView.setText(entity.getPreview().toString());
			holder.predictView.setText(entity.getPredict().toString());
			holder.actualView.setText(entity.getActual().toString());
			break;
		}
		return convertView;
	}

	static class ViewHolder {
		private TextView timeView;
		private TextView titleView;
		private TextView effectView;
		private TextView previewView;
		private TextView predictView;
		private TextView actualView;
	}
}
