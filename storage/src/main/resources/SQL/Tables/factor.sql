delimiter $$

CREATE TABLE `factor` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(45) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `IS_PAPER` binary(1) NOT NULL,
  `SCALE` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8$$