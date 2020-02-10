package com.iu.share.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.guoxiaoxing.phoenix.picker.util.LightStatusBarUtils;
import com.iu.share.Adapter.MomentAdapter;
import com.iu.share.Bean.Comment;
import com.iu.share.Bean.Moment;
import com.iu.share.Fragment.Fruit;
import com.iu.share.Fragment.FruitAdapter;
import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.iu.share.Util.StatusBarUtil;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.RTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class PersonDetailActivity extends AppCompatActivity  {
    @BindView(R.id.username_collapse)
    RTextView usernameCollapse;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.collapse)
    CollapsingToolbarLayout collapse;
    @BindView(R.id.moment_recycerview)
    RecyclerView momentRecycerview;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.username_topbar)
    TextView usernameTopbar;
    @BindView(R.id.buttonBarLayout)
    ButtonBarLayout buttonBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.usericon_topbar)
    RImageView usericonTopbar;
    @BindView(R.id.usericon_collapse)
    RImageView usericonCollapse;
    private String userName = "";
    private int mOffset = 0;
    private int mScrollY = 0;
    private ArrayList<Moment> totals;
    private ArrayList<Moment> current;
    private LinearLayoutManager manager;
    private MomentAdapter adapter;
    private String address = "http://"+ App.ipaddress+":8080/aa/QueryPersonMoment";
    private static final String TAG = "PersonDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_person_detail);
        StatusBarUtil.immersive(this);

        ButterKnife.bind(this);
        initialWidget();
        initialRefreshLayout();
        initialList();
        initialRecycerView();
    }
    public void initialList(){
        if (totals == null) {
            totals = new ArrayList<>();
        }
        if (current == null) {
            current = new ArrayList<>();
        }
    }
    public void initialWidget() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initialRefreshLayout();
        Intent intent = getIntent();
        if (intent!=null) {
            this.userName = intent.getStringExtra("username");
        }
        String iconUrl = "http://" + App.ipaddress + ":88/" + userName + "/usericon.jpg";
        Glide.with(this).load(iconUrl).centerCrop()
                .skipMemoryCache(true)

                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(usericonCollapse);
        Glide.with(this).load(iconUrl).centerCrop()
                .skipMemoryCache(true)

                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(usericonTopbar);
        String bgUrl = "http://" + App.ipaddress + ":88/" + userName + "/background.jpg";
        Glide.with(this)
                .load(bgUrl)


                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        layout.setBackground(resource);
                    }
                });
        usernameCollapse.setText(userName);
        usernameTopbar.setText(userName);
//        ii();
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                HttpUtil.queryMoment1(address,userName, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        ArrayList<Moment> moments = parseString(res);
                        totals.clear();
                        totals.addAll(moments);
                        refreshLayout.getLayout().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "run: total"+totals.size());
                                ArrayList<Moment> moments = new ArrayList<>();
                                int total = totals.size();
                                int max = (total<=10?total:10);
                                for(int i=0;i<max ;i++){
                                    moments.add(totals.get(i));
                                }
                                adapter.clearData();
                                adapter.addData(moments);
                                refreshLayout.finishRefresh();
                            }
                        },1000);
                    }
                });
            }
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int size = adapter.getListSize();
                        int total = totals.size();
                        if (size == total) {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                        else{
                            ArrayList<Moment> moments = new ArrayList<>();
                            int max = (total<=(size+10)?total:(size+10));
                            for(int i=size;i<max ;i++){
                                moments.add(totals.get(i));
                            }
                            adapter.addData(moments);
                            adapter.notifyDataSetChanged();
                            refreshLayout.finishLoadMore();
                        }
                    }
                },2000);
            }
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtil.dp2px(170);
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)&0x00ffffff;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBarLayout.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                }
                lastScrollY = scrollY;
            }
        });
        buttonBarLayout.setAlpha(0);
        toolbar.setBackgroundColor(0);
    }
    public void initialRecycerView(){
        initMoments();
        if (manager == null) {
            manager = new LinearLayoutManager(this);
        }
        if (adapter == null) {
            adapter = new MomentAdapter(current,this);
        }
        momentRecycerview.setLayoutManager(manager);
        momentRecycerview.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        momentRecycerview.addItemDecoration(divider);
    }
    public void initMoments() {
        HttpUtil.queryMoment1(address, userName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                totals.addAll(parseString(res));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Moment> moments = new ArrayList<>();
                        int max = totals.size()<10?totals.size():10;
                        for(int i=0;i<max  ;i++){
                            moments.add(totals.get(i));
                        }
                        adapter.addData(moments);
                    }
                });
            }
        });
    }
    public ArrayList<Moment> parseString(String res) {
        JSONArray array = null;
        ArrayList<Moment> moments = new ArrayList<>();
        try {
            String username,shareText,type;
            long publishTime;
            int good;
            ArrayList<String> videoLists;
            ArrayList<String> imgLists;
            ArrayList<Comment> comments;
            array = new JSONArray(res);
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                username = object.getString("username");
                shareText = object.getString("shareText");
                type = object.getString("type");
                publishTime = object.getLong("publishTime");
                imgLists = parseResult(object.getString("imgLists"));
                videoLists = parseResult(object.getString("videoLists"));
                comments = parseComments(object.getString("comments"));
                good = object.getInt("good");
                Moment moment = new Moment(username,shareText,publishTime,videoLists,imgLists,comments,type,good);
//                Log.d(TAG, "parseString: "+moment);
                moments.add(moment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ;
        return  sortList(moments);
    }
    private ArrayList<Comment> parseComments(String str) throws JSONException {
        JSONArray array = new JSONArray(str);
        Comment comment;
        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String username = object.getString("username");
            String content = object.getString("content");
            long publishtime = object.getLong("publishTime");
            comment = new Comment(username, content, publishtime);
            comments.add(comment);
        }
        return comments;
    }
    public ArrayList<String> parseResult(String source){
        JSONArray jsonArray = null;
        String temp;
        ArrayList<String> res = new ArrayList<>();
        try {
            jsonArray = new JSONArray(source);
            for(int i=0;i< jsonArray.length() ;i++){
                temp = jsonArray.getString(i);
                res.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
    public ArrayList<Moment> sortList(ArrayList<Moment> source){
        Collections.sort(source, new Comparator<Moment>() {
            @Override
            public int compare(Moment o1, Moment o2) {
                long time1 = o1.getPublishTime();
                long time2 = o2.getPublishTime();
                if (time1 == time2)
                    return 0;
                return (int)(time2-time1);
            }
        });
        return source;
    }
    public void initialRefreshLayout() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        RefreshFooter footer = new ClassicsFooter(this);
        ((ClassicsFooter) footer).setPrimaryColor(getResources().getColor(R.color.mediumturquoise));
        ((ClassicsFooter) footer).setAccentColor(getResources().getColor(R.color.dodgerblue));
        refreshLayout.setRefreshFooter(footer);
//        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
    }
}
