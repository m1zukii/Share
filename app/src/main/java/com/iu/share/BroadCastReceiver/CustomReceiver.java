package com.iu.share.BroadCastReceiver;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.iu.share.Activity.ShareActivity;

public class CustomReceiver extends BroadcastReceiver {
    private ShareActivity activity;

    public CustomReceiver(ShareActivity activity) {
        this.activity = activity;

    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String message = intent.getStringExtra("message");
        activity.handler.sendEmptyMessage(Integer.parseInt(message));

    }

}
