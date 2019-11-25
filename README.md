# eeui 安卓  微信登录分享插件 

和官方插件库里的微信支付（eeui-pay）插件兼容  
只支持安卓，用的拿吧  
预计过两天增加微博登录和分享，到时候我再来更新  

使用步骤：  
  
1.在项目根目录执行下面语句：  
```javascript
  eeui plugin create openlogin
```
  
2.下载压缩包后，打开 根目录\plugins\android 文件夹；替换掉 openlogin  

3.用android studio 打开项目，修改包名为你的项目包名，如图：  
![image](https://raw.githubusercontent.com/netzhouxiang/eeui-html/master/1.png)  
![image](https://raw.githubusercontent.com/netzhouxiang/eeui-html/master/2.png)  
![image](https://raw.githubusercontent.com/netzhouxiang/eeui-html/master/3.png)  

4.在项目中，通过以下代码调用：  
```javascript
  const wxlogin = app.requireModule('openlogin');
  wxlogin.login(res=>{
    //res回调
  });
```