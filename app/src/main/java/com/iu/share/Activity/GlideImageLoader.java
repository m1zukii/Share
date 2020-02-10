package com.iu.share.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.bumptech.glide.Glide;

class GlideImageLoader implements AssNineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String s) {
        Glide.with(context).load(s).into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String s) {
        return null;
    }
}
