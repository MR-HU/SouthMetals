package com.innouni.south.adapter;

import java.util.HashMap;
import java.util.List;

import com.innouni.south.util.ShareUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 实时数据详情列表适配器
 * 
 * @author HuGuojun
 * @date 2013-11-26 下午5:03:05
 * @modify
 * @version 1.0.0
 */
public class AllRealTimeDataBottomAdapter extends SimpleAdapter{
	
	private ShareUtil shareUtil;
	
	public AllRealTimeDataBottomAdapter(Context paramContext, List<HashMap<String, String>> paramList, int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt){
	    super(paramContext, paramList, paramInt, paramArrayOfString, paramArrayOfInt);
	    shareUtil = ShareUtil.getInstance(paramContext);
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup){
	    View localView = super.getView(paramInt, paramView, paramViewGroup);
	    TextView localTextView1,localTextView2,localTextView3,localTextView4;
	    float f1,f2;
	    LinearLayout localLinearLayout = (LinearLayout)((LinearLayout)localView).getChildAt(0);
        localTextView1 = (TextView)localLinearLayout.getChildAt(1); //买入
        localTextView2 = (TextView)localLinearLayout.getChildAt(2); //卖出
        localTextView3 = (TextView)localLinearLayout.getChildAt(5); //涨跌
        f1 = Float.parseFloat(localTextView3.getText().toString()); 
        localTextView4 = (TextView)localLinearLayout.getChildAt(6); //涨跌幅 
        String str = localTextView4.getText().toString().replace("%", "");
        boolean bool = str.equals("--");
        f2 = 0.0F;
        if (!bool) f2 = Float.parseFloat(str);
        if (f1 > 0.0F){
    		localTextView1.setTextColor(-65536);
    		if((shareUtil.getIntValues("type_position_bottom") != 1) 
    				&& (shareUtil.getIntValues("type_position_bottom") != 5) 
    				&& (shareUtil.getIntValues("type_position_bottom") != 4)) {
    			localTextView2.setTextColor(-65536);
    		} else {
    			localTextView2.setTextColor(Color.parseColor("#ffcccccc"));
			}
    		localTextView3.setTextColor(-65536);
	        localTextView4.setTextColor(-65536);
		} else if(f1 == 0.0F) {
			localTextView1.setTextColor(-1);
			if((shareUtil.getIntValues("type_position_bottom") != 1) 
    				&& (shareUtil.getIntValues("type_position_bottom") != 5) 
    				&& (shareUtil.getIntValues("type_position_bottom") != 4)) {
				localTextView2.setTextColor(-1);
	    	} else {
    			localTextView2.setTextColor(Color.parseColor("#ffcccccc"));
			}
			localTextView3.setTextColor(-1);
			localTextView4.setTextColor(-1);
		} else {
			localTextView1.setTextColor(-16711936);
	     	if((shareUtil.getIntValues("type_position_bottom") != 1) 
    				&& (shareUtil.getIntValues("type_position_bottom") != 5) 
    				&& (shareUtil.getIntValues("type_position_bottom") != 4)) {
	     		localTextView2.setTextColor(-16711936);
	     	} else {
    			localTextView2.setTextColor(Color.parseColor("#ffcccccc"));
			}
	     	localTextView3.setTextColor(-16711936);
	     	localTextView4.setTextColor(-16711936);
		}
	    return localView;
	}

}
