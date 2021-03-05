CREATE DATABASE `parser` IF NOT EXISTS DEFAULT CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS `web_access_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `http_status_code` int(11) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `blocked_ip_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `duration` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `starting_from_date` datetime(6) DEFAULT NULL,
  `threshold` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP PROCEDURE IF EXISTS GetIPsExceedingLimit;
DROP PROCEDURE IF EXISTS GetIPRequests;

DELIMITER //

CREATE PROCEDURE GetIPsExceedingLimit (IN threshold INT, IN start_date DATETIME, IN end_date DATETIME) READS SQL DATA
BEGIN
    SELECT wal.ip FROM web_access_log wal WHERE wal.date BETWEEN start_date AND end_date GROUP BY (wal.ip) HAVING COUNT(*) > threshold;
END  //

CREATE PROCEDURE GetIPRequests (IN ip VARCHAR(255)) READS SQL DATA
BEGIN
    SELECT * FROM web_access_log wal WHERE wal.ip = ip;
END  //

DELIMITER ;
