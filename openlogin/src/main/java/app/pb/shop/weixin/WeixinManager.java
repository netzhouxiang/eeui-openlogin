package app.pb.shop.weixin;

import android.app.Activity;
import app.pb.shop.*;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WeixinManager {
    Activity context;
    IWXAPI iwxapi;
    LoginCallback callback;
    private static WeixinManager manager = null;

    public WeixinManager() {
    }

    public void init(Activity context) {
        this.context = context;
        iwxapi = WXAPIFactory.createWXAPI(context, LoginConfig.wx_app_id, true);
        iwxapi.registerApp(LoginConfig.wx_app_id);
        manager = this;
    }

    public void setCallback(LoginCallback callback) {
        this.callback = callback;
    }

    public LoginCallback getLoginCallback() {
        return callback;
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
            if (callback != null) {
                LoginResult result = new LoginResult();
                result.setCode(-10);
                result.setMsg("沒有安裝微信");
                callback.onCallback(result);
            }
        }
    }

    public boolean isWXAppInstalled() {
        return iwxapi.isWXAppInstalled();
    }
}
