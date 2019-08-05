package com.git.hui.boot.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "bus_site_visit_total_t")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteVisitTotal {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "SELECT REPLACE(UUID(),'-','')")
    @Column(name = "ID")
    private Long id;

    /**
     * APP名称
     */
    @Column(name = "APP_NAME")
    private String appName;

    /**
     * 访问URI
     */
    @Column(name = "URI")
    private String uri;

    /**
     * 访问类型 1:UV 2:PV 3:HOT
     */
    @Column(name = "TYPE")
    private Byte type;

    /**
     * uri标记
     */
    @Column(name = "URI_TAG")
    private String uriTag;

    /**
     * 初始访问量
     */
    @Column(name = "INITIAL_VISIT")
    private Long initialVisit;

    /**
     * 访问量
     */
    @Column(name = "VISIT_VALUE")
    private Long visitValue;

    /**
     * 计算后访问量
     */
    @Column(name = "END_VISIT")
    private Long endVisit;

    /**
     * 计算日期
     */
    @Column(name = "CALC_DATE")
    private Date calcDate;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 最后更新时间
     */
    @Column(name = "LASTUPDATE_DATE")
    private Date lastupdateDate;

    /**
     * 状态 1:可用
     */
    @Column(name = "STATUS")
    private Byte status;

}