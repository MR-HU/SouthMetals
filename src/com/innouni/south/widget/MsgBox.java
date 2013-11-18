package com.innouni.south.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.innouni.south.activity.R;

/**
 * ˵��:�Ի����װ��
 * @author HuGuojun
 * @����ʱ�� 2013-08-23
 */
public class MsgBox {

	private static AlertDialog alert;
	private DialogInterface.OnClickListener positiveListener, negativeListener;
	private String message;
	private String positiveText = "";
	private String negativeText = "";

	/**
	 * ������߰�ť�ϵ�����
	 */
	public void setPositiveText(String positiveText) {
		this.positiveText = positiveText;
	}

	/**
	 * �����ұ߰�ť�ϵ�����
	 */
	public void setNegativeText(String negativeText) {
		this.negativeText = negativeText;
	}

	public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
		this.positiveListener = positiveListener;
	}

	public void setNegativeListener(DialogInterface.OnClickListener negativeListener) {
		this.negativeListener = negativeListener;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void show(Context context) {
		dismiss();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.alter_title));
		builder.setMessage(message).setCancelable(false);
		if ("".equals(positiveText)) {
			positiveText = context.getResources().getString(R.string.dialog_positive);
		}
		if ("".equals(negativeText)) {
			negativeText = context.getResources().getString(R.string.dialog_negative);
		}
		if (null != positiveListener) {
			builder.setPositiveButton(positiveText, positiveListener);
		}
		if (null != negativeListener) {
			builder.setNegativeButton(negativeText, negativeListener);
		}
		AlertDialog alert = builder.create();
		try {
			alert.show();
		} catch (Exception e) {
			
		}
	}

	public static void dismiss() {
		if (null != alert) {
			alert.dismiss();
			alert = null;
		}
	}

	public static void alert(String msg, Context context) {
		alert(msg, context, null);
	}

	/**
	 * ��ʾ��ʾ��Ϣ
	 * @param msg
	 * @param context
	 * @param listener
	 */
	public static void alert(String msg, Context context, DialogInterface.OnClickListener listener) {
		MsgBox msgbox = new MsgBox();
		msgbox.message = msg;
		if (null == listener) {
			msgbox.positiveListener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MsgBox.dismiss();
				}
			};
		} else {
			msgbox.positiveListener = listener;
		}
		msgbox.show(context);
	}
}
