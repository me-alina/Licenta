-- phpMyAdmin SQL Dump
-- version 4.1.4
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jun 25, 2016 at 01:14 AM
-- Server version: 5.6.15-log
-- PHP Version: 5.4.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `carsharing`
--

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE IF NOT EXISTS `feedback` (
  `id` int(11) NOT NULL,
  `id_masina` int(11) NOT NULL,
  `id_giving` varchar(23) NOT NULL,
  `id_recieving` varchar(23) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `masina`
--

CREATE TABLE IF NOT EXISTS `masina` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ruta_id` int(11) NOT NULL,
  `owner_id` varchar(23) NOT NULL,
  `pass_id1` varchar(23) DEFAULT NULL,
  `pass_id2` varchar(23) DEFAULT NULL,
  `pass_id3` varchar(23) DEFAULT NULL,
  `pass_id4` varchar(23) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `masina`
--

INSERT INTO `masina` (`id`, `ruta_id`, `owner_id`, `pass_id1`, `pass_id2`, `pass_id3`, `pass_id4`, `updated_at`) VALUES
(1, 0, '0', '0', '0', '0', '0', '0000-00-00 00:00:00'),
(2, 3, 'aaa', 'bbb', '', '', '', '0000-00-00 00:00:00'),
(3, 11, 'aaa', 'bbb', 'bbb', 'bbb', 'bbb', '0000-00-00 00:00:00'),
(4, 33, '573dacccc49268.06541459', 'bbb', 'bbb', 'bbb', NULL, '2016-06-19 14:23:45');

-- --------------------------------------------------------

--
-- Table structure for table `ruta`
--

CREATE TABLE IF NOT EXISTS `ruta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` varchar(23) NOT NULL,
  `time` text NOT NULL,
  `date` date NOT NULL,
  `fromplace` text NOT NULL,
  `toplace` text NOT NULL,
  `seats` int(22) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=37 ;

--
-- Dumping data for table `ruta`
--

INSERT INTO `ruta` (`id`, `owner_id`, `time`, `date`, `fromplace`, `toplace`, `seats`) VALUES
(1, '573dacccc49268.06541459', '16:38', '0000-00-00', 'silistra', 'AEM', 2),
(2, '573dacccc49268.06541459', '16:42', '0000-00-00', 'platanilor', 'gara', 2),
(3, '573dacccc49268.06541459', '16:45', '0000-00-00', 'euro', 'sagului', 3),
(4, '573dacccc49268.06541459', '16:46', '0000-00-00', 'euro', 'sagului', 3),
(5, '573dacccc49268.06541459', '17:59', '0000-00-00', 'euro', 'sagului', 3),
(6, '573dacccc49268.06541459', '18:19', '0000-00-00', 'lugojului', 'catedrala', 4),
(7, '573dacccc49268.06541459', '18:50', '0000-00-00', 'freidorf', 'aradului', 3),
(8, '573dacccc49268.06541459', '16:51', '0000-00-00', 'sagului', 'dumbravita', 1),
(9, '573dacccc49268.06541459', '22:30', '0000-00-00', 'iulius mall', 'judetean', 1),
(11, '573dacccc49268.06541459', '12:92', '0000-00-00', 'dd', '9oo', 1),
(12, '', '08:58', '0000-00-00', 'profi sagului', 'centru', 3),
(13, '573dacccc49268.06541459', '19:55', '2016-06-12', 'mall', 'lipovei', 3),
(14, '573dacccc49268.06541459', '23:55', '2016-06-12', 'silisTra', 'platanilor', 2),
(15, '573dacccc49268.06541459', '23:45', '2016-06-12', 'mall', 'gara', 3),
(16, '573dacccc49268.06541459', '23:22', '2016-06-12', 'Mall', 'Gara ', 3),
(17, '573dacccc49268.06541459', '23:34', '2016-06-12', 'mall', 'GaRa', 2),
(18, '573dacccc49268.06541459', '23:35', '2016-06-12', 'mall', 'GaRa', 2),
(19, '573dacccc49268.06541459', '23:37', '2016-06-12', 'mall', 'GaRa', 2),
(20, '573dacccc49268.06541459', '00:04', '2016-06-12', 'calea lipovei', 'gara', 1),
(21, '573dacccc49268.06541459', '00:10', '2016-06-13', 'here', 'there', 1),
(22, '573dacccc49268.06541459', '00:11', '0000-00-00', 'h', 'dd', 4),
(23, '573dacccc49268.06541459', '00:30', '0000-00-00', 'ssss', 'rrrr', 3),
(24, '573dacccc49268.06541459', '00:30', '0000-00-00', 'sed', 'ded', 2),
(25, '573dacccc49268.06541459', '01:37', '2016-06-12', 'yyy', 'nnn', 4),
(26, '573dacccc49268.06541459', '00:20', '2016-06-12', 'ff', 'gg', 4),
(27, '573dacccc49268.06541459', '00:45', '2016-06-12', '2016-06-13', 'ff', 0),
(28, '573dacccc49268.06541459', '00:45', '2016-06-13', 'dd', 'kk', 4),
(29, '573dacccc49268.06541459', '00:14', '0000-00-00', 'bb', 'mm', 1),
(30, '573dacccc49268.06541459', '00:55', '2016-06-13', 'gg', 'dd', 3),
(31, '573dacccc49268.06541459', '23:44', '2016-06-13', 'kk', 'jj', 2),
(32, '573dacccc49268.06541459', '20:41', '2016-06-18', 'centru', 'gara', 2),
(33, '573dacccc49268.06541459', '20:11', '2016-06-18', 'centru', 'gara', 2),
(34, '57664162677605.78263772', '10:42', '2016-06-23', 'tm', 'buc', 3),
(35, '57664162677605.78263772', '22:55', '2016-06-22', 'tm', 'buc', 4),
(36, '57664162677605.78263772', '15:01', '2016-06-30', 'timisoara', 'cluj', 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(23) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `picture` blob,
  `phone` text,
  `city` text,
  `about` longtext,
  `rating_cnt` int(255) DEFAULT NULL,
  `rating_val` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_id` (`unique_id`),
  UNIQUE KEY `email` (`email`(30))
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `updated_at`, `picture`, `phone`, `city`, `about`, `rating_cnt`, `rating_val`) VALUES
(2, '573dacccc49268.06541459', 'Igneaa', 'a@gmail.com', 'ktJeUWzWni81CZFDda3RvF7PHPw0ZWMwYTAwMjA3', '4ec0a00207', '2016-05-19 15:08:44', '2016-06-07 01:47:11', NULL, '0726760914', 'Timisoara', 'ce interesant lorem ipsum bla bla imi bag de ce nu mere', 4, 4.3),
(3, '57657a81509c21.10785889', 'Miha', 'm@yahoo.com', 'QVyRmKrVIi5S24ECCpPdxutYHww1ODMwYTEwNzgz', '5830a10783', '2016-06-18 19:44:49', '2016-06-18 19:46:27', NULL, 'null00', NULL, 'bbbb\nnull', 7, 5),
(4, '57664162677605.78263772', 'Alina', 'ignea.alina@gmail.com', 'gnP4H/yfImryAZNHkgSNmQSoU183N2M5ODI5MGU3', '77c98290e7', '2016-06-19 09:53:22', NULL, NULL, '07', 'Timisoara', '', 0, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
