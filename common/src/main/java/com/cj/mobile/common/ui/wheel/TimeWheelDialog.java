package com.cj.mobile.common.ui.wheel;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cj.mobile.common.R;
import com.cj.mobile.common.util.DateFormatUtils;

/**
 * 弹框——选择日期
 * @author 王力杨
 *
 */
public class TimeWheelDialog extends AlertDialog implements OnClickListener{
	/** 最终显示在此控件上 */
	private TextView showView;
	/** 确定按钮 */
	private Button ok;
	/** 取消按钮 */
	private Button cancel;
	/** 时间选择控件 */
	private AbWheelView hourView,minuteView;
	
	/** 是否初始化默认时间为当前时间 */
	private boolean initStart = false;
	/** 默认小时、分钟 */
	private int defaultHour = -1;
	private int defaultMinute = -1;
	
	/** 屏幕宽度、高度*/
	private int diaplayWidth,diaplayHeight;
	/** 弹出的Dialog的左右边距. */
	private int dialogPadding = 40;
	/** 句柄 */
	private Activity context;
	
	/**
	 * 构造方法(默认选择 指定小时、分钟)
	 * @param context			句柄
	 * @param view				最终显示在此控件上
	 * @param defaultHour		默认选择指定的小时
	 * @param defaultMinute	默认选择指定的分钟
	 * @param diaplayWidth		屏幕的宽度
	 * @param diaplayHeight	屏幕的高度
	 */
	public TimeWheelDialog(Activity context,TextView view,int defaultHour,int defaultMinute,int diaplayWidth,int diaplayHeight) {
		super(context,R.style.dialog);
		
		this.context = context;
		this.showView = view;
		this.defaultHour = defaultHour;
		this.defaultMinute = defaultMinute;
		this.diaplayWidth = diaplayWidth;
		this.diaplayHeight = diaplayHeight;
		
		initDialog();
	}

	/**
	 * 构造方法(默认选择 当前小时、分钟)
	 * @param context			句柄
	 * @param view				最终显示在此控件上
	 * @param initStart			是否初始化默认时间为当前时间
	 * @param diaplayWidth		屏幕的宽度
	 * @param diaplayHeight		屏幕的高度
	 */
	public TimeWheelDialog(Activity context,TextView view,boolean initStart,int diaplayWidth,int diaplayHeight) {
		super(context,R.style.dialog);
		
		this.context = context;
		this.showView = view;
		this.initStart = initStart;
		this.diaplayWidth = diaplayWidth;
		this.diaplayHeight = diaplayHeight;
		
		initDialog();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View inflater = LayoutInflater.from(context).inflate(R.layout.choose_two, null);
		setContentView(inflater,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
		
		initView();
		initDate();
	}
	
	/**
	 * 初始化dialog
	 */
	private void initDialog() {
		
		//如果Dialog不是充满屏幕，要设置这个值
        if(diaplayWidth < 400){
        	dialogPadding = 30;
		}else if(diaplayWidth>700){
			dialogPadding = 50;
		}
		
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		//此处可以设置dialog显示的位置
		window.setGravity(Gravity.BOTTOM); 
		//设置宽度
//		lp.width = diaplayWidth-dialogPadding;
//		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
//		lp.dimAmount = 0f;
		window.setAttributes(lp);
		this.setCanceledOnTouchOutside(false);//点击任意区域不关闭当前Dialog
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		hourView = (AbWheelView) findViewById(R.id.abwhvi_chooseTwo_wheel_A);
		minuteView = (AbWheelView) findViewById(R.id.abwhvi_chooseTwo_wheel_B);
		ok = (Button) findViewById(R.id.btn_chooseTwo_ok);
		cancel = (Button) findViewById(R.id.btn_chooseTwo_cancel);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initDate() {
		
		//时间选择可以这样实现
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
				
		if(initStart){
			defaultHour = hour;
			defaultMinute = minute;
		}
		
		// 时
		hourView.setAdapter(new AbNumericWheelAdapter(0, 23));
		hourView.setCyclic(true);
		hourView.setLabel("点");
		hourView.setCurrentItem(defaultHour);
		hourView.setValueTextSize(32,diaplayWidth,diaplayHeight);
		hourView.setLabelTextSize(30,diaplayWidth,diaplayHeight);
		hourView.setLabelTextColor(0x80000000);
		
		// 分
		minuteView.setAdapter(new AbNumericWheelAdapter(0, 59));
		minuteView.setCyclic(true);
		minuteView.setLabel("分");
		minuteView.setCurrentItem(defaultMinute);
		minuteView.setValueTextSize(32,diaplayWidth,diaplayHeight);
		minuteView.setLabelTextSize(30,diaplayWidth,diaplayHeight);
		minuteView.setLabelTextColor(0x80000000);
		
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_chooseTwo_ok){
			int index2 = hourView.getCurrentItem();
			int index3 = minuteView.getCurrentItem();
			String val = DateFormatUtils.dateTimeFormat(index2+":"+index3) ;
			if(showView != null)
				showView.setText(DateFormatUtils.dateTimeFormat(val));
		}
		
		this.dismiss();
	}
}
