package com.example.kevin.fifastatistics.adapters;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.interfaces.ImageCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageBindingAdapter {

    @BindingAdapter(value = {"imageUrl", "imageCallback"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, ImageCallback imageCallback) {
        if (view != null) {
            view.setImageBitmap(null);
            ImageLoader.getInstance().displayImage(imageUrl, view, imageCallback);
        }
    }
}
