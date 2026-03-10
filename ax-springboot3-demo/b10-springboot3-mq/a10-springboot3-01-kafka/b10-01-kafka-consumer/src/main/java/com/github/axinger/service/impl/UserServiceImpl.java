package com.github.axinger.service.impl;

import com.github.axinger.api.Topic;
import com.github.axinger.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    /**
     * 初始化消息队列，发送之后暂时保存在list中，然后最早头部读取 earliest
     */
    private static final LinkedList<Long> linkedList = new LinkedList<>();
    private static final String content = "";

    /**
     * groupId 不同, 同一个主题都能收到
     * groupId 相同,只能有一个收到主题消息
     */
//    @KafkaListener(topics = {com.ax.kafka.api.Topic.SIMPLE})
//    public void consumer(ConsumerRecord<?,?> consumerRecord){
//        //判断是否为null
//        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
//
//        if(kafkaMessage.isPresent()){
//            //得到Optional实例中的值
//            Object message = kafkaMessage.get();
//            System.err.println("消费消息:"+message);
//        }
//    }
    @Override
    public String getMsg() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.114:9092");
        props.put("group.id", "group1");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        // latest 最新  earliest 最早
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);
        // 指定订阅的topic
        kafkaConsumer.subscribe(List.of(Topic.SIMPLE));
        String content = "";
//        while (true) {
        log.info("nothing available...");

        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1000));
        for (ConsumerRecord<String, String> record : records) {
            UserServiceImpl.log.info("获取消息详情，{}", record.value());
            content = record.value();
            if (content != "") {
                // XyKafkaOutMsg build = new XyKafkaOutMsg();
                // build.setGmtCreate(new Date());
                // build.setFwBh(123L);
                // int insertOutMsg = xyKafkaOutMsgMapper.insertSelective(build);
                // log.info("insertOutMsg result,{}",insertOutMsg==1 ? "成功" : "失败");
                return content;
            }
        }
//        }
        return content;
    }

    @Override
    public synchronized String getMsg2() {
        /*log.info("队列消息list，{}",linkedList);
        while (true){
            if (CollectionUtils.isEmpty(linkedList)){
                log.info("任务队列为空");
                return null;
            }
//            XyKafkaOutMsg outMsg = xyKafkaOutMsgMapper.selectByPrimaryKey(linkedList.getFirst());
            if (outMsg==null){
                log.info("获取档案对象失败");
                //移除队列中数据
                log.info("队列移除的元素={}",linkedList.getFirst());
                linkedList.remove(linkedList.getFirst());
            }else if (RunStatus.SAY_NO.equals(outMsg.getDealFlag())){
                log.info("队列获取消息，总数={}，本条消息档案号={}",linkedList.size(),outMsg.getFwBh().toString());
                log.info("队列移除的元素={}",linkedList.getFirst());
                linkedList.remove(linkedList.getFirst());
                return outMsg.getFwBh().toString();
            }else if (RunStatus.SAY_YES.equals(outMsg.getDealFlag())){
                log.info("已经消费过");
                log.info("队列移除的元素={}",linkedList.getFirst());
                linkedList.remove(linkedList.getFirst());
            }
        }*/

        return null;
    }


    @Override
    public void resolve(Long fuBh) {
   /*     XyKafkaOutMsg outMsg =new  XyKafkaOutMsg();
        outMsg.setFwBh(fuBh);
        outMsg.setDealFlag(new Byte("1"));
        int updateStatus = xyKafkaOutMsgMapper.updateByFwBh(outMsg);
        log.info("insertOutMsg result,{}",updateStatus==1 ? "成功" : "失败");*/
    }

}
