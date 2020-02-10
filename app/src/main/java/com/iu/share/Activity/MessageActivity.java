package com.iu.share.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.iu.share.Adapter.UserAdapter;
import com.iu.share.Bean.User;
import com.iu.share.R;
import com.iu.share.Util.StatusBarUtil;
import com.ruffian.library.widget.RImageView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

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
    @BindView(R.id.message_addContact)
    RImageView messageAddContact;
    @BindView(R.id.message_title)
    TextView messageTitle;

    @BindView(R.id.message_recyclerview)
    RecyclerView messageRecyclerview;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String username;
    private ArrayList<User> users;
    private LinearLayoutManager manager;
    private UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initialColor();
        StatusBarUtil.immersive(this);
        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
        initialList();
        initialRecyclerView();
    }
    public void initialList(){
        if (users == null) {
            users = new ArrayList<>();
        }
        List<User> dbs = LitePal.findAll(User.class);
        users.clear();
        users.addAll(dbs);

    }
    public void initialRecyclerView(){
        if (manager == null) {
            manager = new LinearLayoutManager(this);

        }
        if (adapter == null) {
            adapter = new UserAdapter(users,username,this);
        }
        messageRecyclerview.setLayoutManager(manager);
        messageRecyclerview.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        messageRecyclerview.addItemDecoration(divider);
    }
    public void initialColor() {
        int primary = Color.parseColor("#0980C3");
        int selected = Color.parseColor("#0000FF");
        imgHomepage.setColorFilter(primary);
        txtHomepage.setTextColor(primary);
        imgMessage.setColorFilter(selected);
        txtMessage.setTextColor(selected);
        imgFriend.setColorFilter(primary);
        txtFriend.setTextColor(primary);
        imgMy.setColorFilter(primary);
        txtMy.setTextColor(primary);

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

    @OnClick(R.id.img_my)
    public void onImgMyClicked() {
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.message_addContact)
    public void onMessageAddContactClicked() {
        Intent intent = new Intent(this,AddContactActivity.class);
        startActivity(intent);
        finish();
    }


}
