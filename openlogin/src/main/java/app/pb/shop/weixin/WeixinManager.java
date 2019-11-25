package app.pb.shop.weixin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

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
    /**
     * Bitmap转换
     *
     * @param bitmap
     * @param maxkb
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
            options -= 10;
        }
        return output.toByteArray();
    }

    /**
     * 图片转Bitmap
     *
     * @param url 網絡資源圖片
     * @return Bitmap
     */
    public static Bitmap getLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public void share(final String title, final String info, final String iconUrl, final String webUrl, final boolean timeline) {
        if (!isWXAppInstalled()) {
            if (weixinCallback != null) {
                WeixinResult result = new WeixinResult();
                result.setCode(-10);
                result.setMsg("沒有安裝微信");
                weixinCallback.onCallback(result);
            }
        }
        //如果有圖片
        if (iconUrl != null && iconUrl.length() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srcBit = getLocalOrNetBitmap(iconUrl);
                    if (srcBit != null) {
                        Bitmap thumb = Bitmap.createScaledBitmap(srcBit, 120, 120, true);
                        final byte[] bit = bitmap2Bytes(thumb, 32);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                share(title, info, webUrl, timeline, bit);
                            }
                        });
                    }
                }
            }).start();
        } else {
            share(title, info, webUrl, timeline, null);
        }
    }


    private void share(final String title, final String info, final String webUrl, final boolean timeline, byte[] imgbytes) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = info;

        if(imgbytes != null) {
            msg.thumbData = imgbytes;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = "share";
        req.scene = timeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        iwxapi.sendReq(req);
    }


    public boolean isWXAppInstalled() {
        return iwxapi.isWXAppInstalled();
    }
}
