package com.git.hui.boot.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "bus_site_visit_rank_t")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteVisitRank {
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
     * 访问排名
     */
    @Column(name = "RANK")
    private Long rank;

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