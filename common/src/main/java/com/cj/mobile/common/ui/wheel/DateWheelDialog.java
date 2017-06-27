package com.cj.mobile.common.ui.wheel;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.cj.mobile.common.R;
import com.cj.mobile.common.util.DateFormatUtils;

/**
 * 弹框——选择日期
 * @author 王力杨
 *
 */
public class DateWheelDialog extends AlertDialog implements OnClickListener,AbOnWheelChangedListener{
	/** 最终显示在此控件上 */
	private TextView view;
	/** 确定按钮 */
	private Button ok;
	/** 取消按钮 */
	private Button cancel;
	/** 日期选择控件 */
	private AbWheelView chooseWheelY,chooseWheelM,chooseWheelD;
	/** 分隔符 */
	private String delimiter = "";
	/** 起始年限，结束年限 */
	private int startYear = 1970;
	private int endYearOffset = 100;
	/** 添加大小月月份并将其转换为list,方便之后的判断 */
	private String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	private String[] months_little = { "4", "6", "9", "11" };
	private List<String> list_big = Arrays.asList(months_big);
	private List<String> list_little = Arrays.asList(months_little);
	
	/** 是否初始化默认时间为当前时间 */
	private boolean initStart = false;
	/** 默认年、月、日 */
	private int defaultYear = -1;
	private int defaultMonth = -1;
	private int defaultDay = -1;
	
	/** 屏幕宽度、高度*/
	private int diaplayWidth,diaplayHeight;
	/** 弹出的Dialog的左右边距. */
	private int dialogPadding = 40;
	/** 句柄 */
	private Activity context;
	
	/**
	 * 构造方法(默认选择 指定年月日)
	 * @param context			句柄
	 * @param view				最终显示在此控件上
	 * @param delimiter			日期分隔符(例：2014-12-30、20141230)
	 * @param startYear			起始年份(例：1900、2014。一般为四位)
	 * @param endYearOffset		年结束偏移量(例：10代表10年，年份列表会展示  2014 至 2014+10=2024)
	 * @param defaultYear		默认选择指定的年份
	 * @param defaultMonth		默认选择指定的月份
	 * @param defaultDay		默认选择指定的天份
	 * @param diaplayWidth		屏幕的宽度
	 * @param diaplayHeight		屏幕的高度
	 */
	public DateWheelDialog(Activity context,TextView view,String delimiter,int startYear,int endYearOffset,int defaultYear,int defaultMonth,int defaultDay,int diaplayWidth,int diaplayHeight) {
		super(context,R.style.dialog);
		
		this.context = context;
		this.view = view;
		this.delimiter = delimiter;
		this.startYear = startYear;
		this.endYearOffset = endYearOffset;
		this.defaultYear = defaultYear;
		this.defaultMonth = defaultMonth;
		this.defaultDay = defaultDay;
		this.diaplayWidth = diaplayWidth;
		this.diaplayHeight = diaplayHeight;
		
		initDialog();
	}

