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
import com.innouni.south.entity.MessageInfo;
import com.innouni.south.net.ExecutorsImageLoader;
import com.innouni.south.net.ExecutorsImageLoader.ImageCallback;

/**
 * �����б�������
 * @author HuGuojun
 * @data 2013-09-04
 */
public class MessageAdapter extends ArrayListAdapter<Object> {
	
	private static final int TYPE_USER = 0;
	private static final int TYPE_EXPERT = 1;
	
	private String userImage;
	private String expertImage;
	
	private LayoutInflater inflater = null;
	private AbsListView absListView;
	private ExecutorsImageLoader exeLoader;
	
	public MessageAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		exeLoader = ExecutorsImageLoader.getInstance(context);
	}

	public  void setAbsListView(AbsListView absListView){
		this.absListView = absListView;
	}
	
	//��ʹ��ListViewʱ,���ʹ����getItemViewType,����ֵһ��Ҫ�Ǵ�0��ʼ����,
	//��Ҫ����getViewTypeCount����,
	//������getViewTypeCount > getItemViewType
	@Override
	public int getItemViewType(int position) {
		MessageInfo message = (MessageInfo) mList.get(position);
		if("1".equals(message.getType().toString())){
			return TYPE_USER;
		} else {
			return TYPE_EXPERT;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String url;
		ViewHolder viewHolder = null;   
		int type = getItemViewType(position);
		if(type == TYPE_USER){
			url = getUserImage();
		} else {
			url = getExpertImage();
		}
		
		if(null == convertView){  
        	viewHolder = new ViewHolder(); 
			if(type == TYPE_USER){
				convertView = inflater.inflate(R.layout.item_user_message_listview, parent, false);
				viewHolder.headView = (ImageView) convertView.findViewById(R.id.image_message_head);
				viewHolder.contentView = (TextView) convertView.findViewById(R.id.message_content);
			} else {
				convertView = inflater.inflate(R.layout.item_expert_message_listview, parent, false);
				viewHolder.headView = (ImageView) convertView.findViewById(R.id.image_message_head);
				viewHolder.contentView = (TextView) convertView.findViewById(R.id.message_content);
			}
			convertView.setTag(viewHolder);
        } else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        
		MessageInfo message = (MessageInfo) mList.get(position);
		viewHolder.contentView.setText(message.getContent().toString());
		viewHolder.headView.setTag(position + "_" + url);
		loadRemoteImage(url, viewHolder.headView, position, type);
		return convertView;
	}
	
	private void loadRemoteImage(String url, final ImageView imageView, int pos, int type) {
		final String tag = pos + "_" + url;
		imageView.setTag(tag);
		if (type == TYPE_USER) {
			imageView.setImageResource(R.drawable.message_user_default);
		} else {
			imageView.setImageResource(R.drawable.message_expert_default);
		}
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
		TextView contentView;
	}
	
	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getExpertImage() {
		return expertImage;
	}

	public void setExpertImage(String expertImage) {
		this.expertImage = expertImage;
	}
}
