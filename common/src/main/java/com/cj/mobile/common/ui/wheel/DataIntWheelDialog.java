package com.cj.mobile.common.ui.wheel;

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

/**
 * 弹框——Int值选择器(例：单位)
 * @author 王力杨
 *
 */
public class DataIntWheelDialog extends AlertDialog implements OnClickListener{
	/** 最终显示在此控件上 */
	private TextView showView;
	/** 确定按钮 */
	private Button ok;
	/** 取消按钮 */
	private Button cancel;
	/** 选择控件 */
	private AbWheelView wheelView;
	
	/** 屏幕宽度、高度*/
	private int diaplayWidth,diaplayHeight;
	/** 弹出的Dialog的左右边距. */
	private int dialogPadding = 40;
	/** 句柄 */
	private Activity context;
	
	/** 最小值 */
	private int minValue;
	/** 最大值 */
	private int maxValue;
	/** 列表是否可循环 */
	private boolean isCyclic;
	/** 单位 */
	private String unit;
	/** 选择当前默认项 */
	private int currentItem = 0;
	
	/**
	 * 构造方法()
	 * @param context			句柄
	 * @param view				最终显示在此控件上
	 * @param minValue			选择器最小数据值
	 * @param maxValue			选择器最大数据值
	 * @param isCyclic			可循环滚动
	 * @param unit				单位
	 * @param currentItem		选择器默认选择项
	 * @param diaplayWidth		屏幕的宽度
	 * @param diaplayHeight		屏幕的高度
	 */
	public DataIntWheelDialog(Activity context,TextView view,int minValue, int maxValue,boolean isCyclic,String unit,int currentItem,int diaplayWidth,int diaplayHeight) {
		super(context,R.style.dialog);
		
		this.context = context;
		this.showView = view;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.isCyclic = isCyclic;
		this.unit = unit;
		this.currentItem = currentItem;
		this.diaplayWidth = diaplayWidth;
		this.diaplayHeight = diaplayHeight;
		
		initDialog();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View inflater = LayoutInflater.from(context).inflate(R.layout.choose_one, null);
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
		wheelView = (AbWheelView) findViewById(R.id.abwhvi_chooseOne_wheel);
		ok = (Button) findViewById(R.id.btn_chooseOne_ok);
		cancel = (Button) findViewById(R.id.btn_chooseOne_cancel);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initDate() {
		wheelView.setAdapter(new AbNumericWheelAdapter(minValue, maxValue));
		// 可循环滚动
		wheelView.setCyclic(isCyclic);
		// 添加文字
		wheelView.setLabel(unit);
		// 初始化时显示的数据
		wheelView.setCurrentItem(currentItem);
		wheelView.setValueTextSize(35,diaplayWidth,diaplayHeight);
		wheelView.setLabelTextSize(35,diaplayWidth,diaplayHeight);
		wheelView.setLabelTextColor(0x80000000);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_chooseOne_ok){
			int index = wheelView.getCurrentItem();
			String val = wheelView.getAdapter().getItem(index);
			if(showView != null)
				showView.setText(val+unit);
		}
		
		this.dismiss();
	}
}
