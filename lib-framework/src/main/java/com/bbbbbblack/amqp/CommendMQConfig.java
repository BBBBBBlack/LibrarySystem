package com.bbbbbblack.amqp;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommendMQConfig {
    /**
     * @return DirectExchange
     * 交换机
     * 还未达到发送（过期）时间的消息由delay exchange转发到对应队列
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(Exchanges.DELAY_EXCHANGE);
    }
    /**
     * @return Queue
     * 队列 * 3
     * 分别存放过期时间为1,3,5天的消息
     */
    @Bean
    public Queue oneDayQueue(){
        Map<String,Object> map=new HashMap<>();
        map.put("x-dead-letter-exchange",Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key","one-day");//转发时的routing key
        return new Queue(Queues.DELAY_ONE_QUEUE,true,false,false,map);
    }
    @Bean
    public Queue threeDaysQueue(){
        Map<String,Object> map=new HashMap<>();
        map.put("x-dead-letter-exchange",Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key","three-days");//转发时的routing key
        return new Queue(Queues.DELAY_THREE_QUEUE,true,false,false,map);
    }
    @Bean
    public Queue fiveDaysQueue(){
        Map<String,Object> map=new HashMap<>();
        map.put("x-dead-letter-exchange",Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key","five-days");//转发时的routing key
        return new Queue(Queues.DELAY_FIVE_QUEUE,true,false,false,map);
    }
    /**
     * @return Binding
     * 队列交换机绑定
     */
    @Bean
    public Binding delayOneDayBinding(Queue oneDayQueue, DirectExchange delayExchange){
        return BindingBuilder.bind(oneDayQueue).to(delayExchange).with("one-day");
    }
    @Bean
    public Binding delayThreeDayBinding(Queue threeDaysQueue, DirectExchange delayExchange){
        return BindingBuilder.bind(threeDaysQueue).to(delayExchange).with("three-days");
    }
    @Bean
    public Binding delayFiveDayBinding(Queue fiveDaysQueue, DirectExchange delayExchange){
        return BindingBuilder.bind(fiveDaysQueue).to(delayExchange).with("five-days");
    }
}
