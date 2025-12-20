package com.github.axinger.controller.api;

import cn.hutool.core.io.IoUtil;
import com.github.axinger.service.FlowableService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程实例控制器
 * 展示Flowable流程实例相关操作
 */
@RestController
@RequestMapping("/api/process-instance")
public class ProcessInstanceController {

    @Autowired
    private FlowableService flowableService;

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key
     * @param variables            流程变量
     * @return 流程实例
     */
    @PostMapping("/start")
    public Map<String, Object> startProcessInstance(@RequestParam String processDefinitionKey,
                                                    @RequestParam(required = false) String businessKey,
                                                    @RequestBody(required = false) Map<String, Object> variables) {
        Map<String, Object> result = new HashMap<>();
        try {
            ProcessInstance processInstance;
            if (businessKey != null) {
                processInstance = flowableService.startProcessInstance(processDefinitionKey, businessKey, variables);
            } else {
                processInstance = flowableService.startProcessInstance(processDefinitionKey, variables);
            }

            result.put("success", true);
            result.put("processInstanceId", processInstance.getId());
            result.put("processDefinitionId", processInstance.getProcessDefinitionId());
            result.put("processDefinitionKey", processInstance.getProcessDefinitionKey());
            result.put("businessKey", processInstance.getBusinessKey());
            result.put("isEnded", processInstance.isEnded());
            result.put("isSuspended", processInstance.isSuspended());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取流程实例列表
     *
     * @return 流程实例列表
     */
    @GetMapping("/list")
    public List<?> getProcessInstances() {
        List<ProcessInstance> list = flowableService.createProcessInstanceQuery().list();
        return list.stream().map(processInstance -> {
            Map<String, Object> processInstanceMap = new HashMap<>();
            processInstanceMap.put("id", processInstance.getId());
            processInstanceMap.put("processDefinitionId", processInstance.getProcessDefinitionId());
            processInstanceMap.put("processDefinitionName", processInstance.getProcessDefinitionName());
            processInstanceMap.put("processDefinitionVersion", processInstance.getProcessDefinitionVersion());
            processInstanceMap.put("processDefinitionKey", processInstance.getProcessDefinitionKey());
            processInstanceMap.put("processInstanceId", processInstance.getProcessInstanceId());
            processInstanceMap.put("businessKey", processInstance.getBusinessKey());
            processInstanceMap.put("isEnded", processInstance.isEnded());
            processInstanceMap.put("isSuspended", processInstance.isSuspended());
            processInstanceMap.put("rootProcessInstanceId", processInstance.getRootProcessInstanceId());
            processInstanceMap.put("deploymentId", processInstance.getDeploymentId());
            processInstanceMap.put("tenantId", processInstance.getTenantId());
            return processInstanceMap;
        }).toList();
    }

    /**
     * 根据流程实例ID获取流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    @GetMapping("/{processInstanceId}")
    public ProcessInstance getProcessInstance(@PathVariable String processInstanceId) {
        return flowableService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 操作结果
     */
    @PostMapping("/{processInstanceId}/activate")
    public Map<String, Object> activateProcessInstance(@PathVariable String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.activateProcessInstance(processInstanceId);
            result.put("success", true);
            result.put("message", "流程实例已激活");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 操作结果
     */
    @PostMapping("/{processInstanceId}/suspend")
    public Map<String, Object> suspendProcessInstance(@PathVariable String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.suspendProcessInstance(processInstanceId);
            result.put("success", true);
            result.put("message", "流程实例已挂起");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     * @return 操作结果
     */
    @DeleteMapping("/{processInstanceId}")
    public Map<String, Object> deleteProcessInstance(@PathVariable String processInstanceId,
                                                     @RequestParam String deleteReason) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.deleteProcessInstance(processInstanceId, deleteReason);
            result.put("success", true);
            result.put("message", "流程实例删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取流程实例变量
     *
     * @param processInstanceId 流程实例ID
     * @return 流程变量
     */
    @GetMapping("/{processInstanceId}/variables")
    public Map<String, Object> getProcessVariables(@PathVariable String processInstanceId) {
        return flowableService.getProcessVariables(processInstanceId);
    }

    /**
     * 设置流程实例变量
     *
     * @param processInstanceId 流程实例ID
     * @param variables         流程变量
     * @return 操作结果
     */
    @PostMapping("/{processInstanceId}/variables")
    public Map<String, Object> setProcessVariables(@PathVariable String processInstanceId,
                                                   @RequestBody Map<String, Object> variables) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.setProcessVariables(processInstanceId, variables);
            result.put("success", true);
            result.put("message", "流程变量设置成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取流程实例的标识链接
     *
     * @param processInstanceId 流程实例ID
     * @return 标识链接列表
     */
    @GetMapping("/{processInstanceId}/identity-links")
    public List<HistoricIdentityLink> getHistoricIdentityLinks(@PathVariable String processInstanceId) {
        return flowableService.getHistoricIdentityLinks(processInstanceId);
    }

    /**
     * 获取流程实例的历史变量
     *
     * @param processInstanceId 流程实例ID
     * @return 历史变量列表
     */
    @GetMapping("/{processInstanceId}/historic-variables")
    public List<HistoricVariableInstance> getHistoricVariables(@PathVariable String processInstanceId) {
        return flowableService.getHistoricVariables(processInstanceId);
    }

    /**
     * 获取流程图
     *
     * @param processInstanceId 流程实例ID
     * @param response          HttpServletResponse
     * @throws IOException IO异常
     */
    @GetMapping("/{processInstanceId}/diagram")
    public void getProcessDiagram(@PathVariable String processInstanceId,
                                  HttpServletResponse response) throws IOException {
        InputStream inputStream = flowableService.getProcessDiagram(processInstanceId);

        if (inputStream == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=\"process-diagram.png\"");
        IoUtil.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * 获取子流程实例
     *
     * @param parentProcessInstanceId 父流程实例ID
     * @return 子流程实例列表
     */
    @GetMapping("/sub-process/{parentProcessInstanceId}")
    public List<ProcessInstance> getSubProcessInstances(@PathVariable String parentProcessInstanceId) {
        return flowableService.createProcessInstanceQuery()
                .superProcessInstanceId(parentProcessInstanceId)
                .list();
    }

    /**
     * 获取父流程实例
     *
     * @param subProcessInstanceId 子流程实例ID
     * @return 父流程实例
     */
    @GetMapping("/parent-process/{subProcessInstanceId}")
    public ProcessInstance getParentProcessInstance(@PathVariable String subProcessInstanceId) {
//        ProcessInstance subInstance = flowableService.createProcessInstanceQuery()
//                .processInstanceId(subProcessInstanceId)
//                .singleResult();
//
//        if (subInstance != null && subInstance.getSuperProcessInstanceId() != null) {
//            return flowableService.createProcessInstanceQuery()
//                    .processInstanceId(subInstance.getSuperProcessInstanceId())
//                    .singleResult();
//        }

        return null;
    }
}
