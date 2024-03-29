//package com.iu.share.Util;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Toast;
//
//import net.lucode.hackware.magicindicator.MagicIndicator;
//import net.lucode.hackware.magicindicator.ViewPagerHelper;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
//import net.lucode.hackware.magicindicatordemo.R;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//public class DynamicTabExampleActivity extends AppCompatActivity {
//    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
//    private List<String> mDataList = new ArrayList<String>(Arrays.asList(CHANNELS));
//    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);
//
//    private ViewPager mViewPager;
//    private MagicIndicator mMagicIndicator;
//    private CommonNavigator mCommonNavigator;
//
//    private Toast mToast;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dynamic_tab_example_layout);
//
//        mViewPager = (ViewPager) findViewById(R.id.view_pager);
//        mViewPager.setAdapter(mExamplePagerAdapter);
//
//        mMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator1);
//        mMagicIndicator.setBackgroundColor(Color.parseColor("#d43d3d"));
//        mCommonNavigator = new CommonNavigator(this);
//        mCommonNavigator.setSkimOver(true);
//        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
//
//            @Override
//            public int getCount() {
//                return mDataList.size();
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
//                clipPagerTitleView.setText(mDataList.get(index));
//                clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
//                clipPagerTitleView.setClipColor(Color.WHITE);
//                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mViewPager.setCurrentItem(index);
//                    }
//                });
//                return clipPagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                return null;
//            }
//        });
//        mMagicIndicator.setNavigator(mCommonNavigator);
//        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
//
//        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
//    }
//
//    public void randomPage(View view) {
//        mDataList.clear();
//        int total = new Random().nextInt(CHANNELS.length);
//        for (int i = 0; i <= total; i++) {
//            mDataList.add(CHANNELS[i]);
//        }
//
//        mCommonNavigator.notifyDataSetChanged();    // must call firstly
//        mExamplePagerAdapter.notifyDataSetChanged();
//
//        mToast.setText("" + mDataList.size() + " page");
//        mToast.show();
//    }
//    /*
//      private void initMagicIndicator3() {
//        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator3);
//        magicIndicator.setBackgroundColor(Color.BLACK);
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//
//            @Override
//            public int getCount() {
//                return mDataList.size();
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
//                simplePagerTitleView.setNormalColor(Color.GRAY);
//                simplePagerTitleView.setSelectedColor(Color.WHITE);
//                simplePagerTitleView.setText(mDataList.get(index));
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mViewPager.setCurrentItem(index);
//                    }
//                });
//                return simplePagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
//                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                linePagerIndicator.setColors(Color.WHITE);
//                return linePagerIndicator;
//            }
//        });
//        magicIndicator.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(magicIndicator, mViewPager);
//    }
//     */
//}
