-- phpMyAdmin SQL Dump
-- version 4.0.10.14
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Nov 02, 2016 at 12:43 PM
-- Server version: 10.0.28-MariaDB
-- PHP Version: 5.6.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `adncpadm_kissan`
--

-- --------------------------------------------------------

--
-- Table structure for table `enquiry`
--

CREATE TABLE IF NOT EXISTS `enquiry` (
  `eID` int(11) NOT NULL AUTO_INCREMENT,
  `eRef` varchar(20) NOT NULL,
  `uID` int(11) NOT NULL,
  `gID` int(11) NOT NULL,
  `repTo` int(11) NOT NULL,
  `replied` tinyint(4) NOT NULL DEFAULT '0',
  `eMsg` longtext NOT NULL,
  `eSociety` varchar(50) NOT NULL,
  `eSocAdrs` varchar(50) NOT NULL,
  `eSocCont` varchar(20) NOT NULL,
  `eSocEmail` varchar(50) NOT NULL,
  `eDoc` varchar(50) NOT NULL,
  `eCost` varchar(50) NOT NULL,
  `eTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`eID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=39 ;

--
-- Dumping data for table `enquiry`
--

INSERT INTO `enquiry` (`eID`, `eRef`, `uID`, `gID`, `repTo`, `replied`, `eMsg`, `eSociety`, `eSocAdrs`, `eSocCont`, `eSocEmail`, `eDoc`, `eCost`, `eTime`, `status`) VALUES
(20, 'REF240216123041', 1, 2, 2, 1, 'check', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Chrysanthemum.jpg', '', '2016-02-23 23:30:54', 1),
(21, 'REF240216123041', 2, 1, 1, 1, 'Not Done', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Desert.jpg', '', '2016-02-23 23:32:57', 1),
(22, 'REF240216123041', 1, 2, 2, 1, 'asad', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Hydrangeas.jpg', '', '2016-02-23 23:46:36', 1),
(23, 'REF240216031128', 1, 2, 2, 0, 'nEW tEST', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Tulips.jpg', '', '2016-02-24 02:11:43', 1),
(24, 'REF240216110056', 1, 2, 2, 0, 'First Online', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Chrysanthemum.jpg', '', '2016-02-24 11:02:34', 1),
(25, 'REF240216110815', 1, 2, 2, 0, '@nd Test', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Tulips.jpg', '', '2016-02-24 11:08:55', 1),
(26, 'REF250216052713', 3, 2, 2, 1, 'hello praveen', 'Rajasva', '', '', '', '1men.JPG', '', '2016-02-25 05:28:40', 1),
(27, 'REF250216052713', 2, 1, 3, 1, 'Done', 'Rajasva', '', '', '', 'Koala.jpg', '', '2016-02-25 05:32:21', 1),
(28, 'REF250216052713', 3, 2, 2, 1, 'hw r u?', 'Rajasva', '', '', '', 'blackxls.JPG', '', '2016-02-25 05:45:09', 1),
(29, 'REF250216052713', 2, 1, 3, 0, 'i m fyn', 'Rajasva', '', '', '', 'Desert.jpg', '', '2016-02-25 05:45:57', 1),
(30, 'REF240216123041', 2, 1, 1, 1, 'TEst', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Lighthouse.jpg', '', '2016-02-25 09:08:56', 1),
(31, 'REF240216123041', 1, 2, 2, 1, 'Hiiee', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Tulips.jpg', '', '2016-02-25 09:15:11', 1),
(32, 'REF240216123041', 2, 1, 1, 1, 'yes dude', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Tulips.jpg', '', '2016-02-25 09:16:07', 1),
(33, 'REF240216123041', 1, 2, 2, 1, 'ssdsd', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Tulips.jpg', '', '2016-02-25 09:20:15', 1),
(34, 'REF240216123041', 2, 1, 1, 1, 'heeloo', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Jellyfish.jpg', '', '2016-02-25 09:27:20', 1),
(35, 'REF240216123041', 1, 2, 2, 1, 'ssd', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Tulips.jpg', '', '2016-02-25 09:29:03', 1),
(36, 'REF240216123041', 2, 1, 1, 0, 'dfdf', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Desert.jpg', '', '2016-02-25 09:29:26', 1),
(37, 'REF250216100843', 1, 2, 2, 1, 'DOn1', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Chrysanthemum.jpg', '', '2016-02-25 10:09:30', 1),
(38, 'REF250216100843', 2, 1, 1, 0, 'Hello sir it is ordered', 'Rajasva1', 'asas', '9988776655', 'bbc@gmail.com', 'Koala.jpg', '', '2016-02-25 10:10:40', 1);

-- --------------------------------------------------------

--
-- Table structure for table `orderdetail`
--

CREATE TABLE IF NOT EXISTS `orderdetail` (
  `ordID` int(11) NOT NULL AUTO_INCREMENT,
  `enqRef` varchar(50) NOT NULL,
  `UTR` varchar(50) NOT NULL,
  `ordItem` varchar(50) NOT NULL,
  `ordQty` int(11) NOT NULL,
  `ordPrice` varchar(50) NOT NULL,
  `totamnt` varchar(50) NOT NULL,
  `ordDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ordStatus` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ordID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=43 ;

--
-- Dumping data for table `orderdetail`
--

INSERT INTO `orderdetail` (`ordID`, `enqRef`, `UTR`, `ordItem`, `ordQty`, `ordPrice`, `totamnt`, `ordDate`, `ordStatus`) VALUES
(42, 'REF240216123041', 'SBINC12345678', 'UREA', 12, '21', '12', '2016-02-24 02:06:06', 1);

-- --------------------------------------------------------

--
-- Table structure for table `society`
--

CREATE TABLE IF NOT EXISTS `society` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `soc_name` varchar(100) NOT NULL,
  `soc_contact` varchar(100) NOT NULL,
  `soc_email` varchar(100) NOT NULL,
  `soc_adrs` varchar(200) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `society`
--

INSERT INTO `society` (`id`, `soc_name`, `soc_contact`, `soc_email`, `soc_adrs`, `status`) VALUES
(7, 'Rajasva', '9988776655', 'abc@gmail.com', 'asas', 1),
(8, 'Rajasva1', '9988776655', 'bbc@gmail.com', 'asas', 1);

-- --------------------------------------------------------

--
-- Table structure for table `userdetails`
--

CREATE TABLE IF NOT EXISTS `userdetails` (
  `uID` int(11) NOT NULL AUTO_INCREMENT,
  `gID` int(11) NOT NULL,
  `uName` varchar(50) NOT NULL,
  `uStoreName` varchar(50) NOT NULL,
  `uEmailID` varchar(50) NOT NULL,
  `uPassword` varchar(50) NOT NULL,
  `uCont` varchar(20) NOT NULL,
  `uAddrs` varchar(300) NOT NULL,
  `uPin` int(10) NOT NULL,
  `uCity` varchar(50) NOT NULL,
  `uState` varchar(50) NOT NULL,
  `uCountry` varchar(50) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`uID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `userdetails`
--

INSERT INTO `userdetails` (`uID`, `gID`, `uName`, `uStoreName`, `uEmailID`, `uPassword`, `uCont`, `uAddrs`, `uPin`, `uCity`, `uState`, `uCountry`, `status`) VALUES
(1, 2, 'Praveen', 'Sangli Store', 'praveen@gmail.com', 'Design@123', '8080651339', '', 400607, 'Thane', 'Maharashtra', 'India', 1),
(2, 1, 'Sony', '', 'sony@gmail.com', 'sony123', '9988776655', '', 400603, 'Mulund', 'Maharashtra', 'India', 1),
(3, 2, 'Shweta', 'Seeds Store', 'swetadwivedi1603@gmail.com', '12345678', '9988776655', 'Punjab Chandigarh', 400003, 'Chandigarh', 'Punjab', 'India', 1);

-- --------------------------------------------------------

--
-- Table structure for table `usergroup`
--

CREATE TABLE IF NOT EXISTS `usergroup` (
  `gId` int(11) NOT NULL AUTO_INCREMENT,
  `gName` varchar(50) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`gId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `usergroup`
--

INSERT INTO `usergroup` (`gId`, `gName`, `status`) VALUES
(1, 'admin', 1),
(2, 'obp', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
