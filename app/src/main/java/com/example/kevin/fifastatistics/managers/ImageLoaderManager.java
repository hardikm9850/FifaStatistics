package com.example.kevin.fifastatistics.managers;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Manager for initialization of imageLoader options.
 * <p>
 * {@link #initializeDefaultImageLoader(Context)} should be called within the onCreate() method of
 * {@link com.example.kevin.fifastatistics.FifaApplication}.
 * <p>
 * See the
 * <a href="https://github.com/nostra13/Android-Universal-Image-Loader">Universal Image Loader Github Page</a>
 * for more information on the universalimageloader.
 */
public class ImageLoaderManager {

    private static final int DISK_CACHE_SIZE = 104857600; // 100 * 1024 * 1024

    /**
     * Initialization for the default ImageLoader options that are most commonly used throughout the
     * application.
     * @param context   the Context
     */
    public static void initializeDefaultImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();

        initConfigWithOptions(context, defaultOptions);
    }

    private static void initConfigWithOptions(Context context, DisplayImageOptions options) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(DISK_CACHE_SIZE).build();

        ImageLoader.getInstance().init(config);
    }
}
