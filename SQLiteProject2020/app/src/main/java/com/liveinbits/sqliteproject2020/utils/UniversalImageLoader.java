package com.liveinbits.sqliteproject2020.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.liveinbits.sqliteproject2020.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ConcurrentModificationException;

public class UniversalImageLoader {

    private static  final  int defultImage= R.drawable.ic_launcher_background;

    private Context context;

    public UniversalImageLoader(Context context){
        this.context=context;
    }

    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defultImage)
                .showImageOnFail(defultImage)
                .showImageOnLoading(defultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        return new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100*1024*1024)
                .build();
    }

    public static void loadImage(String url, ImageView imageView,String append){
        ImageLoader loader=ImageLoader.getInstance();
        loader.displayImage(append + url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
}
