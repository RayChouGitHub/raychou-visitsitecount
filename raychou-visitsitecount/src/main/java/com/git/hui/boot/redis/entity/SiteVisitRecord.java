package com.git.hui.boot.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "bus_site_visit_record_t")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteVisitRecord {
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
     * 访问IP
     */
    @Column(name = "IP")
    private String ip;

    /**
     * 第一次访问时间
     */
    @Column(name = "VISIT_TIME")
    private Date visitTime;

    /**
     * 访问日期
     */
    @Column(name = "VISIT_DATE")
    private Date visitDate;

    /**
     * uri标记
     */
    @Column(name = "URI_TAG")
    private String uriTag;

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