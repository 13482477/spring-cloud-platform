DROP TABLE IF EXISTS `inf_host`;

CREATE TABLE `inf_host` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `host_name` varchar(255) DEFAULT NULL,
  `host_id` varchar(255) DEFAULT NULL,
  `root_name` varchar(255) DEFAULT NULL,
  `root_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
