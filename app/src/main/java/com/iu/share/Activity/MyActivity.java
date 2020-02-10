package com.iu.share.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iu.share.Bean.User;
import com.iu.share.R;
import com.iu.share.Util.StatusBarUtil;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.RTextView;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @BindView(R.id.img_homepage)
    ImageView imgHomepage;
    @BindView(R.id.txt_homepage)
    TextView txtHomepage;
    @BindView(R.id.img_friend)
    ImageView imgFriend;
    @BindView(R.id.txt_friend)
    TextView txtFriend;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.img_my)
    ImageView imgMy;
    @BindView(R.id.txt_my)
    TextView txtMy;
    @BindView(R.id.imageicon)
    RImageView imageicon;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.username)
    RTextView username1;
    @BindView(R.id.changeInfo)
    Button changeInfo;
    @BindView(R.id.logoutImg)
    RImageView logoutImg;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        initialColor();
        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
        initialImageIcon();
        loadBackground();
        username1.setText(username);
        StatusBarUtil.immersive(this);

    }

    private void loadBackground() {
        String imageUrl = "http://" + App.ipaddress + ":88/" + username + "/background.jpg";
        Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.pagebackground)
                .placeholder(R.drawable.pagebackground)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        layout.setBackground(resource);
                    }
                });
    }

    public void initialImageIcon() {
        Log.d(TAG, "initialImageIcon: " + App.username);
        String imageUrl = "http://" + App.ipaddress + ":88/" + username + "/usericon.jpg";
        Log.d(TAG, "initialImageIcon: " + imageUrl);
        Glide.with(MyActivity.this).load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.defaulticon)
                .error(R.drawable.defaulticon)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageicon);
    }

    public void initialColor() {
        int primary = Color.parseColor("#0980C3");
        int selected = Color.parseColor("#0000FF");
        imgHomepage.setColorFilter(primary);
        txtHomepage.setTextColor(primary);
        imgMessage.setColorFilter(primary);
        txtMessage.setTextColor(primary);
        imgFriend.setColorFilter(primary);
        txtFriend.setTextColor(primary);
        imgMy.setColorFilter(selected);
        txtMy.setTextColor(selected);
    }

    @OnClick(R.id.img_homepage)
    public void onImgHomepageClicked() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.img_friend)
    public void onImgFriendClicked() {
        Intent intent = new Intent(this, FriendActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.img_message)
    public void onImgMessageClicked() {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imageicon)
    public void onImageiconClicked() {
        Intent intent = new Intent(this, SampleActivity.class);
        intent.putExtra("type", "icon");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.layout)
    public void onLayoutClicked() {
        Intent intent = new Intent(this, SampleActivity.class);
        intent.putExtra("type", "background");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.username)
    public void onUsernameClicked() {
        Intent intent = new Intent(this, PersonDetailActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    @OnClick(R.id.changeInfo)
    public void onChangeInfoClicked() {
        Intent intent = new Intent(this, ChangeInfoActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.logoutImg)
    public void onLogoutImgClicked() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        editor.putBoolean("isLogin", false);
        editor.apply();
//        editor.clear();
//        editor.apply();
        LitePal.deleteAll(User.class);
    }


}
