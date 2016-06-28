delimiter $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `load_online_factor`(factor_id INT, factor_date datetime, factor_value double, is_active binary)
BEGIN
	declare open_price_val double;
	declare close_price_val double;
	declare min_price_val double;
	declare max_price_val double;
	declare factor_date_trc datetime;

	/*Update factor board*/
	if(exists (select 1 from robotrader.factor_board where factor_id = factor_id))
	then
		update robotrader.factor_board 
			set robotrader.factor_board.stock_date = factor_date,
				robotrader.factor_board.value = factor_value,
				robotrader.factor_board.is_active = is_active
		where robotrader.factor_board.factor_id = factor_id;
	else
		insert into robotrader.factor_board(factor_id, stock_date, value, is_active)
		values(factor_id, factor_date, factor_value, is_active);
	end if;
	
	/*Update archive data*/
	if(is_active = 1)
	then
		set factor_date_trc = date_sub(factor_date, INTERVAL SECOND(factor_date) SECOND);

		select 	open_price, close_price, min_price, max_price into open_price_val, close_price_val, min_price_val, max_price_val
		from robotrader.factor_archive archive
		where 1 = 1
		and archive.factor_id = factor_id
		and archive.start_date = factor_date_trc;

		if(open_price_val is null) then set open_price_val = factor_value; end if;
		if(min_price_val is null or min_price_val > factor_value) then set min_price_val = factor_value; end if;
		if(max_price_val is null or max_price_val < factor_value) then set max_price_val = factor_value; end if;
		set close_price_val = factor_value;

		call load_interval_factor(factor_id, factor_date_trc, open_price_val, 
									close_price_val, min_price_val, max_price_val, 0);
	end if;	
END$$