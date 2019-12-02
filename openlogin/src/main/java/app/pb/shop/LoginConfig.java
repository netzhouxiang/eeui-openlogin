package app.pb.shop;

public interface LoginConfig {
    /*
    * 抖音配置
    * */
    public static final String clientkey = "awz0ef0pfbn8twy9";
    /*
     * 微博配置
     * */
    public static final String APP_KEY      = "735879568";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String  SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    /*
     * 微信配置
     * */
    public static final String wx_app_id = "wxeec25d888f630e98";
    /*
     * qq配置  AndroidManifest.xml 里 <data android:scheme="1104848568" /> 同步需要修改
     * */
    public static final String APP_ID = "1104848568";
}
