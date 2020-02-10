package com.iu.share.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.iu.share.Adapter.MomentAdapter;
import com.iu.share.Bean.Comment;
import com.iu.share.Bean.Moment;
import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.iu.share.Util.StatusBarUtil;
import com.ruffian.library.widget.REditText;
import com.ruffian.library.widget.RTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class FriendActivity extends AppCompatActivity implements OnRefreshLoadMoreListener {
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
    @BindView(R.id.searchText)
    REditText searchText;
    @BindView(R.id.searchBtn)
    RTextView searchBtn;
    @BindView(R.id.search_moment_recycerview)
    RecyclerView searchMomentRecycerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private ArrayList<Moment> totals;
    private ArrayList<Moment> current;
    private LinearLayoutManager manager;
    private MomentAdapter adapter;
    private String address = "http://" + App.ipaddress + ":8080/aa/Search";
    private static final String TAG = "FriendActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        initialColor();
        initialRefreshLayout();
        initialList();
        initialRecycerView();
        StatusBarUtil.immersive(this);

    }
    public void initialList() {
        if (totals == null) {
            totals = new ArrayList<>();
        }
        if (current == null) {
            current = new ArrayList<>();
        }
    }
    public void initialRecycerView() {
        if (manager == null) {
            manager = new LinearLayoutManager(this);
        }
        if (adapter == null) {
            adapter = new MomentAdapter(totals, this);
        }
        searchMomentRecycerview.setLayoutManager(manager);
        searchMomentRecycerview.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        searchMomentRecycerview.addItemDecoration(divider);
    }
    public void initialColor() {
        int primary = Color.parseColor("#0980C3");
        int selected = Color.parseColor("#0000FF");
        imgHomepage.setColorFilter(primary);
        txtHomepage.setTextColor(primary);
        imgMessage.setColorFilter(primary);
        txtMessage.setTextColor(primary);
        imgFriend.setColorFilter(selected);
        txtFriend.setTextColor(selected);
        imgMy.setColorFilter(primary);
        txtMy.setTextColor(primary);
    }
    @OnClick(R.id.img_homepage)
    public void onImgHomepageClicked() {
        Intent intent = new Intent(this, HomePageActivity.class);
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
    public void initialRefreshLayout() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        RefreshFooter footer = new ClassicsFooter(this);
        ((ClassicsFooter) footer).setPrimaryColor(getResources().getColor(R.color.mediumturquoise));
        ((ClassicsFooter) footer).setAccentColor(getResources().getColor(R.color.dodgerblue));
        refreshLayout.setRefreshFooter(footer);
    }
    @OnClick(R.id.searchBtn)
    public void onSearchBtnClicked() {
//        queryMoments();
        onRefresh(refreshLayout);
    }
    private void queryMoments() {
        String search = searchText.getText().toString();
        HttpUtil.searchMoment(address, search, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                totals.clear();
                current.clear();
                totals.addAll(parseString(res));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.clearData();
                        ArrayList<Moment> moments = new ArrayList<>();
                        int max = totals.size() < 10 ? totals.size() : 10;
                        for (int i = 0; i < max; i++) {
                            moments.add(totals.get(i));
                        }
                        Log.d(TAG, "run: "+moments.size());
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
            String username, shareText, type;
            long publishTime;
            int good;
            ArrayList<String> videoLists;
            ArrayList<String> imgLists;
            ArrayList<Comment> comments;
            array = new JSONArray(res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                username = object.getString("username");
                shareText = object.getString("shareText");
                type = object.getString("type");
                publishTime = object.getLong("publishTime");
                imgLists = parseResult(object.getString("imgLists"));
                videoLists = parseResult(object.getString("videoLists"));
                comments = parseComments(object.getString("comments"));
                good = object.getInt("good");
                Moment moment = new Moment(username, shareText, publishTime, videoLists, imgLists, comments, type, good);
//                Log.d(TAG, "parseString: "+moment);
                moments.add(moment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ;
        return sortList(moments);
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
    public ArrayList<String> parseResult(String source) {
        JSONArray jsonArray = null;
        String temp;
        ArrayList<String> res = new ArrayList<>();
        try {
            jsonArray = new JSONArray(source);
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getString(i);
                res.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
    public ArrayList<Moment> sortList(ArrayList<Moment> source) {
        Collections.sort(source, new Comparator<Moment>() {
            @Override
            public int compare(Moment o1, Moment o2) {
                long time1 = o1.getPublishTime();
                long time2 = o2.getPublishTime();
                if (time1 == time2)
                    return 0;
                return (int) (time2 - time1);
            }
        });
        return source;
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
    public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
        String search = searchText.getText().toString();
        HttpUtil.searchMoment(address,search, new okhttp3.Callback() {
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
}
