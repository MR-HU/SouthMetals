package com.innouni.south.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.ExpertInfo;
import com.innouni.south.net.ExecutorsImageLoader;
import com.innouni.south.net.ExecutorsImageLoader.ImageCallback;


/**
 * 在线提问页面专家列表的适配器
 * @author HuGuojun
 * @data 2013-09-05
 */
public class ExpertOnlineAdapter extends ArrayListAdapter<Object> {

	private LayoutInflater inflater = null;
	private AbsListView absListView;
	private ExecutorsImageLoader exeLoader;
	
	public ExpertOnlineAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		exeLoader = ExecutorsImageLoader.getInstance(context);
	}

	public  void setAbsListView(AbsListView absListView){
		this.absListView = absListView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpertInfo expert = null;
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_online_question_listview, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.headView = (ImageView) convertView.findViewById(R.id.image_expert_head);
			viewHolder.nameView = (TextView) convertView.findViewById(R.id.txt_expert_name);
			viewHolder.descView = (TextView) convertView.findViewById(R.id.txt_expert_description);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		expert = (ExpertInfo) mList.get(position);
		viewHolder.nameView.setText(expert.getName().toString());
		viewHolder.descView.setText(expert.getDescription().toString());
		
		String url = expert.getImageUrl().toString();
		viewHolder.headView.setTag(position + "_" + url);
		loadRemoteImage(url, viewHolder.headView, position);
		return convertView;
	}
	
	private void loadRemoteImage(String url, final ImageView imageView, int pos) {
		final String tag = pos + "_" + url;
		imageView.setTag(tag);
		imageView.setImageResource(R.drawable.user_expert_default);
		Bitmap map = exeLoader.loadDrawable(url, new ImageCallback() {
			@Override
			public void imageLoaded(Bitmap imageDrawable) {
				ImageView imageView = (ImageView) absListView.findViewWithTag(tag);
				if(null != imageDrawable && null != imageView) {
					imageView.setImageBitmap(imageDrawable);
				}
			}
		});
		if (null != map) {
			imageView.setImageBitmap(map);
		}
	}

	static class ViewHolder {
		ImageView headView;
		TextView nameView;
		TextView descView;
	}
}
