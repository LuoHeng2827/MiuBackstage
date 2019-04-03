
DROP TABLE IF EXISTS `t_user`;
DROP TABLE IF EXISTS `t_user_register`;
CREATE TABLE `t_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `mail` varchar(30) NOT NULL,
  `name` varchar(20) NOT NULL,
  `passwords` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_user_register` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `mail` varchar(30) NOT NULL,
  `token` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;