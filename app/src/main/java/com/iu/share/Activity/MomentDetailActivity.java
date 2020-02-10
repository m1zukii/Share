package com.iu.share.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.assionhonty.lib.assninegridview.AssNineGridViewClickAdapter;
import com.assionhonty.lib.assninegridview.ImageInfo;
import com.bumptech.glide.Glide;
import com.iu.share.Adapter.CommentAdapter;
import com.iu.share.Bean.Comment;
import com.iu.share.Bean.Moment;
import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.ruffian.library.widget.REditText;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.RTextView;
import com.zhuang.likeviewlibrary.LikeView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MomentDetailActivity extends AppCompatActivity {
    @BindView(R.id.moment_usericon)
    RImageView momentUsericon;
    @BindView(R.id.moment_username)
    RTextView momentUsername;
    @BindView(R.id.moment_publishTime)
    RTextView momentPublishTime;
    @BindView(R.id.moment_text)
    RTextView momentText;
    @BindView(R.id.moment_image)
    AssNineGridView momentImage;
    @BindView(R.id.moment_video)
    JZVideoPlayerStandard momentVideo;
    @BindView(R.id.moment_commentlayout)
    LinearLayout momentCommentlayout;
    @BindView(R.id.moment_goodlayout)
    LinearLayout momentGoodlayout;
    @BindView(R.id.comment_recyclerView)
    RecyclerView commentRecyclerView;
    @BindView(R.id.moment_comment_text)
    REditText momentCommentText;
    @BindView(R.id.moment_send_comment)
    Button momentSendComment;
    @BindView(R.id.moment_comment_image)
    ImageView momentCommentImage;
    @BindView(R.id.moment_comment_times)
    TextView momentCommentTimes;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.commentLayout)
    LinearLayout commentLayout;
    @BindView(R.id.likeView)
    LikeView likeView;
    private Moment moment;
    private LinearLayoutManager manager;
    private ArrayList<Comment> comments;
    private CommentAdapter adapter;
    private String username = "";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String TAG = "MomentDetailActivity";
    private String addressGood = "http://" + App.ipaddress + ":8080/aa/Good";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);
        ButterKnife.bind(this);
        initial();
        initialData();
        initialComment();
    }

    private void initial() {
        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
    }

    public void initialData() {
        Intent intent = getIntent();
        if (intent != null) {
            this.moment = intent.getParcelableExtra("moment");
        }
        String temp = "http://" + App.ipaddress + ":88/" + moment.getUsername() + "/";
        String imageUrl = "http://" + App.ipaddress + ":88/" + moment.getUsername() + "/usericon.jpg";
        Glide.with(this).load(imageUrl).centerCrop().into(momentUsericon);
        momentUsername.setText(moment.getUsername());
        momentPublishTime.setText(formatTime(moment.getPublishTime()));
        momentText.setText(moment.getShareText());
        if (moment.getImgLists().size() > 0) {
            momentImage.setVisibility(View.VISIBLE);
            List<ImageInfo> imageInfos = getImageInfos(moment);
            momentImage.setAdapter(new AssNineGridViewClickAdapter(this, imageInfos));
        } else if (moment.getVideoLists().size() > 0) {
            momentVideo.setVisibility(View.VISIBLE);
            momentVideo.setUp(temp + moment.getVideoLists().get(0), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "视频");
        }
        momentCommentTimes.setText("" + moment.getComments().size());
        likeView.setLikeCount(moment.getGood());
        likeView.setOnLikeListeners(new LikeView.OnLikeListeners() {
            @Override
            public void like(boolean isCancel) {
                if (isCancel) {
                    final int count = moment.getGood();
                    likeView.setLikeCount(count-1);
                    HttpUtil.handleGood(addressGood, moment.getUsername(), moment.getPublishTime(), false, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            if (res.startsWith("success")) {
                                moment.setGood(count-1);
                            }
                        }
                    });
                }
                else{
                    final int count = moment.getGood();
                    likeView.setLikeCount(count+1);
                    HttpUtil.handleGood(addressGood, moment.getUsername(), moment.getPublishTime(), true, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            if (res.startsWith("success")) {
                                moment.setGood(count+1);
                            }
                        }
                    });
                }
            }
        });
    }

    public void initialComment() {
        if (manager == null) {
            manager = new LinearLayoutManager(this);
        }
        if (comments == null) {
            comments = this.moment.getComments();
        }
        if (adapter == null) {
            adapter = new CommentAdapter(comments, this);
        }
        commentRecyclerView.setLayoutManager(manager);
        commentRecyclerView.setAdapter(adapter);
    }

    public List<ImageInfo> getImageInfos(Moment moment) {
        ArrayList<String> imgs = moment.getImgLists();
        List<ImageInfo> imageInfos = new ArrayList<>();
        String temp = "http://" + App.ipaddress + ":88/" + moment.getUsername() + "/";
        for (String url : imgs) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setBigImageUrl(temp + url);
            imageInfo.setThumbnailUrl(temp + url);
            imageInfos.add(imageInfo);
        }
        return imageInfos;
    }

    private String formatTime(long time) {
        Calendar c1 = Calendar.getInstance();
        long current = c1.getTime().getTime() / 1000;
        //c2这一天的零点
        Calendar c2 = Calendar.getInstance();
        c2.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE),
                0, 0, 0);
        long time2 = c2.getTime().getTime() / 1000;
        //c3 这个月的第一天的零点
        Calendar c3 = Calendar.getInstance();
        c3.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), 1,
                0, 0, 0);
        long time3 = c3.getTime().getTime() / 1000;
        //c4 这一年的第一天
        Calendar c4 = Calendar.getInstance();
        c4.set(c1.get(Calendar.YEAR), 0, 1,
                0, 0, 0);
        long time4 = c4.getTime().getTime() / 1000;
        long gap;
        String res = "";
        //1小时
        if ((current - time) < 3600) {
            gap = (current - time) / 60;
            res = "" + gap + "分钟前";
        }
        //大于一小时 在今天之内 显示今天的时间 17:32
        else if (time > time2) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date();
            date.setTime(time * 1000);
            res = dateFormat.format(date);
        }
        //大于1天 在本月内
        else if (time > time3) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
            Date date = new Date();
            date.setTime(time * 1000);
            res = dateFormat.format(date);
        }
        //这一年内
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.setTime(time * 1000);
            res = dateFormat.format(date);
        }
        return res;
    }

    @OnClick(R.id.moment_send_comment)
    public void onMomentSendCommentClicked() {
        String uploadAddress = "http://" + App.ipaddress + ":8080/aa/UploadComment";
        String content = momentCommentText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime() / 1000;
        final Comment comment = new Comment(this.username, content, time);
        HttpUtil.uploadComment(uploadAddress, moment.getUsername(), moment.getPublishTime(), comment, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                if (s.startsWith("success")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MomentDetailActivity.this, "评论成功", Toast.LENGTH_LONG).show();
                            momentCommentText.setText("");
                            adapter.addComment(comment);
                            momentCommentTimes.setText("" + adapter.getComments().size());
                        }
                    });
                } else if (s.startsWith("fail")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MomentDetailActivity.this, "评论失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.moment_commentlayout)
    public void onMomentCommentlayoutClicked() {
        commentLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        finish();
    }
}
