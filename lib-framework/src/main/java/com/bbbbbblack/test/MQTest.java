package com.bbbbbblack.test;

import com.bbbbbblack.amqp.publisher.CommendPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MQTest {
    @Autowired
    CommendPublisher publisher;

    @RequestMapping("/mqtest")
    public void mqTest() {
//        publisher.sendMessage(1);
    }
}
