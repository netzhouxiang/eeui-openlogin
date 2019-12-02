package app.pb.shop.qq;
import android.app.Activity;
import app.pb.shop.*;
import com.tencent.tauth.*;
import org.json.JSONObject;
public class QQManager {
    Activity context;
    public static Tencent mTencent;
    public static QQManager manager = null;
    LoginCallback callback;
    public IUiListener loginListener;
    public QQManager() {
    }
    //周祥 2019年12月2日 17:30:56
    public void init(Activity context) {
        this.context = context;
        mTencent = Tencent.createInstance(LoginConfig.APP_ID, context);
        manager = this;
    }
    public void login() {
        if (!mTencent.isSessionValid())
        {
            loginListener = new BaseUiListener();
            mTencent.login(context, "all", loginListener);
        }
    }
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                if (callback != null) {
                    LoginResult result = new LoginResult();
                    result.setCode(0);
                    result.setMsg("登录失败");
                    callback.onCallback(result);
                }
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                if (callback != null) {
                    LoginResult result = new LoginResult();
                    result.setCode(0);
                    result.setMsg("登录失败");
                    callback.onCallback(result);
                }
                return;
            }
            if (callback != null) {
                LoginResult result = new LoginResult();
                result.setCode(1);
                result.setInfo(response.toString());
                result.setMsg("登录成功");
                callback.onCallback(result);
            }
        }

        @Override
        public void onError(UiError e) {
            if (callback != null) {
                LoginResult result = new LoginResult();
                result.setCode(-1);
                result.setMsg("授权失败");
                callback.onCallback(result);
            }
        }

        @Override
        public void onCancel() {
            if (callback != null) {
                LoginResult result = new LoginResult();
                result.setCode(0);
                result.setMsg("取消授权");
                callback.onCallback(result);
            }
        }
    }
    public void setCallback(LoginCallback callback) {
        this.callback = callback;
    }

    public static QQManager get() {
        return manager;
    }
}