-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2024-05-19 15:12:57
-- 伺服器版本： 10.4.32-MariaDB
-- PHP 版本： 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `pet_tracking_platform`
--

-- --------------------------------------------------------

--
-- 資料表結構 `health_record`
--

CREATE TABLE `health_record` (
  `ID` int(11) NOT NULL,
  `type` varchar(255) NOT NULL COMMENT '醫療類型',
  `description` text DEFAULT NULL COMMENT '描述',
  `cost` mediumint(8) UNSIGNED NOT NULL DEFAULT 0 COMMENT '花費'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `health_record`
--

INSERT INTO `health_record` (`ID`, `type`, `description`, `cost`) VALUES
(1, 'Vaccination', 'Rabies vaccination', 500),
(2, 'Parasite Control', 'Flea and tick control', 300),
(3, 'Dental Care', 'Teeth cleaning and checkup', 400),
(4, 'Medical Checkup', 'General health checkup', 200),
(5, 'Surgery', 'Spay/neuter surgery', 1000);

-- --------------------------------------------------------

--
-- 資料表結構 `location`
--

CREATE TABLE `location` (
  `ID` int(11) NOT NULL,
  `latitude` decimal(9,6) NOT NULL,
  `longitude` decimal(9,6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `location`
--

INSERT INTO `location` (`ID`, `latitude`, `longitude`) VALUES
(26, 25.033300, 121.566700),
(27, 24.116700, 120.666700),
(28, 23.533300, 121.333300),
(29, 25.733300, 120.933300),
(30, 24.866700, 122.466700);

-- --------------------------------------------------------

--
-- 資料表結構 `pet`
--

CREATE TABLE `pet` (
  `ID` int(11) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT '寵物姓名',
  `gender` enum('male','female') NOT NULL COMMENT '性別',
  `birthday` date DEFAULT NULL COMMENT '出生日期'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `pet`
--

INSERT INTO `pet` (`ID`, `name`, `gender`, `birthday`) VALUES
(1, 'Max', 'male', '2018-01-01'),
(2, 'Luna', 'female', '2019-06-15'),
(3, 'Charlie', 'male', '2015-03-01'),
(4, 'Daisy', 'female', '2017-09-01'),
(5, 'Rocky', 'male', '2020-02-01');

-- --------------------------------------------------------

--
-- 資料表結構 `pet_location`
--

CREATE TABLE `pet_location` (
  `ID` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `location_id` int(11) NOT NULL,
  `time` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `pet_location`
--

INSERT INTO `pet_location` (`ID`, `pet_id`, `location_id`, `time`) VALUES
(7, 1, 26, '2022-01-01 10:00:00'),
(8, 1, 27, '2022-01-01 14:00:00'),
(9, 1, 28, '2022-01-03 11:00:00');

-- --------------------------------------------------------

--
-- 資料表結構 `pet_record`
--

CREATE TABLE `pet_record` (
  `ID` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `record_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `pet_record`
--

INSERT INTO `pet_record` (`ID`, `pet_id`, `record_id`) VALUES
(1, 1, 1),
(2, 1, 2);

-- --------------------------------------------------------

--
-- 資料表結構 `user`
--

CREATE TABLE `user` (
  `ID` int(11) NOT NULL,
  `username` varchar(100) NOT NULL COMMENT '帳號',
  `password` varchar(255) NOT NULL COMMENT '密碼'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `user`
--

INSERT INTO `user` (`ID`, `username`, `password`) VALUES
(1, 'test', '1234');

-- --------------------------------------------------------

--
-- 資料表結構 `user_pet`
--

CREATE TABLE `user_pet` (
  `ID` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `user_pet`
--

INSERT INTO `user_pet` (`ID`, `user_id`, `pet_id`) VALUES
(1, 1, 1);

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `health_record`
--
ALTER TABLE `health_record`
  ADD PRIMARY KEY (`ID`);

--
-- 資料表索引 `location`
--
ALTER TABLE `location`
  ADD PRIMARY KEY (`ID`);

--
-- 資料表索引 `pet`
--
ALTER TABLE `pet`
  ADD PRIMARY KEY (`ID`);

--
-- 資料表索引 `pet_location`
--
ALTER TABLE `pet_location`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `pet_id` (`pet_id`),
  ADD KEY `pet_location_ibfk_2` (`location_id`);

--
-- 資料表索引 `pet_record`
--
ALTER TABLE `pet_record`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `record_id` (`record_id`),
  ADD KEY `pet_id` (`pet_id`);

--
-- 資料表索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`ID`);

--
-- 資料表索引 `user_pet`
--
ALTER TABLE `user_pet`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `pet_id` (`pet_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `health_record`
--
ALTER TABLE `health_record`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `location`
--
ALTER TABLE `location`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `pet`
--
ALTER TABLE `pet`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `pet_location`
--
ALTER TABLE `pet_location`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `pet_record`
--
ALTER TABLE `pet_record`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `user`
--
ALTER TABLE `user`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `user_pet`
--
ALTER TABLE `user_pet`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `pet_location`
--
ALTER TABLE `pet_location`
  ADD CONSTRAINT `pet_location_ibfk_1` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `pet_location_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `location` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `pet_record`
--
ALTER TABLE `pet_record`
  ADD CONSTRAINT `pet_record_ibfk_1` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`ID`),
  ADD CONSTRAINT `record_id` FOREIGN KEY (`record_id`) REFERENCES `health_record` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `user_pet`
--
ALTER TABLE `user_pet`
  ADD CONSTRAINT `pet_id` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
