package com.github.axinger.wcs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.github.axinger.model.dto.PlcDbDataVO;
import com.github.axinger.model.enums.PlcDataType;
import com.github.axinger.model.event.PlcEvent;
import com.github.axinger.model.plc.PLCBaseReadDTO;
import com.github.axinger.model.plc.PLCReadDTO;
import com.github.axinger.model.plc.PLCWriteDTO;
import com.github.axinger.model.redis.WorkStatus;
import com.github.axinger.wcs.api.PlcFoot;
import com.github.axinger.wcs.job.PlcPool;
import com.github.axinger.wcs.service.PlcService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PlcServiceImpl implements PlcService {

    //    @Autowired
//    RedissonLockClient redissonLock;
    private volatile List<PlcDbDataVO> dataList = new ArrayList<>();
    //    @Resource
//    private RedisTemplate<String, Boolean> redisTemplate;
    @Autowired
    private PlcPool plcPool;
    @Autowired
    private WorkStatus workStatus;

    @EventListener(classes = {PlcEvent.class})
    public void handlePlcEvent(PlcEvent event) {
        dataList = event.getDataList();
    }

    protected PlcFoot foot(Integer dbNo) {
        PlcFoot foot = Optional.ofNullable(plcPool)
                .map(PlcPool::getDataBlockInfoMap)
                .map(val -> val.get(dbNo)).orElse(null);
        if (Objects.isNull(foot)) {
            throw new RuntimeException(StrUtil.format("发送参数确认命令,未找到plc,db{}链接信息", dbNo));
        }
        return foot;
    }

    protected PLCReadDTO readPlc(Integer dbNo) {
        PlcFoot foot = foot(dbNo);
        PLCBaseReadDTO<PLCReadDTO> dto = foot.readClass(PLCReadDTO.class).join();
        PLCReadDTO readDTO = dto.getData();
        if (!Objects.equals(dto.getDbNo(), dbNo)) {
            throw new RuntimeException(StrUtil.format("读取的db号,与参数db号不一致"));
        }
        return readDTO;
    }


    /**
     * 写入 桁架站点代码,焊接程序号
     */

    @Override
    public void write(Integer dbCode, String offset, Object value) {
        try {
            Optional.ofNullable(plcPool)
                    .map(PlcPool::getDataBlockInfoMap)
                    .map(val -> val.get(dbCode)).ifPresentOrElse(info -> {
                        info.write(offset, value);
                    }, () -> {
                        log.error("写入数据错误,未发现PLC链接");
                    });
        } catch (Exception e) {

            log.error("写入数据错误={}", e.getMessage());
        }
    }

    @Override
    public boolean write(Integer dbCode, String offset, PlcDataType type, Object value) {
        try {
            PlcFoot foot = Optional.ofNullable(plcPool)
                    .map(PlcPool::getDataBlockInfoMap)
                    .map(val -> val.get(dbCode)).orElse(null);
            if (Objects.isNull(foot)) {
                throw new RuntimeException(StrUtil.format("写入数据错误未找到plc,db{}链接信息", dbCode));
            }
            return foot.write(offset, type, value);
        } catch (Exception e) {
            log.error("写入数据错误={}", e.getMessage());
            throw new RuntimeException(StrUtil.format("写入数据错误={}", e.getMessage()));
        }
    }


    /**
     * 发送  DB1.DBW150	桁架站点代码  和 DB1.DBW154	焊接程序号
     *
     * @param dbNo db号
     * @param line 焊缝
     * @return 成功
     */
    @Override
    public boolean sendLine(Integer dbNo, Integer line) {

        PLCReadDTO readDTO = readPlc(dbNo);
        if (!canStartTask(readDTO)) {
            throw new RuntimeException(StrUtil.format("dbNo:{}有任务进行中", dbNo));
        }
        PlcFoot foot = foot(dbNo);
        PLCWriteDTO writeDTO = new PLCWriteDTO();
        writeDTO.setDb190(false);
        writeDTO.setDb150(dbNo);
        writeDTO.setDb154(line);
        return foot.writeClassSync(writeDTO);
    }

    /**
     * 发送参数确认命令 190.0
     *
     * @param dbNo db号
     * @param line 焊缝
     * @return 成功
     */
    @Override
    public boolean sendParameter(Integer dbNo, @NotNull Integer line) {
        PLCReadDTO readDTO = readPlc(dbNo);
        if (!canStartTask(readDTO)) {
            throw new RuntimeException(StrUtil.format("dbNo:{}有任务进行中", dbNo));
        }
        PLCWriteDTO writeDTO = new PLCWriteDTO();
        writeDTO.setDb190(true);
        PlcFoot foot = foot(dbNo);
        return foot.writeClassSync(writeDTO);

    }


    /**
     * 发送 任务执行 190.1, 先判断DB1.DBBX142.0 (发送190.0结果),是否为true,
     *
     * @param dbNo db号
     * @param line 焊缝
     * @return 成功
     */
    @Override
    public boolean sendStart(Integer dbNo, Integer line) {

        PLCReadDTO readDTO = readPlc(dbNo);
        if (!canStartTask(readDTO)) {
            throw new RuntimeException(StrUtil.format("dbNo:{}有任务进行中", dbNo));
        }

        if (!Objects.equals(readDTO.getDbd142(), true)) {
            throw new RuntimeException("发送任务执行错误,参数确认命令未发送");
        }


        if (ObjUtil.notEqual(readDTO.getDbd80(), dbNo) || ObjUtil.notEqual(readDTO.getDbd84(), line)) {
            throw new RuntimeException("发送参数确认命令错误,dbNo,line与写入的不一致");
        }


        PLCWriteDTO writeDTO = new PLCWriteDTO();
        writeDTO.setDb190_1(true);
        PlcFoot foot = foot(dbNo);
        boolean b = foot.writeClassSync(writeDTO);

        if (b) {
            workStatus.setCurrentLine(dbNo, line);
            return true;
        }
        return false;
    }

    /**
     * 机器人上电  机器人断电
     *
     * @param db db号
     * @param on true 开机, false关机
     * @return 成功
     */
    @SneakyThrows
    @Override
    public boolean robotOn(Integer db, Boolean on) {

        PLCWriteDTO writeDTO = new PLCWriteDTO();
        PlcFoot foot = foot(db);
        // 先设置false,
        writeDTO.setRobotOn(false);
        writeDTO.setRobotOff(false);
        writeDTO.setWeldingMachineOn(false);
        writeDTO.setWeldingMachineOff(false);

        if (!foot.writeClassSync(writeDTO)) {
            return false;
        }
        TimeUnit.SECONDS.sleep(1);
        foot = foot(db);
        writeDTO = new PLCWriteDTO();
        if (on) {
            writeDTO.setRobotOn(true);
        } else {
            writeDTO.setRobotOff(true);
        }

        if (!foot.writeClassSync(writeDTO)) {
            return false;
        }

        TimeUnit.SECONDS.sleep(1);
        // 重重false
        writeDTO = new PLCWriteDTO();
        foot = foot(db);
        // 先设置false,
        writeDTO.setRobotOn(false);
        writeDTO.setRobotOff(false);
        writeDTO.setWeldingMachineOn(false);
        writeDTO.setWeldingMachineOff(false);
        foot.writeClassSync(writeDTO);
        return true;
    }


    /**
     * 焊机上电 焊机断电
     *
     * @param db db号
     * @param on true 开机, false关机
     * @return 成功
     */
    @SneakyThrows
    @Override
    public boolean weldingMachineOn(Integer db, Boolean on) {

        PLCWriteDTO writeDTO = new PLCWriteDTO();
        PlcFoot foot = foot(db);
        // 先设置false,
        writeDTO.setRobotOn(false);
        writeDTO.setRobotOff(false);
        writeDTO.setWeldingMachineOn(false);
        writeDTO.setWeldingMachineOff(false);

        if (!foot.writeClassSync(writeDTO)) {
            return false;
        }
        TimeUnit.SECONDS.sleep(1);

        writeDTO = new PLCWriteDTO();
        if (on) {
            writeDTO.setWeldingMachineOn(true);
        } else {
            writeDTO.setWeldingMachineOff(true);
        }
        foot = foot(db);

        if (!foot.writeClassSync(writeDTO)) {
            return false;
        }

        TimeUnit.SECONDS.sleep(1);
        // 重重false
        writeDTO = new PLCWriteDTO();
        foot = foot(db);
        // 先设置false,
        writeDTO.setRobotOn(false);
        writeDTO.setRobotOff(false);
        writeDTO.setWeldingMachineOn(false);
        writeDTO.setWeldingMachineOff(false);
        foot.writeClassSync(writeDTO);
        return true;
    }


    /**
     * 不能运行任务
     *
     * @param readDTO
     * @return
     */
    private Boolean canStartTask(PLCReadDTO readDTO) {

        if (Objects.isNull(readDTO.getDeviceFree()) && Objects.isNull(readDTO.getTaskComplete())) {
            return true;
        }

        if (Objects.equals(readDTO.getDeviceFree(), true)) {
            return true;
        }

        return Objects.equals(readDTO.getTaskComplete(), true);
    }


    @Override
    public boolean clearCompleteLine(Integer db) {
        PlcFoot foot = foot(db);
        PLCWriteDTO writeDTO = new PLCWriteDTO();
        writeDTO.setCompleteLine(0);
        return foot.writeClassSync(writeDTO);
    }


    @SneakyThrows
    @Override
    public boolean sendOilPump(Integer dbNo) {

        if (workStatus.isOilPump(dbNo)) {
            throw new RuntimeException(StrUtil.format("{}正在打油,请稍等", dbNo));
        }

        PlcFoot foot = foot(dbNo);
        PLCWriteDTO writeDTO = new PLCWriteDTO();
        writeDTO.setOilPumpX(true);
        writeDTO.setOilPumpYZ(true);
        boolean b = foot.writeClassSync(writeDTO);
        log.info("开始打油={}", b);
        workStatus.setOilPump(dbNo, true);

        TimeUnit.SECONDS.sleep(3);

        writeDTO.setOilPumpX(false);
        writeDTO.setOilPumpYZ(false);
        boolean b1 = foot.writeClassSync(writeDTO);
        log.info("结束打油={}", b1);
        workStatus.setOilPump(dbNo, false);
        return b1;
    }


    @Override
    public boolean allInput() {


        // 先1,3先进入
//        for (Integer dbNo : List.of(1, 3)) {
        for (Integer dbNo : List.of(1)) {
            PlcFoot foot = foot(dbNo);
            PLCWriteDTO writeDTO = new PLCWriteDTO();
            writeDTO.setDb190_2(true);
            foot.writeClassSync(writeDTO);
        }
        boolean b = false;
        while (!b) {
            try {
                b = isInputWith1() && isInputWith3();
                if (!b) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                return false;
            }
        }

        // 2,4回到1,3的停靠点
        for (Integer dbNo : List.of(2, 4)) {
            PlcFoot foot = foot(dbNo);
            PLCWriteDTO writeDTO = new PLCWriteDTO();
            writeDTO.setDb190_4(true);
            foot.writeClassSync(writeDTO);
        }

        boolean b2 = false;


        while (!b2) {
            try {
                b2 = isInputWith2() && isInputWith4();
                if (!b2) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                return false;
            }
        }
        log.info("进仓成功................");

        return true;
    }

    @Override
    public boolean allOutput() {


        //  DB1.DBx190.3	2,4机器人回到零点, 1,3 本来就在原点,也要发一下
        for (Integer dbNo : List.of(2, 4)) {
            PlcFoot foot = foot(dbNo);
            PLCWriteDTO writeDTO = new PLCWriteDTO();
            writeDTO.setDb190_3(true);
            foot.writeClassSync(writeDTO);
        }
        boolean b = false;
        while (!b) {
            try {
                b = isOutputWith2() && isOutputWith4();

                log.info("出仓中:{}", b ? "2,4出仓成功" : "2,4出仓还未成功");
                if (!b) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                return false;
            }
        }

        for (Integer dbNo : List.of(1, 3)) {
            PlcFoot foot = foot(dbNo);
            PLCWriteDTO writeDTO = new PLCWriteDTO();
            writeDTO.setDb190_2(true);
            foot.writeClassSync(writeDTO);
        }

        boolean b2 = false;
        while (!b2) {
            try {
                b2 = isOutputWith1() && isOutputWith3();
                log.info("出仓中:{}", b2 ? "1,3出仓成功" : "1,3出仓还未成功");
                if (!b2) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                return false;
            }
        }

        log.info("出仓成功................");
        ;
        return true;
    }

    @Override
    public boolean isAllOutput() {
        return isOutputWith1() && isOutputWith3() && isOutputWith2() && isOutputWith4();
    }

    @SneakyThrows
    @Override
    public boolean reset(Integer db) {
        {
            PLCWriteDTO writeDTO = new PLCWriteDTO();
            writeDTO.setReset(true);
            PlcFoot foot = foot(db);
            foot.writeClassSync(writeDTO);
        }

        TimeUnit.SECONDS.sleep(1);
        PLCWriteDTO writeDTO = new PLCWriteDTO();
        writeDTO.setReset(false);
        PlcFoot foot = foot(db);
        return foot.writeClassSync(writeDTO);
    }

    @SneakyThrows
    @Override
    public boolean restart(Integer db) {
        {
            PLCWriteDTO writeDTO = new PLCWriteDTO();
            writeDTO.setRestart(true);
            PlcFoot foot = foot(db);
            foot.writeClassSync(writeDTO);
        }

        TimeUnit.SECONDS.sleep(1);
        PLCWriteDTO writeDTO = new PLCWriteDTO();
        writeDTO.setRestart(false);
        PlcFoot foot = foot(db);
        return foot.writeClassSync(writeDTO);
    }

    public boolean isInputWith1() {
        for (PlcDbDataVO vo : dataList) {
            if (ObjUtil.equal(vo.getDbNo(), 1)) {
                return ObjUtil.equal(vo.getDbd142_1(), true);
            }
        }
        return false;
    }

    public boolean isInputWith2() {
        for (PlcDbDataVO vo : dataList) {
            if (ObjUtil.equal(vo.getDbNo(), 2)) {
                return ObjUtil.equal(vo.getDbd142_3(), true);
            }
        }
        return false;
    }

    public boolean isInputWith3() {
//        for (PlcDbDataVO vo : dataList) {
//            if (ObjUtil.equal(vo.getDbNo(), 3)) {
//                return ObjUtil.equal(vo.getDbd142_1(), true);
//            }
//        }
//        return false;
        return true;
    }


    public boolean isInputWith4() {
//        for (PlcDbDataVO vo : dataList) {
//            if (ObjUtil.equal(vo.getDbNo(), 4)) {
//                return ObjUtil.equal(vo.getDbd142_3(), true);
//            }
//        }
//        return false;
        return true;
    }


    public boolean isOutputWith1() {
        for (PlcDbDataVO vo : dataList) {
            if (ObjUtil.equal(vo.getDbNo(), 1)) {
                return ObjUtil.equal(vo.getDbd142_1(), true);
            }
        }
        return false;
    }

    public boolean isOutputWith2() {
        for (PlcDbDataVO vo : dataList) {
            if (ObjUtil.equal(vo.getDbNo(), 2)) {
                return ObjUtil.equal(vo.getDbd142_2(), true);
            }
        }
        return false;
    }


    public boolean isOutputWith3() {
//        for (PlcDbDataVO vo : dataList) {
//            if (ObjUtil.equal(vo.getDbNo(), 3)) {
//                return ObjUtil.equal(vo.getDbd142_1(), true);
//            }
//        }
//        return false;
        return true;
    }


    public boolean isOutputWith4() {
//        for (PlcDbDataVO vo : dataList) {
//            if (ObjUtil.equal(vo.getDbNo(), 2)) {
//                return ObjUtil.equal(vo.getDbd142_2(), true);
//            }
//        }
//        return false;
        return true;
    }
}
