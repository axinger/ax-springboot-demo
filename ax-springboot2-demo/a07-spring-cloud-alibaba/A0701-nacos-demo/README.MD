```text
第一种写法  ：
shared-configs[0]:
    data-id: mysql-dev.yaml # 配置文件名-Data Id
    group: PEARL_GROUP   # 默认为DEFAULT_GROUP
    refresh: true   # 是否动态刷新，默认为false
第二种写法：
shared-configs:                               
- data-id: mysql-dev.yaml # 配置文件名-Data Id   
  group: PEARL_GROUP   # 默认为DEFAULT_GROUP   
  refresh: true   # 是否动态刷新，默认为false         

```

```text
第一种写法  ：
 extension-configs[0]:
       data-id: mysql-dev.yaml # 配置文件名-Data Id
       group: PEARL_GROUP   # 默认为DEFAULT_GROUP
        refresh: true   # 是否动态刷新，默认为false
第二种写法：
   extension-configs:                               
    - data-id: mysql-dev.yaml # 配置文件名-Data Id   
      group: PEARL_GROUP   # 默认为DEFAULT_GROUP   
      refresh: true   # 是否动态刷新，默认为false     

```

```text
shared-configs 与 extension-configs 都能实现加载多个配置文件 ，第一种写法中优先级：[0] < [1]
extension-configs > shared-configs 的优先级 
```

