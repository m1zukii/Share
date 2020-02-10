package com.iu.share.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iu.share.Adapter.MsgAdapter;
import com.iu.share.Bean.Msg;
import com.iu.share.Bean.User;
import com.iu.share.R;
import com.iu.share.Service.LoadMsgService;
import com.iu.share.Util.HttpCallbackListener;
import com.iu.share.Util.HttpUtil;
import com.iu.share.Util.StatusBarUtil;
import com.ruffian.library.widget.RImageView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//消息活动
public class MsgActivity extends AppCompatActivity {
    @BindView(R.id.backUserActivity)
    RImageView backUserActivity;
    @BindView(R.id.contact_user)
    TextView contactUser;
    @BindView(R.id.msg_recyclerview)
    RecyclerView msgRecyclerview;
    @BindView(R.id.input_text)
    EditText inputText;
    @BindView(R.id.send)
    Button send;
    private MsgAdapter adapter;
    private ArrayList<Msg> msgList;
    private LinearLayoutManager manager;
    private String myUsername;
    private String toUsername;
    private static final String TAG = "MsgActivity";
    private String addressQuery = "http://" + App.ipaddress + ":8080/aa/lizhien";
    private String addressInsert = "http://" + App.ipaddress + ":8080/aa/erke";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                adapter.notifyDataSetChanged();
                msgRecyclerview.scrollToPosition(msgList.size() - 1);

            }
        }
    };
    private LoadMsgService loadMsgService;
    private LoadMsgService.MyBinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (LoadMsgService.MyBinder) service;
            loadMsgService = binder.getService();
            loadMsgService.username1 = myUsername;
            loadMsgService.username2 = toUsername;
            binder.loadData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            toUsername = intent.getStringExtra("toUsername");
            myUsername = intent.getStringExtra("myUserName");
        }
        contactUser.setText(toUsername);
        StatusBarUtil.immersive(this);
        initialMsg();
        initialRecview();
        Intent intent1 = new Intent(this, LoadMsgService.class);
        bindService(intent1, connection, BIND_AUTO_CREATE);
        updateMsg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void initialRecview() {
        if (manager == null) {
            manager = new LinearLayoutManager(this);
        }
        msgRecyclerview.setLayoutManager(manager);
        if (adapter == null) {
            adapter = new MsgAdapter(msgList, myUsername, this);
        }
        msgRecyclerview.setAdapter(adapter);
    }

    public Notification getNotification(String title, String message, String toUsername) {
        Intent intent = new Intent(MsgActivity.this, MsgActivity.class);
        intent.putExtra("toUsername", toUsername);
        PendingIntent pendingIntent = PendingIntent.getActivity(MsgActivity.this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(MsgActivity.this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message).build();
        return notification;
    }

    public NotificationManager getManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private ArrayList<Msg> list;

    public void updateMsg() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                list = loadMsgService.getMsgs();
                if (msgList.size() < list.size()) {
                    for (int i = msgList.size(); i < list.size(); i++) {
                        final Msg msg = list.get(i);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                msgList.add(msg);
                                adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                                msgRecyclerview.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行

                            }
                        });
                    }


                }
            }


        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 1000, 2000);
    }

    //初始化消息
    public void initialMsg() {
        if (msgList == null) {
            msgList = new ArrayList<Msg>();
        }
        HttpUtil.queryMsg(addressQuery, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "onFinish: " + response);
                ArrayList<Msg> msgs = sortList(parseJSONWithJSONObject(response));
                if (msgs.size() > 0) {
                    msgList.clear();
                    msgList.addAll(msgs);
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        }, myUsername, toUsername);

    }

    //用JSON格式化数据
    private ArrayList<Msg> parseJSONWithJSONObject(String jsonData) {
        String content;
        long time;
        String fromUser, toUser;
        Msg msg;
        ArrayList<Msg> msgs = new ArrayList<Msg>();
        try {
            if (jsonData != null && jsonData.length() != 0) {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    content = jsonObject.getString("content");
                    time = jsonObject.getLong("time");
                    fromUser = jsonObject.getString("fromUser");
                    toUser = jsonObject.getString("toUser");
                    msg = new Msg(fromUser, toUser, content, time);
                    msgs.add(msg);
                }
            }
        } catch (Exception e) {
            Log.d("LJR", "parseJSONWithJSONObject: " + e.getMessage());
        }
        return msgs;
    }

    //根据消息的时间进行排序
    public ArrayList<Msg> sortList(ArrayList<Msg> source) {
        Collections.sort(source, new Comparator<Msg>() {
            @Override
            public int compare(Msg o1, Msg o2) {
                int res = (int) (o1.getTime() - o2.getTime());
                return res;
            }
        });
        return source;
    }

    @OnClick(R.id.backUserActivity)
    public void onBackUserActivityClicked() {
        finish();
        Msg msg = msgList.get(msgList.size()-1);

        if(msg.getFromUser().equals(myUsername)){
            ContentValues values = new ContentValues();
            values.put("lastMessage","我:"+msg.getContent());
            LitePal.updateAll(User.class,values,"username = ?",toUsername);
        }
        else{
            ContentValues values = new ContentValues();
            values.put("lastMessage",msg.getFromUser()+":"+msg.getContent());
            LitePal.updateAll(User.class,values,"username = ?",toUsername);
        }
        Intent intent = new Intent(this,MessageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.send)
    public void onSendClicked() {
        String content = inputText.getText().toString();
        if (TextUtils.isEmpty(content))
            return;
        long time = System.currentTimeMillis() / 1000;
        final Msg msg = new Msg(myUsername, toUsername, content, time);
        HttpUtil.insertMsg(addressInsert, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean flag = Boolean.valueOf(response);
                //判断插入是否成功
                if (flag) {
                    Log.d(TAG, "onFinish: success insert");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msgList.add(msg);
                            // 当有新消息时，刷新ListView中的显示
                            adapter.notifyItemInserted(msgList.size() - 1);
                            // 将recyclerview定位到最后一行
                            msgRecyclerview.scrollToPosition(msgList.size() - 1);
                            // 清空输入框中的内容
                            inputText.setText("");
                        }
                    });
                } else {
                    Log.d(TAG, "onFinish:fail insert ");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        }, msg);
    }
}
