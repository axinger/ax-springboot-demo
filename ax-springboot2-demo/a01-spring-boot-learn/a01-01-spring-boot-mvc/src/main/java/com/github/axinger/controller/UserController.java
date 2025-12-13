package com.github.axinger.controller;

import com.alibaba.fastjson2.JSON;
import com.axing.common.response.dto.PageDTO;
import com.axing.common.response.dto.PageResult;
import com.axing.common.response.dto.Result;
import com.github.axinger.config.MyTrackId;
import com.github.axinger.model.bean.AxingerUserProperties;
import com.github.axinger.model.dto.Dog;
import com.github.axinger.model.dto.PersonDTO;
import com.github.axinger.model.dto.UserDTO;
import com.github.axinger.model.dto.UserPojo;
import com.github.axinger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "UserController")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/one")
    public AxingerUserProperties getUser() {
        AxingerUserProperties user = new AxingerUserProperties("jim", "123", new AxingerUserProperties.Dog(1, 2));
//        User user2 = new User();
        String name = user.username();
        System.out.println("name = " + name);

        AxingerUserProperties.range range = new AxingerUserProperties.range(1, 2);

        System.out.println(user.all());
        return user;
    }

    @GetMapping("/person")
    public PersonDTO person() throws InterruptedException {
        Thread.sleep(1000L);

        PersonDTO person = new PersonDTO();
        person.setName("jim");
        return person;
    }

    @GetMapping("/list")
    public Result<List<UserPojo<UserPojo.BookPojo>>> list(@RequestHeader HttpHeaders headers,
                                                          @RequestHeader(value = "token", required = false) String token) throws InterruptedException {
        System.out.println("headers = " + headers);
        System.out.println("token = " + token);


        List<UserPojo.BookPojo> books = new ArrayList<>();
        books.add(UserPojo.BookPojo.builder().id(11L).name("海底两万里").build());

        UserPojo<UserPojo.BookPojo> user1 = new UserPojo<>();
        user1.setId(1L);
        user1.setName("jim");
        user1.setSex(1);
        user1.setData(books);

        return Result.success(List.of(
                user1
        ));
    }

    @Operation(summary = "post分页参数")
    @PostMapping("/page")
    public Result<List<Dog>> person(@RequestBody PageDTO<Dog> dto) {
        System.out.println("dto = " + dto);
        System.out.println(dto.getData().id());
        System.out.println("JSON.toJSONString(dto) = " + JSON.toJSONString(dto));


        PageResult<List<Dog>> result = new PageResult<>();
        result.setSuccess(true);
        result.setData(List.of(dto.getData()));
        result.setPageSize(dto.getPageSize());
        result.setPageNo(dto.getPageNo());
        result.setTotal(100);
        return result;
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        System.out.println("MyTrackId.getId() = " + MyTrackId.getId());

        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
}

