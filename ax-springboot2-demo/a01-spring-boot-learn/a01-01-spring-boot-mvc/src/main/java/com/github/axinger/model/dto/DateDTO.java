package com.github.axinger.model.dto;

import lombok.Data;

import java.util.Date;

@Data
@CheckTimeInterval(beginTime = {"beginTime", "start"}, endTime = {"endTime", "end"})
public class DateDTO {

//    @NotBlank(message = "身份证号码不为空")
//    private String idcard;

    private Date beginTime;
    private Date endTime;


//    private LocalDateTime start;
//    private LocalDateTime end;

}
