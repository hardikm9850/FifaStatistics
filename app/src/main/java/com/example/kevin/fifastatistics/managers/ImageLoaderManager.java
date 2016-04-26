package com.example.kevin.fifastatistics.managers;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageLoaderManager
{
    private static final int DISK_CACHE_SIZE = 100 * 1024 * 1024;

    public static void initializeDefaultImageLoader(Context context)
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        initConfigWithOptions(context, defaultOptions);
    }

    public static void initializeNotificationsImageLoader(Context context)
    {
        DisplayImageOptions notificationOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        initConfigWithOptions(context, notificationOptions);
    }

    private static void initConfigWithOptions(
            Context context, DisplayImageOptions options)
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(DISK_CACHE_SIZE).build();

        ImageLoader.getInstance().init(config);
    }
}
