package com.iu.share.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;

import com.iu.share.R;
import com.iu.share.Util.HttpCallbackListener;
import com.iu.share.Util.HttpUtil;
import com.ruffian.library.widget.RImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//注册页面
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.iv_userName1)
    EditText ivUserName1;
    @BindView(R.id.iv_userNameClear1)
    ImageView ivUserNameClear1;
    @BindView(R.id.iv_password1)
    EditText ivPassword1;
    @BindView(R.id.iv_passwordClear1)
    ImageView ivPasswordClear1;
    @BindView(R.id.btn_register1)
    Button btnRegister1;
    @BindView(R.id.iv_password2)
    EditText ivPassword2;
    @BindView(R.id.iv_passwordClear2)
    ImageView ivPasswordClear2;

    @BindView(R.id.iv_passwordIcon1)
    ImageView ivPasswordIcon1;
    @BindView(R.id.iv_passwordIcon2)
    ImageView ivPasswordIcon2;
    @BindView(R.id.back)
    RImageView back;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String TAG = "RegisterActivity";
    private String addressRegister = "http://" + App.ipaddress + ":8080/aa/r";

    //    private String addressRegister1 = "http://192.168.0.102:8080/aa/rrr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        sharedPreferences = getSharedPreferences("Share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initial();
    }

    public void initial() {
        addEditTextListener(ivUserName1, ivUserNameClear1);
        addEditTextListener(ivPassword1, ivPasswordClear1);
        addEditTextListener(ivPassword2, ivPasswordClear2);
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

    public String validateForm(String username, String password, String passwordVa) {
        if ((!TextUtils.isEmpty(username)) && (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(passwordVa))) {
            if (password.equals(passwordVa))
                return "正确";
            return "两次密码填写不同,请认真填写";
        }
        return "请完整填写表单";
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();



    }

    @OnClick(R.id.btn_register1)
    public void onBtnRegister1Clicked() {


        final ProgressDialog dialog = ProgressDialog.show(this, "提示", "正在注册中，请耐心等待", false, false);
        dialog.show();
        String username = ivUserName1.getText().toString(),
                password = ivPassword1.getText().toString(),
                passwordVa = ivPassword2.getText().toString();
        //验证表单正确性
        final String str = validateForm(username, password, passwordVa);
        if (!str.equals("正确")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(RegisterActivity.this);
                    dialog1.setTitle("提示")
                            .setMessage(str)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    dialog1.show();
                }
            });
            return;
        }
        //发送username,password进行注册
        HttpUtil.register(addressRegister, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                String result = response;
                switch (result) {
                    //注册成功
                    case "success":
                        Log.d(TAG, "onResponse: " + "success");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.cancel();
                                AlertDialog.Builder dialog1 = new AlertDialog.Builder(RegisterActivity.this);
                                dialog1.setTitle("提示")
                                        .setMessage("注册成功")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                dialog1.show();
                            }
                        });
                        break;
                    //注册失败
                    case "fail":
                    case "exist":
                        Log.d(TAG, "onResponse: " + result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.cancel();
                                AlertDialog.Builder dialog2 = new AlertDialog.Builder(RegisterActivity.this);
                                dialog2.setTitle("提示")
                                        .setMessage("注册失败")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                dialog2.show();
                            }
                        });
                        break;
                    default:
                        ;
                }
            }

            @Override
            public void onError(Exception e) {
            }
        }, username, password);
//        HttpUtil.register(addressRegister1,username,password, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: "+e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, "onResponse: "+response.body().string());
//            }
//        });
    }


    @OnClick(R.id.back)
    public void onBackClicked() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
