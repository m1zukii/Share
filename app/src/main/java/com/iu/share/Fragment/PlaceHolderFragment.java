package com.iu.share.Fragment;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.iu.share.Activity.App;
import com.iu.share.Adapter.MomentAdapter;
import com.iu.share.Bean.Comment;
import com.iu.share.Bean.Moment;
import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import okhttp3.Call;
import okhttp3.Response;
public class PlaceHolderFragment extends BaseFragment implements OnRefreshListener, OnRefreshLoadMoreListener {
    private String contentTheme;
//    private SwipeRefreshLayout refreshLayout;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MomentAdapter momentAdapter;
    public  Context context;
    private String address = "http://"+ App.ipaddress+":8080/aa/QueryMoment";
    private ArrayList<Moment> totals;
    private ArrayList<Moment> current;
    private static final String TAG = "PlaceHolderFragment";
    public static PlaceHolderFragment newInstance(String theme,Context context){
        PlaceHolderFragment placeHolderFragment = new PlaceHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("theme",theme);
        placeHolderFragment.setArguments(bundle);
        placeHolderFragment.context = context;
        return placeHolderFragment;
    }
    public void initialList(){
        if (totals == null) {
            totals = new ArrayList<>();
        }
        if (current == null) {
            current = new ArrayList<>();
        }

    }
    @Override
    public void initVariables(Bundle bundle) {
        super.initVariables(bundle);
        String theme = bundle.getString("theme");
        this.contentTheme = theme;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initialList();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placeholder,null,false);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(container.getContext());
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        RefreshFooter footer = new ClassicsFooter(getContext());
        ((ClassicsFooter) footer).setPrimaryColor(getResources().getColor(R.color.mediumturquoise));
        ((ClassicsFooter) footer).setAccentColor(getResources().getColor(R.color.dodgerblue));
        refreshLayout.setRefreshFooter(footer);
//        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        if (momentAdapter == null) {
            momentAdapter = new MomentAdapter(current,context);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(momentAdapter);
        return view;
    }
    @Override
    protected void initData() {
        HttpUtil.queryMoment(address,contentTheme, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                totals.addAll(parseString(res));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Moment> moments = new ArrayList<>();
                        int max = totals.size()<10?totals.size():10;
                        for(int i=0;i<max  ;i++){
                            moments.add(totals.get(i));
                        }
                        momentAdapter.addData(moments);
                    }
                });
            }
        });
    }
    @Override
    protected void setDefaultFragmentTitle(String title) {
    }
    public ArrayList<Moment> parseString(String source){
        JSONArray array = null;
        ArrayList<Moment> moments = new ArrayList<>();
        try {
            String username,shareText,type;
            long publishTime;
            int good;
            ArrayList<String> videoLists;
            ArrayList<String> imgLists;
            ArrayList<Comment> comments;
            array = new JSONArray(source);
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
    @Override
    public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                int size = momentAdapter.getListSize();
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
                    momentAdapter.addData(moments);
                    momentAdapter.notifyDataSetChanged();
                    refreshLayout.finishLoadMore();
                }
            }
        },2000);
    }

    @Override
    public void onRefresh(@NonNull final  RefreshLayout refreshLayout) {
        HttpUtil.queryMoment(address,contentTheme, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                ArrayList<Moment> moments = parseString(res);
                totals.clear();
                totals.addAll(moments);
//                Log.d(TAG, "onResponse: "+moments.size());
//                if (moments.size()==totals.size()){
//                    refreshLayout.finishRefresh();
//                    return;
//                }
//                long time = totals.get(0).getPublishTime();
//                Moment temp;
//                for(int i=0;i<moments.size() ;i++){
//                    temp = moments.get(i);
//                    if (temp.getPublishTime()>time) {
//                        totals.add(temp);
//                    }
//                    else if (temp.getPublishTime() == time)break;
//                }
//               totals = sortList(totals);

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
                        momentAdapter.clearData();

                        momentAdapter.addData(moments);
//                        momentAdapter.refreshData(totals);
                        refreshLayout.finishRefresh();
                    }
                },1000);

            }
        });
    }

}
