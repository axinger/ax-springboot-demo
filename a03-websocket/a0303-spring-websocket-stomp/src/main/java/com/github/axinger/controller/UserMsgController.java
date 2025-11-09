package com.github.axinger.controller;

import com.alibaba.fastjson2.JSONObject;
import com.github.axinger.pojo.MessageDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@Tag(name = "UserMsgController", description = "发送消息")
@Slf4j
public class UserMsgController {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 根据ID 把消息推送给指定用户
     * 1. 这里用了 @SendToUser 和 返回值 其意义是可以在发送成功后回执给发送放其信息发送成功
     * 2. 非必须，如果实际业务不需要关心此，可以不用@SendToUser注解，方法返回值为void
     * 3. 这里接收人的参数是用restful风格带过来了，websocket把参数带到后台的方式很多，除了url路径，
     * 还可以在header中封装用@Header或者@Headers去取等多种方式
     * //     * @param accountId 消息接收人ID
     *
     * @param value          消息JSON字符串
     * @param headerAccessor
     * @return
     */
//    @MessageMapping("/alone")
//    @SendToUser(value = "/queue/chat", broadcast = false)
//    @Operation(summary = "单点")
//    public String greeting(
//            @RequestBody Message msg,
//            StompHeaderAccessor headerAccessor
//    ) {
//        log.info("服务端收到单点消息");
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("date", LocalDateTime.now());
//        jsonObject.put("type", "发送成功");
//        jsonObject.put("data", msg);
//        template.convertAndSendToUser(msg.getTo(), "/message", msg);
//
//        return jsonObject.toString();
//    }

//    @MessageMapping("/alone")
//    @SendToUser(value = "/queue/reply")
//    @Operation(summary = "单点")
//    public String greeting(
//            @Payload Message msg,
//            Principal principal
//    ) {
//        log.info("服务端收到单点消息 = {}",msg);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("date", LocalDateTime.now());
//        jsonObject.put("type", "发送成功");
//        jsonObject.put("data", msg);
//        template.convertAndSendToUser(msg.getTo(), "/message", msg);
//
//        return jsonObject.toString();
//    }


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    //    因为 @SendToUser 注解的改进允许我们通过 /user/{sessionId}/... 而不是 /user/{user}/... 向目标用户发送消息。
//
//    这意味着该注解依赖于输入消息的 Session ID，从而有效地将回复发送到会话私有的目标位置：
    @MessageMapping("/alone")
    @SendToUser("/hi") // 返回给发送方
    public String processMessageFromClient(
            @Payload MessageDTO messageDTO,
            Principal principal) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", LocalDateTime.now());
        jsonObject.put("type", "发送成功");
        jsonObject.put("data", messageDTO);
        jsonObject.put("principal", principal);
        log.info("jsonObject = {}", jsonObject);
        template.convertAndSendToUser(messageDTO.getTo(), "/message", messageDTO);
        return jsonObject.toString();
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
