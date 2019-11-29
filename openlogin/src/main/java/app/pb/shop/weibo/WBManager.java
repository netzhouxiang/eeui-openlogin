package app.pb.shop.weibo;

import android.app.Activity;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;

public class WBManager {
    Activity context;
    public SsoHandler mSsoHandler;
    WBCallback wbCallback;
    Oauth2AccessToken mAccessToken;
    public static WBManager manager = null;
    public WBManager() {
    }
    //周祥 2019年11月26日 14:27:40 初始化
    public void init(Activity context) {
        this.context = context;
        AuthInfo a = new AuthInfo(context, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        WbSdk.install(context,new AuthInfo(context, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
        mSsoHandler = new SsoHandler(context);
        manager = this;
    }
    public void login() {
        mSsoHandler.authorize(new SelfWbAuthListener());
    }
    public class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                        if (wbCallback != null) {
                            WBResult result = new WBResult();
                            result.setCode(1);
                            result.setInfo("{\"uid\":"+token.getUid()+",\"access_token\":\""+token.getToken()+"\",\"refresh_token\":\""+token.getRefreshToken()+"\",\"phone_num\":\""+token.getPhoneNum()+"\",\"expires_in\":\""+token.getExpiresTime()+"\"}");
                            result.setMsg("授权成功");
                            wbCallback.onWBCallback(result);
                        }
                    }
                }
            });
        }
        @Override
        public void cancel() {
            if (wbCallback != null) {
                WBResult result = new WBResult();
                result.setCode(0);
                result.setMsg("取消授权");
                wbCallback.onWBCallback(result);
            }
        }
        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            if (wbCallback != null) {
                WBResult result = new WBResult();
                result.setCode(-1);
                result.setMsg("授权失败");
                wbCallback.onWBCallback(result);
            }
        }
    }
    public void setWBCallback(WBCallback wbCallback) {
        this.wbCallback = wbCallback;
    }

    public static WBManager get() {
        return manager;
    }
}
