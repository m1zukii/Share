package com.iu.share.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iu.share.Fragment.ContentFragment;
import com.iu.share.Fragment.PlaceHolderFragment;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private String[] datas = new String[]{"普通","政治","经济","社会时事","娱乐","体育","地区","电影音乐","游戏","搞笑","明星","学习"};
    private List<PlaceHolderFragment> fragments = new ArrayList<>();
    public Context context;
    public MyFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        init();
    }

    public void init(){
        for(int i=0;i<12;i++){
            PlaceHolderFragment placeHolderFragment = PlaceHolderFragment.newInstance(datas[i],context);
            fragments.add(placeHolderFragment);
//            ContentFragment contentFragment = ContentFragment.newInstance(datas[i]);
//            fragments.add(contentFragment);
        }
    }
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return 12;
    }
}
