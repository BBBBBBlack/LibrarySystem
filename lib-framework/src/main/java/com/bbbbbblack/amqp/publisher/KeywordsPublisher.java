package com.bbbbbblack.amqp.publisher;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.domain.entity.Search;
import com.bbbbbblack.utils.IKUtil;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KeywordsPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;
    public void sendMessage(Search search){
        Map<String,Object> map=new HashMap<>();
        map.put("userId",SecurityUtil.getNowUserId().toString());
        map.put("record",search);
        rabbitTemplate.convertAndSend(Exchanges.KEYWORDS_EXCHANGE,"record",map);
        if(StringUtils.hasText(search.getKeywords())){
            List<String> tags = IKUtil.getTags(search);
            map.put("record",tags);
            rabbitTemplate.convertAndSend(Exchanges.KEYWORDS_EXCHANGE,"tags",map);
        }
    }
}
