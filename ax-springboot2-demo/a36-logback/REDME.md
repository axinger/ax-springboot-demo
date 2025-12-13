```
关键细节说明

    ${PID:- } 的用途：

        显示进程 ID（Process ID），常用于分布式系统中追踪日志来源。

        如果未定义 PID 环境变量，则显示一个空格（通过 :- 指定默认值）。

    %-5level 的对齐：

        - 表示左对齐，5 表示固定占位宽度为5字符。若级别名称不足5字符（如 INFO），右侧补空格；若超过（如 WARN），自动扩展宽度。

    %logger{100} 的缩写规则：

        当日志记录器的完整类名超过100字符时，Logback 会按包名逐级缩写。例如：

            完整名称：org.springframework.web.servlet.DispatcherServlet

            缩写后：o.s.w.s.DispatcherServlet

    %msg 的扩展性：

        支持输出异常堆栈（需配合 %ex 或 %throwable），但在此模板中未显式包含
```
