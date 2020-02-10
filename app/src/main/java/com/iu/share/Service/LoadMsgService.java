package com.iu.share.Service;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.iu.share.Activity.App;
import com.iu.share.Bean.Msg;
import com.iu.share.Util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class LoadMsgService extends Service {
    private static final String TAG = "LoadMsgService";
    private String addressLoad = "http://" + App.ipaddress + ":8080/aa/LoadMsg";
    public class MyBinder extends Binder {
        public boolean isFinish = false;
        public LoadMsgService getService() {
            return LoadMsgService.this;
        }

        public boolean isFinish() {
            return isFinish;
        }

        public void setFinish(boolean finish) {
            isFinish = finish;
        }

        public void loadData(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isFinish) {
                        HttpUtil.loadMsg(addressLoad, username1, username2, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                ArrayList<Msg> msgs1 = sortList(parseJSONWithJSONObject(response.body().string()));
                                if (msgs1.size() > 0) {
                                    msgs.clear();
                                    msgs.addAll(msgs1);
                                }
                                Log.d(TAG, "onResponse: ");
                                for(int i=0;i<msgs.size()  ;i++){
                                    Log.d(TAG, "onResponse: "+msgs.get(i));

                                }
                                 Log.d(TAG, "onResponse: ");
                            }
                        });
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
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
    private MyBinder binder = new MyBinder();
    public String username1;
    public String username2;
    private ArrayList<Msg> msgs = new ArrayList<>();
    public LoadMsgService() {
    }
    public String getUsername1() {
        return username1;
    }
    public void setUsername1(String username1) {
        this.username1 = username1;
    }
    public String getUsername2() {
        return username2;
    }
    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public ArrayList<Msg> getMsgs() {
        return msgs;
    }

    public void setMsgs(ArrayList<Msg> msgs) {
        this.msgs = msgs;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binder.setFinish(true);
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
