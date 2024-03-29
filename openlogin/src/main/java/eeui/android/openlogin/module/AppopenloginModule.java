package eeui.android.openlogin.module;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import app.pb.shop.*;
import app.pb.shop.weixin.*;
import app.pb.shop.weibo.*;
import app.pb.shop.douyin.*;
import app.pb.shop.qq.*;
import com.tencent.tauth.Tencent;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class AppopenloginModule extends WXModule implements LoginCallback {
    WeixinManager weixinManager;
    WBManager wbManager;
    DYManager dyManager;
    QQManager qqManager;
    JSCallback jscallback;
    public int t_code = 1;
    //微信登录
    @JSMethod
    public void WeiXinLogin(JSCallback callback) {
        createWeixinManager(callback);
        weixinManager.login();
    }
    //新浪微博登录
    @JSMethod
    public void WeiBoLogin(JSCallback callback) {
        createWBManager(callback);
        t_code = 1;
        wbManager.login();
    }

    //抖音登录
    @JSMethod
    public void DouYinLogin(JSCallback callback) {
        createDYManager(callback);
        dyManager.login();
    }

    //qq登录
    @JSMethod
    public void QQLogin(JSCallback callback) {
        createQQManager(callback);
        t_code = 2;
        qqManager.login();
    }


    private void createQQManager(JSCallback callback) {
        this.jscallback = callback;
        if (qqManager == null) {
            qqManager = new QQManager();
            qqManager.init((Activity) mWXSDKInstance.getContext());
        }
        qqManager.setCallback(this);
    }
    private void createDYManager(JSCallback callback) {
        this.jscallback = callback;
        if (dyManager == null) {
            dyManager = new DYManager();
            dyManager.init((Activity) mWXSDKInstance.getContext());
        }
        dyManager.setCallback(this);
    }
    private void createWeixinManager(JSCallback callback) {
        this.jscallback = callback;
        if (weixinManager == null) {
            weixinManager = new WeixinManager();
            weixinManager.init((Activity) mWXSDKInstance.getContext());
        }
        weixinManager.setCallback(this);
    }
    private void createWBManager(JSCallback callback) {
        this.jscallback = callback;
        if (wbManager == null) {
            wbManager = new WBManager();
            wbManager.init((Activity) mWXSDKInstance.getContext());
        }
        wbManager.setCallback(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (t_code==1 && wbManager!=null && wbManager.mSsoHandler != null) {
            wbManager.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (t_code==2 && qqManager!=null && qqManager.loginListener != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqManager.loginListener);
        }
    }
    @Override
    public void onCallback(LoginResult result) {
        if (jscallback == null) {
            Toast.makeText(mWXSDKInstance.getContext(), "code=" + result.getCode() + ", msg=" + result.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            jscallback.invoke(result);
            jscallback = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
