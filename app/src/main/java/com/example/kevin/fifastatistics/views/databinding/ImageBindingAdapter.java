package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.interfaces.ImageCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageBindingAdapter {

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
