package com.github.axinger.controller;

import cn.hutool.core.io.IoUtil;
import com.github.axinger.vo.TaskVO;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/history")
@RestController
@Slf4j
public class HistoryTaskController {


    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;

    @RequestMapping(value = "/finished")
    public Object finishedList() {
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .finished() // 流程状态
                .processFinished()
                .list();
        List<TaskVO> list = TaskVO.fromHistoricTaskInstance(historicTasks);
        log.info("已经完成的流程列表={}", list);
        return list;
    }

    @RequestMapping(value = "/processFinished")
    public Object processFinished() {
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processFinished()
                .list();
        List<TaskVO> list = TaskVO.fromHistoricTaskInstance(historicTasks);
        log.info("已经完成的流程列表={}", list);
        return list;
    }

    // 待提交的,  正在执行的都算
    @RequestMapping(value = "/processUnfinished")
    public Object processUnfinished() {
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processUnfinished()
                .list();
        List<TaskVO> list = TaskVO.fromHistoricTaskInstance(historicTasks);
        log.info("已经完成的流程列表={}", list);
        return list;
    }

    @RequestMapping(value = "/unfinished")
    public Object unfinished() {

        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .unfinished() // 流程状态
                .list();
        List<TaskVO> list = TaskVO.fromHistoricTaskInstance(historicTasks);


//        List<VariableInstance> getVariableInstancesByExecutionIds(Set<String> executionIds);


        for (TaskVO vo : list) {
            Map<String, Object> variablesLocal = runtimeService.getVariablesLocal(vo.getProcessInstanceId());
            vo.setVariablesLocal(variablesLocal);
        }

        log.info("未完成的流程列表={}", list);
        return list;
    }


    /**
     * 流程申请 流转图片输入流
     */

    @RequestMapping(value = "/img")
    public void genProcessDiagram(HttpServletResponse response, String processId) throws Exception {


        String procDefId;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
            procDefId = historicProcessInstance.getProcessDefinitionId();
        } else {
            procDefId = processInstance.getProcessDefinitionId();
        }


        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        String imageType = "png"; // 生成图片的类型
        List<String> highLightedActivities = new ArrayList<>(); // 高亮节点集合
        List<String> highLightedFlows = new ArrayList<>(); // 高亮连线集合
        List<HistoricActivityInstance> hisActInsList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processId)
                .list(); // 查询所有历史节点信息
        hisActInsList.forEach(historicActivityInstance -> {
            // 遍历
            if ("sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                // 添加高亮连线
                highLightedFlows.add(historicActivityInstance.getActivityId());
            } else {
                // 添加高亮节点
                highLightedActivities.add(historicActivityInstance.getActivityId());
            }
        });


//        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
//        String InstanceId = task.getProcessInstanceId();
//        List<Execution> executions = runtimeService
//                .createExecutionQuery()
//                .processInstanceId(InstanceId)
//                .list();
//        //得到正在执行的Activity的Id
//        List<String> activityIds = new ArrayList<>();
//        List<String> flows = new ArrayList<>();
//        for (Execution exe : executions) {
//            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
//            activityIds.addAll(ids);
//        }


        String activityFontName = "宋体"; // 节点字体
        String labelFontName = "微软雅黑"; // 连线标签字体
        String annotationFontName = "宋体"; // 连线标签字体
        ClassLoader customClassLoader = null; // 类加载器
        double scaleFactor = 1.0d; // 比例因子，默认即可
        boolean drawSequenceFlowNameWithNoLabelDI = true; // 不设置连线标签不会画

        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator(); // 创建默认的流程图生成器
        // 生成图片
        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, imageType, highLightedActivities
                , highLightedFlows, activityFontName, labelFontName, annotationFontName, customClassLoader,
                scaleFactor, drawSequenceFlowNameWithNoLabelDI); // 获取输入流


        response.setContentType("image/png");
        IoUtil.copy(inputStream, response.getOutputStream());
        response.flushBuffer();

    }

    @RequestMapping(value = "/img2")
    public void img2(HttpServletResponse response, String processId) throws Exception {


        String procDefId;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
            procDefId = historicProcessInstance.getProcessDefinitionId();
        } else {
            procDefId = processInstance.getProcessDefinitionId();
        }


        // BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);

        String imageType = "png"; // 生成图片的类型
        // 高亮节点集合
        List<String> pointList = new ArrayList<>();
        // 高亮连线集合
        List<String> lineList = new ArrayList<>();


        List<HistoricActivityInstance> hisActInsList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processId)
                .list(); // 查询所有历史节点信息

        for (HistoricActivityInstance historicActivityInstance : hisActInsList) {
            // 遍历
            if ("sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                // 添加高亮连线
                lineList.add(historicActivityInstance.getActivityId());
            } else {
                // 添加高亮节点
                pointList.add(historicActivityInstance.getActivityId());
            }
        }
        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);

        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();

        // 创建默认的流程图生成器
        // DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
        ProcessDiagramGenerator diagramGenerator = configuration.getProcessDiagramGenerator();
        @Cleanup InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel,
                imageType,
                pointList,
                lineList,
                configuration.getActivityFontName(),
                configuration.getLabelFontName(),
                configuration.getAnnotationFontName(),
                configuration.getClassLoader(),
                1.0,
                true);

        response.setContentType("image/png");
        IoUtil.copy(inputStream, response.getOutputStream());
        response.flushBuffer();

    }


}
