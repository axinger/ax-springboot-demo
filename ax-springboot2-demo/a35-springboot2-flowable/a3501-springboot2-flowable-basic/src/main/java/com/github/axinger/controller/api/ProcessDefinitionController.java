package com.github.axinger.controller.api;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.service.FlowableService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程定义控制器
 * 展示Flowable流程定义相关操作
 */
@RestController
@RequestMapping("/api/process-definition")
public class ProcessDefinitionController {

    @Autowired
    private FlowableService flowableService;

    /**
     * 部署流程定义 - 通过BPMN文件
     *
     * @param file BPMN文件
     * @return 部署结果
     */
    @PostMapping("/deploy/bpmn")
    public Map<String, Object> deployBpmn(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            Deployment deployment = flowableService.deployProcessFromClasspath(file.getOriginalFilename(), file.getOriginalFilename());
            result.put("success", true);
            result.put("deploymentId", deployment.getId());
            result.put("deploymentName", deployment.getName());
            result.put("deploymentTime", deployment.getDeploymentTime());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 部署流程定义 - 通过ZIP文件
     *
     * @param file ZIP文件（包含BPMN和PNG等）
     * @return 部署结果
     */
//    @PostMapping("/deploy/zip")
//    public Map<String, Object> deployZip(@RequestParam("file") MultipartFile file) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
////            Deployment deployment = flowableService.createDeploymentQuery()
//                    .deploy(zipInputStream)
//                    .name(file.getOriginalFilename())
//                    .deploy();
//            result.put("success", true);
//            result.put("deploymentId", deployment.getId());
//            result.put("deploymentName", deployment.getName());
//            result.put("deploymentTime", deployment.getDeploymentTime());
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", e.getMessage());
//        }
//        return result;
//    }

    /**
     * 获取所有流程定义
     *
     * @return 流程定义列表
     */
    @GetMapping("/list")
    public Object getProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = flowableService.getProcessDefinitions();

        // 转换为简单的 DTO 对象，避免序列化原始实体时出现问题
        List<Map<String, Object>> dtoList = processDefinitions.stream().map(pd -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", pd.getId());
            dto.put("name", pd.getName());
            dto.put("key", pd.getKey());
            dto.put("version", pd.getVersion());
            dto.put("deploymentId", pd.getDeploymentId());
            dto.put("description", pd.getDescription());
            dto.put("category", pd.getCategory());
            dto.put("tenantId", pd.getTenantId());
            return dto;
        }).collect(Collectors.toList());
        return dtoList;
    }

    /**
     * 根据key获取最新版本的流程定义
     *
     * @param key 流程定义key
     * @return 流程定义
     */
    @GetMapping("/key/{key}")
    public ProcessDefinition getProcessDefinitionByKey(@PathVariable String key) {
        return flowableService.createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .latestVersion()
                .singleResult();
    }

    /**
     * 获取所有部署信息
     *
     * @return 部署列表
     */
    @GetMapping("/deployments")
    public List<Deployment> getDeployments() {
        return flowableService.createDeploymentQuery().list();
    }

    /**
     * 激活流程定义
     *
     * @param processDefinitionId 流程定义ID
     * @return 操作结果
     */
    @PostMapping("/{processDefinitionId}/activate")
    public Map<String, Object> activateProcessDefinition(@PathVariable String processDefinitionId) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            result.put("success", true);
            result.put("message", "流程定义已激活");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 挂起流程定义
     *
     * @param processDefinitionId 流程定义ID
     * @return 操作结果
     */
    @PostMapping("/{processDefinitionId}/suspend")
    public Map<String, Object> suspendProcessDefinition(@PathVariable String processDefinitionId) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            result.put("success", true);
            result.put("message", "流程定义已挂起");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 删除部署
     *
     * @param deploymentId 部署ID
     * @param cascade      是否级联删除
     * @return 操作结果
     */
    @DeleteMapping("/deployment/{deploymentId}")
    public Map<String, Object> deleteDeployment(@PathVariable String deploymentId,
                                                @RequestParam(defaultValue = "false") boolean cascade) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.deleteDeployment(deploymentId, cascade);
            result.put("success", true);
            result.put("message", "部署删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

//    /**
//     * 获取流程定义XML
//     *
//     * @param processDefinitionId 流程定义ID
//     * @param response            HttpServletResponse
//     * @throws IOException IO异常
//     */
//    @GetMapping("/{processDefinitionId}/xml")
//    public void getProcessDefinitionXml(@PathVariable String processDefinitionId,
//                                        HttpServletResponse response) throws IOException {
//        InputStream inputStream = flowableService.createProcessDefinitionQuery()
//                .processDefinitionId(processDefinitionId)
//                .singleResult()
//                .getResourceName();
//
//        response.setContentType("application/xml");
//        response.setHeader("Content-Disposition", "attachment; filename=\"process.xml\"");
//
//        try (OutputStream out = response.getOutputStream()) {
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = inputStream.read(buffer)) != -1) {
//                out.write(buffer, 0, len);
//            }
//            out.flush();
//        }
//    }
}
