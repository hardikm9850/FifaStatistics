package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.ImageCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageBindingAdapter {

    private static final int DEFAULT_HEADSHOT_RES_ID = R.drawable.circle_diagonal;

    @BindingAdapter(value = "headshotImageUrl")
    public static void loadHeadshotImage(ImageView view, String imageUrl) {
        loadImage(view, imageUrl, null, DEFAULT_HEADSHOT_RES_ID);
    }

    @BindingAdapter(value = {"imageUrl", "imageCallback", "defaultResId"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, ImageCallback imageCallback, @DrawableRes int defaultResId) {
        if (view != null) {
            view.setImageBitmap(null);
            if (imageUrl != null) {
                ImageLoader.getInstance().displayImage(imageUrl, view, imageCallback);
            } else {
                view.setImageResource(defaultResId);
            }
        }
    }
}
