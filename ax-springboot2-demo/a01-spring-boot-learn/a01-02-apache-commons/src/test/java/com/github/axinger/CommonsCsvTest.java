package com.github.axinger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

class CommonsCsvTest {

    @Test
    void testReadCsv() throws IOException {
        String csvData = "name,age,city\n" +
                "Alice,25,Beijing\n" +
                "Bob,30,Shanghai\n" +
                "Charlie,35,Guangzhou";

        // 解析 CSV
        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new StringReader(csvData));

        for (CSVRecord record : parser) {
            String name = record.get("name");
            String age = record.get("age");
            String city = record.get("city");
            System.out.println(name + " | " + age + " | " + city);
        }

        parser.close();
    }

    @Test
    void testWriteCsv() throws IOException {
        StringWriter writer = new StringWriter();

        // 定义格式并写入
        CSVFormat format = CSVFormat.DEFAULT.withHeader("name", "age", "city");
        CSVPrinter printer = new CSVPrinter(writer, format);

        // 写入记录
        printer.printRecord("Alice", 25, "Beijing");
        printer.printRecord("Bob", 30, "Shanghai");
        printer.printRecord("Charlie", 35, "Guangzhou");

        // 写入包含特殊字符的记录（自动处理引号和逗号）
        printer.printRecord("David, Jr.", 40, "New York");
        printer.printRecord("Eve", 28, "Shenzhen");

        printer.flush();
        printer.close();

        System.out.println(writer);
    }

    @Test
    void testWriteCsvFromList() throws IOException {
        StringWriter writer = new StringWriter();

        List<List<String>> data = Arrays.asList(
                Arrays.asList("产品", "价格", "数量"),
                Arrays.asList("iPhone", "6999", "10"),
                Arrays.asList("MacBook", "12999", "5"),
                Arrays.asList("AirPods", "1999", "20")
        );

        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecords(data);
        printer.close();

        System.out.println(writer);
    }
}
