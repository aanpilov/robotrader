delimiter $$

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `factor_archive_30_minute` AS select `arch`.`FACTOR_ID` AS `FACTOR_ID`,(`arch`.`START_DATE` - interval (minute(`arch`.`START_DATE`) % 30) minute) AS `START_DATE`,`arch`.`OPEN_PRICE` AS `OPEN_PRICE`,(select `arch1`.`CLOSE_PRICE` from `factor_archive` `arch1` where ((`arch1`.`FACTOR_ID` = `arch`.`FACTOR_ID`) and (`arch1`.`START_DATE` = max(`arch`.`START_DATE`)))) AS `CLOSE_PRICE`,min(`arch`.`MIN_PRICE`) AS `MIN_PRICE`,max(`arch`.`MAX_PRICE`) AS `MAX_PRICE`,sum(`arch`.`VOLUME`) AS `VOLUME` from `factor_archive` `arch` group by `arch`.`FACTOR_ID`,(`arch`.`START_DATE` - interval (minute(`arch`.`START_DATE`) % 30) minute)$$