-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: eloanbootsecuredb
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Dumping data for table `loan`
--

LOCK TABLES `loan` WRITE;
/*!40000 ALTER TABLE `loan` DISABLE KEYS */;
INSERT INTO `loan` VALUES (1,'Salaried','Organization',6,1000000,'2020-12-03 02:07:00','My First Loan','Rejected due to high amount',-1,'Tax-Payer'),(2,'Non-Salaried','Individual',6,500000,'2020-12-03 02:07:00','My Second Loan',NULL,2,'Tax-Payer'),(3,'Non-Salaried','Individual',6,500000,'2020-12-03 02:07:00','My Third Loan',NULL,2,'Tax-Payer'),(4,'Billed','Organization',7,100000,'2020-12-04','new loan',NULL,0,'test'),(5,NULL,NULL,NULL,NULL,NULL,NULL,'Rejected due to high amount',NULL,NULL),(6,'Billed','Organization',6,0,'2020-12-04','new loan',NULL,0,'test'),(7,'Billed','Organization',6,0,'2020-12-06','new loan',NULL,0,'test'),(8,'Billed','Organization',6,0,'2020-12-06','new loan 2',NULL,0,'test'),(9,'Billed','Organization',6,0,'2020-12-06','new loan 3',NULL,0,'test');
/*!40000 ALTER TABLE `loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `processing_info`
--

LOCK TABLES `processing_info` WRITE;
/*!40000 ALTER TABLE `processing_info` DISABLE KEYS */;
INSERT INTO `processing_info` VALUES (1,2,'101, Street No. 1, Secbad-10','test',500000,1,3,550000,'2020-12-04'),(9,3,'101, Street No. 1, Secbad-10','test new',800000,2,3,1050000,'2020-12-04'),(10,0,'10','test new',800000,3,3,1050000,'2020-12-04');
/*!40000 ALTER TABLE `processing_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sanction_info`
--

LOCK TABLES `sanction_info` WRITE;
/*!40000 ALTER TABLE `sanction_info` DISABLE KEYS */;
INSERT INTO `sanction_info` VALUES (1,1000000,2,'2035-12-04',5,156823384855.61926,'2020-12-04',15),(2,1000000,3,'2035-12-04',5,23199.01309523811,'2020-12-04',7);
/*!40000 ALTER TABLE `sanction_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'test@gmail.com','first clerk','name','1234567890','Loan Clerk'),(2,'te123@gmail.com','first manager','last name','1234567890','Manager'),(3,'te123@gmail.com','admin register','last name','1234567890','Loan Clerk'),(4,'te123@gmail.com','admin register','last name','1234567890','Loan Clerk'),(5,'te123@gmail.com','admin register','last name','1234567890','Manager'),(6,'te123@gmail.com','cust register','last name','1234567890','Customer'),(7,'te123@gmail.com','cust register 2','last name','1234567890','Customer');
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

-- Dump completed on 2020-12-06 18:20:48
