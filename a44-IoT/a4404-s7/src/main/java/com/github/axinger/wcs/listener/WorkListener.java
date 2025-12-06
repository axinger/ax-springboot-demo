package com.github.axinger.wcs.listener;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.github.axinger.model.dto.PlcDbDataVO;
import com.github.axinger.model.dto.WsMessageVO;
import com.github.axinger.model.event.PlcEvent;
import com.github.axinger.model.event.StartWorkEvent;
import com.github.axinger.model.keys.WsCmdKeys;
import com.github.axinger.model.redis.WorkStatus;
import com.github.axinger.wcs.service.PlcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class WorkListener {

    final String dbWorkKey = "db-work";
    @Autowired
    private WorkStatus workStatus;
    @Autowired
    private PlcService plcService;

    private volatile List<PlcDbDataVO> dataList = new ArrayList<>();


    @EventListener(classes = {PlcEvent.class})
    public void handlePlcEvent(PlcEvent event) {

        List<PlcDbDataVO> dataList = event.getDataList();
        this.dataList = dataList;

        List<Integer> workDbList = workStatus.getWorkDbList();
        Boolean workFlag = ObjUtil.isNotEmpty(workDbList);
        if (workFlag) {
            for (PlcDbDataVO dbDataVO : dataList) {
                Integer dbNo = dbDataVO.getDbNo();
                // 有一个完成了
                if (dbDataVO.getCompleteLine() > 0) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("db", dbDataVO.getDbNo());
                    jsonObject.put("alias", dbDataVO.getAlias());
                    jsonObject.put("msg", StrUtil.format("{},焊缝{}完成", dbDataVO.getAlias(), dbDataVO.getCompleteLine()));

                    WsMessageVO<JSONObject> wsMessageVO = new WsMessageVO<>();
                    wsMessageVO.setCode(200);
                    wsMessageVO.setCmd(WsCmdKeys.PLC_TASK_COMPLETE);
                    wsMessageVO.setData(jsonObject);
                    wsMessageVO.setDate(LocalDateTime.now());
//                    SendWebSocket.sendMessage("plc_web", wsMessageVO);
                    plcService.clearCompleteLine(dbNo);

                    workStatus.setCompletedLine(dbNo, dbDataVO.getCompleteLine());
                    //查询是否有下一个工作的
                    // 判断
                    if (dbDataVO.getTaskComplete() || dbDataVO.getDeviceFree()) {
                        workDb(dbNo);
                    }
                }
            }
        }


        //工作状态
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("work", workFlag);
        WsMessageVO<JSONObject> wsMessageVO = new WsMessageVO<>();
        wsMessageVO.setCode(200);
        wsMessageVO.setCmd(WsCmdKeys.PLC_HAS_WORKING);
        wsMessageVO.setDate(LocalDateTime.now());
        wsMessageVO.setData(jsonObject);
//        SendWebSocket.sendMessage("plc_web", wsMessageVO);
    }

    @EventListener(classes = {StartWorkEvent.class})
    public void handleStartWorkEvent(StartWorkEvent event) {


        workStatus.clearAll();

        String projectId = event.getProjectId();
        if (ObjUtil.isEmpty(projectId)) {
            log.error("projectId不能为空");
            return;
        }

        // 开始工作
        for (Integer dbNo : workStatus.getWorkDbList()) {
            workDb(dbNo);
        }

    }

    private void workDb(Integer dbNo) {
        try {
//            redissonLock.lock(dbWorkKey);

            Integer nextWorkLine = workStatus.getNextWorkLine(dbNo);
            if (null == nextWorkLine) {
                log.info("{}没有将要焊接的焊缝", dbNo);
                workStatus.removeCurrentLine(dbNo);
                workStatus.clearFromWorkDbList(dbNo);
                return;
            }

            //发送指令
            plcService.sendLine(dbNo, nextWorkLine);
            plcService.sendParameter(dbNo, nextWorkLine);
            plcService.sendStart(dbNo, nextWorkLine);

            // 存储状态
            workStatus.setCurrentLine(dbNo, nextWorkLine);
//            workStatus.setWorkFlag(dbNo, true);

        } catch (Exception e) {
            log.error("开始工作={}", e.getMessage());
        } finally {
//            redissonLock.unlock(dbWorkKey);
        }
    }
}
