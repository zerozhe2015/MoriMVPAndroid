package com.moriarty.base.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import com.moriarty.base.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class IOUtil {


    public static String readAssetFile(Context context, String fileName) {

        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader in = null;
            in = new InputStreamReader(context.getAssets().open(fileName));
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                sb.append((char) tempbyte);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    /**
     * 将一个输入流转为字符串
     *
     * @param in
     * @return
     */
    public static String getStringFromInputStream(InputStream in) {

        try {
            return new String(getBytesFromInputStream(in), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将一个输入流转换为byte数组
     *
     * @param in
     * @return
     */
    public static byte[] getBytesFromInputStream(InputStream in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    /**
     * 拷贝asset下的文件至输出文件夹
     *
     * @param assetDir
     * @param dir
     * @param context
     */

    public static void CopyAssets(String assetDir, String dir, Context context) {
        String[] files;
        try {
            files = context.getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        // 判断文件是否存在
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
            }
        }

        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                // 根据名字是否含.判断为文件夹还是文件
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(fileName, dir + fileName + "/", context);
                    } else {
                        CopyAssets(assetDir + "/" + fileName, dir + fileName + "/", context);
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + fileName);
                else
                    in = context.getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将asset目录下的图片拷贝到本地sdcard根目录
     *
     * @param imageName
     */
    public static void copyImage(String imageName) {
        File file = new File(Environment.getExternalStorageDirectory(), "ic_logo.png");
        Context context = BaseApplication.getApplication();
        if (file.exists() && file.length() > 0) {
            System.out.println("图片已经存在，不需要再拷贝");
        } else {
            try {
                InputStream is = context.getAssets().open(imageName);
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                is.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件 或文件夹大小
     *
     * @param f
     * @return
     */

    public static long getFileSize(File f) {
        long size = 0;

        if (f == null || !f.exists()) {
            return 0;
        }

        if (f.isFile()) {
            size += f.length();
        } else if (f.isDirectory()) {
            for (File child : f.listFiles()) {

                if (child.isFile()) {
                    size += child.length();
                } else {
                    size += getFileSize(child);
                }

            }
        }

        return size;
    }

    /**
     * 删除文件
     *
     * @param f
     */

    public static void deleteFile(File f) {
        if (f == null || !f.exists()) {
            return;
        }
        if (f.isFile()) {
            f.delete();
        } else if (f.isDirectory()) {
            for (File child : f.listFiles()) {
                deleteFile(child);
            }
            f.delete();
        }
    }

    public static String SerializableToString(Serializable s) {

        if (s == null)
            return null;

        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(s);
            String str = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
            bos.close();
            oos.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends Serializable> T StringToSerializable(String str) {

        if (TextUtils.isEmpty(str))
            return null;
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(Base64.decode(str, Base64.DEFAULT));
            ObjectInputStream ois = new ObjectInputStream(input);
             Object o = ois.readObject();
            input.close();
            ois.close();
            return (T) o;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getNetFileSize(String urlStr) {

        long size = 0;
        try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            //			String headerField = conn.getHeaderField("Content-Length");
            return conn.getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }


}
