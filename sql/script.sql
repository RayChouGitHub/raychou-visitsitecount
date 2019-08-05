/*
 Navicat Premium Data Transfer

 Source Server         : gddz_dev
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 47.106.224.222:13306
 Source Schema         : gddz_dev

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 05/08/2019 11:27:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bus_site_visit_rank_t
-- ----------------------------
DROP TABLE IF EXISTS `bus_site_visit_rank_t`;
CREATE TABLE `bus_site_visit_rank_t`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `APP_NAME` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APP名称',
  `URI` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问URI',
  `IP` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问IP',
  `RANK` bigint(20) NULL DEFAULT NULL COMMENT '访问排名',
  `URI_TAG` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'uri标签 ps：首页',
  `CREATE_DATE` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `LASTUPDATE_DATE` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  `STATUS` tinyint(4) NULL DEFAULT 1 COMMENT '状态 1:可用',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'IP访问排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bus_site_visit_record_t
-- ----------------------------
DROP TABLE IF EXISTS `bus_site_visit_record_t`;
CREATE TABLE `bus_site_visit_record_t`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `APP_NAME` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APP名称',
  `URI` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问URI',
  `IP` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问IP',
  `VISIT_TIME` datetime(3) NULL DEFAULT NULL COMMENT '第一次访问时间',
  `VISIT_DATE` date NULL DEFAULT NULL COMMENT '访问日期',
  `URI_TAG` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'uri标签 ps：首页',
  `CREATE_DATE` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `LASTUPDATE_DATE` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  `STATUS` tinyint(4) NULL DEFAULT 1 COMMENT '状态 1:可用',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'IP访问记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bus_site_visit_total_t
-- ----------------------------
DROP TABLE IF EXISTS `bus_site_visit_total_t`;
CREATE TABLE `bus_site_visit_total_t`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `APP_NAME` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APP名称',
  `URI` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问URI',
  `TYPE` tinyint(4) NULL DEFAULT NULL COMMENT '访问类型 1:UV 2:PV 3:HOT',
  `URI_TAG` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'uri标签 ps：首页',
  `INITIAL_VISIT` bigint(20) NULL DEFAULT NULL COMMENT '初始访问量',
  `VISIT_VALUE` bigint(20) NULL DEFAULT NULL COMMENT '访问量',
  `END_VISIT` bigint(20) NULL DEFAULT NULL COMMENT '计算后访问量',
  `CALC_DATE` date NULL DEFAULT NULL COMMENT '计算日期',
  `CREATE_DATE` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `LASTUPDATE_DATE` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  `STATUS` tinyint(4) NULL DEFAULT 1 COMMENT '状态 1:可用',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'UV&PV&HOT访问统计表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
