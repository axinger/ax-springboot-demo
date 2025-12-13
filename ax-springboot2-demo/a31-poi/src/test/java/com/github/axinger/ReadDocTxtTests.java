package com.github.axinger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.SneakyThrows;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ReadDocTxtTests {


    ///  读取doc内容
    @SneakyThrows
    @Test
    public void test1() {
        ClassPathResource resource = new ClassPathResource("file/test01.docx");

        try (InputStream inputStream = resource.getInputStream()) {

            XWPFDocument document = new XWPFDocument(inputStream);
            // 获取word中的所有段落与表格
            List<IBodyElement> elements = document.getBodyElements();
            for (IBodyElement element : elements) {
                // 段落
                if (element instanceof XWPFParagraph) {
                    String text = WordRead.getParagraphText((XWPFParagraph) element);
                    System.out.println("段落text = " + text);
                }
                // 表格
                else if (element instanceof XWPFTable xwpfTable) {
                    Table<Integer, Integer, String> table = WordRead.getTableText(xwpfTable);
                    System.out.println("表格text = " + table);
                }
            }
        }
    }

    /**
     * 读取doc
     *
     * @throws IOException
     */
    @Test
    void test2() throws IOException {
        com.google.common.collect.Table<Integer, Integer, String> tables = HashBasedTable.create();
        HWPFDocument document = new HWPFDocument(new FileInputStream("D:\\Users\\cepai\\Desktop\\123.doc"));
        TableIterator tableIter = new TableIterator(document.getRange());
        while (tableIter.hasNext()) {
            org.apache.poi.hwpf.usermodel.Table table = tableIter.next();
            for (int i = 0; i < table.numRows(); i++) {
                TableRow row = table.getRow(i);
                for (int j = 0; j < row.numCells(); j++) {
                    TableCell cell = row.getCell(j);
                    String content = cell.getParagraph(0).text();
                    tables.put(i, j, content);
                }
            }
        }


        document.close();
        System.out.println("tables = " + tables);
        System.out.println("tables.get(1,1) = " + Objects.requireNonNull(tables.get(1, 1)).replace("\u0007", ""));
    }
}
