delete from finam_archive_load;

load data local infile 'D:/Java Projects/RoboTrader/db/SANDP-FUT_120101_121121.csv' 
into table finam_archive_load
fields terminated by ';'
lines terminated by '\n' (TICKER, PER, DATE, TIME, OPEN, HIGH, LOW, CLOSE, VOL);

select *
from finam_archive_load
order by str_to_date(concat(`DATE`, ' ', `TIME`), '%d/%m/%y');

INSERT INTO `robotrader`.`factor_archive`
(`FACTOR_ID`,
`START_DATE`,
`OPEN`,
`CLOSE`,
`MIN`,
`MAX`,
`VOLUME`)
SELECT
f.ID,
str_to_date(concat(`DATE`, ' ', `TIME`), '%d/%m/%y %H:%i:%s') START_DATE,
arch.`OPEN`,
arch.`CLOSE`,
arch.`LOW`,
arch.`HIGH`,
arch.`VOL`
FROM finam_archive_load arch,
     factor f
WHERE 1 = 1
and f.code = 'SP500_FUT_PRICE';

	