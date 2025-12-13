package com.github.axinger.model.event;

import com.github.axinger.model.dto.PlcDbDataVO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class PlcEvent extends ApplicationEvent {
    List<PlcDbDataVO> dataList;

    public PlcEvent(List<PlcDbDataVO> source) {
        super(source);
        this.dataList = source;
    }

}
