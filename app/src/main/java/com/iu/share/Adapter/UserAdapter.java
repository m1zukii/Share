package com.iu.share.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.iu.share.Activity.App;
import com.iu.share.Activity.MsgActivity;
import com.iu.share.Activity.MyActivity;
import com.iu.share.Bean.User;
import com.iu.share.R;
import com.iu.share.Service.LoadMsgService;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.RTextView;
import java.util.ArrayList;
//用户适配器
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> users;
    private String myName;
    private Context context;
    class ViewHolder extends RecyclerView.ViewHolder {
        RTextView username;
        RTextView userLastMsg;
        RImageView usericon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = (RTextView) itemView.findViewById(R.id.user_name);
            userLastMsg = (RTextView) itemView.findViewById(R.id.user_lastMessage);
            usericon = (RImageView) itemView.findViewById(R.id.user_icon);
        }
    }
    public UserAdapter(ArrayList<User> users, String myName, Context context) {
        this.users = users;
        this.myName = myName;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                User user = users.get(position);
                //1
                Intent intent = new Intent(context, MsgActivity.class);
                intent.putExtra("toUsername", user.getUsername());
                intent.putExtra("myUserName", myName);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        };
        viewHolder.username.setOnClickListener(listener);
        viewHolder.userLastMsg.setOnClickListener(listener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = users.get(i);
        String imageUrl = "http://" + App.ipaddress + ":88/" + user.getUsername() + "/usericon.jpg";

        viewHolder.username.setText(user.getUsername());
        String content= user.getLastMessage();
        if(content.length()>12){
            content = content.substring(0,12)+"...";
        }

        viewHolder.userLastMsg.setText(content);
        Glide.with(context).load(imageUrl).placeholder(R.drawable.defaulticon).into(viewHolder.usericon);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
}
