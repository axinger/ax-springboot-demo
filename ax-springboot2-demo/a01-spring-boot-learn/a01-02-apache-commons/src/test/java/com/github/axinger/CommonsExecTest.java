package com.github.axinger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Apache Commons Exec 外部进程执行示例
 */
class CommonsExecTest {

    // ==================== 基本命令执行 ====================

    @Test
    void testBasicExecution() throws IOException {
        // 执行简单命令（Windows 用 cmd /c，Linux/Mac 用对应 shell）
        String command = isWindows() ? "cmd /c echo Hello Commons Exec" : "echo Hello Commons Exec";
        CommandLine cmdLine = CommandLine.parse(command);

        DefaultExecutor executor = new DefaultExecutor();
        int exitCode = executor.execute(cmdLine);

        System.out.println("退出码: " + exitCode); // 0 表示成功
    }

    @Test
    void testCommandWithArguments() throws IOException {
        // 使用 CommandLine 构建带参数的命令（更安全，避免注入）
        CommandLine cmdLine = new CommandLine(isWindows() ? "cmd" : "echo");

        if (isWindows()) {
            cmdLine.addArgument("/c");
            cmdLine.addArgument("echo");
        }

        cmdLine.addArgument("Hello");
        cmdLine.addArgument("World");
        cmdLine.addArgument("from");
        cmdLine.addArgument("Commons Exec");

        DefaultExecutor executor = new DefaultExecutor();
        int exitCode = executor.execute(cmdLine);
        System.out.println("退出码: " + exitCode);
    }

    // ==================== 捕获输出 ====================

    @Test
    void testCaptureOutput() throws IOException {
        CommandLine cmdLine = isWindows()
                ? CommandLine.parse("cmd /c echo Captured Output")
                : CommandLine.parse("echo Captured Output");

        DefaultExecutor executor = new DefaultExecutor();

        // 使用流捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        executor.setStreamHandler(streamHandler);

        int exitCode = executor.execute(cmdLine);

        String stdout = outputStream.toString(StandardCharsets.UTF_8).trim();
        String stderr = errorStream.toString(StandardCharsets.UTF_8).trim();

        System.out.println("退出码: " + exitCode);
        System.out.println("标准输出: " + stdout);
        System.out.println("错误输出: [" + stderr + "]");
    }

    @Test
    void testCaptureWithSystemCommand() throws IOException {
        // 执行系统命令并捕获输出（Windows: ver, Linux: uname -a）
        CommandLine cmdLine;
        if (isWindows()) {
            cmdLine = CommandLine.parse("cmd /c ver");
        } else {
            cmdLine = CommandLine.parse("uname -a");
        }

        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStream));

        executor.execute(cmdLine);

        String result = outputStream.toString(StandardCharsets.UTF_8).trim();
        System.out.println("系统信息:\n" + result);
    }

    // ==================== 超时控制 ====================

    @Test
    void testTimeout() {
        // 设置执行超时（毫秒）
        ExecuteWatchdog watchdog = new ExecuteWatchdog(2000); // 2秒超时

        // 模拟一个长时间运行的命令
        CommandLine cmdLine = isWindows()
                ? CommandLine.parse("cmd /c timeout /t 5 /nobreak")
                : CommandLine.parse("sleep 5");

        DefaultExecutor executor = new DefaultExecutor();
        executor.setWatchdog(watchdog);

        try {
            int exitCode = executor.execute(cmdLine);
            System.out.println("退出码: " + exitCode);
        } catch (ExecuteException e) {
            if (watchdog.killedProcess()) {
                System.out.println("进程因超时被终止");
            } else {
                System.out.println("执行异常: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("IO 异常: " + e.getMessage());
        }
    }

    // ==================== 工作目录设置 ====================

    @Test
    void testWorkingDirectory() throws IOException {
        CommandLine cmdLine = isWindows()
                ? CommandLine.parse("cmd /c cd")
                : CommandLine.parse("pwd");

        DefaultExecutor executor = new DefaultExecutor();

        // 设置工作目录
        java.io.File workingDir = new java.io.File(System.getProperty("java.io.tmpdir"));
        executor.setWorkingDirectory(workingDir);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStream));

        executor.execute(cmdLine);

        String result = outputStream.toString(StandardCharsets.UTF_8).trim();
        System.out.println("当前工作目录: " + result);
        System.out.println("预期目录: " + workingDir.getAbsolutePath());
    }

    // ==================== 退出码处理 ====================

    @Test
    void testExitValueHandling() {
        // 测试非零退出码
        CommandLine cmdLine = isWindows()
                ? CommandLine.parse("cmd /c exit 1")
                : CommandLine.parse("sh -c 'exit 1'");

        DefaultExecutor executor = new DefaultExecutor();

        try {
            int exitCode = executor.execute(cmdLine);
            System.out.println("退出码: " + exitCode);
        } catch (ExecuteException e) {
            System.out.println("进程异常退出，退出码: " + e.getExitValue());
        } catch (IOException e) {
            System.out.println("IO 异常: " + e.getMessage());
        }
    }

    @Test
    void testCustomExitValue() throws IOException {
        // 允许特定退出码
        CommandLine cmdLine = isWindows()
                ? CommandLine.parse("cmd /c exit 42")
                : CommandLine.parse("sh -c 'exit 42'");

        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(42); // 将 42 视为成功退出码

        int exitCode = executor.execute(cmdLine);
        System.out.println("退出码: " + exitCode); // 42
    }

    // ==================== 环境变量 ====================

    @Test
    void testEnvironmentVariables() throws IOException {
        CommandLine cmdLine = isWindows()
                ? CommandLine.parse("cmd /c echo %MY_VAR%")
                : CommandLine.parse("sh -c 'echo $MY_VAR'");

        DefaultExecutor executor = new DefaultExecutor();

        // 设置环境变量
        java.util.Map<String, String> env = new java.util.HashMap<>();
        env.put("MY_VAR", "Hello from Env");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStream));

        executor.execute(cmdLine, env);

        String result = outputStream.toString(StandardCharsets.UTF_8).trim();
        System.out.println("环境变量值: " + result);
    }

    // ==================== 异步执行 ====================

    @Test
    void testAsyncExecution() throws Exception {
        CommandLine cmdLine = isWindows()
                ? CommandLine.parse("cmd /c echo Async Execution")
                : CommandLine.parse("echo Async Execution");

        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStream));

        // 异步执行
        executor.execute(cmdLine, new org.apache.commons.exec.ExecuteResultHandler() {
            @Override
            public void onProcessComplete(int exitValue) {
                System.out.println("进程完成，退出码: " + exitValue);
            }

            @Override
            public void onProcessFailed(ExecuteException e) {
                System.out.println("进程失败: " + e.getMessage());
            }
        });

        // 等待异步执行完成
        Thread.sleep(1000);

        String result = outputStream.toString(StandardCharsets.UTF_8).trim();
        System.out.println("输出: " + result);
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
