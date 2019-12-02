package app.pb.shop.tiktokapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import app.pb.shop.*;
import app.pb.shop.douyin.*;
import com.bytedance.sdk.open.aweme.TikTokConstants;
import com.bytedance.sdk.open.aweme.TikTokOpenApiFactory;
import com.bytedance.sdk.open.aweme.api.TikTokApiEventHandler;
import com.bytedance.sdk.open.aweme.api.TiktokOpenApi;
import com.bytedance.sdk.open.aweme.authorize.model.Authorization;

import com.bytedance.sdk.open.aweme.common.model.BaseReq;
import com.bytedance.sdk.open.aweme.common.model.BaseResp;

public class TikTokEntryActivity extends Activity implements TikTokApiEventHandler {

    TiktokOpenApi ttOpenApi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttOpenApi = TikTokOpenApiFactory.create(this, TikTokConstants.TARGET_APP.AWEME);
        ttOpenApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == TikTokConstants.ModeType.SEND_AUTH_RESPONSE) {
            // 授权成功可以获得authCode
            DYManager manager = DYManager.get();
            if (manager != null) {
                LoginCallback callback = manager.getLoginCallback();
                if (callback != null) {
                    LoginResult result = new LoginResult();
                    switch (resp.errorCode) {
                        case 0:
                            Authorization.Response response = (Authorization.Response) resp;
                            result.setMsg("登录成功");
                            result.setInfo(response.authCode);
                            result.setCode(1);
                            break;
                        case -2:
                            result.setCode(0);
                            result.setMsg("你取消了授权");
                            break;
                        default:
                            result.setCode(-1);
                            result.setMsg(resp.errorMsg);
                            break;
                    }
                    callback.onCallback(result);
                }
            }
        }
        finish();
    }

    @Override
    public void onErrorIntent(Intent intent) {
        // 错误数据
        Toast.makeText(this, "授权失败，Intent出错", Toast.LENGTH_LONG).show();
        finish();
    }
}
