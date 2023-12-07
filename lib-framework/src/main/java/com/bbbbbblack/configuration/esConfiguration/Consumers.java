package com.bbbbbblack.configuration.esConfiguration;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.utils.ESUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Consumers {

    @Autowired
    ESUtil esUtil;

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.CANAL_QUEUE1),
            exchange = @Exchange(name = Exchanges.CANAL_EXCHANGE),
            key = {"example"}
    )})
    public void getCanalMessage1(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        JSONObject result = JSONUtil.parseObj(msg);
        List<Object> books = (List<Object>) result.get("data");
        List<JSONObject> bookList=new ArrayList<>();
        for (Object book : books) {
            ParserConfig config = new ParserConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            Book book1 = JSON.parseObject(JSONUtil.toJsonStr(book), Book.class, config);
            JSONObject parseObj = JSONUtil.parseObj(book);
            parseObj.append("title_suggest", book1.getTitle());
            parseObj.append("author_suggest", book1.getAuthor());
            bookList.add(parseObj);
        }
        String type = (String) result.get("type");
        String database = result.get("database") + "_" + result.get("table");
        try {
            switch (type) {
                case "INSERT":
                    esUtil.bulkInsert(database, bookList);
                    break;
                case "DELETE":
                    esUtil.delDocByIds(database, bookList);
                    break;
                case "UPDATE":
                    esUtil.bulkUpdate(database, bookList);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.CANAL_QUEUE2),
            exchange = @Exchange(name = Exchanges.CANAL_EXCHANGE),
            key = {"example"}
    )})
    public void getCanalMessage2(String msg) throws IOException, SQLException {
        JSONObject result = JSONUtil.parseObj(msg);
        List<Object> books = (List<Object>) result.get("data");
//        for (Object book : books) {
//            ParserConfig config=new ParserConfig();
//            config.propertyNamingStrategy=PropertyNamingStrategy.SnakeCase;
//            Book book1 = JSON.parseObject(JSONUtil.toJsonStr(book), Book.class, config);
//            System.out.println("1"+book1);
//        }
    }
}
