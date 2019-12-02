package app.pb.shop.weibo;

import app.pb.shop.*;
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
    LoginCallback callback;
    Oauth2AccessToken mAccessToken;
    public static WBManager manager = null;
    public WBManager() {
    }
    //周祥 2019年11月26日 14:27:40 初始化
    public void init(Activity context) {
        this.context = context;
        WbSdk.install(context,new AuthInfo(context, LoginConfig.APP_KEY, LoginConfig.REDIRECT_URL, LoginConfig.SCOPE));
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
                        if (callback != null) {
                            LoginResult result = new LoginResult();
                            result.setCode(1);
                            result.setInfo("{\"uid\":"+token.getUid()+",\"access_token\":\""+token.getToken()+"\",\"refresh_token\":\""+token.getRefreshToken()+"\",\"phone_num\":\""+token.getPhoneNum()+"\",\"expires_in\":\""+token.getExpiresTime()+"\"}");
                            result.setMsg("授权成功");
                            callback.onCallback(result);
                        }
                    }
                }
            });
        }
        @Override
        public void cancel() {
            if (callback != null) {
                LoginResult result = new LoginResult();
                result.setCode(0);
                result.setMsg("取消授权");
                callback.onCallback(result);
            }
        }
        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            if (callback != null) {
                LoginResult result = new LoginResult();
                result.setCode(-1);
                result.setMsg("授权失败");
                callback.onCallback(result);
            }
        }
    }
    public void setCallback(LoginCallback callback) {
        this.callback = callback;
    }

    public static WBManager get() {
        return manager;
    }
}
