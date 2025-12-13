package com.github.axinger.server;

import com.axing.common.response.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component

// name 或者 name 必须有，
@FeignClient(value = "abc", url = "http://localhost:8080")

public interface UserServiceHttp {

    @GetMapping("/getUserToken")
    Result<Map<String, Object>> getUseToken(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestHeader("token") String token);


}

