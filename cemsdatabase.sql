-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cemsdatabase
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `CourseID` varchar(2) NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `maxExamNumber` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`CourseID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('01','Advanced Java Programming','02'),('02','Data Structures and Algorithms with Java','01'),('03','Introduction to Python','00'),('04','Advanced English','00'),('05','C++ for Beginners','00'),('06','Discrete Math','00');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coursedepartment`
--

DROP TABLE IF EXISTS `coursedepartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coursedepartment` (
  `CourseID` varchar(50) NOT NULL,
  `DepartmentID` varchar(50) NOT NULL,
  PRIMARY KEY (`CourseID`,`DepartmentID`),
  KEY `did_idx` (`DepartmentID`),
  CONSTRAINT `CID` FOREIGN KEY (`CourseID`) REFERENCES `course` (`CourseID`),
  CONSTRAINT `DeparID` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coursedepartment`
--

LOCK TABLES `coursedepartment` WRITE;
/*!40000 ALTER TABLE `coursedepartment` DISABLE KEYS */;
INSERT INTO `coursedepartment` VALUES ('01','1'),('01','2');
/*!40000 ALTER TABLE `coursedepartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coursesubject`
--

DROP TABLE IF EXISTS `coursesubject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coursesubject` (
  `CourseID` varchar(50) NOT NULL,
  `SubjectID` varchar(50) NOT NULL,
  PRIMARY KEY (`CourseID`,`SubjectID`),
  KEY `SubjID_idx` (`SubjectID`),
  CONSTRAINT `coursID` FOREIGN KEY (`CourseID`) REFERENCES `course` (`CourseID`),
  CONSTRAINT `SubjID` FOREIGN KEY (`SubjectID`) REFERENCES `subjects` (`SubjectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coursesubject`
--

LOCK TABLES `coursesubject` WRITE;
/*!40000 ALTER TABLE `coursesubject` DISABLE KEYS */;
INSERT INTO `coursesubject` VALUES ('01','01'),('02','01'),('03','01'),('05','01'),('06','02'),('04','03');
/*!40000 ALTER TABLE `coursesubject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `DepartmentID` varchar(50) NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`DepartmentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES ('1','Information Systems Engineering'),('2','Software Engineering'),('3','Industrial Engineering'),('4','Mechanical Engineering'),('5','Optical Engineering'),('6','Civil Engineering'),('7','Biotechnology Engineering'),('8','Electrical Engineering'),('9','Applied Mathematics');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exams`
--

DROP TABLE IF EXISTS `exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exams` (
  `ID` varchar(45) NOT NULL,
  `subjectID` varchar(45) DEFAULT NULL,
  `courseID` varchar(45) DEFAULT NULL,
  `commentsLecturer` varchar(45) DEFAULT NULL,
  `commentsStudents` varchar(45) DEFAULT NULL,
  `duration` varchar(45) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `questionsInExam` varchar(45) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exams`
--

LOCK TABLES `exams` WRITE;
/*!40000 ALTER TABLE `exams` DISABLE KEYS */;
INSERT INTO `exams` VALUES ('010102','01','01','DVDVXFVFXV','zxcxzcxcccc','566','Omri Sharof','01003,01006,01008','j7gg'),('010201','01','02','comments_lecturers','comments_students','45','Omri Sharof','01004,01006,01008','g4E6');
/*!40000 ALTER TABLE `exams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finishedexam`
--

DROP TABLE IF EXISTS `finishedexam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finishedexam` (
  `examID` varchar(45) NOT NULL,
  `studentID` varchar(45) NOT NULL,
  `answers` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`examID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finishedexam`
--

LOCK TABLES `finishedexam` WRITE;
/*!40000 ALTER TABLE `finishedexam` DISABLE KEYS */;
/*!40000 ALTER TABLE `finishedexam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `headofdepartment`
--

DROP TABLE IF EXISTS `headofdepartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `headofdepartment` (
  `ID` varchar(50) NOT NULL,
  `UserName` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Department` varchar(50) DEFAULT NULL,
  `isLogged` int DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `headofdepartment`
--

LOCK TABLES `headofdepartment` WRITE;
/*!40000 ALTER TABLE `headofdepartment` DISABLE KEYS */;
INSERT INTO `headofdepartment` VALUES ('2233','Yossi.Ohayon','123123','Yossi Ohayion','Yossi.Ohayon@e.braude.ac.il','Software Engineering',0);
/*!40000 ALTER TABLE `headofdepartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturer`
--

DROP TABLE IF EXISTS `lecturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturer` (
  `LecturerID` varchar(50) NOT NULL,
  `UserName` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `isLogged` int DEFAULT NULL,
  `courses` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`LecturerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturer`
--

LOCK TABLES `lecturer` WRITE;
/*!40000 ALTER TABLE `lecturer` DISABLE KEYS */;
INSERT INTO `lecturer` VALUES ('206391146','Omri.Sharof','111111','Omri Sharof','Omri.Sharof@e.braude.ac.il',0,'01,02,03'),('333444555','Jason.Smith','123456','Jason Smith','Jason.Smith@e.braude.ac.il',0,'01,02,03');
/*!40000 ALTER TABLE `lecturer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturercourse`
--

DROP TABLE IF EXISTS `lecturercourse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturercourse` (
  `LecturerID` varchar(50) NOT NULL,
  `CourseID` varchar(50) NOT NULL,
  PRIMARY KEY (`LecturerID`,`CourseID`),
  KEY `LID_idx` (`LecturerID`),
  KEY `CorID_idx` (`CourseID`),
  CONSTRAINT `CorID` FOREIGN KEY (`CourseID`) REFERENCES `course` (`CourseID`),
  CONSTRAINT `LID` FOREIGN KEY (`LecturerID`) REFERENCES `lecturer` (`LecturerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturercourse`
--

LOCK TABLES `lecturercourse` WRITE;
/*!40000 ALTER TABLE `lecturercourse` DISABLE KEYS */;
INSERT INTO `lecturercourse` VALUES ('206391146','01'),('206391146','02'),('206391146','03'),('206391146','04'),('206391146','05'),('206391146','06');
/*!40000 ALTER TABLE `lecturercourse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturerdepartment`
--

DROP TABLE IF EXISTS `lecturerdepartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturerdepartment` (
  `LecturerID` varchar(50) NOT NULL,
  `DepartmentID` varchar(50) NOT NULL,
  PRIMARY KEY (`LecturerID`,`DepartmentID`),
  KEY `DID_idx` (`DepartmentID`),
  CONSTRAINT `DID` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`),
  CONSTRAINT `LecID` FOREIGN KEY (`LecturerID`) REFERENCES `lecturer` (`LecturerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturerdepartment`
--

LOCK TABLES `lecturerdepartment` WRITE;
/*!40000 ALTER TABLE `lecturerdepartment` DISABLE KEYS */;
INSERT INTO `lecturerdepartment` VALUES ('206391146','1'),('206391146','2'),('333444555','6');
/*!40000 ALTER TABLE `lecturerdepartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturersubject`
--

DROP TABLE IF EXISTS `lecturersubject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturersubject` (
  `LecturerID` varchar(50) NOT NULL,
  `SubjectID` varchar(50) NOT NULL,
  PRIMARY KEY (`LecturerID`,`SubjectID`),
  KEY `sbid_idx` (`SubjectID`),
  CONSTRAINT `lectid` FOREIGN KEY (`LecturerID`) REFERENCES `lecturer` (`LecturerID`),
  CONSTRAINT `sbid` FOREIGN KEY (`SubjectID`) REFERENCES `subjects` (`SubjectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturersubject`
--

LOCK TABLES `lecturersubject` WRITE;
/*!40000 ALTER TABLE `lecturersubject` DISABLE KEYS */;
INSERT INTO `lecturersubject` VALUES ('206391146','01'),('206391146','02'),('206391146','03');
/*!40000 ALTER TABLE `lecturersubject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` varchar(5) NOT NULL,
  `subjectID` varchar(45) DEFAULT NULL,
  `questionText` varchar(1000) DEFAULT NULL,
  `questionNumber` varchar(45) DEFAULT NULL,
  `answerCorrect` varchar(45) DEFAULT NULL,
  `answerWrong1` varchar(45) DEFAULT NULL,
  `answerWrong2` varchar(45) DEFAULT NULL,
  `answerWrong3` varchar(45) DEFAULT NULL,
  `lecturer` varchar(45) DEFAULT NULL,
  `LecturerID` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `LectuID_idx` (`LecturerID`),
  KEY `sbjID_idx` (`subjectID`),
  CONSTRAINT `subjeID` FOREIGN KEY (`subjectID`) REFERENCES `subjects` (`SubjectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES ('01001','01','What is the correct syntax for a loop in Python?q','001','a1q','a2q','a3q','a4q','Omri Sharof','206391146'),('01002','01','What is the correct syntax for a pointer? ','002','b1','b2','b3','b4','Amanda Lee',''),('01003','01','What is the difference between a stack and a queue?','003',NULL,NULL,NULL,NULL,'John Smith',NULL),('01004','01','What is the time complexity of binary search?','004',NULL,NULL,NULL,NULL,'Mosa Darwish',NULL),('01005','01','What is the difference between pass by value and pass by reference?','005',NULL,NULL,NULL,NULL,'Emily Chen',NULL),('01006','01','testttttt','006','h1','h2','h3','h4','Omri Sharof','206391146'),('01008','01','sdasdasdasdsad','008','jkjk','lflflf','sdfgdfg','trtrter','Omri Sharof','206391146'),('02001','02','If a set B has n elements, then what is the total number of subsets of B. Justify your answer','001',NULL,NULL,NULL,NULL,'Omri Sharof','206391146'),('03001','03','Why is English so important?','001',NULL,NULL,NULL,NULL,'Omri Sharof','206391146');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questioncourse`
--

DROP TABLE IF EXISTS `questioncourse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questioncourse` (
  `questionID` varchar(45) NOT NULL,
  `courseID` varchar(45) NOT NULL,
  PRIMARY KEY (`questionID`,`courseID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questioncourse`
--

LOCK TABLES `questioncourse` WRITE;
/*!40000 ALTER TABLE `questioncourse` DISABLE KEYS */;
INSERT INTO `questioncourse` VALUES ('01001','03'),('01002','03'),('01003','01'),('01004','02'),('01005','05'),('01006','01'),('01006','02'),('01006','03'),('01008','01'),('01008','02'),('01008','03'),('01008','05'),('02001','06'),('03001','04');
/*!40000 ALTER TABLE `questioncourse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionsexam`
--

DROP TABLE IF EXISTS `questionsexam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionsexam` (
  `questionID` varchar(45) NOT NULL,
  `examID` varchar(45) NOT NULL,
  `questionText` varchar(256) DEFAULT NULL,
  `answerCorrect` varchar(45) DEFAULT NULL,
  `answerWrong1` varchar(45) DEFAULT NULL,
  `answerWrong2` varchar(45) DEFAULT NULL,
  `answerWrong3` varchar(45) DEFAULT NULL,
  `points` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`questionID`,`examID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionsexam`
--

LOCK TABLES `questionsexam` WRITE;
/*!40000 ALTER TABLE `questionsexam` DISABLE KEYS */;
INSERT INTO `questionsexam` VALUES ('01003','010102','What is the difference between a stack and a queue?',NULL,NULL,NULL,NULL,'50.0'),('01004','010201','What is the time complexity of binary search?',NULL,NULL,NULL,NULL,'40.0'),('01006','010102','testttttt','h1','h2','h3','h4','38.0'),('01006','010201','testttttt','h4','h1','h2','h3','50.0'),('01008','010102','sdasdasdasdsad','jkjk','lflflf','sdfgdfg','trtrter','12.0'),('01008','010201','sdasdasdasdsad','jkjk','sdfgdfg','lflflf','trtrter','10.0');
/*!40000 ALTER TABLE `questionsexam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `ID` varchar(50) NOT NULL,
  `UserName` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Department` varchar(50) DEFAULT NULL,
  `DepartmentID` varchar(50) DEFAULT NULL,
  `isLogged` int DEFAULT NULL,
  `courses` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('206392246','Aleksander.Pitkin','123456','Aleksander Pitkin','Aleksander.Pitkin@e.braude.ac.il','GayClub','2',0,NULL),('316350768','Nadav.Goldin','123456','Nadav Goldin','Nadav.Goldin@e.braude.ac.il','Software Engineering','1',0,'01,02,03');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studentcourse`
--

DROP TABLE IF EXISTS `studentcourse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studentcourse` (
  `StudentID` varchar(50) NOT NULL,
  `CourseID` varchar(50) NOT NULL,
  PRIMARY KEY (`StudentID`,`CourseID`),
  KEY `CourseID_idx` (`CourseID`),
  CONSTRAINT `CourseID` FOREIGN KEY (`CourseID`) REFERENCES `course` (`CourseID`),
  CONSTRAINT `SID` FOREIGN KEY (`StudentID`) REFERENCES `student` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studentcourse`
--

LOCK TABLES `studentcourse` WRITE;
/*!40000 ALTER TABLE `studentcourse` DISABLE KEYS */;
INSERT INTO `studentcourse` VALUES ('206392246','01'),('206392246','02');
/*!40000 ALTER TABLE `studentcourse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
  `SubjectID` varchar(2) NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `MaxQuestionNumber` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SubjectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjects`
--

LOCK TABLES `subjects` WRITE;
/*!40000 ALTER TABLE `subjects` DISABLE KEYS */;
INSERT INTO `subjects` VALUES ('01','Coding','009'),('02','Math','001'),('03','Language','002');
/*!40000 ALTER TABLE `subjects` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-06 21:06:02