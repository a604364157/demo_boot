/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : ball

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-01-24 11:54:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for two_color
-- ----------------------------
DROP TABLE IF EXISTS `two_color`;
CREATE TABLE `two_color` (
  `id_no` int(11) NOT NULL AUTO_INCREMENT,
  `red1` int(2) NOT NULL,
  `red2` int(2) NOT NULL,
  `red3` int(2) NOT NULL,
  `red4` int(2) NOT NULL,
  `red5` int(2) NOT NULL,
  `red6` int(2) NOT NULL,
  `blue` int(2) NOT NULL,
  `run_date` datetime NOT NULL,
  `op_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id_no`),
  KEY `id_no_pk` (`id_no`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
