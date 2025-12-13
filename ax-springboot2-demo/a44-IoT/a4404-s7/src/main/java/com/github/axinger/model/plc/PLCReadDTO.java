package com.github.axinger.model.plc;

import com.github.axinger.model.annotation.PlcField;
import com.github.axinger.model.enums.PlcDataType;
import lombok.Data;

import java.io.Serializable;

@Data
public class PLCReadDTO implements Serializable {
    /**
     * 焊机反馈电压
     */
    @PlcField(offset = "0", dataType = PlcDataType.REAL)
    public Float pressure;

    /**
     * 焊机反馈电流
     */
    @PlcField(offset = "4", dataType = PlcDataType.REAL)
    public Float current;


    /**
     * 机器人J1角度
     */
    @PlcField(offset = "8", dataType = PlcDataType.REAL)
    public Float j1;


    /**
     * 机器人J2角度
     */
    @PlcField(offset = "12", dataType = PlcDataType.REAL)
    public Float j2;

    /**
     * 机器人J3角度
     */
    @PlcField(offset = "16", dataType = PlcDataType.REAL)
    public Float j3;

    /**
     * 机器人J4角度
     */
    @PlcField(offset = "20", dataType = PlcDataType.REAL)
    public Float j4;

    /**
     * 机器人J5角度
     */
    @PlcField(offset = "24", dataType = PlcDataType.REAL)
    public Float j5;

    /**
     * 机器人J6角度
     */
    @PlcField(offset = "28", dataType = PlcDataType.REAL)
    public Float j6;


    /**
     * 桁架x
     */
    @PlcField(offset = "32", dataType = PlcDataType.REAL)
    public Float x;

    /**
     * 桁架y
     */
    @PlcField(offset = "36", dataType = PlcDataType.REAL)
    public Float y;
    /**
     * 桁架z
     */
    @PlcField(offset = "40", dataType = PlcDataType.REAL)
    public Float z;


    /**
     * 桁架站点代码-接受反馈
     */
    @PlcField(offset = "80", dataType = PlcDataType.WORD)
    public Integer dbd80;


    /**
     * 桁架站点代码-接受反馈
     */
    @PlcField(offset = "82", dataType = PlcDataType.WORD)
    public Integer dbd82;

    /**
     * 桁架站点代码-接受反馈
     */
    @PlcField(offset = "84", dataType = PlcDataType.WORD)
    public Integer dbd84;

    /**
     * 完成的焊接程序号,与
     */
    @PlcField(offset = "86", dataType = PlcDataType.WORD)
    public Integer completeLine;


    /**
     * 机器人面板急停
     */
    @PlcField(offset = "140.0", dataType = PlcDataType.BOOL)
    public Boolean dbd140;

    /**
     * 机器人示教器急停
     */
    @PlcField(offset = "140.1", dataType = PlcDataType.BOOL)
    public Boolean dbd140_1;

    /**
     * 焊机故障输出
     */
    @PlcField(offset = "140.2", dataType = PlcDataType.BOOL)
    public Boolean dbd140_2;

    /**
     * 接近开关NC
     */
    @PlcField(offset = "140.3", dataType = PlcDataType.BOOL)
    public Boolean dbd140_3;

    /**
     * 三色灯-红
     */
    @PlcField(offset = "140.4", dataType = PlcDataType.BOOL)
    public Boolean red;

    /**
     * 三色灯-绿
     */
    @PlcField(offset = "140.5", dataType = PlcDataType.BOOL)
    public Boolean green;

    /**
     * 三色灯-黄
     */
    @PlcField(offset = "140.6", dataType = PlcDataType.BOOL)
    public Boolean yellow;

    /**
     * 清枪
     */
    @PlcField(offset = "140.7", dataType = PlcDataType.BOOL)
    public Boolean dbd140_7;


    /**
     * 剪丝
     */
    @PlcField(offset = "141.0", dataType = PlcDataType.BOOL)
    public Boolean dbd141;

    /**
     * 喷油
     */
    @PlcField(offset = "141.1", dataType = PlcDataType.BOOL)
    public Boolean dbd141_1;

    /**
     * x油泵
     */
    @PlcField(offset = "141.2", dataType = PlcDataType.BOOL)
    public Boolean dbd141_2;

    /**
     * yz油泵打油
     */
    @PlcField(offset = "141.3", dataType = PlcDataType.BOOL)
    public Boolean dbd141_3;

    /**
     * 机器人空闲
     */
    @PlcField(offset = "141.6", dataType = PlcDataType.BOOL)
    public Boolean deviceFree;

    /**
     * 任务完成
     */
    @PlcField(offset = "141.7", dataType = PlcDataType.BOOL)
    public Boolean taskComplete;

    /**
     * 参数接收成功
     */
    @PlcField(offset = "142.0", dataType = PlcDataType.BOOL)
    public Boolean dbd142;

    /**
     * 1,3回到原点	DB1.DBBX142.1
     */
    @PlcField(offset = "142.1", dataType = PlcDataType.BOOL)
    public Boolean dbd142_1;


    /**
     * 2,4回到原点	DB1.DBBX142.2
     */
    @PlcField(offset = "142.2", dataType = PlcDataType.BOOL)
    public Boolean dbd142_2;


    /**
     * 2,4在1,3区域	DB1.DBBX142.3
     */
    @PlcField(offset = "142.3", dataType = PlcDataType.BOOL)
    public Boolean dbd142_3;


    /**
     * 混合气体
     */
    @PlcField(offset = "143.5", dataType = PlcDataType.BOOL)
    public Boolean mixedGas;

    /**
     * 压缩空气
     */
    @PlcField(offset = "143.6", dataType = PlcDataType.BOOL)
    public Boolean compressedAir;

    /**
     * 焊丝检测
     */
    @PlcField(offset = "143.7", dataType = PlcDataType.BOOL)
    public Boolean weldingWire;


    /**
     * 机器人通电信号
     */
    @PlcField(offset = "144.0", dataType = PlcDataType.BOOL)
    public Boolean robotPower;

    /**
     * 焊机通电信号
     */
    @PlcField(offset = "144.1", dataType = PlcDataType.BOOL)
    public Boolean weldingMachinePower;

}
