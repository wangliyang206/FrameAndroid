package com.cj.mobile.common.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

import android.database.Cursor;
import android.graphics.Bitmap;

/**
 * 内存回收工具类
 * 
 * @author 王力杨
 * 
 */
public class MemoryRecovery {
	/** 回收BitMap对象 */
	public static void recoveryBitMap(Bitmap bit) {
		// 先判断是否已经回收
		if (bit != null && !bit.isRecycled()) {
			// 回收并且置为null
			bit.recycle();
			bit = null;
		}
	}
	
	/**回收 ByteArrayOutputStream 对象*/
	public static void recoveryStream(ByteArrayOutputStream baos){
		if (baos != null) {
			try {
				baos.flush();
				baos.close();
			} catch (IOException e) {}
		}
	}
	
	/** 回收 Cursor 对象 */
	public static void recoveryCursor(Cursor cursor){
		if (cursor != null)  
            cursor.close();
	}

	/**
	 * 关闭流
	 *
	 * @param closeables Closeable
	 */
	@SuppressWarnings("WeakerAccess")
	public static void close(Closeable... closeables) {
		if (closeables == null || closeables.length == 0)
			return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
