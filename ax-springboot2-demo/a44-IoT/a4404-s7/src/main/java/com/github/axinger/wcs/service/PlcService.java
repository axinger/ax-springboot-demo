package com.github.axinger.wcs.service;


import com.github.axinger.model.enums.PlcDataType;

public interface PlcService {

//
//    void wirteSetp(int dbCode, List<Integer> lineList);

    void write(Integer dbCode, String offset, Object value);


    boolean write(Integer dbCode, String offset, PlcDataType type, Object value);


    /**
     * 发 发送  DB1.DBW150	桁架站点代码  和 DB1.DBW154	焊接程序号
     *
     * @param dbNo db号
     * @param line 焊缝
     * @return 成功
     */
    boolean sendLine(Integer dbNo, Integer line);

    /**
     * 发送参数确认命令 190.0
     *
     * @param dbNo db号
     * @param line 焊缝
     * @return 成功
     */
    boolean sendParameter(Integer dbNo, Integer line);

    /**
     * 发送 任务执行 190.1, 先判断DB1.DBBX142.0 (发送190.0结果),是否为true,
     *
     * @param dbNo db号
     * @param line 焊缝
     * @return 成功
     */
    boolean sendStart(Integer dbNo, Integer line);

    /**
     * 机器人上电  机器人断电
     *
     * @param db db号
     * @param on true 开机, false关机
     * @return 成功
     */
    boolean robotOn(Integer db, Boolean on);

    /**
     * 焊机上电 焊机断电
     *
     * @param db db号
     * @param on true 开机, false关机
     * @return 成功
     */
    boolean weldingMachineOn(Integer db, Boolean on);


    boolean clearCompleteLine(Integer db);

    /**
     * xyz打油
     *
     * @param dbNo db
     * @return true成功
     */
    boolean sendOilPump(Integer dbNo);

    boolean allInput();

    boolean allOutput();


    boolean isAllOutput();

    boolean reset(Integer db);

    boolean restart(Integer db);
}
