package com.iu.share.Activity;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.iu.share.R;
import com.yalantis.ucrop.view.UCropView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class ResultActivity extends BaseActivity {
    private static final String TAG = "ResultActivity";
    private static final String CHANNEL_ID = "3000";
    private static final int DOWNLOAD_NOTIFICATION_ID_DONE = 911;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String username = "";
    private String type = "";
    private NotificationManager manager;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResultActivity.this);
                dialog.setTitle(getString(R.string.app_name))
                        .setMessage("上传成功")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(ResultActivity.this, MyActivity.class);
                                startActivity(intent);
                            }
                        });
                dialog.show();
                Notification notification = new Notification.Builder(ResultActivity.this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("图片上传成功")
                        .setTicker("上传成功")
                        .setSmallIcon(R.drawable.save1)
                        .setOngoing(false)
                        .setAutoCancel(true).build();
                manager.notify(1,notification);
            }
            else if (msg.what == 2) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResultActivity.this);
                dialog.setTitle(getString(R.string.app_name))
                        .setMessage("上传失败")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(ResultActivity.this, MyActivity.class);
                                startActivity(intent);
                            }
                        });
                dialog.show();
                Notification notification = new Notification.Builder(ResultActivity.this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("图片上传失败")
                        .setTicker("上传失败")
                        .setSmallIcon(R.drawable.save2)
                        .setOngoing(false)
                        .setAutoCancel(true).build();
                manager.notify(2,notification);
            }
        }
    };
    public static void startWithUri(@NonNull Context context, @NonNull Uri uri,String type) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.setData(uri);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
        manager =  (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
                UCropView uCropView = findViewById(R.id.ucrop);
                uCropView.getCropImageView().setImageUri(uri, null);
                uCropView.getOverlayView().setShowCropFrame(false);
                uCropView.getOverlayView().setShowCropGrid(false);
                uCropView.getOverlayView().setDimmedColor(Color.TRANSPARENT);
            } catch (Exception e) {
                Log.e(TAG, "setImageUri", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        Intent intent = getIntent();
        if (intent!=null) {
            type = intent.getStringExtra("type");
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath(), options);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.format_crop_result_d_d, options.outWidth, options.outHeight));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            saveCroppedImage();
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveCroppedImage();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void saveCroppedImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Uri imageUri = getIntent().getData();
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    copyFileToDownloads(getIntent().getData());
                } catch (Exception e) {
                    Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, imageUri.toString(), e);
                }
            } else {
                Toast.makeText(ResultActivity.this, getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {
        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());
        File saveFile = new File(downloadsDirectoryPath, filename);
        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
        showNotification(saveFile);
        uploadImage(croppedFileUri,filename);
    }
    public void uploadImage(Uri croppedFileUri, String fileName) {
        String path = croppedFileUri.getPath();
        Log.d(TAG, "uploadImage: " + path);
        String mimetype = "";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            mimetype = "image/jpeg";
        } else if (path.endsWith(".png")) {
            mimetype = "image/png";
        } else if (path.endsWith(".gif")) {
            mimetype = "image/gif";
        }
        Log.d(TAG, "uploadImage: "+mimetype);
        Log.d(TAG, "uploadImage: filename"+fileName);
        final File file = new File(croppedFileUri.getPath());
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("type",this.type)
                .addFormDataPart("filename", fileName,
                        RequestBody.create(MediaType.parse(mimetype), file))

                .build();
        String uploadImgAddress = "http://" + App.ipaddress + ":8080/aa/UploadSingleImage";
        Request request = new Request.Builder().url(uploadImgAddress).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               handler.sendEmptyMessage(2);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                if (res.startsWith("success")) {
                    handler.sendEmptyMessage(1);
                }
                else{
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }
    private void showNotification(@NonNull File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri fileUri = FileProvider.getUriForFile(
                this,
                getString(R.string.file_provider_authorities),
                file);
        intent.setDataAndType(fileUri, "image/*");
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resInfoList) {
            grantUriPermission(
                    info.activityInfo.packageName,
                    fileUri, FLAG_GRANT_WRITE_URI_PERMISSION | FLAG_GRANT_READ_URI_PERMISSION);
        }
        NotificationCompat.Builder notificationBuilder;
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(createChannel());
            }
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        notificationBuilder
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_image_saved_click_to_preview))
                .setTicker(getString(R.string.notification_image_saved))
                .setSmallIcon(R.drawable.ic_done)
                .setOngoing(false)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setAutoCancel(true);
        if (notificationManager != null) {
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID_DONE, notificationBuilder.build());
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannel createChannel() {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.channel_name), importance);
        channel.setDescription(getString(R.string.channel_description));
        channel.enableLights(true);
        channel.setLightColor(Color.YELLOW);
        return channel;
    }
}
