package com.iu.share;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;
import com.iu.share.Fragment.HomePageFragment;
import com.iu.share.Fragment.MessageFragment;
import com.iu.share.Fragment.MyFragment;
import com.iu.share.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class HomePageActivity extends FragmentActivity {
    private static final String TAG = "HomePageActivity";
    @BindView(R.id.homepage_viewpager)
    ViewPager homepageViewpager;
    @BindView(R.id.img_homepage)
    ImageView imgHomepage;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    @BindView(R.id.img_my)
    ImageView imgMy;
    @BindView(R.id.txt_homepage)
    TextView txtHomepage;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.txt_my)
    TextView txtMy;
    private HomePageFragment homePageFragment;
    private MessageFragment messageFragment;
    private MyFragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        initialFragment();
        initialViewpager();
    }
    public void initialFragment(){
        if (homePageFragment == null){
            homePageFragment = new HomePageFragment();
        }
        if (messageFragment == null){
            messageFragment = new MessageFragment();
        }
        if (myFragment == null){
            myFragment = new MyFragment();
        }
    }
    public void initialViewpager() {
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return homePageFragment;
                    case 1:
                        return messageFragment;
                    case 2:
                        return myFragment;
                }
                return null;
            }
            @Override
            public int getCount() {
                return 3;

            }
        };
        homepageViewpager.setAdapter(adapter);
        changeViewPage(0);
        homepageViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                changeViewPage(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        homepageViewpager.setOffscreenPageLimit(1);

    }
    public void changeViewPage(int current) {
        homepageViewpager.setCurrentItem(current);
        int currentC = Color.parseColor("#4169E1");
        int previousC = Color.parseColor("#DC143C");
        switch (current){
            case 0 :
                imgHomepage.setImageResource(R.drawable.homepage1);
                txtHomepage.setTextColor(currentC);
                imgMessage.setImageResource(R.drawable.message);
                txtMessage.setTextColor(previousC);
                imgMy.setImageResource(R.drawable.my);
                txtMy.setTextColor(previousC);
                break;
            case 1:
                imgHomepage.setImageResource(R.drawable.homepage);
                txtHomepage.setTextColor(previousC);
                imgMessage.setImageResource(R.drawable.message1);
                txtMessage.setTextColor(currentC);
                imgMy.setImageResource(R.drawable.my);
                txtMy.setTextColor(previousC);
                break;
            case 2:
                imgHomepage.setImageResource(R.drawable.homepage);
                txtHomepage.setTextColor(previousC);
                imgMessage.setImageResource(R.drawable.message);
                txtMessage.setTextColor(previousC);
                imgMy.setImageResource(R.drawable.my1);
                txtMy.setTextColor(currentC);
                break;
        }
    }
    @OnClick(R.id.img_homepage)
    public void onImgHomepageClicked() {
        homepageViewpager.setCurrentItem(0);
        changeViewPage(0);
    }
    @OnClick(R.id.img_message)
    public void onImgMessageClicked() {
        homepageViewpager.setCurrentItem(1);
        changeViewPage(1);
    }
    @OnClick(R.id.img_my)
    public void onImgMyClicked() {
        homepageViewpager.setCurrentItem(2);
        changeViewPage(2);
    }
}
