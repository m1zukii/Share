package com.iu.share.Util;

import android.util.Log;

import com.iu.share.Bean.Comment;
import com.iu.share.Bean.Moment;
import com.iu.share.Bean.Msg;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

// http工具类 用于和servlet做交互
public class HttpUtil {
    public static void searchMoment(final String address,final String search,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("search", search)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void loadMsg(final String address,final String username1,final String username2,final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username1", username1)
                .add("username2", username2)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void queryuser(final String address,final String username,final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void queryContact(final String address,final String phoneNumber,final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber", phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void changeNumber(final String address,final String current,final String username,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()

                .add("current", current)
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void changeUsername(final String address,final String last,final String current,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("last", last)
                .add("current", current)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void handleGood(final String address,final String username,final long publishtime,final boolean flag,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        String temp = "";
        if (flag) {
            temp = "true";
        }
        else{
            temp = "false";
        }
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("publishtime",""+publishtime)
                .add("flag", temp)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void uploadComment(final String address, final String username1, final long publishtime1, final Comment comment,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username1", username1)
                .add("username2", comment.getUsername())
                .add("publishtime1", ""+publishtime1)
                .add("publishtime2", ""+comment.getPublishTime())
                .add("content",comment.getContent())
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void queryMoment1(final String address, final String username,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void queryMoment(final String address, final String type,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("type", type)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void uploadMoment(final String address, final Moment moment, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username",moment.getUsername())
                .add("publishtime",moment.getPublishTime()+"")
                .add("imglists",parseListToString(moment.getImgLists()))
                .add("videolists",parseListToString(moment.getVideoLists()))
                .add("sharetext",moment.getShareText())
                .add("type",moment.getType())
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static String parseListToString(ArrayList<String> list){
        JSONArray array = new JSONArray(list);
        return array.toString();
    }
    public static void register(final String address, final String username, String password, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //查询消息 fromuser 发送的人的用户名 touser 发送到的人的用户名
    public static void queryMsg(final String address, final HttpCallbackListener listener, final String fromUser, final String toUser) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    // connection.setRequestProperty("Content-Type","text/plain;charset=UTF-8");
                    connection.connect();
                    PrintWriter writer = new PrintWriter(connection.getOutputStream());
                    writer.print("fromUser=" + fromUser
                            + "&toUser=" + toUser);
                    writer.flush();
                    writer.close();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //插入消息 msg 插入的消息
    public static void insertMsg(final String address, final HttpCallbackListener listener, final Msg msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.connect();
                    PrintWriter writer = new PrintWriter(connection.getOutputStream());
                    writer.print("content=" + msg.getContent()
                            + "&time=" + msg.getTime()
                            + "&fromUser=" + msg.getFromUser()
                            + "&toUser=" + msg.getToUser());
                    writer.flush();
                    writer.close();
                    DataInputStream inputStream = new DataInputStream(connection.getInputStream());
                    boolean fl = inputStream.readBoolean();
                    String data = String.valueOf(fl);
                    if (listener != null) {
                        listener.onFinish(data);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //根据username用户名查找用户是否存在
    public static void queryUserExists(final String address, final HttpCallbackListener listener, final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.connect();
                    PrintWriter writer = new PrintWriter(connection.getOutputStream());
                    writer.print("username=" + username);
                    writer.flush();
                    writer.close();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //注册 用户名和密码
    public static void register(final String address, final HttpCallbackListener listener, final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.connect();
                    PrintWriter writer = new PrintWriter(connection.getOutputStream());
                    writer.print("username=" + username
                            + "&password=" + password);
                    writer.flush();
                    writer.close();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //用户名 密码登录
    public static void login(final String address, final HttpCallbackListener listener, final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.connect();
                    PrintWriter writer = new PrintWriter(connection.getOutputStream());
                    writer.print("username=" + username
                            + "&password=" + password);
                    writer.flush();
                    writer.close();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void queryUidAvailable(final String address, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).callTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void register1(final String address, final okhttp3.Callback callback, final String uid, final String username, final String password) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).callTimeout(10, TimeUnit.SECONDS).build();
        FormBody formBody = new FormBody.Builder()
                .add("uid", uid)
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