	/**
	 * 构造方法(默认选择 当前年月日)
	 * @param context			句柄
	 * @param view				最终显示在此控件上
	 * @param delimiter			日期分隔符(例：2014-12-30、20141230)
	 * @param startYear			起始年份(例：1900、2014。一般为四位)
	 * @param endYearOffset		年结束偏移量(例：10代表10年，年份列表会展示  2014 至 2014+10=2024)
	 * @param initStart			是否初始化默认时间为当前时间
	 * @param diaplayWidth		屏幕的宽度
	 * @param diaplayHeight		屏幕的高度
	 */
	public DateWheelDialog(Activity context,TextView view,String delimiter,int startYear,int endYearOffset,boolean initStart,int diaplayWidth,int diaplayHeight) {
		super(context,R.style.dialog);
		
		this.context = context;
		this.view = view;
		this.delimiter = delimiter;
		this.startYear = startYear;
		this.endYearOffset = endYearOffset;
		this.initStart = initStart;
		this.diaplayWidth = diaplayWidth;
		this.diaplayHeight = diaplayHeight;
		
		initDialog();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View inflater = LayoutInflater.from(context).inflate(R.layout.choose_three, null);
		setContentView(inflater,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
//		setContentView(inflater,new LayoutParams(diaplayWidth-dialogPadding,400));
		
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
		chooseWheelY = (AbWheelView) findViewById(R.id.abwhvi_chooseThree_wheel_Y);
		chooseWheelM = (AbWheelView) findViewById(R.id.abwhvi_chooseThree_wheel_M);
		chooseWheelD = (AbWheelView) findViewById(R.id.abwhvi_chooseThree_wheel_D);
		ok = (Button) findViewById(R.id.btn_chooseThree_ok);
		cancel = (Button) findViewById(R.id.btn_chooseThree_cancel);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initDate() {
		
		int endYear = startYear+endYearOffset;
		//时间选择可以这样实现
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
				
		if(initStart){
			 defaultYear = year;
			 defaultMonth = month;
			 defaultDay = day;
		}
		
		//设置"年"的显示数据
		chooseWheelY.setAdapter(new AbNumericWheelAdapter(startYear, endYear));
		chooseWheelY.setCyclic(true);// 可循环滚动
		chooseWheelY.setLabel("年");  // 添加文字
		chooseWheelY.setCurrentItem(defaultYear != -1 ?defaultYear - startYear:0);// 初始化时显示的数据
		chooseWheelY.setValueTextSize(32,diaplayWidth,diaplayHeight);
		chooseWheelY.setLabelTextSize(30,diaplayWidth,diaplayHeight);
		chooseWheelY.setLabelTextColor(0x80000000);
		
		// 月
		chooseWheelM.setAdapter(new AbNumericWheelAdapter(1, 12));
		chooseWheelM.setCyclic(true);
		chooseWheelM.setLabel("月");
		chooseWheelM.setCurrentItem(defaultMonth != -1 ?defaultMonth-1:0);
		chooseWheelM.setValueTextSize(32,diaplayWidth,diaplayHeight);
		chooseWheelM.setLabelTextSize(30,diaplayWidth,diaplayHeight);
		chooseWheelM.setLabelTextColor(0x80000000);
		
		// 日
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month))) {
			chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if (DateFormatUtils.isLeapYear(year)){
				chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 29));
			}else{
				chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 28));
			}
		}
		chooseWheelD.setCyclic(true);
		chooseWheelD.setLabel("日");
		chooseWheelD.setCurrentItem(defaultDay != -1 ?defaultDay - 1:0);
		chooseWheelD.setValueTextSize(32,diaplayWidth,diaplayHeight);
		chooseWheelD.setLabelTextSize(30,diaplayWidth,diaplayHeight);
		chooseWheelD.setLabelTextColor(0x80000000);
		
		//添加监听
		chooseWheelY.addChangingListener(this);
		chooseWheelM.addChangingListener(this);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_chooseThree_ok){
			int indexYear = chooseWheelY.getCurrentItem();
			String year = chooseWheelY.getAdapter().getItem(indexYear);
			
			int indexMonth = chooseWheelM.getCurrentItem();
			String month = chooseWheelM.getAdapter().getItem(indexMonth);
			
			int indexDay = chooseWheelD.getCurrentItem();
			String day = chooseWheelD.getAdapter().getItem(indexDay);
			if(view != null)
				view.setText(DateFormatUtils.dateTimeFormat(year+delimiter+month+delimiter+day));
		}
		
		this.dismiss();
	}

	@Override
	public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
		if(wheel.getId() == R.id.abwhvi_chooseThree_wheel_Y){
			// 添加"年"监听
			int year_num = newValue + startYear;
			int mDIndex = chooseWheelM.getCurrentItem();
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(chooseWheelM.getCurrentItem() + 1))) {
				chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(chooseWheelM.getCurrentItem() + 1))) {
				chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 30));
			} else {
				if (DateFormatUtils.isLeapYear(year_num))
					chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 29));
				else
					chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 28));
			}
			chooseWheelM.setCurrentItem(mDIndex);
		}else if(wheel.getId() == R.id.abwhvi_chooseThree_wheel_M){
			// 添加"月"监听
			int month_num = newValue + 1;
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(month_num))) {
				chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month_num))) {
				chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 30));
			} else {
				int year_num = chooseWheelY.getCurrentItem() + startYear;
				if (DateFormatUtils.isLeapYear(year_num))
					chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 29));
				else
					chooseWheelD.setAdapter(new AbNumericWheelAdapter(1, 28));
			}
			chooseWheelD.setCurrentItem(0);
		}
	}
}
