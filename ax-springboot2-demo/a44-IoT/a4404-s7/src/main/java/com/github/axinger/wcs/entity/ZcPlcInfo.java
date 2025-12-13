package com.github.axinger.wcs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: plc信息
 * @Author: jeecg-boot
 * @Date: 2024-04-18
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
//@ApiModel(value = "zc_plc_info对象", description = "plc信息")
public class ZcPlcInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    //@TableId(type = IdType.ASSIGN_ID)
    //@ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    //@ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    //@ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
    //@ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**
     * 编号
     */
    //@Excel(name = "编号", width = 15)
    //@ApiModelProperty(value = "编号")
    private Integer no;

    //@Excel(name = "编号", width = 15)
    //@ApiModelProperty(value = "编号")
    private Integer dbNo;
    /**
     * 名称
     */
    //@Excel(name = "名称", width = 15)
    //@ApiModelProperty(value = "名称")
    private String name;
    /**
     * 上级编号
     */
    //@Excel(name = "上级编号", width = 15)
    //@ApiModelProperty(value = "上级编号")
    private Integer parentNo;
    /**
     * IP地址
     */
    //@Excel(name = "IP地址", width = 15)
    //@ApiModelProperty(value = "IP地址")
    private String ip;

    //@ApiModelProperty(value = "定时任务id")
    private String jobId;


    //@ApiModelProperty(value = "PLC链接状态,0未链接,1链接失败,2连接成功")
    private Integer plcStatus;

}
