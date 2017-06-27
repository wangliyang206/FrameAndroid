package com.cj.mobile.common.module;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cj.mobile.common.ui.clipImage.ClipImageLayout;

import java.io.ByteArrayOutputStream;

/**
 * 裁剪图片入口</br>
 * 调用方式：</br>
 * Intent intent = new Intent(this,ClipPicActivity.class);</br>
 * Bundle bundle = new Bundle();</br>
 * bundle.putString("ImgPath", path);</br>
 * intent.putExtras(bundle);</br>
 * //跳转致剪裁界面</br>
 * this.startActivityForResult(intent,1111);</br>
 * </br></br>
 * 返回：</br>
 * key=bitmap，类型：Base64 字符串</br>
 * @author wly
 *
 */
public class ClipPicActivity extends Activity
{
	private ClipImageLayout mClipImageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.cj.mobile.common.R.layout.clip_pic_activity);

		mClipImageLayout = (ClipImageLayout) findViewById(com.cj.mobile.common.R.id.id_clipImageLayout);
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null){
			String ImgPath = bundle.getString("ImgPath");
			mClipImageLayout.setImg(ImgPath);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(com.cj.mobile.common.R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == com.cj.mobile.common.R.id.id_action_clip){
			Bitmap bitmap = mClipImageLayout.clip();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] datas = baos.toByteArray();
			
			Intent intent = new Intent();
			intent.putExtra("bitmap", datas);
			
			this.setResult(RESULT_OK,intent);
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** 返回 */
	@Override
	public void onBackPressed() {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}
}
