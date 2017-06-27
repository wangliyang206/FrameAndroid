package com.cj.mobile.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 类名：ResManager.java
 * 类描述：获取工程中assets目下的文字、图片等资源
 *
 * @author 王力杨  创建时间：2014-02-20 10:10
 */
public class ResManager {

    /**
     * 从工程资源加载图片资源（路径是assets/images/**.png）
     *
     * @param fileName 图片资源路径
     */
    public static Bitmap loadImageRes(Context activity, String fileName) {
        Bitmap bitmap = null;
        InputStream is = null;
        FileInputStream fis = null;
        try {
            is = activity.getAssets().open(fileName);
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
            } finally {
                is = null;
                fis = null;
            }
        }
        return bitmap;
    }

    /**
     * 从工程资源目录中加载图片名称(所有图片名称)
     *
     * @param activity 句柄
     * @param path     目录
     * @return
     */
    public static String[] loadImageNameRes(Activity activity, String path) {
        try {
            return activity.getAssets().list(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 从工程资源加载文字资源（路径是：assets/textRes/**.properties）
     *
     * @param fileName
     */
    public static ArrayList<String> loadTextRes(Context context, String fileName) {
        return loadProperties(context, fileName);
    }

    /**
     * 读取配置文件读取配置信息
     *
     * @param filename 配置文件路径
     * @return 包含配置信息的hashmap键值对
     */
    private static ArrayList<String> loadProperties(Context context, String filename) {
        Log.d("loadProperties", "loadProperties");
        ArrayList<String> properties = new ArrayList<String>();
        InputStream is = null;
        FileInputStream fis = null;
        InputStreamReader rin = null;

        // 将配置文件放到res/raw/目录下，可以通过以下的方法获取
        // is = context.getResources().openRawResource(R.raw.system);

        // 这是读取配置文件的第二种方法
        // 将配置文件放到assets目录下，可以通过以下的方法获取
        // is = context.getAssets().open("system.properties");

        // 用来提取键值对的临时字符串
        StringBuffer tempStr = new StringBuffer();

        // 用来存放读取的每个字符
        int ch = 0;

        // 用来保存读取的配置文件一行的信息
        String line = null;
        try {
            Log.d("loadProperties", "the filename is: " + filename);
            is = context.getAssets().open(filename);
            rin = new InputStreamReader(is, "UTF-8");

            while (ch != -1) {
                tempStr.delete(0, tempStr.length());
                while ((ch = rin.read()) != -1) {
                    if (ch != '\n') {
                        tempStr.append((char) ch);
                    } else {
                        break;
                    }
                }
                line = tempStr.toString().trim();
                Log.d("loadProperties", "line: " + line);
                // 判断读出的那行数据是否有效,#开头的代表注释,如果是注释行那么跳过下面,继续上面操作
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                properties.add(line);
            }
        } catch (IOException e) {
            // LogX.trace("read property file", e.getMessage() + "fail");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (null != rin) {
                    rin.close();
                }
            } catch (IOException e) {
                // LogX.trace("read property file", e.getMessage() + "fail");
            } finally {
                is = null;
                fis = null;
                rin = null;
            }
        }
        return properties;
    }

    /**
     * 从工程资源加载文字资源（路径是：assets/textRes/**.properties）
     *
     * @param fileName
     */
    public static String loadTextResStr(Context context, String fileName) {
        return loadPropertiesStr(fileName, context);
    }

    /**
     * 读取配置文件读取配置信息
     *
     * @param filename 配置文件路径
     * @return 包含配置信息的hashmap键值对
     */
    private static String loadPropertiesStr(String filename, Context context) {
        // 用来拼接字符串，并返回给上一层
        StringBuffer resultStr = new StringBuffer();
        Log.d("loadProperties", "loadProperties");
        InputStream is = null;
        FileInputStream fis = null;
        InputStreamReader rin = null;

        // 将配置文件放到res/raw/目录下，可以通过以下的方法获取
        // is = context.getResources().openRawResource(R.raw.system);

        // 这是读取配置文件的第二种方法
        // 将配置文件放到assets目录下，可以通过以下的方法获取
        // is = context.getAssets().open("system.properties");

        // 用来提取键值对的临时字符串
        StringBuffer tempStr = new StringBuffer();

        // 用来存放读取的每个字符
        int ch = 0;

        // 用来保存读取的配置文件一行的信息
        String line = null;
        try {
            Log.d("loadProperties", "the filename is: " + filename);
            is = context.getAssets().open(filename);
            rin = new InputStreamReader(is, "UTF-8");

            while (ch != -1) {
                tempStr.delete(0, tempStr.length());
                while ((ch = rin.read()) != -1) {
                    if (ch != '\n') {
                        tempStr.append((char) ch);
                    } else {
                        break;
                    }
                }
                line = tempStr.toString().trim();
                Log.d("loadProperties", "line: " + line);
                // 判断读出的那行数据是否有效,#开头的代表注释,如果是注释行那么跳过下面,继续上面操作
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                resultStr.append(line);
            }
        } catch (IOException e) {
            // LogX.trace("read property file", e.getMessage() + "fail");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (null != rin) {
                    rin.close();
                }
            } catch (IOException e) {
                // LogX.trace("read property file", e.getMessage() + "fail");
            } finally {
                is = null;
                fis = null;
                rin = null;
            }
        }
        return resultStr.toString();
    }

    /**
     * 读取文本数据
     *
     * @param context  句柄
     * @param fileName 完整名称(包含目录)
     * @return 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;
        try {
            is = context.getAssets().open(fileName);
            if (is != null) {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }
}
