package com.iu.share.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;

import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.ruffian.library.widget.REditText;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeInfoActivity extends AppCompatActivity {

    @BindView(R.id.change_phoneNumber)
    REditText changePhoneNumber;
    @BindView(R.id.change_username)
    REditText changeUsername;
    @BindView(R.id.save_number)
    Button saveNumber;
    @BindView(R.id.save_name)
    Button saveName;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String username = "";
    private String phoneNumber = "";
    private String addressUsername = "http://"+App.ipaddress+":8080/aa/ChangeUsername";
    private String addressNumber = "http://"+App.ipaddress+":8080/aa/ChangeNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        changeUsername.setText(username);
        changePhoneNumber.setText(phoneNumber);
    }




    private void changeName(final String username) {
        HttpUtil.changeUsername(addressUsername, this.username, username, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeInfoActivity.this);
                dialog.setTitle("提示")
                        .setMessage("保存失败")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                if (res.startsWith("success")) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeInfoActivity.this);
                    dialog.setTitle("提示")
                            .setMessage("保存成功")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
                    editor.putString("username",username);
                    ChangeInfoActivity.this.username = username;
                    editor.apply();
                }
                else if (res.startsWith("fail")) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeInfoActivity.this);
                    dialog.setTitle("提示")
                            .setMessage("保存失败")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
                }
            }
        });
    }

    private void changeNumber(final String phoneNumber,final String username) {
        HttpUtil.changeNumber(addressNumber, phoneNumber,username, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                if (res.startsWith("success")) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeInfoActivity.this);
                    dialog.setTitle("提示")
                            .setMessage("保存成功")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
                    editor.putString("phoneNumber",phoneNumber);
                    ChangeInfoActivity.this.phoneNumber = phoneNumber;
                    editor.apply();
                }
                else if (res.startsWith("fail")) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeInfoActivity.this);
                    dialog.setTitle("提示")
                            .setMessage("保存失败")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.save_number)
    public void onSaveNumberClicked() {
        String phoneNumber = changePhoneNumber.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber)) {
            changeNumber(phoneNumber,this.username);
        }
    }

    @OnClick(R.id.save_name)
    public void onSaveNameClicked() {
        String username = changeUsername.getText().toString();
        if (!TextUtils.isEmpty(username)) {
            changeName(username);
        }
    }
}
