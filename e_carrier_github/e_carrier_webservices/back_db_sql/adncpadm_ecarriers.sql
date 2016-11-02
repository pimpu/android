-- phpMyAdmin SQL Dump
-- version 4.0.10.14
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Nov 02, 2016 at 12:42 PM
-- Server version: 10.0.28-MariaDB
-- PHP Version: 5.6.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `adncpadm_ecarriers`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookedserviceinfo`
--

CREATE TABLE IF NOT EXISTS `bookedserviceinfo` (
  `bookedId` int(11) NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fk_enquiryId` int(11) NOT NULL,
  `fk_driverId` int(11) NOT NULL,
  `status` varchar(5) NOT NULL,
  `enquired_lats` varchar(50) NOT NULL,
  `enquired_longs` varchar(50) NOT NULL,
  `enquired_meter` decimal(10,2) DEFAULT NULL,
  `bill_amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`bookedId`),
  KEY `fk_enquiryId` (`fk_enquiryId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `bookedserviceinfo`
--

INSERT INTO `bookedserviceinfo` (`bookedId`, `creationTime`, `fk_enquiryId`, `fk_driverId`, `status`, `enquired_lats`, `enquired_longs`, `enquired_meter`, `bill_amount`) VALUES
(7, '2016-05-08 09:00:53', 7, 25, 'open', '19.2119178', '72.8725665', '1000.00', NULL),
(8, '2016-05-31 16:32:44', 8, 0, 'open', '19.0935328', '72.8567107', '1000.00', NULL),
(9, '2016-06-28 15:17:35', 9, 0, 'open', '19.1226022', '72.8880032', '1000.00', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `bookingenquiry`
--

CREATE TABLE IF NOT EXISTS `bookingenquiry` (
  `enquiryId` int(11) NOT NULL AUTO_INCREMENT,
  `fk_userId` int(11) NOT NULL,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `beginningArea` varchar(100) NOT NULL,
  `destinationArea` varchar(100) NOT NULL,
  `deliveryDateTime` varchar(50) NOT NULL,
  `weight` decimal(10,2) NOT NULL,
  `unit` varchar(10) NOT NULL,
  `vehicleType` varchar(10) NOT NULL,
  `vehicle` longtext NOT NULL,
  `valueAddedServices` varchar(50) NOT NULL,
  `pickupAddress` varchar(100) NOT NULL,
  `deliveryAddress` varchar(100) NOT NULL,
  `kgForExtraManPwr` longtext NOT NULL,
  PRIMARY KEY (`enquiryId`),
  KEY `fk_userId` (`fk_userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `bookingenquiry`
--

INSERT INTO `bookingenquiry` (`enquiryId`, `fk_userId`, `creationTime`, `beginningArea`, `destinationArea`, `deliveryDateTime`, `weight`, `unit`, `vehicleType`, `vehicle`, `valueAddedServices`, `pickupAddress`, `deliveryAddress`, `kgForExtraManPwr`) VALUES
(7, 23, '2016-05-08 09:00:53', 'Mumbai, Maharashtra, India', 'Chennai International Airport, Meenambakkam, Chennai, Tamil Nadu, India', '0', '600.00', 'Kg', 'Closed', 'MARUTI VAN (Cargo)', '[packing,labour_charges]', 'Mumbai', 'Chennai', '0'),
(8, 23, '2016-05-31 16:32:43', 'Mumbai Airport, Mumbai, Maharashtra, India', 'Chennai, Tamil Nadu, India', '1464712200000', '600.00', 'Kg', 'Open', 'MARUTI VAN (Cargo)', '[noVAS]', 'Andheri', 'Chennai Airport ', '0'),
(9, 23, '2016-06-28 15:17:35', 'Mumbai, Maharashtra, India', 'Chennai, Tamil Nadu, India', '1467126900000', '600.00', 'Kg', 'Closed', 'MARUTI VAN (Cargo)', '[noVAS]', 'Mumbai ', 'Chennai ', '0');

-- --------------------------------------------------------

--
-- Table structure for table `drivervehicletype`
--

CREATE TABLE IF NOT EXISTS `drivervehicletype` (
  `driver_vehicle_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `vehicle` varchar(100) NOT NULL,
  `vehicle_type` varchar(10) NOT NULL,
  `fk_driver_uesr_id` int(11) NOT NULL,
  `lats` varchar(50) DEFAULT NULL,
  `longs` varchar(50) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`driver_vehicle_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `drivervehicletype`
--

INSERT INTO `drivervehicletype` (`driver_vehicle_type_id`, `creationTime`, `vehicle`, `vehicle_type`, `fk_driver_uesr_id`, `lats`, `longs`, `postal_code`) VALUES
(2, '2016-04-30 09:24:18', 'MARUTI VAN (Cargo)', 'open', 25, '19.1226019', '72.8882651', '400072');

-- --------------------------------------------------------

--
-- Table structure for table `profilepic`
--

CREATE TABLE IF NOT EXISTS `profilepic` (
  `fk_userId` int(11) NOT NULL AUTO_INCREMENT,
  `image` blob NOT NULL,
  PRIMARY KEY (`fk_userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `requestingalldriver`
--

CREATE TABLE IF NOT EXISTS `requestingalldriver` (
  `requesting_all_driver_id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_booked_id` int(11) NOT NULL,
  `requesting_driver` int(11) DEFAULT NULL,
  PRIMARY KEY (`requesting_all_driver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `userlogin`
--

CREATE TABLE IF NOT EXISTS `userlogin` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `emailId` varchar(30) NOT NULL,
  `password` varchar(20) NOT NULL,
  `phoneNo` varchar(15) NOT NULL,
  `fullName` varchar(30) NOT NULL,
  `gcmId` longtext,
  `userType` char(8) DEFAULT NULL,
  `isLogin` varchar(5) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=26 ;

--
-- Dumping data for table `userlogin`
--

INSERT INTO `userlogin` (`userId`, `creationTime`, `emailId`, `password`, `phoneNo`, `fullName`, `gcmId`, `userType`, `isLogin`) VALUES
(23, '2016-04-30 09:09:21', 'jijoy.joseph@gmail.com', 'test@123', '8655664017', 'Jijoy Joseph', 'APA91bHAuhTlfmkte4E2YgtgjCGKJj6SyONa-VqiUhznCmei_grjBE419oXFsgd6sfPcjMggHFA_6KTl4zfqcZYh2d7tTd1-cHVFShlfLMcsao65HGuYlm6Wv4Ur2pDW6tJl64n75zR3r09FCnpMNYVwagZiQHcHfw', 'customer', 'true'),
(25, '2016-04-30 09:24:18', 'driver@test.com', 'test@123', '8694646494', 'Driver', 'APA91bHjSxhRl5fBGSozniACJRgPsXinYqsyX5bJ98xTDShsTPm2akmZVPk-H_miLNVIIRtqF0GQUB65H4CZXn6kL8a8VRCqaBJ_gINHQyuEsLQRfRF6QXGbwtSocf2Pl_ymKu9Gdb3mnd-p0iNC2EKpoeRPMRTCyQ', 'driver', 'false');

-- --------------------------------------------------------

--
-- Table structure for table `valueaddedservices`
--

CREATE TABLE IF NOT EXISTS `valueaddedservices` (
  `vas_id` int(11) NOT NULL AUTO_INCREMENT,
  `manpower` int(11) NOT NULL,
  `insurance` decimal(1,1) NOT NULL,
  `risk` int(11) NOT NULL,
  `packing` int(11) NOT NULL,
  `handling` int(11) NOT NULL,
  `holiday_delivery` int(11) NOT NULL,
  `shipment` int(11) NOT NULL,
  `labour_charges` int(11) NOT NULL,
  `vehicle` longtext NOT NULL,
  `rsPerKm` int(11) NOT NULL,
  PRIMARY KEY (`vas_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `valueaddedservices`
--

INSERT INTO `valueaddedservices` (`vas_id`, `manpower`, `insurance`, `risk`, `packing`, `handling`, `holiday_delivery`, `shipment`, `labour_charges`, `vehicle`, `rsPerKm`) VALUES
(1, 50, '0.2', 2, 0, 0, 400, 0, 100, 'MARUTI VAN (Cargo)', 25),
(2, 50, '0.2', 2, 0, 0, 400, 0, 100, 'TATA ZIP', 25),
(3, 75, '0.2', 2, 0, 0, 500, 0, 150, 'TATA ACE', 30),
(4, 80, '0.2', 2, 0, 0, 800, 0, 150, 'TATA 407', 50),
(5, 80, '0.2', 2, 0, 0, 1500, 0, 200, 'TATA 709', 60),
(6, 100, '0.2', 2, 0, 0, 2000, 0, 250, 'TATA 909', 60),
(7, 150, '0.2', 2, 0, 0, 2500, 0, 400, 'TATA 1109 / EICHER 1110', 80);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookingenquiry`
--
ALTER TABLE `bookingenquiry`
  ADD CONSTRAINT `bookingenquiry_ibfk_1` FOREIGN KEY (`fk_userId`) REFERENCES `userlogin` (`userId`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
