delimiter $$

CREATE TABLE `factor_board` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FACTOR_ID` int(11) DEFAULT NULL,
  `STOCK_DATE` datetime DEFAULT NULL,
  `VALUE` double DEFAULT NULL,
  `IS_ACTIVE` binary(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FACTOR_BOARD_FACTOR_FK_idx` (`FACTOR_ID`),
  CONSTRAINT `FACTOR_BOARD_FACTOR_FK0` FOREIGN KEY (`FACTOR_ID`) REFERENCES `factor` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8$$