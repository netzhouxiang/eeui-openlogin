package app.pb.shop.weixin;

import android.app.Activity;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WeixinManager {

    public static String wx_app_id = "wxeec25d888f630e98";

    Activity context;
    IWXAPI iwxapi;
    WeixinCallback weixinCallback;
    private static WeixinManager manager = null;

    public WeixinManager() {
    }

    public void init(Activity context) {
        this.context = context;
        iwxapi = WXAPIFactory.createWXAPI(context, wx_app_id, true);
        iwxapi.registerApp(wx_app_id);
        manager = this;
    }

    public void setWeixinCallback(WeixinCallback weixinCallback) {
        this.weixinCallback = weixinCallback;
    }

    public WeixinCallback getWeixinCallback() {
        return weixinCallback;
    }

    public static WeixinManager get() {
        return manager;
    }

    public IWXAPI getWxapi() {
        return iwxapi;
    }

    //登录
    public void login() {
        if (isWXAppInstalled()) {
            // send oauth request
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "pb";
            req.transaction = "login";
            iwxapi.sendReq(req);
        } else {
            if (weixinCallback != null) {
                WeixinResult result = new WeixinResult();
                result.setCode(-10);
                result.setMsg("沒有安裝微信");
                weixinCallback.onCallback(result);
            }
        }
    }

    public boolean isWXAppInstalled() {
        return iwxapi.isWXAppInstalled();
    }
}
