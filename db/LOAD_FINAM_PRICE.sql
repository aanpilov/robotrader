delete from finam_price_load;

load data local infile 'D:/Java Projects/RoboTrader/db/SPFB.SBRF_121021_121031.csv' 
into table finam_price_load
fields terminated by ';'
lines terminated by '\n' (TICKER, PER, DATE, TIME, LAST, VOL);

select *
from finam_price_load
order by str_to_date(concat(`DATE`, ' ', `TIME`), '%d/%m/%y');

INSERT INTO `mock_adapter`.`paper_price`
(`PAPER_ID`,
`PRICE_DATE`,
`VALUE`,
`VOLUME`)
SELECT
p.ID,
str_to_date(concat(`DATE`, ' ', `TIME`), '%d/%m/%y %H:%i:%s') START_DATE,
price.`LAST`,
price.`VOL`
FROM finam_price_load price,
     papers p
WHERE 1 = 1
and p.paper_code = 'SPFB.SBRF';

	