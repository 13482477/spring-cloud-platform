DROP TABLE IF EXISTS `tx_serial_number`;
CREATE TABLE `tx_serial_number` (
  `serial_name` varchar(255) NOT NULL COMMENT '序列号名称',
  `current_value` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前值',
  `step` int(8) NOT NULL DEFAULT '1' COMMENT '步长',
  `prefix` varchar(64) NOT NULL DEFAULT '' COMMENT '序列号前缀',
  `current_date` datetime NOT NULL,
  `number_length` int(11) NOT NULL DEFAULT '8',
  PRIMARY KEY (`serial_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列号生成器';

INSERT INTO `tx_serial_number` VALUES ('default',0,1,'', current_timestamp(),8);

delimiter $$
CREATE FUNCTION `seq_currval`(`seq_name` VARCHAR(50))
  RETURNS VARCHAR(255)
  BEGIN
    DECLARE retval VARCHAR(255);
    SET retval = '-999999999';

    SELECT concat(
        prefix,
        date_format(current_date, '%Y%m%d'),
        lpad(cast(current_value AS CHAR), number_length, '00000000')
    )
    INTO
      retval
    FROM
      tx_serial_number
    WHERE
      serial_name = seq_name;
    RETURN retval;
  END; $$

delimiter $$
CREATE FUNCTION `seq_nextval`(`seq_name` VARCHAR(50))
  RETURNS VARCHAR(64)
  BEGIN
    UPDATE
      tx_serial_number
    SET
      current_value = if(
          date_format(current_date, '%Y%m%d') != date_format(now(), '%Y%m%d'),
          0,
          current_value + step
      )
    WHERE
      serial_name = seq_name;
    RETURN
    seq_currval(seq_name);
  END;$$

delimiter $$
CREATE FUNCTION `seq_setval`(`seq_name` VARCHAR(50), `value` INT(11))
  RETURNS VARCHAR(255)
  BEGIN
    UPDATE
      tx_serial_number
    SET
      current_value = value
    WHERE
      serial_name = seq_name;
    RETURN
    seq_currval(seq_name);
  END;$$