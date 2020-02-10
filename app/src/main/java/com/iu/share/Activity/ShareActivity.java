package com.iu.share.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.iu.share.Adapter.MediaAdapter;
import com.iu.share.Bean.Comment;
import com.iu.share.Bean.Moment;
import com.iu.share.BroadCastReceiver.CustomReceiver;
import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.iu.share.Util.StatusBarUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShareActivity extends AppCompatActivity implements MediaAdapter.OnAddMediaListener {
    @BindView(R.id.share_cancel)
    Button shareCancel;
    @BindView(R.id.share_publish)
    Button sharePublish;
    @BindView(R.id.share_text)
    EditText shareText;
    @BindView(R.id.share_recycler_view)
    RecyclerView shareRecyclerView;
    @BindView(R.id.putong)
    RadioButton putong;
    @BindView(R.id.zhengzhi)
    RadioButton zhengzhi;
    @BindView(R.id.jingji)
    RadioButton jingji;
    @BindView(R.id.yule)
    RadioButton yule;
    @BindView(R.id.tiyu)
    RadioButton tiyu;

    @BindView(R.id.diqu)
    RadioButton diqu;
    @BindView(R.id.youxi)
    RadioButton youxi;
    @BindView(R.id.gaoxiao)
    RadioButton gaoxiao;
    @BindView(R.id.xuexi)
    RadioButton xuexi;
    @BindView(R.id.mingxing)
    RadioButton mingxing;

    @BindView(R.id.radiogroup1)
    RadioGroup radiogroup1;
    @BindView(R.id.radiogroup2)
    RadioGroup radiogroup2;
    @BindView(R.id.yinyue)
    RadioButton yinyue;
    @BindView(R.id.dianying)
    RadioButton dianying;

    private int currentButtonId;
    private int REQUEST_CODE = 0x000111;
    private MediaAdapter mediaAdapter;

    private static final String TAG = "ShareActivity";
    private String username = "";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int SUCCESS_MESSAGE = 1;
    private int ERROR_MESSAGE = 2;
    private RadioButton[] buttons = new RadioButton[]{putong, zhengzhi, jingji, tiyu, dianying,
            yinyue, xuexi, youxi, gaoxiao, mingxing, yule, diqu};
    private NotificationManager manager;
    private CustomReceiver receiver;
    private IntentFilter filter;
    private String actionStr = "com.iu.share.CustomBroadcast";
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS_MESSAGE) {
                //Toast.makeText(ShareActivity.this, "发布动态成功", Toast.LENGTH_SHORT).show();
                showDialog("成功发布动态").setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mediaAdapter.clearData();
                        finish();
                    }
                }).show();
                Notification notification = new Notification.Builder(ShareActivity.this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("发布动态成功")
                        .setTicker("发布成功")
                        .setSmallIcon(R.drawable.save1)
                        .setOngoing(false)
                        .setAutoCancel(true).build();
                manager.notify(1,notification);

            } else if (msg.what == ERROR_MESSAGE) {
                //Toast.makeText(ShareActivity.this, "发布动态失败", Toast.LENGTH_SHORT).show();
                showDialog("发布动态失败").setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                Notification notification = new Notification.Builder(ShareActivity.this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("发布动态失败")
                        .setTicker("发布失败")
                        .setSmallIcon(R.drawable.save2)
                        .setOngoing(false)
                        .setAutoCancel(true).build();
                manager.notify(1,notification);
            }
        }
    };
    public AlertDialog.Builder showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage(message);
        return builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initial();
        StatusBarUtil.immersive(this);
        manager =  (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        filter = new IntentFilter();
        filter.addAction(actionStr);
        receiver = new CustomReceiver(this);
        registerReceiver(receiver,filter);
    }

    public void initial() {

        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
        shareRecyclerView.setLayoutManager(new GridLayoutManager(ShareActivity.this, 4, GridLayoutManager.VERTICAL, false));
        if (mediaAdapter == null) {
            mediaAdapter = new MediaAdapter(ShareActivity.this);
        }
        shareRecyclerView.setAdapter(mediaAdapter);
        mediaAdapter.setOnItemClickListener(new MediaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (mediaAdapter.getData().size() > 0) {
                    //预览
                    Phoenix.with()
                            .pickedMediaList(mediaAdapter.getData())
                            .start(ShareActivity.this, PhoenixOption.TYPE_BROWSER_PICTURE, 0);
                }
            }
        });
        putong.setChecked(true);
        currentButtonId = R.id.putong;
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton)findViewById(currentButtonId);
                button.setChecked(false);
                currentButtonId = checkedId;
            }
        });
        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton)findViewById(currentButtonId);
                button.setChecked(false);
                currentButtonId = checkedId;
            }
        });
    }

    public String getType() {
        RadioButton radioButton = (RadioButton)findViewById(currentButtonId);
        return radioButton.getText().toString();
    }

    @OnClick(R.id.share_cancel)
    public void onShareCancelClicked() {
        Intent intent = new Intent(ShareActivity.this, HomePageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.share_publish)
    public void onSharePublishClicked() {
        Calendar calendar = Calendar.getInstance();
        long publishTime = calendar.getTime().getTime()/1000;
        String sharetext = shareText.getText().toString();
        ArrayList<String> imgLists = new ArrayList<String>();
        ArrayList<String> videoLists = new ArrayList<String>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        List<MediaEntity> results = mediaAdapter.getData();
        String path;
        File file;
        String fileName;
        MediaEntity mediaEntity;
        int fileType;
        String type = getType();
        if(sharetext.length()==0&&results.size()==0){
            showDialog("内容和图片(视频)不能全为空，发布动态失败").setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
//            Intent intent = new Intent(actionStr);
//            intent.putExtra("message","内容和图片(视频)不能全为空，发布动态失败");
//            sendBroadcast(intent);
            return;

        }
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        for (int i = 0; i < results.size(); i++) {
            mediaEntity = results.get(i);
            fileType = mediaEntity.getFileType();
            if(fileType == 1){
                list1.add("1");
            }
            else if (fileType == 2) {
                list2.add("2");
            }
        }
        if((list1.size()*list2.size())!=0){
            showDialog("图片和视频只能选其中一种，发布动态失败").setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
//            Intent intent = new Intent(actionStr);
//            intent.putExtra("message","图片和视频只能选其中一种，发布动态失败");
//            sendBroadcast(intent);
            return;
        }
        for (int i = 0; i < results.size(); i++) {
            mediaEntity = results.get(i);
            path = mediaEntity.getFinalPath();
            file = new File(path);
            fileName = file.getName();
            fileType = mediaEntity.getFileType();
            if (fileType == 1) {
                imgLists.add(fileName);
            } else if (fileType == 2) {
                videoLists.add(fileName);
            }
            Log.d(TAG, "onSharePublishClicked: "+mediaEntity.getMimeType());

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("filename", fileName,
                            RequestBody.create(MediaType.parse(mediaEntity.getMimeType()), file))
                    .build();
            String uploadImgAddress = "http://" + App.ipaddress + ":8080/aa/bgbg";
            Request request = new Request.Builder().url(uploadImgAddress).post(requestBody).build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(2);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        }
        Moment moment = new Moment(username, sharetext, publishTime, videoLists, imgLists, comments, type,0);
        String uploadMomentAddress = "http://" + App.ipaddress + ":8080/aa/UploadMoment";
        HttpUtil.uploadMoment(uploadMomentAddress, moment, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Intent intent = new Intent(actionStr);
                intent.putExtra("message","2");
                sendBroadcast(intent);
//                handler.sendEmptyMessage(2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ss = response.body().string();
                Log.d(TAG, "onResponse:1" + ss);
                if (ss.startsWith("success")) {
                    Log.d(TAG, "onResponse: 2");
                    Intent intent = new Intent(actionStr);
                    intent.putExtra("message","1");
                    sendBroadcast(intent);
//                    handler.sendEmptyMessage(1);
                } else if (ss.startsWith("fail")) {
                    Intent intent = new Intent(actionStr);
                    intent.putExtra("message","2");
                    sendBroadcast(intent);
//                    handler.sendEmptyMessage(2);
                }

                Log.d(TAG, "onResponse: 5");
            }
        });
