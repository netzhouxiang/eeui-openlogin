package app.pb.shop.douyin;
import android.app.Activity;
import app.pb.shop.*;
import com.bytedance.sdk.open.aweme.TikTokOpenApiFactory;
import com.bytedance.sdk.open.aweme.TikTokOpenConfig;
import com.bytedance.sdk.open.aweme.api.TiktokOpenApi;
import com.bytedance.sdk.open.aweme.authorize.model.Authorization;
import com.bytedance.sdk.open.aweme.TikTokConstants;

public class DYManager{
    Activity context;
    public static DYManager manager = null;
    TiktokOpenApi bdOpenApi;
    LoginCallback callback;
    public DYManager() {
    }
    //周祥 2019年11月29日 17:30:56 初始化
    public void init(Activity context) {
        this.context = context;
        TikTokOpenApiFactory.init(new TikTokOpenConfig(LoginConfig.clientkey));
        bdOpenApi = TikTokOpenApiFactory.create(context, TikTokConstants.TARGET_APP.AWEME);
        manager = this;
    }
    public void login() {
        Authorization.Request request = new Authorization.Request();
        request.scope = "user_info";                          // 用户授权时必选权限
        request.state = "pb";                                   // 用于保持请求和回调的状态，授权请求后原样带回给第三方。
        bdOpenApi.authorize(request);
    }
    public void setCallback(LoginCallback callback) {
        this.callback = callback;
    }
    public LoginCallback getLoginCallback() {
        return callback;
    }

    public static DYManager get() {
        return manager;
    }
}
