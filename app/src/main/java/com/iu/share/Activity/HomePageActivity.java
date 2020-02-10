package com.iu.share.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.iu.share.Adapter.MyFragmentAdapter;
import com.iu.share.Adapter.ExamplePagerAdapter;
import com.iu.share.Fragment.ContentFragment;
import com.iu.share.R;
import com.iu.share.Util.StatusBarUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

public class HomePageActivity extends AppCompatActivity {


    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.homepage_viewpager)
    ViewPager homepageViewpager;
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
    @BindView(R.id.addMoment)
    ImageView addMoment;

    private String[] channels = new String[]{"普通","政治","经济","社会时事","娱乐","体育","地区","电影音乐","游戏","搞笑","明星","学习"};
    private List<String> mDataList = new ArrayList<String>(Arrays.asList(channels));
    private ContentFragment fragment;
    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page2);
        ButterKnife.bind(this);
        initialColor();
        initAdapter();
        initMagicIndicator();
        StatusBarUtil.immersive(this);
    }
//    colorLogin
//    darkblue
    public void initialColor() {
        int primary = Color.parseColor("#0980C3");
        int selected = Color.parseColor("#0000FF");
        imgHomepage.setColorFilter(selected);
        txtHomepage.setTextColor(selected);
        imgMessage.setColorFilter(primary);
        txtMessage.setTextColor(primary);
        imgFriend.setColorFilter(primary);
        txtFriend.setTextColor(primary);
        imgMy.setColorFilter(primary);
        txtMy.setTextColor(primary);
    }
    public void initAdapter(){
        homepageViewpager.setOffscreenPageLimit(2);
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),this);
        homepageViewpager.setAdapter(myFragmentAdapter);
    }
    private void initMagicIndicator() {

//        homepageViewpager.setAdapter(mExamplePagerAdapter);

        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.parseColor("#455a64"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#87CEFA"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#4169E1"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homepageViewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, homepageViewpager);
    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() { //选择适当的声明周期释放
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
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

    @OnClick(R.id.img_my)
    public void onImgMyClicked() {
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);

    }


    @OnClick(R.id.addMoment)
    public void onAddMomentClicked() {
        Intent intent = new Intent(this,ShareActivity.class);
        startActivity(intent);
    }


}