//        HttpUtil.uploadMoment(uploadMomentAddress,moment, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: "+e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });
    }

    @Override
    public void onaddMedia() {
        Phoenix.with()
                .theme(PhoenixOption.THEME_BLUE)// 主题
                .fileType(MimeType.ofAll())//显示的文件类型图片、视频、图片和视频
                .maxPickNumber(20)// 最大选择数量
                .minPickNumber(0)// 最小选择数量
                .spanCount(3)// 每行显示个数
                .enablePreview(true)// 是否开启预览
                .enableCamera(true)// 是否开启拍照
                .enableAnimation(true)// 选择界面图片点击效果
                .enableCompress(true)// 是否开启压缩
                .compressPictureFilterSize(1024)//多少kb以下的图片不压缩
                .compressVideoFilterSize(2018)//多少kb以下的视频不压缩
                .thumbnailHeight(160)// 选择界面图片高度
                .thumbnailWidth(160)// 选择界面图片宽度
                .enableClickSound(false)// 是否开启点击声音
                .pickedMediaList(mediaAdapter.getData())// 已选图片数据
                .videoFilterTime(30)//显示多少秒以内的视频
                .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                .start(ShareActivity.this, PhoenixOption.TYPE_PICK_MEDIA, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //返回的数据
            List<MediaEntity> result = Phoenix.result(data);
            mediaAdapter.setData(result);
        }
    }

}
