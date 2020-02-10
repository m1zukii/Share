package com.iu.share.Activity;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.picker.Phoenix;

import org.litepal.LitePal;

public class App extends Application {
    public static String ipaddress = "10.252.1.160";
    public static String username = "";
    @Override
    public void onCreate() {
        super.onCreate();

        Phoenix.config().imageLoader(new ImageLoader() {
                    @Override
                    public void loadImage(Context context, ImageView imageView, String imagePath, int type) {
                        Glide.with(context)
                                .load(imagePath)
                                .into(imageView);
                    }
                });
        LitePal.initialize(this);

        AssNineGridView.setImageLoader(new GlideImageLoader());
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        App.username = username;
    }
}
