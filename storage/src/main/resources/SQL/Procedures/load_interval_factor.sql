delimiter $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `load_interval_factor`(factor_id INT, start_date TIMESTAMP, open_price double, close_price double,
									  min_price double, max_price double, volume bigint)
BEGIN
	declare recordId int;

	select id into recordId
	from robotrader.factor_archive archive
	where 1 = 1
	and archive.factor_id = factor_id
	and archive.start_date = start_date;

	if recordId is null 
	then
		insert into robotrader.factor_archive(factor_id, start_date, open_price, close_price, min_price, max_price, volume)
		values(factor_id, start_date, open_price, close_price, min_price, max_price, volume);
	else
		update robotrader.factor_archive
			set robotrader.factor_archive.open_price = open_price,
				robotrader.factor_archive.close_price = close_price,
				robotrader.factor_archive.min_price = min_price,
				robotrader.factor_archive.max_price = max_price,
				robotrader.factor_archive.volume = volume
		where robotrader.factor_archive.id = recordId;
	end if;
END$$