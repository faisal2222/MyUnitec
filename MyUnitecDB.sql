CREATE DATABASE  IF NOT EXISTS `myunitecdb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `myunitecdb`;
-- MySQL dump 10.13  Distrib 5.6.17, for osx10.6 (i386)
--
-- Host: localhost    Database: myunitecdb
-- ------------------------------------------------------
-- Server version	5.6.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `grades`
--

DROP TABLE IF EXISTS `grades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grades` (
  `assesment` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `moduleid` int(11) NOT NULL,
  `semester` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `grade` varchar(10) DEFAULT '00.00',
  PRIMARY KEY (`assesment`,`username`,`moduleid`,`semester`,`year`),
  KEY `gradeFK1_idx` (`moduleid`),
  KEY `gradeFK2_idx` (`username`),
  KEY `GradesFK1_idx` (`username`,`moduleid`,`semester`,`year`),
  CONSTRAINT `GradesFK1` FOREIGN KEY (`username`, `moduleid`, `semester`, `year`) REFERENCES `moduleenrollment` (`username`, `moduleid`, `semester`, `year`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grades`
--

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;
INSERT INTO `grades` VALUES ('Assignment 1','hampton',5420,2,2016,'86.50'),('Assignment 2','hampton',5420,2,2016,'93.70');
/*!40000 ALTER TABLE `grades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `module` (
  `moduleid` int(11) NOT NULL,
  `modulename` varchar(100) NOT NULL,
  PRIMARY KEY (`moduleid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `module`
--

LOCK TABLES `module` WRITE;
/*!40000 ALTER TABLE `module` DISABLE KEYS */;
INSERT INTO `module` VALUES (5420,'programming fundamentals'),(5422,'multimedia and website development '),(5430,'Professional Skills for IT Practitioners'),(6411,'Project Planning and Control');
/*!40000 ALTER TABLE `module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moduleenrollment`
--

DROP TABLE IF EXISTS `moduleenrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moduleenrollment` (
  `username` varchar(100) NOT NULL,
  `moduleid` int(11) NOT NULL,
  `semester` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `status` varchar(100) NOT NULL,
  PRIMARY KEY (`username`,`moduleid`,`semester`,`year`),
  KEY `modulefk4_idx` (`moduleid`),
  CONSTRAINT `modulefk4` FOREIGN KEY (`moduleid`) REFERENCES `module` (`moduleid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userfk1` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moduleenrollment`
--

LOCK TABLES `moduleenrollment` WRITE;
/*!40000 ALTER TABLE `moduleenrollment` DISABLE KEYS */;
INSERT INTO `moduleenrollment` VALUES ('hampton',5420,2,2016,'enrolled'),('hampton',5420,2,2017,'pending'),('hampton',5430,2,2016,'enrolled');
/*!40000 ALTER TABLE `moduleenrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moduletimetable`
--

DROP TABLE IF EXISTS `moduletimetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moduletimetable` (
  `moduleid` int(11) NOT NULL,
  `semester` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  PRIMARY KEY (`moduleid`,`semester`,`year`),
  CONSTRAINT `modulefk1` FOREIGN KEY (`moduleid`) REFERENCES `module` (`moduleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moduletimetable`
--

LOCK TABLES `moduletimetable` WRITE;
/*!40000 ALTER TABLE `moduletimetable` DISABLE KEYS */;
INSERT INTO `moduletimetable` VALUES (5420,2,0);
/*!40000 ALTER TABLE `moduletimetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programme`
--

DROP TABLE IF EXISTS `programme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programme` (
  `programmeid` int(11) NOT NULL,
  `programmename` varchar(100) NOT NULL,
  PRIMARY KEY (`programmeid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programme`
--

LOCK TABLES `programme` WRITE;
/*!40000 ALTER TABLE `programme` DISABLE KEYS */;
INSERT INTO `programme` VALUES (111,'Bacholer of IT'),(222,'Bacholer of business'),(333,'Bachelor of Networking');
/*!40000 ALTER TABLE `programme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programmeenrollment`
--

DROP TABLE IF EXISTS `programmeenrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programmeenrollment` (
  `username` varchar(100) NOT NULL,
  `programmeid` int(11) NOT NULL,
  `startdate` varchar(50) NOT NULL,
  `enddate` varchar(50) DEFAULT NULL,
  `status` varchar(100) NOT NULL,
  PRIMARY KEY (`username`,`programmeid`),
  KEY `programmefk_idx` (`programmeid`),
  CONSTRAINT `programmefk` FOREIGN KEY (`programmeid`) REFERENCES `programme` (`programmeid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userfk` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programmeenrollment`
--

LOCK TABLES `programmeenrollment` WRITE;
/*!40000 ALTER TABLE `programmeenrollment` DISABLE KEYS */;
INSERT INTO `programmeenrollment` VALUES ('hampton',111,'24/2/2014',NULL,'enrolled'),('hampton',222,'24/2/2014',NULL,'enrolled'),('hampton',333,'27/2/2015',NULL,'enrolled'),('testuser',111,'27/2/2015',NULL,'enrolled');
/*!40000 ALTER TABLE `programmeenrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programmemodule`
--

DROP TABLE IF EXISTS `programmemodule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programmemodule` (
  `programmeid` int(11) NOT NULL,
  `moduleid` int(11) NOT NULL,
  PRIMARY KEY (`programmeid`,`moduleid`),
  KEY `modulefk_idx` (`moduleid`),
  CONSTRAINT `modulefk` FOREIGN KEY (`moduleid`) REFERENCES `module` (`moduleid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `programmefk1` FOREIGN KEY (`programmeid`) REFERENCES `programme` (`programmeid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programmemodule`
--

LOCK TABLES `programmemodule` WRITE;
/*!40000 ALTER TABLE `programmemodule` DISABLE KEYS */;
INSERT INTO `programmemodule` VALUES (111,5420),(111,5422),(222,5422),(111,5430),(222,5430),(111,6411),(222,6411);
/*!40000 ALTER TABLE `programmemodule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timetableentry`
--

DROP TABLE IF EXISTS `timetableentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timetableentry` (
  `weekday` varchar(100) NOT NULL,
  `room` varchar(10) NOT NULL,
  `timeperiod` varchar(100) NOT NULL,
  `semester` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `moduleid` int(11) NOT NULL,
  PRIMARY KEY (`weekday`,`room`,`timeperiod`,`semester`,`year`),
  KEY `modulefk3_idx` (`moduleid`),
  CONSTRAINT `modulefk3` FOREIGN KEY (`moduleid`) REFERENCES `moduletimetable` (`moduleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timetableentry`
--

LOCK TABLES `timetableentry` WRITE;
/*!40000 ALTER TABLE `timetableentry` DISABLE KEYS */;
INSERT INTO `timetableentry` VALUES ('sunday','G105','13:00,14:00',2,2016,5420);
/*!40000 ALTER TABLE `timetableentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(100) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `password` varchar(24) NOT NULL,
  `salt` varchar(24) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('hampton','Bahman','Sarrafpour','rbLDm/Zuas+aoZNVkIFq6g==','LXRbRcwn+kCrWr9DEsgu+A=='),('testuser','Joe','Bloggs','klP79ic4dBgZ4WkGIgMcOA==','uw2hdlTj29UaentbN33Kgw==');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-06 19:39:24
