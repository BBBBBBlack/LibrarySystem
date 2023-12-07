package com.bbbbbblack.utils;

import cn.hutool.json.JSONUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.nio.charset.StandardCharsets;

public class CanalConvert implements MessageConverter {
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {

        return new Message(JSONUtil.toJsonStr(o).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        return JSONUtil.parseObj(s);
    }
}
