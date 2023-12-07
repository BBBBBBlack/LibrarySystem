package com.bbbbbblack.configuration;

import java.net.URLEncoder;

public class WechatConfig {
    public static final String WECHAT_TOKEN="bbbbbblackfree";
    //这个url的域名必须在公众号中进行注册验证，这个地址是成功后的回调地址
    public static final String LOGIN_BASEURL="https://i5101b0918.oicp.vip/user/loginByWechat";
    public static final String REGISTER_BASEURL="https://i5101b0918.oicp.vip/user/registerByWechat";
    //appid、secret为自己公众号平台的appid和secret
    public static final String APPID="wxdc69f3789750b9f8";
    public static final String APPSECRET ="64f3f8c4ee5e584f06d7523ec523b040";
//    通过code换取网页授权access_token的url
    public static String getUrl(String code){
        return "https://api.weixin.qq.com/sns/oauth2/access_token?"
                + "appid=" + APPID
                + "&secret=" + APPSECRET
                + "&code=" + code
                + "&grant_type=authorization_code";
    }
    //请求地址  snsapi_base   snsapi_userinfo
    public static String getCodeUrl(String encodedBaseUrl){
        return "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + APPID +
                "&redirect_uri=" + encodedBaseUrl +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect" +
                "&forcePopup=true&forceSnapShot=true";
    }
//    请求微信用户信息url
    public static String getUserInfoUrl(String openid,String accessToken){
        return "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken
                + "&openid=" + openid
                + "&lang=zh_CN";
    }
}
