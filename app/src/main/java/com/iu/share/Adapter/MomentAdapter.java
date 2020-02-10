package com.iu.share.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.assionhonty.lib.assninegridview.AssNineGridViewClickAdapter;
import com.assionhonty.lib.assninegridview.ImageInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iu.share.Activity.App;
import com.iu.share.Activity.MomentDetailActivity;
import com.iu.share.Activity.PersonDetailActivity;
import com.iu.share.Bean.Moment;
import com.iu.share.Fragment.FruitAdapter;
import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.RTextView;
import com.zhuang.likeviewlibrary.LikeView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MyViewHolder> {
    private ArrayList<Moment> moments ;
    private Context context;
    private int color = Color.parseColor("#48D1CC");
    private int click = Color.parseColor("#7CFC00");
    private static final String TAG = "MomentAdapter";
    private String addressGood = "http://" + App.ipaddress + ":8080/aa/Good";
    public MomentAdapter(ArrayList<Moment> moments, Context context) {
        this.moments = moments;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.moment_item,viewGroup,false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.momentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewGroup.getContext(),"You click text",Toast.LENGTH_LONG).show();
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Moment moment = moments.get(position);
                Intent intent = new Intent(context, MomentDetailActivity.class);
                intent.putExtra("moment",moment);
                context.startActivity(intent);
            }
        };
        viewHolder.momentText.setOnClickListener(listener);
        viewHolder.momentCommentLayout.setOnClickListener(listener);
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Moment moment = moments.get(position);
                Intent intent = new Intent(context, PersonDetailActivity.class);
                intent.putExtra("username",moment.getUsername());
                context.startActivity(intent);
            }
        };
        viewHolder.momentUsername.setOnClickListener(listener1);
        viewHolder.momentUserIcon.setOnClickListener(listener1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final Moment moment = moments.get(i);
        final int number = i;
        String temp = "http://"+App.ipaddress+":88/"+moment.getUsername()+"/";
        String imageUrl = "http://" + App.ipaddress + ":88/" + moment.getUsername() + "/usericon.jpg";
        Glide.with(context).load(imageUrl)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop().into(myViewHolder.momentUserIcon);
        myViewHolder.momentUsername.setText(moment.getUsername());
        myViewHolder.momentPublishTime.setText(formatTime(moment.getPublishTime()));
        myViewHolder.momentText.setText(moment.getShareText());
        if (moment.getImgLists().size()>0) {
            myViewHolder.momentImage.setVisibility(View.VISIBLE);
            List<ImageInfo> imageInfos = getImageInfos(moment);
            myViewHolder.momentImage.setAdapter(new AssNineGridViewClickAdapter(context,imageInfos));
        }
        else if(moment.getVideoLists().size()>0) {
            myViewHolder.momentVideo.setVisibility(View.VISIBLE);
            myViewHolder.momentVideo.setUp(temp+moment.getVideoLists().get(0),JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "视频");
        }
        myViewHolder.momentCommentTimes.setText(""+moment.getComments().size());
        myViewHolder.likeView.setLikeCount(moment.getGood());
        myViewHolder.likeView.setOnLikeListeners(new LikeView.OnLikeListeners() {
            @Override
            public void like(boolean isCancel) {
                if (isCancel) {
                    final int count = moment.getGood();
                    myViewHolder.likeView.setLikeCount(count-1);
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
                    myViewHolder.likeView.setLikeCount(count+1);
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
    public List<ImageInfo> getImageInfos(Moment moment){
        ArrayList<String> imgs = moment.getImgLists();
        List<ImageInfo> imageInfos = new ArrayList<>();
        String temp = "http://"+App.ipaddress+":88/"+moment.getUsername()+"/";
        for (String url:imgs) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setBigImageUrl(temp+url);
            imageInfo.setThumbnailUrl(temp+url);
            imageInfos.add(imageInfo);
        }
        return imageInfos;
    }
    @Override
    public int getItemCount() {
        return moments.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        RImageView momentUserIcon;
        RTextView momentUsername;
        RTextView momentPublishTime;
        RTextView momentText;
        AssNineGridView momentImage;
        JZVideoPlayerStandard momentVideo;
        LinearLayout momentCommentLayout;
        ImageView momentCommentImage;
        TextView momentCommentTimes;
        LikeView likeView;
//        TextView momentGoodTimes;
//        ImageView momentGoodImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            momentUserIcon = (RImageView) itemView.findViewById(R.id.moment_usericon);
            momentUsername = (RTextView) itemView.findViewById(R.id.moment_username);
            momentPublishTime = (RTextView) itemView.findViewById(R.id.moment_publishTime);
            momentText = (RTextView) itemView.findViewById(R.id.moment_text);
            momentImage = (AssNineGridView) itemView.findViewById(R.id.moment_image);
            momentVideo = (JZVideoPlayerStandard) itemView.findViewById(R.id.moment_video);
            momentCommentLayout = (LinearLayout)itemView.findViewById(R.id.moment_commentlayout);
            momentCommentImage = (ImageView) itemView.findViewById(R.id.moment_comment_image);
            momentCommentTimes = (TextView) itemView.findViewById(R.id.moment_comment_times);

            likeView = (LikeView) itemView.findViewById(R.id.likeView);
        }
    }
    public String formatTime(long time){
        Calendar c1 = Calendar.getInstance();
        long current = c1.getTime().getTime()/1000;
        //c2这一天的零点
        Calendar c2 = Calendar.getInstance();
        c2.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.DATE),
                0,0,0);
        long time2 = c2.getTime().getTime()/1000;
        //c3 这个月的第一天的零点
        Calendar c3 = Calendar.getInstance();
        c3.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),1,
                0,0,0);
        long time3 = c3.getTime().getTime()/1000;
        //c4 这一年的第一天
        Calendar c4 = Calendar.getInstance();
        c4.set(c1.get(Calendar.YEAR),0,1,
                0,0,0);
        long time4 = c4.getTime().getTime()/1000;
        long gap;
        String res = "";
        //1小时
        if ((current-time)<3600) {
            gap = (current-time)/60;
            res = ""+gap+"分钟前";
        }
        //大于一小时 在今天之内 显示今天的时间 17:32
        else if (time>time2) {
//            long a = (time - time2)/3600;
//            long b = (time - time2 - a*3600 )/60;
//            res = ""+a+":"+b;
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        //大于1天 在本月内
        else if(time>time3){
//            Calendar publish = Calendar.getInstance();
//            Date date = new Date();
//            date.setTime(time);
//            publish.setTime(date);
//            int month = publish.get(Calendar.MONTH)+1;
//            int day = publish.get(Calendar.DATE);
//            res = ""+month+"-"+day;
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        //这一年内
        else{
//            Calendar publish = Calendar.getInstance();
//            Date date = new Date();
//            date.setTime(time);
//            publish.setTime(date);
//            int year = publish.get(Calendar.YEAR);
//            int month = publish.get(Calendar.MONTH)+1;
//            int day = publish.get(Calendar.DATE);
//            res = ""+year+"-"+month+"-"+day;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        return res;
    }
    public void addData(ArrayList<Moment> source){

        moments.addAll(source);
        notifyDataSetChanged();
    }
    public int getListSize(){
        return moments.size();
    }
    public void refreshData(ArrayList<Moment> source){
        moments.clear();
        moments.addAll(source);
        notifyDataSetChanged();
    }
    public void clearData(){
        moments.clear();
        notifyDataSetChanged();
    }
}
