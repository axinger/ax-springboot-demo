package com.github.axinger.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class WsMessageVO<T> {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("data")
    private T data;

    @JsonProperty("dateTime")
    private LocalDateTime date;

    /**
     * 业务类型
     */
    @JsonProperty("cmd")
    private String cmd;
}
