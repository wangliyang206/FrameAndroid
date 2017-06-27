package com.cj.mobile.common.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cj.mobile.common.R;
import com.cj.mobile.common.ui.picker.ScrollerNumberPicker;
import com.cj.mobile.common.util.ToastManager;
import com.cj.mobile.common.util.Validate;


/**
 * 通用dialog(列表性质)
 * @author 王力杨
 *
 */
public class CommDialog  extends AlertDialog implements OnClickListener{
	public interface CommDialogChangeView{
		void oCommDialogChangeView();
	}
	/** 最终显示在此控件上 */
	private TextView view;
	/** 确定按钮 */
	private Button ok;
	/** 取消按钮 */
	private Button cancel;
	/** 展示的内容 */
	private ScrollerNumberPicker content;
	/**需要显示的内容*/
	private List<String> title;
	/**对应内容的id*/
	private List<String> code;
	private Activity context;
	private CommDialogChangeView callBack = null;
	/**
	 * 构造方法
	 * @param context		句柄
	 * @param view			最终显示在此控件上
	 */
	public CommDialog(Activity context,TextView view,List<String> title,List<String> code,CommDialogChangeView callBack){
		super(context,R.style.dialog);
		this.title = title;
		this.code = code;
		this.view = view;
		this.context = context;
		this.callBack = callBack;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_dialog_layout);
		
		initView();
	}

	/**
	 * 初始化控件
	 */
	@SuppressWarnings("deprecation")
	private void initView() {
		content = (ScrollerNumberPicker) findViewById(R.id.pic_commDialogLayout_content);
		ok = (Button) findViewById(R.id.btn_commDialogLayout_ok);
		cancel = (Button) findViewById(R.id.btn_commDialogLayout_cancel);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		lp.dimAmount = 0f;
		//此处可以设置dialog显示的位置
		window.setGravity(Gravity.BOTTOM); 
		window.setAttributes(lp);
		if(title != null && title.size() > 0){
			content.setData((ArrayList<String>) title);
			content.setDefault(0);
		}else{
			ToastManager.instance().show(context, "暂无信息");
			content.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_commDialogLayout_ok){
			if(view != null){
				int index = content.getSelected();
				if(title == null || title.size() == 0)
					return;
				String val = Validate.isEmptyReturnStr(title.get(index));
				view.setText(val);
				if(code != null && code.size() > 0)
					view.setTag(code.get(index));
			}
			if(callBack != null){
				callBack.oCommDialogChangeView();
			}
		}
		
		this.dismiss();
	}
}