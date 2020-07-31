/*
Navicat MySQL Data Transfer

Source Server         : 192.168.9.174
Source Server Version : 50627
Source Host           : 192.168.9.174:3306
Source Database       : sparkdb

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2018-01-09 16:49:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for compute
-- ----------------------------
DROP TABLE IF EXISTS `compute`;
CREATE TABLE `compute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ftime` varchar(255) DEFAULT NULL,
  `serialNo` int(11) DEFAULT NULL,
  `express` varchar(255) DEFAULT NULL,
  `result` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
