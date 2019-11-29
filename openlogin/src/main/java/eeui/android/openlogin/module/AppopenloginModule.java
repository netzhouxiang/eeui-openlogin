package eeui.android.openlogin.module;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import app.pb.shop.weixin.WeixinCallback;
import app.pb.shop.weixin.WeixinManager;
import app.pb.shop.weixin.WeixinResult;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import app.pb.shop.weibo.WBManager;
import app.pb.shop.weibo.WBCallback;
import app.pb.shop.weibo.WBResult;
import app.pb.shop.douyin.*;

public class AppopenloginModule extends WXModule implements WeixinCallback,WBCallback,DYCallback {
    WeixinManager weixinManager;
    WBManager wbManager;
    DYManager dyManager;
    JSCallback jscallback;

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
        wbManager.login();
    }

    //抖音登录
    @JSMethod
    public void DouYinLogin(JSCallback callback) {
        createDYManager(callback);
        dyManager.login();
    }
    private void createDYManager(JSCallback callback) {
        this.jscallback = callback;
        if (dyManager == null) {
            dyManager = new DYManager();
            dyManager.init((Activity) mWXSDKInstance.getContext());
        }
        dyManager.setDYCallback(this);
    }

    private void createWeixinManager(JSCallback callback) {
        this.jscallback = callback;
        if (weixinManager == null) {
            weixinManager = new WeixinManager();
            weixinManager.init((Activity) mWXSDKInstance.getContext());
        }
        weixinManager.setWeixinCallback(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (wbManager!=null && wbManager.mSsoHandler != null) {
            wbManager.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    private void createWBManager(JSCallback callback) {
        this.jscallback = callback;
        if (wbManager == null) {
            wbManager = new WBManager();
            wbManager.init((Activity) mWXSDKInstance.getContext());
        }
        wbManager.setWBCallback(this);
    }
    @Override
    public void onWBCallback(WBResult result) {
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
    @Override
    public void onCallback(WeixinResult result) {
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
    @Override
    public void onDYCallback(DYResult result) {
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
