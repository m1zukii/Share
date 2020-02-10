package com.iu.share.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.iu.share.Adapter.ContactAdapter;
import com.iu.share.Bean.Contact;
import com.iu.share.Bean.User;
import com.iu.share.R;
import com.iu.share.Util.HttpUtil;
import com.ruffian.library.widget.REditText;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.RTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddContactActivity extends AppCompatActivity {

    @BindView(R.id.backUserActivity)
    RImageView backUserActivity;
    @BindView(R.id.iv_message)
    RTextView ivMessage;
    @BindView(R.id.contactName)
    REditText contactName;
    @BindView(R.id.addContactBtn)
    Button addContactBtn;
    @BindView(R.id.contact_recyclerView)
    RecyclerView contactRecyclerView;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();
    private String addressContact = "http://" + App.ipaddress + ":8080/aa/QueryContact";
    private String addressAdd = "http://" + App.ipaddress + ":8080/aa/QueryUser";
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
        }
        initial();
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        contactRecyclerView.addItemDecoration(divider);
    }

    private static final String TAG = "AddContactActivity";
    private void readContacts() {
        Cursor cursor = null;
        try {
            // 查询联系人数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // 获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    numbers.add(formatString(number));
                    Log.d(TAG, "readContacts: "+number);
                }
                handler.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public String formatString(String str){
        String temp = str.trim();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<temp.length()  ;i++){
            char c = temp.charAt(i);
            if ((c==' ')||(c=='-')) continue;
            builder.append(c);
        }
        return builder.toString();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void initial() {
        adapter = new ContactAdapter(contacts, this);
        contactRecyclerView.setAdapter(adapter);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                adapter.notifyDataSetChanged();
            }
            if (msg.what == 2) {
                JSONArray array = new JSONArray(numbers);
                HttpUtil.queryContact(addressContact, array.toString(), new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String source = response.body().string();
                        final ArrayList<Contact> contact1 = parseResult(source);
                        AddContactActivity.this.contacts.clear();
                        AddContactActivity.this.contacts.addAll(contact1);
                        handler.sendEmptyMessage(1);

                    }
                });
            }
        }
    };

    private ArrayList<Contact> parseResult(String source) {
        JSONArray array = null;
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            array = new JSONArray(source);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String username = object.getString("username");
                String phoneNumber = object.getString("phoneNumber");
                Contact contact = new Contact("", phoneNumber, username);
                contacts.add(contact);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @OnClick(R.id.backUserActivity)
    public void onBackUserActivityClicked() {
        finish();
    }

    @OnClick(R.id.addContactBtn)
    public void onAddContactBtnClicked() {
        String temp = contactName.getText().toString().trim();
        HttpUtil.queryuser(addressAdd, temp, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                if (res.equals("no")) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(AddContactActivity.this);
                    dialog.setTitle("提示")
                            .setMessage("添加失败")
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
                else{
                    int a = res.indexOf("="),b=res.indexOf("&phoneNumber");
                    Log.d(TAG, "onResponse: "+res);
                    String name = res.substring(9,b);
                    int c = res.lastIndexOf("&phoneNumber=")+13;
                    String phoneNumber = res.substring(c);
                    User user = new User(name,phoneNumber,"");
                    user.save();
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(AddContactActivity.this);
                    dialog.setTitle("提示")
                            .setMessage("添加成功")
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
                    List<User> users = LitePal.findAll(User.class);
                    for(int i=0;i<users.size()  ;i++){
                        Log.d(TAG, "onResponse: "+users.get(i));
                    }
                }
            }
        });
    }
}
