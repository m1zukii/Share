package com.iu.share.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;


import com.iu.share.R;
import com.iu.share.Util.HttpCallbackListener;
import com.iu.share.Util.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.iv_userName)
    EditText ivUserName;
    @BindView(R.id.iv_userNameClear)
    ImageView ivUserNameClear;
    @BindView(R.id.iv_password)
    EditText ivPassword;
    @BindView(R.id.iv_passwordClear)
    ImageView ivPasswordClear;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.iv_checkbox)
    CheckBox ivCheckbox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String TAG = "LoginActivity";
    private String addressLogin = "http://"+App.ipaddress+":8080/aa/b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initial();
    }
    //初始化
    public void initial() {
        //确定是否记住密码
        boolean isRemember = sharedPreferences.getBoolean("remember_password",false);
        if(isRemember){
            String username = sharedPreferences.getString("username","");
            String password = sharedPreferences.getString("password","");
            ivUserName.setText(username);
            ivPassword.setText(password);
            ivCheckbox.setChecked(true);
        }
        //是否已经登录 如果已经登录则直接跳转至登录界面
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if (isLogin) {
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
        //输入框的清空绑定
        addEditTextListener(ivUserName, ivUserNameClear);
        addEditTextListener(ivPassword, ivPasswordClear);
    }
    public void addEditTextListener(final EditText text, final ImageView imageView) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String str = s + "";
                if (str.length() > 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
            }
        });
    }
    //验证表单为空
    public boolean validateLogin(String username, String password) {
        if ((!TextUtils.isEmpty(username)) && (!TextUtils.isEmpty(password)))
            return true;
        return false;
    }
    //登录按钮
    @OnClick(R.id.btn_login)
    public void onBtnLoginClicked() {
        final String username = ivUserName.getText().toString(),
                password = ivPassword.getText().toString();
        //如果为空 提示用户
        if (!validateLogin(username, password)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("提示")
                    .setMessage("用户名或密码不能为空")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            dialog.show();
            return;
        }
        //否则 登录
        HttpUtil.login(addressLogin, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //用户名密码正确
                if (response.equals("success")) {
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin", true);
                    editor.putString("username", username);


                    //根据是否记住账号密码
                    if(ivCheckbox.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("password",password);
                    }
                    else{
                        editor.putBoolean("remember_password",false);
                        editor.putString("password","");
                    }
                    editor.apply();
                    startActivity(intent);
                    finish();
                }
                //用户名密码不正确
                else if (response.equals("fail")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("提示")
                                    .setMessage("用户名或密码错误")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            dialog.show();
                        }
                    });
                }
            }
            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        }, username, password);
    }
    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

