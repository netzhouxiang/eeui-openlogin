package app.pb.shop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import app.pb.shop.weixin.WeixinCallback;
import app.pb.shop.weixin.WeixinManager;
import app.pb.shop.weixin.WeixinResult;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handleIntent(this.getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.handleIntent(intent);
    }

    protected void handleIntent(Intent intent) {
        WeixinManager manager = WeixinManager.get();
        if (manager != null) {
            IWXAPI api = manager.getWxapi();
            if (api != null) {
                api.handleIntent(intent, this);
            }
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        WeixinManager manager = WeixinManager.get();
        if (manager != null) {
            IWXAPI api = manager.getWxapi();

        }

        this.finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        WeixinManager manager = WeixinManager.get();
        if (manager != null) {
            WeixinCallback weixinCallback = manager.getWeixinCallback();
            if (weixinCallback != null) {
                WeixinResult result = new WeixinResult();
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        String code = ((SendAuth.Resp) baseResp).code;
                        result.setMsg("登录成功");
                        result.setInfo(code);
                        result.setCode(1);
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        result.setCode(-1);
                        result.setMsg("你拒绝了授权");
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        result.setCode(0);
                        result.setMsg("你取消了授权");
                        break;
                    default:
                        result.setCode(-1);
                        result.setMsg("未知原因，登录失败");
                        break;
                }
                weixinCallback.onCallback(result);
            }
        }

        this.finish();
    }
}
