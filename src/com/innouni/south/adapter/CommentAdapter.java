package com.innouni.south.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.MarketNews;


/**
 * ‘Á÷–ÕÌ∆¿¬€  ≈‰∆˜
 * @author HuGuojun
 * @data 2013-09-04
 */
public class CommentAdapter extends ArrayListAdapter<Object> {

	private LayoutInflater inflater = null;
	
	public CommentAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MarketNews news = null;
		ViewHolder viewHolder = null;
		if(null == convertView){
			convertView = inflater.inflate(R.layout.item_market_news_listview, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.titleView = (TextView) convertView.findViewById(R.id.txt_market_news_title);
			viewHolder.contentView = (TextView) convertView.findViewById(R.id.txt_market_news_content);
			viewHolder.timeView = (TextView) convertView.findViewById(R.id.txt_market_news_time);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		news = (MarketNews) mList.get(position);
		viewHolder.titleView.setText(news.getTitle().toString());
		viewHolder.contentView.setText(news.getDescription().toString());
		viewHolder.timeView.setText(news.getTime().toString());
		return convertView;
	}
	
	static class ViewHolder{
		TextView titleView;
		TextView contentView;
		TextView timeView;
	}

}
