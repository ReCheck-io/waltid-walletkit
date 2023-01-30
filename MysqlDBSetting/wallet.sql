-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: localhost    Database: wallet
-- ------------------------------------------------------
-- Server version	8.0.32-0buntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `issuances`
--

DROP TABLE IF EXISTS `issuances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issuances` (
  `issuanceID` varchar(255) NOT NULL,
  `id` varchar(255) DEFAULT NULL,
  `sessionID` varchar(255) DEFAULT NULL,
  `vc` mediumtext,
  PRIMARY KEY (`issuanceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuances`
--

LOCK TABLES `issuances` WRITE;
/*!40000 ALTER TABLE `issuances` DISABLE KEYS */;
INSERT INTO `issuances` VALUES ('b43b4714-ad22-4353-98c3-a2bd161e9bda','yordan@gmail.com','b1bb8604-7f50-4e95-b9d7-d926f18a160a','empty');
/*!40000 ALTER TABLE `issuances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sessionID` varchar(255) DEFAULT NULL,
  `did` varchar(255) DEFAULT NULL,
  `issuerID` varchar(255) DEFAULT NULL,
  `issuanceID` varchar(255) DEFAULT NULL,
  `schemaIDs` varchar(255) DEFAULT NULL,
  `walletRedirectUri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `did` varchar(255) DEFAULT NULL,
  `issuer` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('daka@gmail.com','71138deab2cce562c9233a27b7e8036b7d8c5040ad62fcaadf9171e9a7b953d8','did:key:z6MkrgGn89hMd2Tpe2LKgCXhkDw56SuGyVhke453JXHjxDug',0),('emo@emo.com','dca9cd96e0a72fd1211357f3e2f2236ec824839f15c9c146118866affc284a08','did:key:z6Mktr88e8yrwwCALpHqW82wxjGWJcAXPDi2nDKppCW6brCt',0),('issuer@recheck.io','a604b80498eb61634e21ba22635d678ea77e57ce839c091ca62edd4bde9a842d','did:key:z6MkqMJkWr16u96UUFi4P22jJ8FtboRZq6ubf7UeMJAffwRh',0),('yordan@gmail.com','71138deab2cce562c9233a27b7e8036b7d8c5040ad62fcaadf9171e9a7b953d8','did:key:z6MkuPSWgnVmQNFTGia3gF1VkGYEP86MCf1UWuXpLcHJpA6M',0),('yordan@recheck.io','71138deab2cce562c9233a27b7e8036b7d8c5040ad62fcaadf9171e9a7b953d8','did:key:z6MksR6xxrMdC8fNS94YBTc6y44heX1D8iamWDJMo8msf6Ci',0);
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

-- Dump completed on 2023-01-30 15:26:19
