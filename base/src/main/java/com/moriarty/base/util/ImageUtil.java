package com.moriarty.base.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.moriarty.base.R;
import com.moriarty.base.log.L;
import com.moriarty.base.ui.notify.UiTip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


/**
 * Created by yangming on 16/12/30.
 */

public class ImageUtil {

    private static MediaScannerConnection msc = null;

    /**
     * 保存相片到相册
     */
    public static void saveImageToAlbum(Activity context, String imgurl) {
        L.i("imgurl=" + imgurl);

        new Thread(new Runnable() {
            @Override
            public void run() {


                String result = "";
                try {
                    String sdcard = Environment.getExternalStorageDirectory().toString();
//                    String sdcard = context.getApplicationContext().getFilesDir().getAbsolutePath();
                    File file = new File(sdcard + "/Download");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    int idx = imgurl.lastIndexOf(".");
                    String ext = imgurl.substring(idx);
                    String path = sdcard + "/Download/" + new Date().getTime() + ext;
                    file = new File(path);
                    InputStream inputStream = null;
                    URL url = new URL(imgurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(20000);
                    if (conn.getResponseCode() == 200) {
                        inputStream = conn.getInputStream();
                    }
                    byte[] buffer = new byte[4096];
                    int len = 0;
                    FileOutputStream outStream = new FileOutputStream(file);
                    while ((len = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    outStream.close();

                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                file.getAbsolutePath(), path, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UiTip.toast("图片已保存至相册");
                        }
                    });


                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UiTip.toast("请您打开存储权限");
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    /**
     * 弹出保存图片的框
     *
     * @param activity
     * @param url
     */
    public static void showSaveImageToAlbum(Activity activity, String url) {
        try {

            View contentView = LayoutInflater.from(activity).inflate(R.layout.pop_phone_sel, null);

            Dialog dialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(activity), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(contentView, params);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);


            TextView galleryTv = (TextView) contentView.findViewById(R.id.save_tv);
            galleryTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveImageToAlbum(activity, url);
                    dialog.dismiss();
                }
            });

            Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog.dismiss();
                }
            });

            dialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
