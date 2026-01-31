package com.github.axinger;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;

import java.util.List;

public class PulsarTenantLister {

    public static void main(String[] args) {
        // Pulsar Admin 服务的 URL
        String serviceHttpUrl = "http://hadoop103:18080"; // 替换为你的 Pulsar Admin URL

        // 创建 PulsarAdmin 客户端
        try (PulsarAdmin admin = PulsarAdmin.builder().serviceHttpUrl(serviceHttpUrl).build()) {
            // 查询所有租户
            List<String> tenants = listTenants(admin);

            // 打印租户列表
            System.out.println("已有的租户列表=" + tenants);
        } catch (Exception e) {
            System.err.println("查询租户失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<String> listTenants(PulsarAdmin admin) throws PulsarAdminException {
        // 获取 Tenants 接口
        return admin.tenants().getTenants();
    }
}
