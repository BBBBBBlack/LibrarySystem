//package com.bbbbbblack.configuration;
//
//import com.bbbbbblack.dao.BookDao;
//import com.bbbbbblack.domain.entity.Book;
//import com.bbbbbblack.utils.PushUtil;
//import com.getui.push.v2.sdk.api.PushApi;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Configuration
//public class ReturnBookListener implements MessageListener {
//
//    @Autowired
//    BookDao bookDao;
//    @Autowired
//    RedisTemplate<Object, Object> redisTemplate;
//    @Autowired
//    PushApi pushApi;
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        assert pattern != null;
//        String channel = new String(pattern, StandardCharsets.UTF_8);
//        List<String> list = (List<String>) redisTemplate.opsForValue().get("order:" + message);
//        for (String clientId : list) {
//            Book book = bookDao.selectById(message);
//            PushUtil.pushMessage(pushApi,
//                    "预订提醒",
//                    "您订阅的书名为" + book.getTitle() + "的书有人归还", clientId);
//        }
//    }
//}
