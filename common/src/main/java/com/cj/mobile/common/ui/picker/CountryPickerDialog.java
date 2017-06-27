package com.cj.mobile.common.ui.picker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cj.mobile.common.R;
import com.cj.mobile.common.model.Region;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CountryPickerDialog extends AlertDialog implements OnClickListener{

	public interface CityPickerChangeView{
		void oCityPickerChangeView();
	}
	/** 最终显示在此控件上 */
	private TextView view;
	/** 确定按钮 */
	private Button ok;
	/** 取消按钮 */
	private Button cancel;
	/** 省市区控件 */
	private CountryPicker content;
	private Region region = null;
	private CityPickerChangeView cpc = null;
	/**
	 * 构造方法
	 * @param context		句柄
	 * @param view			最终显示在此控件上
	 */
	public CountryPickerDialog(Context context,TextView view) {
		super(context, R.style.dialog);
		
		this.view = view;
	}
	
	/**
	 * 构造方法
	 * @param context		句柄
	 * @param view			最终显示在此控件上
	 * @param region		传入的数据
	 * @param cpc			监听控件改变
	 */
	public CountryPickerDialog(Context context,TextView view,Region region,CityPickerChangeView cpc) {
		super(context,R.style.dialog);
		
		this.view = view;
		this.cpc = cpc;
		this.region = region;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.country_picker_dialog);
		
		initView();
	}

	/**
	 * 初始化控件
	 */
	@SuppressWarnings("deprecation")
	private void initView() {
		content = (CountryPicker) findViewById(R.id.cipi_cityPickerDialog_content);
		ok = (Button) findViewById(R.id.btn_cityPickerDialog_ok);
		cancel = (Button) findViewById(R.id.btn_cityPickerDialog_cancel);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		if(region != null)
			content.setData(region);
		content.loadView(false);
		
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		lp.dimAmount = 0f;
		//此处可以设置dialog显示的位置
		window.setGravity(Gravity.BOTTOM); 
		window.setAttributes(lp);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_cityPickerDialog_ok){
			if(view != null){
				view.setText(content.getCity_string());
				List<String> list = new ArrayList<String>();
				list.add(content.getProvinceCode());
				list.add(content.getCityCode());
//				list.add(content.getCounyCode());
				Timber.d("#1="+list.get(0)+"；#2="+list.get(1));
				view.setTag(list);
			}
			if(this.cpc != null){
				this.cpc.oCityPickerChangeView();
			}
		}
		
		this.dismiss();
	}

}
