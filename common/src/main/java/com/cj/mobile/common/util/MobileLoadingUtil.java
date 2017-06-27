package com.cj.mobile.common.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.cj.mobile.common.R;

public class MobileLoadingUtil {
	public interface CSSListenerLoading {
		public void onLoadingOver(String msg);
	}
	
	public static class LoadingHandler extends Handler{
		public CSSListenerLoading cssListenerLoading = null;
		public ProgressDialog progressDialog = null;
		
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null)
				progressDialog.dismiss();

			if (cssListenerLoading != null)
				cssListenerLoading.onLoadingOver(msg.getData().getString("msg"));
		}
		
		public void sendMsg(String msg){
			Message message = new Message();
			message.what = 1;
			Bundle bundle = new Bundle();
			bundle.putString("msg", msg);
			message.setData(bundle);
			// 向handler发消息
			this.sendMessage(message);
		}

		public void closeDialog() {
			if (progressDialog != null)
				progressDialog.dismiss();
		}
	}

	public static LoadingHandler showLoadingDialog(Context context, String text,
			CSSListenerLoading cssListenerLoading) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(text);
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		LoadingHandler handler = new MobileLoadingUtil.LoadingHandler();
		handler.cssListenerLoading = cssListenerLoading;
		handler.progressDialog = progressDialog;

		return handler;
	}
	
	public static LoadingHandler showLoadingDialog(Context context, View view,
			CSSListenerLoading cssListenerLoading) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.show();
		progressDialog.setContentView(view);
		
		LoadingHandler handler = new MobileLoadingUtil.LoadingHandler();
		handler.cssListenerLoading = cssListenerLoading;
		handler.progressDialog = progressDialog;

		return handler;
	}
	
	public static LoadingHandler showLoadingDialog(Context context, int layoutResID,
			CSSListenerLoading cssListenerLoading) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.show();
		progressDialog.setContentView(layoutResID);

		LoadingHandler handler = new MobileLoadingUtil.LoadingHandler();
		handler.cssListenerLoading = cssListenerLoading;
		handler.progressDialog = progressDialog;

		return handler;
	}

	public static LoadingHandler showHaveCLoseDialog(Context context,String contentText,final CSSListenerLoading cssListenerLoading) {
		final ProgressDialog progressDialog = new ProgressDialog(context);
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

		progressDialog.setCancelable(false);
		progressDialog.show();
		progressDialog.setContentView(R.layout.dialog_locate_layout);
		ImageView closeIv = (ImageView) progressDialog.findViewById(R.id.dialog_locate_layout_close_iv);
		ViewGroup.LayoutParams closeIvParams = closeIv.getLayoutParams();
		closeIvParams.width = screenWidth / 20;
		closeIvParams.height = screenWidth / 20;
		closeIv.setLayoutParams(closeIvParams);

		closeIv.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				if (progressDialog != null)
					progressDialog.dismiss();
				if (cssListenerLoading != null) {
					cssListenerLoading.onLoadingOver("close");
				}
			}
		});

		ImageView loadiconIv = (ImageView)progressDialog.findViewById(R.id.dialog_locate_layout_loadingicon_tv);
		ViewGroup.LayoutParams loadiconParams = loadiconIv.getLayoutParams();
		loadiconParams.width = screenWidth / 10;
		loadiconParams.height = screenWidth / 10;
		loadiconIv.setLayoutParams(loadiconParams);

		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.image_rotation_anim);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			loadiconIv.startAnimation(operatingAnim);
		}

		TextView  contentTextTv = (TextView) progressDialog.findViewById(R.id.dialog_locate_layout_content_text_tv);
		contentTextTv.setText(contentText);
		LoadingHandler handler = new MobileLoadingUtil.LoadingHandler();
		handler.cssListenerLoading = cssListenerLoading;
		handler.progressDialog = progressDialog;
		return handler;
	}
}

