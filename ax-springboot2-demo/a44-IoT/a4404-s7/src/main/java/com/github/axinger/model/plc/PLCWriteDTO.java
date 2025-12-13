package com.github.axinger.model.plc;

import com.github.axinger.model.annotation.PlcField;
import com.github.axinger.model.enums.PlcDataType;
import lombok.Data;

@Data
public class PLCWriteDTO {

    public Integer dbNum;

    /**
     * 完成的焊接程序号,与 154 对应关系,接受>-1后,完成逻辑,再写成-1
     */
    @PlcField(offset = "86", dataType = PlcDataType.WORD)
    public Integer completeLine;


    /**
     * 桁架站点代码
     */
    @PlcField(offset = "150", dataType = PlcDataType.WORD)
    public Integer db150;

    /**
     * 焊接程序号
     */
    @PlcField(offset = "154", dataType = PlcDataType.WORD)
    public Integer db154;

    /**
     * 参数下发通知 1时plc去读取任务，为0plc不读取任务
     */
    @PlcField(offset = "190.0", dataType = PlcDataType.BOOL)
    public Boolean db190;

    /**
     * DB1.DBx190.2	1,3机器人回到零点
     */
    @PlcField(offset = "190.2", dataType = PlcDataType.BOOL)
    public Boolean db190_2;

    /**
     * DB1.DBx190.3	2,4机器人回到零点
     */
    @PlcField(offset = "190.3", dataType = PlcDataType.BOOL)
    public Boolean db190_3;

    /**
     * DB1.DBx190.4	2,4回到1,3的停靠点
     */
    @PlcField(offset = "190.4", dataType = PlcDataType.BOOL)
    public Boolean db190_4;

    /**
     * DB1.DBx191.7	 重置
     */
    @PlcField(offset = "191.2", dataType = PlcDataType.BOOL)
    public Boolean reset;


    /**
     * DB1.DBx191.7	 再启动
     */
    @PlcField(offset = "191.7", dataType = PlcDataType.BOOL)
    public Boolean restart;

//
    /**
     * 任务执行 1时plc执行任务，为0时候plc不执行任务
     */
    @PlcField(offset = "190.1", dataType = PlcDataType.BOOL)
    public Boolean db190_1;


    /**
     * x油泵
     */
    @PlcField(offset = "191.0", dataType = PlcDataType.BOOL)
    public Boolean oilPumpX;

    /**
     * yz油泵打油
     */
    @PlcField(offset = "191.1", dataType = PlcDataType.BOOL)
    public Boolean oilPumpYZ;

    /**
     * 机器人上电
     */
    @PlcField(offset = "191.3", dataType = PlcDataType.BOOL)
    public Boolean robotOn;

    /**
     * 机器人断电
     */
    @PlcField(offset = "191.4", dataType = PlcDataType.BOOL)
    public Boolean robotOff;
    /**
     * 焊机上电
     */
    @PlcField(offset = "191.5", dataType = PlcDataType.BOOL)
    public Boolean weldingMachineOn;

    /**
     * 焊机断电
     */
    @PlcField(offset = "191.6", dataType = PlcDataType.BOOL)
    public Boolean weldingMachineOff;


}
