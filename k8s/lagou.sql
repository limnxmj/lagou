/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50730
Source Host           : 127.0.0.1:3306
Source Database       : lagou

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-10-10 18:32:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `position`
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`salary`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`city`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=6

;

-- ----------------------------
-- Records of position
-- ----------------------------
BEGIN;
INSERT INTO `position` VALUES ('2', '高级技术美术', '25k-40k', '天津'), ('3', 'c++开发工程师', '10k-20k', '上海'), ('4', 'SCRM产品经理', '15k-25k', '杭州'), ('5', 'java', '10k', '上海');
COMMIT;

-- ----------------------------
-- Auto increment value for `position`
-- ----------------------------
ALTER TABLE `position` AUTO_INCREMENT=6;
