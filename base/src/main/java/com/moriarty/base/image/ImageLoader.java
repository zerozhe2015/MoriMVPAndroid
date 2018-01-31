package com.moriarty.base.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.moriarty.base.R;
import com.moriarty.base.util.ResUtils;
import com.moriarty.base.util.ScreenUtils;


public class ImageLoader {

    private static ImageLoader instance = new ImageLoader();

    private ImageLoader() {

    }


    public static ImageLoader getInstance() {
        return instance;
    }


    public void loadImage(Activity activity, ImageView iv, String imageUrl) {
//        Glide.with(activity).load(imageUrl)
//                .placeholder(R.drawable.img_wait_big)
//                .into(iv);
        Glide.with(activity).load(imageUrl).asBitmap().placeholder(new ColorDrawable(ResUtils.getColorRes(R.color.windowBackgroundColor))).into(new MyBitmapImageViewTarget(iv));
    }

    public void loadImageFitWidth(Activity activity, ImageView iv, String imageUrl) {

        Glide.with(activity).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = ScreenUtils.getScreenWidth(activity) * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = iv.getLayoutParams();
                para.height = height;
                iv.setLayoutParams(para);
                Glide.with(activity).load(imageUrl).asBitmap().placeholder(R.drawable.img_wait_big).into(new MyBitmapImageViewTarget(iv));
            }
        });
    }

    public void loadImage(Activity activity, ImageView iv, String imageUrl, int placeholderResId) {
//        Glide.with(activity).load(imageUrl).
//                placeholder(placeholderResId)
//                .into(iv);
        Glide.with(activity).load(imageUrl).asBitmap().placeholder(placeholderResId).into(new MyBitmapImageViewTarget(iv));
    }

    public void loadImage(Activity activity, ImageView iv, String imageUrl, int placeholderResId, int errorImageResId) {
//        Glide.with(activity).load(imageUrl).placeholder(placeholderResId).error(errorImageResId).into(iv);

        Glide.with(activity).load(imageUrl).asBitmap().placeholder(placeholderResId).error(errorImageResId).into(new MyBitmapImageViewTarget(iv));
    }


    public void loadImage(Fragment fragment, ImageView iv, String imageUrl) {

//        Glide.with(fragment).load(imageUrl)
//                .placeholder(R.drawable.img_wait_big)
//                .into(iv);

//        Glide.with(fragment).load(imageUrl)
//                .placeholder(fragment.getResources().getDrawable(R.drawable.img_wait_big))
//                .into(iv);

        Glide.with(fragment).load(imageUrl).asBitmap().placeholder(new ColorDrawable(ResUtils.getColorRes(R.color.windowBackgroundColor))).into(new MyBitmapImageViewTarget(iv));
    }


    public void loadImage(Fragment fragment, ImageView iv, String imageUrl, int placeholderResId) {
//        Glide.with(fragment).load(imageUrl).placeholder(placeholderResId).into(iv);
        Glide.with(fragment).load(imageUrl).asBitmap().placeholder(placeholderResId).into(new MyBitmapImageViewTarget(iv));
    }

    public void loadImage(Fragment fragment, ImageView iv, String imageUrl, int placeholderResId, int errorImageResId) {
//        Glide.with(fragment).load(imageUrl).placeholder(placeholderResId).error(errorImageResId).into(iv);
        Glide.with(fragment).load(imageUrl).asBitmap().placeholder(placeholderResId).error(errorImageResId).into(new MyBitmapImageViewTarget(iv));
    }

//    public void loadImagePlaceholderSmall(Fragment fragment, ImageView iv, String imageUrl) {
//        Glide.with(fragment).load(imageUrl).asBitmap().placeholder(R.drawable.img_waiting_small).into(new MyBitmapImageViewTarget(iv));
//    }
//
//    public void loadImagePlaceholderSmall(Activity activity, ImageView iv, String imageUrl) {
//        Glide.with(activity).load(imageUrl).asBitmap().placeholder(R.drawable.img_waiting_small).into(new MyBitmapImageViewTarget(iv));
//    }

}
