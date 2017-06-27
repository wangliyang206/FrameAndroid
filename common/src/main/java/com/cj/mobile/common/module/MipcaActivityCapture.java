package com.cj.mobile.common.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cj.mobile.common.R;
import com.cj.mobile.common.ui.zxing.camera.CameraManager;
import com.cj.mobile.common.ui.zxing.camera.PlanarYUVLuminanceSource;
import com.cj.mobile.common.ui.zxing.decoding.CaptureActivityHandler;
import com.cj.mobile.common.ui.zxing.decoding.InactivityTimer;
import com.cj.mobile.common.ui.zxing.decoding.RGBLuminanceSource;
import com.cj.mobile.common.ui.zxing.decoding.Utils;
import com.cj.mobile.common.ui.zxing.view.ViewfinderView;
import com.cj.mobile.common.util.ActivityUtils;
import com.cj.mobile.common.util.AlertDialogCustom;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 扫描二维码、条码 Initial the camera
 *
 * @author Ryan.Tang
 */
@SuppressLint("HandlerLeak")
public class MipcaActivityCapture extends Activity implements Callback,
        OnClickListener {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ImageView mo_scanner_photo;
    private static final int REQUEST_CODE = 234;
    private String photo_path;
    private Bitmap scanBitmap;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        // ViewUtil.addTopView(getApplicationContext(), this,
        // R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mo_scanner_photo = (ImageView) findViewById(R.id.mo_scanner_photo);
        mo_scanner_photo.setVisibility(View.GONE);
        mo_scanner_photo.setOnClickListener(this);
        TextView txt = (TextView) findViewById(R.id.txtvi_news_header);
        txt.setText(ActivityUtils.getString(this, R.string.qr_code));
        ImageView mButtonBack = (ImageView) findViewById(R.id.imgvi_news_back);

        mButtonBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MipcaActivityCapture.this.finish();

            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {// 扫描失败
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!",
                    Toast.LENGTH_SHORT).show();
            MipcaActivityCapture.this.finish();
        } else {// 扫描成功
            getResult(resultString, barcode);
        }

    }

    /**
     * 扫描成功后的处理
     */
    private void getResult(String resultString, Bitmap barcode) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", resultString);
        resultIntent.putExtras(bundle);
        MipcaActivityCapture.this.setResult(RESULT_OK, resultIntent);
        MipcaActivityCapture.this.finish();
        /*
        final String key = ":";// 字符串分割符
		// 第一步，验证当前字符串长度是否符合业务需求
		if (resultString.length() > 6) {
			String ss = resultString.substring(0, 7).toLowerCase();
			// 第二步，验证是否符合解析的key，如果符合则返回对应的values。如果不符合则可继续扫描
			if ("goodsid".equals(ss)) {
//				String a[] = resultString.split(key);
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", resultString);
				// bundle.putParcelable("bitmap",
				// barcode);//个别手机会异常退出，如果以后要用图片，则希望保存至本地，然后这里只对外提供本地地址
				resultIntent.putExtras(bundle);
				MipcaActivityCapture.this.setResult(RESULT_OK, resultIntent);
				MipcaActivityCapture.this.finish();
			} else if ("referno".equals(ss)) {
//				String a[] = resultString.split(key);
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", resultString);
				// bundle.putParcelable("bitmap",
				// barcode);//个别手机会异常退出，如果以后要用图片，则希望保存至本地，然后这里只对外提供本地地址
				resultIntent.putExtras(bundle);
				MipcaActivityCapture.this.setResult(RESULT_OK, resultIntent);
				MipcaActivityCapture.this.finish();

			} else {
				error(resultString);
			}
		} else {
			error(resultString);
		}
		*/
    }

    Handler mess = new Handler() {
        public void handleMessage(Message msg) {
            String content = msg.getData().getString("content");
            AlertDialogCustom.CSSShowAlert css = new AlertDialogCustom.CSSShowAlert() {

                @Override
                public void onOver(boolean isSucceed) {
                    // 长度不符合，可继续扫描(Handler：可连续扫描)
                    if (handler != null)
                        handler.sendEmptyMessage(R.id.restart_preview);
                }
            };
            String tips = ActivityUtils.getString(MipcaActivityCapture.this,
                    R.string.showAlerttitle_Information);
            AlertDialogCustom.showAlert(MipcaActivityCapture.this,
                    R.drawable.information, tips, content, ActivityUtils
                            .getString(MipcaActivityCapture.this,
                                    R.string.common_ok), css);
        }

        ;
    };

    private void error(String content) {
        Message msg = new Message();
        msg.what = 1;
        Bundle data = new Bundle();
        data.putString("content", content);
        msg.setData(data);
        mess.sendMessage(msg);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        photo();
    }

    private void photo() {

        Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        // innerIntent.setAction(Intent.ACTION_GET_CONTENT);

        innerIntent.setType("image/*");

        Intent wrapperIntent = Intent.createChooser(innerIntent, ActivityUtils
                .getString(MipcaActivityCapture.this, R.string.select_scan));

        this.startActivityForResult(wrapperIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE:

                    String[] proj = {MediaStore.Images.Media.DATA};
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(),
                            proj, null, null, null);

                    if (cursor.moveToFirst()) {

                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(column_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(),
                                    data.getData());
                            Log.i("123path  Utils", photo_path);
                        }
                        Log.i("123path", photo_path);

                    }

                    cursor.close();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Result result = scanningImage(photo_path);
                            // String result = decode(photo_path);
                            if (result == null) {
                                Looper.prepare();
                                String errorStr = ActivityUtils.getString(MipcaActivityCapture.this, R.string.picture_error);
                                Toast.makeText(
                                        getApplicationContext(),
                                        errorStr,
                                        Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                // 数据返回
                                String recode = recode(result.toString());
                                getResult(recode, null);
                                // Intent resultIntent = new Intent();
                                // Bundle bundle = new Bundle();
                                // bundle.putString("result", recode);
                                // resultIntent.putExtras(bundle);
                                // setResult(RESULT_OK, resultIntent);
                                // MipcaActivityCapture.this.finish();
                            }
                        }
                    }).start();
                    break;

            }

        }

    }

    // TODO: 解析部分图片
    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {

            return null;

        }
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        // --------------测试的解析方法---PlanarYUVLuminanceSource-这几行代码对project没作功----------

        LuminanceSource source1 = new PlanarYUVLuminanceSource(
                rgb2YUV(scanBitmap), scanBitmap.getWidth(),
                scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(),
                scanBitmap.getHeight());
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                source1));
        MultiFormatReader reader1 = new MultiFormatReader();
        Result result1;
        try {
            result1 = reader1.decode(binaryBitmap);
            String content = result1.getText();
            Log.i("123content", content);
        } catch (NotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // ----------------------------

        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {

            return reader.decode(bitmap1, hints);

        } catch (NotFoundException e) {

            e.printStackTrace();

        } catch (ChecksumException e) {

            e.printStackTrace();

        } catch (FormatException e) {

            e.printStackTrace();

        }

        return null;

    }

    /**
     * 中文乱码
     * <p/>
     * 暂时解决大部分的中文乱码 但是还有部分的乱码无法解决 .
     * <p/>
     * 如果您有好的解决方式 请联系 2221673069@qq.com
     * <p/>
     * 我会很乐意向您请教 谢谢您
     *
     * @return
     */
    private String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }

    /**
     * //TODO: TAOTAO 将bitmap由RGB转换为YUV //TOOD: 研究中
     *
     * @param bitmap 转换的图形
     * @return YUV数据
     */
    public byte[] rgb2YUV(Bitmap bitmap) {
        // 该方法来自QQ空间
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
                // yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                // yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }
}