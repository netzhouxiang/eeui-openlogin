package eeui.android.openlogin.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import app.pb.shop.weixin.WeixinCallback;
import app.pb.shop.weixin.WeixinManager;
import app.pb.shop.weixin.WeixinResult;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class AppopenloginModule extends WXModule implements WeixinCallback {
    WeixinManager weixinManager;
    JSCallback jscallback;

    @JSMethod
    public void init(String weixinAppKey) {
        WeixinManager.wx_app_id = weixinAppKey;
    }

    @JSMethod
    public void isInstalled(JSCallback callback) {
        createWeixinManager(callback);
        boolean install = weixinManager.isWXAppInstalled();
        WeixinResult result = new WeixinResult();
        result.setCode(install ? 1 : -1);
        result.setMsg(install ? "已安裝" : "未安裝");
        onCallback(result);
    }

    @JSMethod
    public void login(JSCallback callback) {
        createWeixinManager(callback);
        weixinManager.login();
    }

    @JSMethod
    public void share(String params, JSCallback callback) {
        //String title, String info, String webUrl, String iconUrl, String timeline;

        Log.e("axa", "params="+params);

        com.alibaba.fastjson.JSONObject shareJson = com.alibaba.fastjson.JSONObject.parseObject(params);

        Log.e("axa", shareJson.toString());

        createWeixinManager(callback);
        try {
            weixinManager.share(shareJson.getString("title"),
                    shareJson.getString("info"),
                    shareJson.getString("iconUrl"),
                    shareJson.getString("webUrl"), "1".equals(shareJson.getString("timeline")));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    /**
     * 简单演示
     * @param msg
     */
    @JSMethod
    public void simple(String msg) {
        Toast.makeText(mWXSDKInstance.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 回调演示
     * @param msg
     * @param callback
     */
    @JSMethod
    public void call(final String msg, final JSCallback callback) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(mWXSDKInstance.getContext());
        localBuilder.setTitle("demo");
        localBuilder.setMessage(msg);
        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.invoke("返回：" + msg); //多次回调请使用invokeAndKeepAlive
                }
            }
        });
        AlertDialog dialog = localBuilder.setCancelable(false).create();
        dialog.show();
    }

    /**
     * 同步返回演示
     * @param msg
     * @return
     */
    @JSMethod(uiThread = false)
    public String retMsg(String msg) {
        return "返回：" + msg;
    }
}
