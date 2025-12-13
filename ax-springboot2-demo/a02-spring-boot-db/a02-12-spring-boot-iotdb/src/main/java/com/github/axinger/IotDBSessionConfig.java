package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.isession.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class IotDBSessionConfig {

    @Value("${spring.iotdb-session.username:root}")
    private String username;

    @Value("${spring.iotdb-session.password:root}")
    private String password;

    @Value("${spring.iotdb-session.ip:127.0.0.1}")
    private String ip;

    @Value("${spring.iotdb-session.port:6667}")
    private int port;

    @Value("${spring.iotdb-session.maxSize:10}")
    private int maxSize;

    @Bean
    public SessionPool sessionPool() {
        return new SessionPool(ip, port, username, password, maxSize);
    }


    public void insertRecord(String deviceId,
                             long time, List<String> measurements,
                             List<String> values) {
        try {
            sessionPool().insertRecord(deviceId, time, measurements, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(String root) {
        try {
            sessionPool().executeNonQueryStatement(String.format("delete timeseries %s", root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> executeQuery(String sql) {

        SessionDataSetWrapper wrapper = null;
        try {
            wrapper = sessionPool().executeQueryStatement(sql);
            final List<String> columnNames = wrapper.getColumnNames();

            List<Map<String, Object>> list = new ArrayList();
            while (wrapper.hasNext()) {
                Map<String, Object> map = new HashMap<>();
                list.add(map);

                final List<org.apache.tsfile.read.common.Field> fields = wrapper.next().getFields();
                if (columnNames.size() > fields.size()) {
                    for (int i = 0; i < columnNames.size() - 1; i++) {
                        map.put(columnNames.get(i + 1), fields.get((i)));
                    }
                } else if (columnNames.size() == fields.size()) {
                    for (int i = 0; i < columnNames.size(); i++) {
                        map.put(columnNames.get(i), fields.get((i)));
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionPool().closeResultSet(wrapper);
        }
        return null;
    }

}


