-- ----------------------------
-- Table structure for nr_drop_rate
-- ----------------------------
DROP TABLE IF EXISTS `nr_drop_rate`;
CREATE TABLE `nr_drop_rate`  (
  `id` int NOT NULL,
  `mob_rate` int DEFAULT 100,
  `boss_rate` int DEFAULT 100,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of nr_drop_rate
-- ----------------------------
INSERT INTO `nr_drop_rate` VALUES (1, 100, 100);


-- ----------------------------
-- Table structure for nr_top_superrank
-- ----------------------------
DROP TABLE IF EXISTS `nr_top_superrank`;
CREATE TABLE `nr_top_superrank` (
  `id` int NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL,
  `rank` int NOT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
