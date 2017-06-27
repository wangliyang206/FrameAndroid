package com.cj.mobile.common.ui.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cj.mobile.common.R;
import com.cj.mobile.common.model.Region;
import com.cj.mobile.common.util.JsonUtil;
import com.cj.mobile.common.util.ResManager;

/**
 * 城市Picker
 * 
 * @author 王力杨
 * 
 */
@SuppressLint("HandlerLeak")
public class CityPicker extends LinearLayout {
	/*---------------------- 滑动控件 ----------------------*/
	/** 省 */
	private ScrollerNumberPicker provincePicker;
	
	/** 市 */
	private ScrollerNumberPicker cityPicker;
	
	/** 区 */
	private ScrollerNumberPicker counyPicker;
	
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	
	/*---------------------- 临时日期 ----------------------*/
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private int tempCounyIndex = -1;
	
	/** 句柄 */
	private Context context;
	
	/*---------------------- 临时数据 ----------------------*/
	private Region region;

	/** 工具对象 */
	private CitycodeUtil citycodeUtil;
	private String city_code_string;
	private String city_string;

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CityPicker(Context context) {
		super(context);
		this.context = context;
	}
	
	/**设置数据*/
	public void setData(Region region){
		this.region = region;
	}
	
	/**
	 * 是否加载本地数据(如果设置false那么需要在调用此方法前先调用setData()方法)
	 * @param isLocal
	 */
	public void loadView(boolean isLocal){
		super.onFinishInflate();
		
		if(isLocal){
			try {
				// 读取城市信息string
				String area_str = ResManager.readAssets(context, "region.properties");
				region = JsonUtil.getObject(area_str, Region.class);
			} catch (Exception e) {
			}
		}
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker_layout, this);
		citycodeUtil = CitycodeUtil.getSingleton();
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.pic_cityPickerLayout_province);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.pic_cityPickerLayout_city);
		counyPicker = (ScrollerNumberPicker) findViewById(R.id.pic_cityPickerLayout_couny);
		provincePicker.setData(citycodeUtil.getProvince(region.provinces));
		provincePicker.setDefault(0);
		cityPicker.setData(citycodeUtil.getCity(region.city, citycodeUtil.getProvince_list_code().get(0)));
		cityPicker.setDefault(0);
		counyPicker.setData(citycodeUtil.getCouny(region.area, citycodeUtil.getCity_list_code().get(0)));
		counyPicker.setDefault(0);
		provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (tempProvinceIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					// 城市数组
					cityPicker.setData(citycodeUtil.getCity(region.city,citycodeUtil.getProvince_list_code().get(id)));
					cityPicker.setDefault(0);
					counyPicker.setData(citycodeUtil.getCouny(region.area,citycodeUtil.getCity_list_code().get(0)));
					counyPicker.setDefault(0);
					int lastDay = Integer.valueOf(provincePicker.getListSize());
					if (id > lastDay) {
						provincePicker.setDefault(lastDay - 1);
					}
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {}
		});
		cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					counyPicker.setData(citycodeUtil.getCouny(region.area,citycodeUtil.getCity_list_code().get(id)));
					counyPicker.setDefault(0);
					int lastDay = Integer.valueOf(cityPicker.getListSize());
					if (id > lastDay) {
						cityPicker.setDefault(lastDay - 1);
					}
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {}
		});
		counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {

				if (text.equals("") || text == null)
					return;
				if (tempCounyIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = cityPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					// 城市数组
					city_code_string = citycodeUtil.getCouny_list_code().get(id);
					int lastDay = Integer.valueOf(counyPicker.getListSize());
					if (id > lastDay) {
						counyPicker.setDefault(lastDay - 1);
					}
				}
				tempCounyIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {}
		});
	}

//	@Override
//	protected void onFinishInflate() {}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getCity_code_string() {
		return city_code_string;
	}
	
	/**省*/
	public String getProvinceCode(){
		return citycodeUtil.getProvince_list_code().get(provincePicker.getSelected());
	}
	
	/**市*/
	public String getCityCode(){
		return citycodeUtil.getCity_list_code().get(cityPicker.getSelected());
	}
	
	/**区*/
	public String getCounyCode(){
		return citycodeUtil.getCouny_list_code().get(counyPicker.getSelected());
	}

	public String getCity_string() {
		city_string = provincePicker.getSelectedText() + cityPicker.getSelectedText() + counyPicker.getSelectedText();
		return city_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
