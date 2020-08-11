DROP TABLE IF EXISTS `position`;
CREATE TABLE `position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `salary` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `position` VALUES ('1', '资深测试工程师', '30k-50k', '北京');
INSERT INTO `position` VALUES ('2', '高级技术美术', '25k-40k', '天津');
INSERT INTO `position` VALUES ('3', 'c++开发工程师', '10k-20k', '上海');
INSERT INTO `position` VALUES ('4', 'SCRM产品经理', '15k-25k', '杭州');
