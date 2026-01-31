package com.github.axinger.controller;

import com.github.axinger.model.Book;
import com.github.axinger.model.Order;
import com.github.axinger.model.RedisKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "TestController")
public class TestController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @PostMapping("/sendOrder")
    public Object sendOrder(@RequestBody Order order) {
        redisTemplate.convertAndSend(RedisKeys.PLACE_ORDER_KEY, order);
        return order;
    }


    @PostMapping("/sendStream")
    @Operation(summary = "发送stream")
    public void test2() {
//        String streamKey = "your-stream-name";
//
//        Map<String, String> messageMap = new HashMap<>();
//        messageMap.put("name", "jim");
//
//
//        ObjectRecord<String, Map<String, String>> record = StreamRecords.newRecord()
//                .in(streamKey)
//                .ofObject(messageMap)
////                .withId(RecordId.autoGenerate())
//                ;
//
//        RecordId recordId = redisTemplate.opsForStream().add(record);
//        if (recordId != null) {
//            System.out.println("Message sent to Stream '" + streamKey + "' with RecordId: " + recordId);
//        }


        Book book = new Book();
        book.setId(1);
        book.setName("11111");
        log.info("产生一本书的信息:[{}]", book);

        ObjectRecord<String, Book> record = StreamRecords.newRecord()
                .in("test1111")
                .ofObject(book)
                .withId(RecordId.autoGenerate());

        RecordId recordId = redisTemplate.opsForStream()
                .add(record);
        System.out.println("recordId = " + recordId);
    }


}
