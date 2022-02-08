-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: banka2
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `isplata`
--

DROP TABLE IF EXISTS `isplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `isplata` (
  `IdTra` int NOT NULL,
  `IdFil` int NOT NULL,
  PRIMARY KEY (`IdTra`),
  CONSTRAINT `FK_IdTra_isplata` FOREIGN KEY (`IdTra`) REFERENCES `transakcija` (`IdTra`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `isplata`
--

LOCK TABLES `isplata` WRITE;
/*!40000 ALTER TABLE `isplata` DISABLE KEYS */;
INSERT INTO `isplata` VALUES (3,2),(6,2);
/*!40000 ALTER TABLE `isplata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `komitent`
--

DROP TABLE IF EXISTS `komitent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `komitent` (
  `IdKom` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdKom`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `komitent`
--

LOCK TABLES `komitent` WRITE;
/*!40000 ALTER TABLE `komitent` DISABLE KEYS */;
INSERT INTO `komitent` VALUES (1,'Pera Peric','Vuka Karadzica 1',2),(2,'Mika Mikic','Viktora Bubnja 16',2),(3,'Zika Zikic','Negotinska bb',1),(4,'Tika Spic','Pirotska 22',3);
/*!40000 ALTER TABLE `komitent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenos`
--

DROP TABLE IF EXISTS `prenos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenos` (
  `IdTra` int NOT NULL,
  `NaRac` int NOT NULL,
  PRIMARY KEY (`IdTra`),
  KEY `FK_IdRac_prenos_idx` (`NaRac`),
  CONSTRAINT `FK_IdRac_prenos` FOREIGN KEY (`NaRac`) REFERENCES `racun` (`IdRac`) ON UPDATE CASCADE,
  CONSTRAINT `FK_IdTra_prenos` FOREIGN KEY (`IdTra`) REFERENCES `transakcija` (`IdTra`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenos`
--

LOCK TABLES `prenos` WRITE;
/*!40000 ALTER TABLE `prenos` DISABLE KEYS */;
INSERT INTO `prenos` VALUES (4,4),(2,5);
/*!40000 ALTER TABLE `prenos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `racun`
--

DROP TABLE IF EXISTS `racun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `racun` (
  `IdRac` int NOT NULL AUTO_INCREMENT,
  `DatumVreme` datetime NOT NULL,
  `Stanje` float NOT NULL,
  `DozvMinus` float NOT NULL,
  `Status` varchar(45) NOT NULL,
  `BrTransakcija` int NOT NULL,
  `IdFil` int NOT NULL,
  `IdKom` int NOT NULL,
  PRIMARY KEY (`IdRac`),
  KEY `FK_IdKom_racun_idx` (`IdKom`),
  CONSTRAINT `FK_IdKom_racun` FOREIGN KEY (`IdKom`) REFERENCES `komitent` (`IdKom`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `racun`
--

LOCK TABLES `racun` WRITE;
/*!40000 ALTER TABLE `racun` DISABLE KEYS */;
INSERT INTO `racun` VALUES (1,'2020-11-01 12:00:10',5000,2000,'Aktivan',2,1,3),(2,'2021-12-20 13:04:01',0,3000,'Aktivan',0,3,1),(3,'2019-02-20 14:04:01',-1000,1000,'Blokiran',1,2,2),(4,'2022-01-20 14:04:01',2000,1000,'Aktivan',3,2,3),(5,'2018-02-20 17:14:21',0,5000,'Ugasen',2,4,4);
/*!40000 ALTER TABLE `racun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transakcija`
--

DROP TABLE IF EXISTS `transakcija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transakcija` (
  `IdTra` int NOT NULL AUTO_INCREMENT,
  `DatumVreme` datetime NOT NULL,
  `Iznos` float NOT NULL,
  `RedBr` int NOT NULL,
  `Svrha` varchar(45) NOT NULL,
  `IdRac` int NOT NULL,
  PRIMARY KEY (`IdTra`),
  KEY `FK_IdRac_transakcija_idx` (`IdRac`),
  CONSTRAINT `FK_IdRac_transakcija` FOREIGN KEY (`IdRac`) REFERENCES `racun` (`IdRac`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transakcija`
--

LOCK TABLES `transakcija` WRITE;
/*!40000 ALTER TABLE `transakcija` DISABLE KEYS */;
INSERT INTO `transakcija` VALUES (1,'2020-12-01 20:03:14',10000,1,'Uplata za stan',1),(2,'2020-12-02 21:53:14',5000,2,'Neodredjeno',1),(3,'2022-04-02 11:30:14',1000,1,'Podizaj novca',3),(4,'2022-01-12 11:30:14',5000,1,'Uplata letovanje',5),(5,'2022-01-12 11:31:14',1000,1,'Uplata novca',4),(6,'2022-01-13 11:31:14',4000,2,'Podizanje novca',4);
/*!40000 ALTER TABLE `transakcija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uplata`
--

DROP TABLE IF EXISTS `uplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uplata` (
  `IdTra` int NOT NULL,
  `IdFil` int NOT NULL,
  PRIMARY KEY (`IdTra`),
  CONSTRAINT `FK_IdTra_uplata` FOREIGN KEY (`IdTra`) REFERENCES `transakcija` (`IdTra`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uplata`
--

LOCK TABLES `uplata` WRITE;
/*!40000 ALTER TABLE `uplata` DISABLE KEYS */;
INSERT INTO `uplata` VALUES (1,1),(5,2);
/*!40000 ALTER TABLE `uplata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-06  9:02:02
