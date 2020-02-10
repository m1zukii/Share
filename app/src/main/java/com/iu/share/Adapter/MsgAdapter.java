package com.iu.share.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.iu.share.Activity.App;
import com.iu.share.Bean.Msg;
import com.iu.share.R;
import com.ruffian.library.widget.RImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//消息适配器
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;
    private String myUsername;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg, leftTime;
        TextView rightMsg, rightTime;
        RImageView lefticon,righticon;
        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
            leftTime = (TextView) view.findViewById(R.id.left_msg_time);
            rightTime = (TextView) view.findViewById(R.id.right_msg_time);
            lefticon = (RImageView) view.findViewById(R.id.left_user_icon);
            righticon = (RImageView) view.findViewById(R.id.right_user_icon);
        }
    }
    public MsgAdapter(List<Msg> mMsgList, String myUsername,Context context) {
        this.mMsgList = mMsgList;
        this.myUsername = myUsername;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        String imageUrl = "http://" + App.ipaddress + ":88/" + msg.getFromUser() + "/usericon.jpg";
        // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
        if (msg.getFromUser().equals(myUsername)) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            holder.leftTime.setText(formatTime1(msg.getTime()));
            Glide.with(context).load(imageUrl).into(holder.lefticon);
        }
        // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
        else {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            holder.rightTime.setText(formatTime1(msg.getTime()));
            Glide.with(context).load(imageUrl).into(holder.righticon);
        }
    }
    //格式化时间
    public String formatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒");
        Date date = new Date();
        date.setTime(time);
        String result = simpleDateFormat.format(date);
        return result;
    }
    public String formatTime1(long time){
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        //这一年内
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = new Date();
            date.setTime(time*1000);
            res = dateFormat.format(date);
        }
        return res;
    }
    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
