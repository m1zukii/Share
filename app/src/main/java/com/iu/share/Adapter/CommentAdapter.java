package com.iu.share.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.iu.share.Activity.App;
import com.iu.share.Bean.Comment;
import com.iu.share.R;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.RTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{
    private ArrayList<Comment> comments;
    private Context context;
    private static final String TAG = "CommentAdapter";
    public CommentAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
        for(int i=0;i< comments.size() ;i++){
            Log.d(TAG, "CommentAdapter: "+comments.get(i));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item,viewGroup,false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Comment comment = comments.get(i);
        String imageUrl = "http://" + App.ipaddress + ":88/" + comment.getUsername() + "/usericon.jpg";
        Glide.with(context).load(imageUrl).centerCrop().into(myViewHolder.commentUserIcon);
        myViewHolder.commentUsername.setText(comment.getUsername());
        myViewHolder.commentPublishTime.setText(formatTime(comment.getPublishTime()));
        myViewHolder.commentText.setText(comment.getContent());
    }

    private String formatTime(long time) {
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        //大于1天 在本月内
        else if(time>time3){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        //这一年内
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        return res;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RImageView commentUserIcon;
        RTextView commentUsername;
        RTextView commentPublishTime;
        RTextView commentText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUserIcon = (RImageView) itemView.findViewById(R.id.comment_usericon);
            commentUsername = (RTextView) itemView.findViewById(R.id.comment_username);
            commentPublishTime = (RTextView) itemView.findViewById(R.id.comment_publishTime);
            commentText = (RTextView) itemView.findViewById(R.id.comment_text);
        }
    }
    public void addComment(Comment comment){
        comments.add(comment);
        this.notifyDataSetChanged();
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
