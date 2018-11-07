CREATE SCHEMA `money` ;
USE `money` ;

CREATE USER 'srv_money'@'%' IDENTIFIED BY 'money123';
-- ALTER USER 'srv_money'@'%' IDENTIFIED WITH mysql_native_password BY 'money123';
GRANT ALL ON money.* TO 'srv_money'@'%';
CREATE TABLE `account` (
  `id` BIGINT(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `balance` float(53) NOT NULL DEFAULT 0.00,
  `currency` varchar(3) NOT NULL DEFAULT 'HKD',
  `created_ts` DATETIME,
  `updated_ts` DATETIME,
  PRIMARY KEY (`id`),
  KEY `ind_name_currency` (`name`,`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `account` ( `id`, `name`, `balance`, `currency`, `created_ts`, `updated_ts`) VALUES ('521877048123','CHAN TAI MAN','1000.00','HKD','2018-01-01 11:11:11','2018-01-01 11:11:11');
INSERT INTO `account` ( `id`, `name`, `balance`, `currency`, `created_ts`, `updated_ts`) VALUES ('477333222431','WONG SHEUNG','99999.00','HKD','2018-01-02 11:11:11','2018-01-02 11:11:11');
INSERT INTO `account` ( `id`, `name`, `balance`, `currency`, `created_ts`, `updated_ts`) VALUES ('987654321000','CHEUNG SIU MING','7777.00','HKD','2018-07-01 11:11:11','2018-07-01 11:11:11');
