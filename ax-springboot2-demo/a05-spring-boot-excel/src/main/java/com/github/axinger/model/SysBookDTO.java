package com.github.axinger.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 图书信息数据传输对象（DTO），用于 EasyExcel 导出/导入 Excel 表格
 * <p>
 * 该类结合了 Lombok 和 Alibaba EasyExcel 注解，实现自动化的 Excel 读写操作。
 * 可用于导出图书列表，包含基本信息、状态、时间等字段。
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// 合并第0列（用户ID）从第1行到第100行中连续相同的内容
//@ContentLoopMerge(columnIndex = 0, firstRowIndex = 1, lastRowIndex = 100)
// 可选：设置表头样式（背景色、字体等）
//@HeadStyle(fillForegroundColor = IndexedColors.LIGHT_BLUE.index)
//@HeadFontStyle(color = IndexedColors.WHITE.index, bold = BooleanEnum.TRUE)
@HeadFontStyle(color = 4, bold = BooleanEnum.TRUE, fontName = "微软雅黑", fontHeightInPoints = 10)
@ContentFontStyle(color = 14, fontName = "Arial", fontHeightInPoints = 10)
@ContentRowHeight(60)
//@OnceAbsoluteMerge(firstRowIndex = 1, lastRowIndex = 100)
public class SysBookDTO {

    /**
     * 用户ID - 映射到 Excel 第一列，表头为 "用户ID"
     * index = 0 表示该字段在 Excel 中的列顺序（从0开始）
     */
    @ExcelProperty(value = "用户ID", index = 0)
//    @HeadFontStyle(color = IndexedColors.LIGHT_BLUE.INDIGO, fontName = "微软雅黑", fontHeightInPoints = 10, bold = BooleanEnum.TRUE)
    private Long id;

    /**
     * 姓名 - 使用多级表头：第一级为“基本信息”，第二级为“姓名”
     * 在 Excel 中将显示为合并的二级表头结构
     */
    @ExcelProperty({"基本信息", "姓名"})
    private String name;

    /**
     * 作者 - 同样属于“基本信息”下的二级表头
     */
    @ExcelProperty({"基本信息", "作者"})
    // 合并第2列（作者）
    @ContentLoopMerge(eachRow = 3)  // 每2行合并一次，避免参数错误
    private String author;

    /**
     * 年龄 - 指定在导出时排序顺序为第2位（优先级高于 index）
     * 注意：order 和 index 可同时使用，但 order 优先级更高（EasyExcel 3.x+）
     */
    @ExcelProperty(value = "年龄", order = 2)
    private Integer age;

    /**
     * 状态 - 使用自定义转换器将数据库中的数字状态转换为可读文本（如：0=禁用, 1=启用）
     * 转换器需实现 Converter 接口
     */
    @ExcelProperty(value = "状态", converter = StatusConverter.class)
    private Integer status;

    /**
     * 创建时间 - 多级表头“时间”下的“创建时间”
     * 使用 DateTimeFormat 格式化输出为 "yyyy-MM-dd HH:mm:ss"
     */
    @ExcelProperty(value = {"时间", "创建时间"})
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(80)
    private Date createTime = new Date();

    /**
     * 更新时间 - 同样属于“时间”分组下的字段
     */
    @ExcelProperty(value = {"时间", "更新时间"})
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(80)
    private Date updateTime = new Date();

    /**
     * del 字段被标记为忽略，不会参与 Excel 的读写操作
     * 适用于不需要导出/导入的临时或内部字段
     */
    @ExcelIgnore
    private String del;

    // ====================== 其他常用 EasyExcel 特性补充 ======================

    /**
     * 示例：设置列宽（需要配合 @ColumnWidth 使用）
     * 如果希望某列更宽，比如作者名较长
     */
    // @ColumnWidth(30)
    // private String author;

    /**
     * 示例：内容对齐方式（需自定义样式策略）
     * 可通过注解或拦截器设置水平/垂直对齐
     */
    // @ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)

    /**
     * 示例：单元格颜色、背景等（可通过 @HeadStyle / @ContentStyle 设置）
     * 更复杂的样式建议使用 WriteHandler 实现
     */

    /**
     * 示例：条件高亮（如状态为0标红）—— 需要自定义 CellWriteHandler
     * 可通过事件监听器在写入时动态修改样式
     */

    /**
     * 示例：下拉框（数据验证）
     * 可通过自定义 WriteHandler 添加 Excel 下拉列表
     */

    /**
     * 示例：超链接、图片插入（需使用 ImageModel 或 CellData）
     * 支持写入图片或超链接到单元格
     */
}
