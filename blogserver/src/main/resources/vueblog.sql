CREATE DATABASE `vueblog` DEFAULT CHARACTER SET utf8;

USE `vueblog`;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'ADMIN', NULL);
INSERT INTO `roles` VALUES (2, 'NORMAL', NULL);
INSERT INTO `roles` VALUES (3, 'TEST', NULL);

-- ----------------------------
-- Table structure for roles_user
-- ----------------------------
DROP TABLE IF EXISTS `roles_users`;
CREATE TABLE `roles_users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rid` bigint DEFAULT '2',
  `uid` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rid` (`rid`),
  KEY `roles_users_ibfk_2` (`uid`),
  CONSTRAINT `roles_users_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `roles` (`id`),
  CONSTRAINT `roles_users_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles_user
-- ----------------------------
INSERT INTO `roles_users` VALUES (1, 1, 1, NULL);
INSERT INTO `roles_users` VALUES (2, 2, 2, NULL);
INSERT INTO `roles_users` VALUES (3, 3, 3, NULL);


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enable` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;