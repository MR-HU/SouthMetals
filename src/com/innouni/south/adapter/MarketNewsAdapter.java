package com.innouni.south.adapter;

import android.R.integer;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.MarketNews;


/**
 * 新闻资讯(包括实时银讯，每日精评，全球市场，外汇要闻，世界财经，名家专栏等同类型数据)填充数据的适配器
 * @author HuGuojun
 * @data 2013-09-04
 */
public class MarketNewsAdapter extends ArrayListAdapter<Object> {
	
	private LayoutInflater inflater = null;
	
	public MarketNewsAdapter(Context context) {
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
		viewHolder.contentView.setText(subHtml(news.getContent().toString()));
		viewHolder.timeView.setText(news.getTime().toString());
		return convertView;
	}
	
	static class ViewHolder{
		TextView titleView;
		TextView contentView;
		TextView timeView;
	}
	
//	private String subContent(String content) {
//		String sub = content.substring(content.indexOf("<p>"), content.indexOf("</p>"));
//		return sub;
//	}
	
	private String subHtml(String content) {
		Spanned spanned = Html.fromHtml(content);
		int length = spanned.length();
		if (spanned.length() > 50) {
			length = 50;
		}
		return spanned.subSequence(0, length).toString().trim();
	}

}
