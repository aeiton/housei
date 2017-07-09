package com.housey.aeiton.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageSwitcher;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by JAR on 21-06-2017.
 */

public class PicassoImageSwitcher implements Target {

    private Context context;
    private ImageSwitcher imageSwitcher;

    public PicassoImageSwitcher(){

    }

    public PicassoImageSwitcher(Context con, ImageSwitcher switcher){
        context = con;
        imageSwitcher = switcher;
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        imageSwitcher.setVisibility(View.VISIBLE);
        imageSwitcher.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        imageSwitcher.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

}
