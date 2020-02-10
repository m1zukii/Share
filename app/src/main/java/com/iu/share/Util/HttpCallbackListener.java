package com.iu.share.Util;
//回调接口

import android.os.Handler;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}