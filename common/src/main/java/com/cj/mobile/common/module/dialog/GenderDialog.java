package com.cj.mobile.common.module.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.cj.mobile.common.R;

/**
 * 选择性别弹框
 * @author 王力杨
 *
 */
public class GenderDialog extends AlertDialog implements OnCheckedChangeListener{
	private TextView view = null;//最终显示到此控件中
	private EditText edit = null;//最终显示到此控件中
	private int genderPosition = -1;//默认显示(1男，2女，9其它)
	private Drawable drawable;
	private RadioGroup group;
	private RadioButton male,female;

	/**
	 * 构造方法
	 * @param context		句柄
	 * @param theme			样式
	 * @param view			最终显示到此控件中
	 * @param genderPosition		默认显示(1男，2女，9其它)
	 */
	public GenderDialog(Context context, int theme, TextView view, int genderPosition) {
		super(context,theme);
		this.view = view;
		this.genderPosition = genderPosition;
		drawable = context.getResources().getDrawable(R.drawable.dialog_gender_selected);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
	}
	
	/**
	 * 构造方法
	 * @param context					句柄
	 * @param theme					样式
	 * @param edit						最终显示到此控件中
	 * @param genderPosition			默认显示(1男，2女，9其它)
	 */
	public GenderDialog(Context context, int theme, EditText edit, int genderPosition) {
		super(context,theme);
		this.edit = edit;
		this.genderPosition = genderPosition;
		drawable = context.getResources().getDrawable(R.drawable.dialog_gender_selected);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_gender_layout);
		
		initView();
		initData();
	}
	
	private void initView() {
		group = (RadioGroup) findViewById(R.id.rdioGrup_dialogGenderLayout_gender);
		male = (RadioButton) findViewById(R.id.rdio_dialogGenderLayout_male);
		female = (RadioButton) findViewById(R.id.rdio_dialogGenderLayout_female);
		
		group.setOnCheckedChangeListener(this);
	}
	
	private void initData() {
		if(genderPosition == 1){
			male.setChecked(true);
		}else if(genderPosition == 2){
			female.setChecked(true);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(group.getCheckedRadioButtonId() == R.id.rdio_dialogGenderLayout_male){
			male.setCompoundDrawables(null, null, drawable, null);
			female.setCompoundDrawables(null, null, null, null);
			if(view != null){
				view.setText("男");
				view.setTag(1);
			}
			if(edit != null){
				edit.setText("男");
				edit.setTag(1);
			}
		}else{
			male.setCompoundDrawables(null, null, null, null);
			female.setCompoundDrawables(null, null, drawable, null);
			if(view != null){
				view.setText("女");
				view.setTag(2);
			}
			if(edit != null){
				edit.setText("女");
				edit.setTag(2);
			}
		}
		
		this.dismiss();
	}
}
