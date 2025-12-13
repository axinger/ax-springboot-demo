package com.github.axinger.model.dto;

import com.github.axinger.model.plc.PLCReadDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PlcDbDataVO extends PLCReadDTO implements Serializable {

    public Date date;
    public Integer dbNo;
    public String deviceName;
    public String alias;

    private Boolean on = true;
    private String working;

    @SuppressWarnings("all")
    public Boolean getOn() {
        return Objects.equals(robotPower, Boolean.TRUE) && Objects.equals(weldingMachinePower, Boolean.TRUE);
    }

    @SuppressWarnings("all")
    public String getWorking() {
        if (Objects.isNull(deviceFree) || Objects.isNull(taskComplete) || deviceFree || taskComplete) {
            return "空闲";
        }
        return "忙碌";
    }
}
