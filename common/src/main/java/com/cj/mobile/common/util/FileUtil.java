package com.cj.mobile.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 文件工具类
 */
public class FileUtil {
    public FileUtil() {
    }

    /**
     * 文件是否存在
     *
     * @param path 路径
     * @return true 表示 目录存在，false表示目录不存在
     */
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Title：创建一个目录
     * </p>
     * <p>
     * Description：这个函数将会在指定目录路径创建一个目录，支持递归创建
     * </p>
     *
     * @param sDir 要创建的目录的绝对路径
     * @return 返回创建是否成功:0创建成功，-1创建失败，-2该文件夹已经存在
     */
    public static int mkDir(String sDir) {
        File destDir = new File(sDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
            if (destDir.exists())
                return 0; // 创建成功
            else
                return -1; // 创建失败
        } else
            return -2;// 该文件夹已经存在;
    }

    /**
     * 读取文件
     *
     * @param context  句柄
     * @param fileName 文件名。
     * @return 文件中的内容
     */
    public static String read(Context context, String fileName) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(fileName);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) > 0) {
                byteArray.write(buffer, 0, len);
            }

            String ret = byteArray.toString();
            fileInputStream.close();

            return ret;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 保存文件
     *
     * @param context     句柄
     * @param fileName    文件名称
     * @param fileContent 文件内容
     */
    public static void save(Context context, String fileName, String fileContent) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            fileOutputStream.write(fileContent.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取Sdcard文件
     *
     * @param fileName 文件名称(全路径)
     * @return 文件内容(字符串类型)
     */
    public String readFileSdcard(String fileName) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 读取Sdcard文件
     *
     * @param fileName 文件名称(全路径)
     * @return 文件内容(数据类型)
     */
    public static byte[] read(String fileName) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(fileName);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) > 0) {
                byteArray.write(buffer, 0, len);
            }

            fileInputStream.close();

            return byteArray.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 1、验证文件路径是否有效；<br/>
     * 2、返回文件对象；<br/>
     * @param fileName  完整路径(路径+文件名)
     * @return 文件对象
     */
    public static File createFileByPath(String fileName) {
        File file = new File(fileName);
        String dirStr = file.getParent();
        if (dirStr == null)
            return null;

        file = new File(dirStr);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    /**
     * 新建目录
     *
     * @param fileName 完整路径
     */
    private static void createFileDirByFileFullPath(String fileName) {
        File file = new File(fileName);
        String dirStr = file.getParent();
        if (dirStr == null)
            return;

        file = new File(dirStr);
        if (!file.exists())
            file.mkdirs();
    }

    /**
     * 保存文件
     *
     * @param is       内容流
     * @param fileName 完整路径
     */
    public static void save(InputStream is, String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            createFileDirByFileFullPath(fileName);

            File file = new File(fileName);
            fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }

            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (fileOutputStream != null)
                fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存文件
     *
     * @param fileName    文件名称(完整路径)
     * @param fileContent 文件内容(数组类型)
     */
    public static void save(String fileName, byte[] fileContent) {
        FileOutputStream fileOutputStream;
        try {
            createFileDirByFileFullPath(fileName);

            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(fileContent);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(
                        FileUtil.class.getClass().getName(), e);
            }
        }
    }

    /**
     * 调用手机中的截图(1、剪裁的图片尺寸为800*800；2、保存的图片类型为png；3、相册选择的图片不用担心地址有问题(4.4.4之前版本和之后版本地址不一致问题))
     *
     * @param activity    句柄
     * @param path        图源；例：/storage/emulated/0/chinamall/info/BusinessLicense.png
     * @param targetPath  完成截图后保存的完整路径；例：/sdcard/yd/img/test.png
     * @param requestCode 回调标识
     */
    public static void androidCROP(Activity activity, String path, String targetPath, int requestCode) {
        Uri savePath = Uri.fromFile(new File(targetPath));
        Uri uri = Uri.fromFile(new File(path));
        Intent intent = new Intent(
                "com.android.camera.action.CROP");
        // 图源
        if (android.os.Build.VERSION.SDK_INT >= 0x00000013) {
            String url = ImageAbsolute.getImageAbsolutePath(activity, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        // 可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        // 裁剪后保存图片地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, savePath);
        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);
        // 输出文件类型
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.PNG.toString());
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", false);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @Method: deleteFile
     * @Description: 按文件路径删除文件
     * @param: String 文件路径
     * @return: boolean
     * @throws:
     * @author: 周松
     * @date: 2013-6-27上午9:15:57
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * @Method: deleteDirectory
     * @Description: 按文件路径删除文件夹
     * @param: String 文件路径
     * @return: boolean 是否成功
     * @throws:
     * @author: 周松
     * @date: 2013-6-27上午9:12:28
     */
    public static boolean deleteDirectory(String sPath) {
        boolean flag = false;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath); // 文件夹
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) // 删除子文件
            {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else // 删除子目录
            {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    public static File createTmpFile(Context context) throws IOException{
        File dir = null;
        if(TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!dir.exists()) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
                if (!dir.exists()) {
                    dir = getCacheDirectory(context, true);
                }
            }
        }else{
            dir = getCacheDirectory(context, true);
        }
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
    }


    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return Cache {@link File directory}.<br />
     * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
     * {@link android.content.Context#getCacheDir() Context.getCacheDir()} returns null).
     */
    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> (if card is mounted and app has appropriate permission) or
     * on device's file system depending incoming parameters.
     *
     * @param context        Application context
     * @param preferExternal Whether prefer external location for cache
     * @return Cache {@link File directory}.<br />
     * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
     * {@link android.content.Context#getCacheDir() Context.getCacheDir()} returns null).
     */
    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
     * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted and app has
     * appropriate permission. Else - Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @param cacheDir Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
     * @return Cache {@link File directory}
     */
    public static File getIndividualCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(appCacheDir, cacheDir);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = appCacheDir;
            }
        }
        return individualCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public static  String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public static boolean initDirs(String mSDCardPath,String APP_FOLDER_NAME) {
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void deleteCacheFile(String f) {
        if (f != null && f.length() > 0) {
            File files = new File(f);
            if (files.exists() && files.isDirectory()) {
                for (File file : files.listFiles()) {
                    if (!file.isDirectory() && (file.getName().contains(".ts") || file.getName().contains("temp"))) {
                        file.delete();
                    }

                }
            }
        }
    }
    public static void deleteCacheFile2TS(String f) {
        if (f != null && f.length() > 0) {
            File files = new File(f);
            if (files.exists() && files.isDirectory()) {
                for (File file : files.listFiles()) {
                    if (!file.isDirectory() && (file.getName().contains(".ts"))) {
                        file.delete();
                    }

                }
            }
        }
    }

    /**
     * 检测文件是否可用
     */
    public static boolean checkFile(File f) {
        if (f != null && f.exists() && f.canRead() && (f.isDirectory() || (f.isFile() && f.length() > 0))) {
            return true;
        }
        return false;
    }

    /**
     * 检测文件是否可用
     */
    public static boolean checkFile(String path) {
        if (!Validate.isEmpty(path)) {
            File f = new File(path);
            if (f != null && f.exists() && f.canRead() && (f.isDirectory() || (f.isFile() && f.length() > 0)))
                return true;
        }
        return false;
    }
}
