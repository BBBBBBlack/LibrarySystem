package com.bbbbbblack.utils;

import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *关于个推
 */
public class PushUtil {

    public static Long setTime(Integer days) {
        if (days == 1 || days == 3 || days == 5) {
            return TimeUnit.SECONDS.toMillis(days * 10);
//            return TimeUnit.DAYS.toMillis(1);
        }
//        return TimeUnit.DAYS.toMillis(3);
        return TimeUnit.SECONDS.toMillis(10);
    }

    public static String setRoutingKey(Integer days) {
        if (days == 3) {
            return "three-days";
        } else if (days == 5) {
            return "five-days";
        } else {
            return "one-day";
        }
    }

    public static void pushMessage(PushApi pushApi, String title, String body,String clientId) {
        //根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        /**** 设置个推通道参数 *****/
        PushMessage pushMessage = new PushMessage();
        pushDTO.setPushMessage(pushMessage);
        GTNotification notification = new GTNotification();
        pushMessage.setNotification(notification);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setClickType("url");
        notification.setUrl("https://www.getui.com");
        /**** 设置个推通道参数，更多参数请查看文档或对象源码 *****/

        /**** 设置厂商相关参数 ****/
        PushChannel pushChannel = new PushChannel();
        pushDTO.setPushChannel(pushChannel);
        /*配置安卓厂商参数*/
        AndroidDTO androidDTO = new AndroidDTO();
        pushChannel.setAndroid(androidDTO);
        Ups ups = new Ups();
        androidDTO.setUps(ups);
        ThirdNotification thirdNotification = new ThirdNotification();
        ups.setNotification(thirdNotification);
        thirdNotification.setTitle("厂商title");
        thirdNotification.setBody("厂商body");
        thirdNotification.setClickType("url");
        thirdNotification.setUrl("https://www.getui.com");
        // 两条消息的notify_id相同，新的消息会覆盖老的消息，取值范围：0-2147483647
        // thirdNotification.setNotifyId("11177");
        /*配置安卓厂商参数结束，更多参数请查看文档或对象源码*/

        /*设置ios厂商参数*/
//        IosDTO iosDTO = new IosDTO();
//        pushChannel.setIos(iosDTO);
//        // 相同的collapseId会覆盖之前的消息
//        iosDTO.setApnsCollapseId("xxx");
//        Aps aps = new Aps();
//        iosDTO.setAps(aps);
//        Alert alert = new Alert();
//        aps.setAlert(alert);
//        alert.setTitle("ios title");
//        alert.setBody("ios body");
        /*设置ios厂商参数结束，更多参数请查看文档或对象源码*/

        /*设置接收人信息*/
        Audience audience = new Audience();
        pushDTO.setAudience(audience);
        audience.addCid(clientId);
        /*设置接收人信息结束*/
        /**** 设置厂商相关参数，更多参数请查看文档或对象源码 ****/

        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) {
            // success
            System.out.println(apiResult.getData());
        } else {
            // failed
            System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }
}
