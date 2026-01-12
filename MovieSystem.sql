-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: movie_rating_system
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `actor`
--

DROP TABLE IF EXISTS `actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actor` (
  `actor_id` bigint NOT NULL AUTO_INCREMENT COMMENT '演员ID',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `gender` char(1) DEFAULT 'M' COMMENT '性别 M:男 F:女',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `nationality` varchar(50) DEFAULT NULL COMMENT '国籍',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '演员照片',
  PRIMARY KEY (`actor_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='演员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor`
--

LOCK TABLES `actor` WRITE;
/*!40000 ALTER TABLE `actor` DISABLE KEYS */;
INSERT INTO `actor` VALUES (1,'莱昂纳多·迪卡普里奥','M','1974-11-11','美国','http://localhost:9000/movie-system/b1418efc-5ae7-46ad-a4c8-4cefcccefa7b.jpg'),(2,'基里安·墨菲','M','1976-05-25','爱尔兰','http://localhost:9000/movie-system/448d2a30-c7b3-4954-bb10-5180decd2d1a.jpg'),(3,'玛格特·罗比','F','1990-07-02','澳大利亚','http://localhost:9000/movie-system/310a434f-3023-465d-b42e-4b72921e2a95.jpg'),(4,'提摩西·查拉梅','M','1995-12-27','美国','http://localhost:9000/movie-system/850b5774-96d2-4f0a-b505-fd0901fbb174.jpg'),(6,'瑞恩·高斯林','M','1980-11-12','加拿大','http://localhost:9000/movie-system/9a8ba3d2-e0e3-4c0a-bc34-e111bfc76915.jpg'),(7,'汤姆·哈迪','M','1977-09-15','英国','http://localhost:9000/movie-system/72197260-9fe8-42d4-b571-6a20cbd79500.jpg'),(8,'艾米莉·布朗特','F','1983-02-23','英国','http://localhost:9000/movie-system/f333595f-300b-485a-8d46-4f6d9cefead7.png'),(9,'赞达亚','F','1996-09-01','美国','http://localhost:9000/movie-system/3bef0259-edf4-4a38-8624-df6c510c57e4.jpg'),(10,'小罗伯特·唐尼','M','1965-04-04','美国','http://localhost:9000/movie-system/cacb9764-b6e5-4b7c-bb99-ad50daff41a9.png'),(11,'渡边谦','M','1959-10-21','日本','http://localhost:9000/movie-system/e8b72ca4-a0e1-4f3b-a149-504aa67243f7.png'),(12,'艾利奥特·佩吉','M','1987-02-21','加拿大','http://localhost:9000/movie-system/de160673-a5b4-4435-9352-ba42d80cd261.jpg'),(13,'约瑟夫·高登-莱维特','M','1981-02-17','美国','http://localhost:9000/movie-system/7e1c1d71-5f63-48ab-b05f-36129e4f90ba.jpg'),(14,'弗洛伦丝·皮尤','F','1996-01-03','英国','http://localhost:9000/movie-system/df5d402a-eb6b-48ca-b62d-38dced13807b.jpg'),(15,'奥斯汀·巴特勒','M','1991-08-17','美国','http://localhost:9000/movie-system/2d81e048-38cf-4a7c-a195-a2b085dcbd2c.jpg'),(16,'蕾雅·赛杜','F','1985-07-01','法国','http://localhost:9000/movie-system/830a0886-dc77-4a63-8fe4-cbe41ebcf96a.jpg'),(17,'柊瑠美','F','1987-08-01','日本','http://localhost:9000/movie-system/8fa6f88f-4f40-4eb4-953a-57fd38a72be6.png'),(18,'入野自由','M','1988-02-19','日本','http://localhost:9000/movie-system/67ca13b2-d531-4c72-87b6-c4e647d42339.png'),(19,'吴京','M','1974-04-03','中国','http://localhost:9000/movie-system/21cf7a27-d7f6-43a2-b2e7-3e8df3913890.png'),(20,'刘德华','M','1961-09-27','中国','http://localhost:9000/movie-system/581e641b-730e-4b80-a729-aeea5032dfa4.jpg'),(21,'李雪健','M','1954-02-20','中国','http://localhost:9000/movie-system/5602c1c8-1b3d-4ad2-9148-b106fc0aafe3.png'),(22,'张国荣','M','1956-09-12','中国香港','http://localhost:9000/movie-system/fe3336b0-a607-4d41-80fe-1cede5cc7759.jpg'),(23,'张丰毅','M','1956-09-01','中国','http://localhost:9000/movie-system/670f16e0-6f30-4067-8e2b-4fcc4fa08332.jpg'),(24,'巩俐','F','1965-12-31','新加坡','http://localhost:9000/movie-system/b7312951-4462-427e-9e10-1f0034984cf9.png'),(25,'查理兹·塞隆','F',NULL,'南非','http://localhost:9000/movie-system/655aad0b-b854-4755-9127-abbad506c2cd.jpg'),(26,'尼古拉斯·霍尔特','M',NULL,'英国','http://localhost:9000/movie-system/80487d55-4d6c-4193-ab42-f90a007e7e59.jpg'),(27,'摩根·弗里曼','M',NULL,'美国','http://localhost:9000/movie-system/e9d7bccf-4806-4379-800d-4d1c9ae09b00.png'),(28,'蒂姆·罗宾斯','M',NULL,'美国','http://localhost:9000/movie-system/fd9718e3-df4f-4296-a3d5-34993539bcea.jpg'),(29,'凯特·温丝莱特','F',NULL,'英国','http://localhost:9000/movie-system/64fe41bb-77f1-4eb3-b9ea-75dbda8b4148.jpg'),(30,'朱茵','F','1971-10-25','中国','http://localhost:9000/movie-system/0161465a-7df6-4d8c-9d04-a79e93f6cc2c.png'),(31,'伊恩·麦克莱恩','M','1939-05-25','英国','https://api.example.com/act/ian.jpg'),(32,'布拉德·皮特','M','1963-12-18','美国','https://api.example.com/act/pitt.jpg'),(33,'宋康昊','M','1967-01-17','韩国','https://api.example.com/act/skh.jpg'),(34,'阿米尔·汗','M','1965-03-14','印度','https://api.example.com/act/amir.jpg'),(35,'上白石萌音','F','1998-01-27','日本','https://api.example.com/act/sbs.jpg'),(36,'马特·达蒙','M','1970-10-08','美国','https://api.example.com/act/damon.jpg'),(37,'古天乐','M','1970-10-21','中国','https://api.example.com/act/gtl.jpg'),(38,'井柏然','M',NULL,'中国','http://localhost:9000/movie-system/a2cff462-6e77-4332-9b0a-bae11fc62f12.jpg'),(39,'鹿晗','M',NULL,'中国','http://localhost:9000/movie-system/5f4cf38b-48df-4ea7-80e7-212ac03e9b20.jpg'),(40,'罗宾·怀特','F',NULL,'美国','http://localhost:9000/movie-system/92096267-91da-49da-86d3-6046ef0b62c7.jpg'),(41,'汤姆·汉克斯','M',NULL,'美国','http://localhost:9000/movie-system/1adf86da-d668-4638-8fb5-b49c24fb921f.png');
/*!40000 ALTER TABLE `actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `director`
--

DROP TABLE IF EXISTS `director`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `director` (
  `director_id` bigint NOT NULL AUTO_INCREMENT COMMENT '导演ID',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `gender` char(1) DEFAULT 'M' COMMENT '性别 M:男 F:女',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `nationality` varchar(50) DEFAULT NULL COMMENT '国籍',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '导演照片',
  PRIMARY KEY (`director_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='导演表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `director`
--

LOCK TABLES `director` WRITE;
/*!40000 ALTER TABLE `director` DISABLE KEYS */;
INSERT INTO `director` VALUES (1,'克里斯托弗·诺兰','M','1970-07-30','英国','http://localhost:9000/movie-system/917a86c4-8d2c-4142-8988-892061d51b4d.png'),(2,'张艺谋','M','1950-04-02','中国','http://localhost:9000/movie-system/cc7cf1d8-3025-4a5d-ad2c-65493c2277e5.jpg'),(3,'格蕾塔·葛韦格','F','1983-08-04','美国','http://localhost:9000/movie-system/037db584-f79f-47b0-b4e6-54c1f5403f3e.png'),(4,'宫崎骏','M','1941-01-05','日本','http://localhost:9000/movie-system/c5693355-ba48-4ab4-9f6f-3d139c873e6b.png'),(5,'丹尼斯·维伦纽瓦','M','1967-10-03','加拿大','http://localhost:9000/movie-system/6c314fdc-b3ca-4d4b-874c-0175398c0af0.png'),(8,'郭帆','M','1980-12-15','中国','http://localhost:9000/movie-system/cef795c9-9fba-4cbf-81ac-7369a9f5b4e3.jpg'),(9,'陈凯歌','M','1952-08-12','中国','http://localhost:9000/movie-system/739b42a3-777a-4a51-bc28-be89806eb270.jpg'),(10,'乔治·米勒','M',NULL,'澳大利亚','http://localhost:9000/movie-system/bfb0cc21-9452-42e0-9e1c-8f05e87fd922.jpg'),(11,'弗兰克·德拉邦特','M',NULL,'匈牙利/美国','http://localhost:9000/movie-system/147d8704-b7cd-493a-9618-ee67f28a551c.jpg'),(12,'詹姆斯·卡梅隆','M',NULL,'加拿大','http://localhost:9000/movie-system/9317a97d-9a50-4750-a6ba-02784d173a78.jpg'),(13,'刘镇伟','M','1952-08-02','中国','https://api.example.com/dir/lzw.jpg'),(14,'彼得·杰克逊','M','1961-10-31','新西兰','https://api.example.com/dir/pj.jpg'),(15,'大卫·芬奇','M','1962-08-28','美国','https://api.example.com/dir/df.jpg'),(16,'奉俊昊','M','1969-09-14','韩国','http://localhost:9000/movie-system/eea2eb05-b675-4442-9e4d-065b9879fee9.png'),(17,'拉吉库马尔·希拉尼','M','1962-11-20','印度','https://api.example.com/dir/hirani.jpg'),(18,'新海诚','M','1973-02-09','日本','https://api.example.com/dir/xhc.jpg'),(19,'雷德利·斯科特','M','1937-11-30','英国','https://api.example.com/dir/ridley.jpg'),(20,'李仁港','M',NULL,'中国','http://localhost:9000/movie-system/5aad343d-28d0-4243-b3cc-c4fb26cf8b4c.png');
/*!40000 ALTER TABLE `director` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie`
--

DROP TABLE IF EXISTS `movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie` (
  `movie_id` bigint NOT NULL AUTO_INCREMENT COMMENT '电影ID',
  `title` varchar(100) NOT NULL COMMENT '电影名称',
  `release_year` int DEFAULT NULL COMMENT '发行年份',
  `duration` int DEFAULT NULL COMMENT '时长(分钟)',
  `genre` varchar(50) DEFAULT NULL COMMENT '类型/流派',
  `language` varchar(50) DEFAULT NULL COMMENT '语言',
  `country` varchar(50) DEFAULT NULL COMMENT '国家/地区',
  `synopsis` text COMMENT '简介',
  `cover_url` varchar(255) DEFAULT NULL COMMENT '封面图片URL',
  `rating` decimal(3,1) DEFAULT '0.0' COMMENT '评分(平均分)',
  `rating_count` int DEFAULT '0' COMMENT '评分人数(用于计算平均分)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movie_id`),
  KEY `idx_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie`
--

LOCK TABLES `movie` WRITE;
/*!40000 ALTER TABLE `movie` DISABLE KEYS */;
INSERT INTO `movie` VALUES (1,'盗梦空间',2010,148,'科幻','英语','美国','一名利用潜意识进入他人梦境窃取机密的商业间谍。','http://localhost:9000/movie-system/83e2ce95-87f3-479d-9a1c-55db1298e3bf.png',9.5,2,'2025-12-31 15:44:16'),(2,'奥本海默',2023,180,'传记','英语','美国','讲述了美国理论物理学家罗伯特·奥本海默研制原子弹的过程。','http://localhost:9000/movie-system/7d21d807-79ec-4c78-b38b-69b3dc413ed0.png',9.0,1,'2025-12-31 15:44:16'),(3,'芭比',2023,114,'喜剧','英语','美国','在芭比乐园过着完美生活的芭比，突然发现自己变得不完美。','http://localhost:9000/movie-system/be2dac01-a08f-4de9-bf18-e819651a4202.png',7.0,1,'2025-12-31 15:44:16'),(4,'沙丘2',2024,166,'科幻','英语','美国','保罗·厄崔迪携手契妮和弗雷曼人，对毁灭他家族的阴谋者开战。','http://localhost:9000/movie-system/22f053ca-73f9-4188-88db-f5fe10161237.png',9.0,1,'2025-12-31 15:44:16'),(5,'千与千寻',2001,125,'动画','日语','日本','少女千寻意外进入了神灵世界，为了救父母经历重重磨难。','http://localhost:9000/movie-system/f7cda38d-bef8-43fd-b156-eaa6c30acb10.png',10.0,1,'2025-12-31 15:44:16'),(6,'流浪地球2',2023,173,'科幻','中文','中国','太阳危机下的全人类命运共同体，为了生存而开启的星际征途。','http://localhost:9000/movie-system/64c646c6-f619-4c02-86cf-4107af9428f7.png',9.0,1,'2026-01-08 23:20:05'),(7,'霸王别姬',1993,171,'剧情','中文','中国','演艺界的一对师兄弟由于性格和命运的差异，在时代变迁中展开的悲剧故事。','http://localhost:9000/movie-system/8535d093-4cc7-4cab-bac3-76001cf70718.png',10.0,1,'2026-01-10 12:09:43'),(8,'疯狂的麦克斯4：狂暴之路',2015,120,'动作/科幻/冒险','英语','澳大利亚/美国','世界毁灭后的荒漠，为了生存而展开的一场疯狂的公路追逐战。','http://localhost:9000/movie-system/24ce3b30-69f6-40a0-90c4-a2478e09bb47.png',0.0,0,'2026-01-10 20:14:37'),(9,'肖申克的救赎',1994,142,'剧情/犯罪','英语','美国','一场谋杀案使银行家安迪蒙冤入狱，在肖申克监狱中，他用智慧与坚持完成了灵魂的救赎并重获自由。','http://localhost:9000/movie-system/ccf04ba0-c135-4200-a492-f707b9b73108.png',10.0,1,'2026-01-10 20:21:19'),(10,'泰坦尼克号',1997,194,'剧情/爱情/灾难','英语','美国','穷画家杰克和贵族女露丝在豪华巨轮泰坦尼克号上相识相恋，却遭遇冰山撞击，谱写了一段生死绝恋。','http://localhost:9000/movie-system/09eabc9e-b67f-49f5-85ac-f72e8909f84c.png',8.0,1,'2026-01-10 21:25:05'),(11,'大话西游之大圣娶亲',1995,95,'喜剧/爱情/奇幻','粤语','中国香港','至尊宝为了救白晶晶穿越回五百年前，却遇到了紫霞仙子。','http://localhost:9000/movie-system/77119321-7850-4926-9280-179176aa49e6.png',0.0,0,'2026-01-10 23:57:13'),(12,'指环王1：护戒使者',2001,178,'剧情/动作/奇幻','英语','新西兰','为了摧毁拥有奴役世界力量的魔戒，少年弗罗多开启了征程。','http://localhost:9000/movie-system/2655f8af-f3f7-4ef9-8d7b-4e1f26a729d0.png',0.0,0,'2026-01-10 23:57:13'),(13,'搏击俱乐部',1999,139,'剧情/悬疑','英语','美国','一个失眠的职员和一名肥皂商人建立了一个地下组织。','http://localhost:9000/movie-system/21653d19-098d-4988-b3ca-44cc58efc767.png',0.0,0,'2026-01-10 23:57:13'),(14,'寄生虫',2019,132,'剧情/惊悚','韩语','韩国','一家贫困家庭通过欺骗手段进入富有家庭工作，引发意外。','http://localhost:9000/movie-system/fb3ad896-08a3-4e2b-9c42-45155b1e0fa8.png',0.0,0,'2026-01-10 23:57:13'),(15,'三傻大闹宝莱坞3',2009,171,'剧情/喜剧','印地语','印度','三个性格迥异的大学生在精英教育体制下寻找自我的故事。','http://localhost:9000/movie-system/ad1e3871-1c23-4384-a966-767e1845299d.png',0.0,0,'2026-01-10 23:57:13'),(16,'星际穿越',2014,169,'科幻/冒险','英语','美国','一组探险家利用新发现的虫洞，跨越星际寻找人类新家园。','http://localhost:9000/movie-system/d3d846b4-9935-48ce-b443-8ef6b61e71ae.png',0.0,0,'2026-01-10 23:57:13'),(17,'教父',1972,175,'剧情/犯罪','英语','美国','黑帮家族首领柯里昂将权柄传递给小儿子的传奇故事。','http://localhost:9000/movie-system/ca280cab-cd09-4af4-bce3-727eb6cb7758.png',0.0,0,'2026-01-10 23:57:13'),(18,'狮子王',1994,89,'动画/歌舞','英语','美国','小狮子辛巴在经历了逃亡后，终于回归并夺回王位。','http://localhost:9000/movie-system/e8449fb8-0c88-4a81-afb7-1234e6a4ee51.png',0.0,0,'2026-01-10 23:57:13'),(19,'西西里的美丽传说',2000,109,'剧情/战争','意大利语','意大利','二战期间，一个少年对小镇最美女性玛莲娜的迷恋。','http://localhost:9000/movie-system/dd4519ec-9438-4031-92d6-ccb3d06635d2.png',0.0,0,'2026-01-10 23:57:13'),(20,'让子弹飞',2010,132,'剧情/喜剧/动作','中文','中国','土匪张麻子当上县官后，与鹅城恶霸黄四郎斗智斗勇。','http://localhost:9000/movie-system/dffb4cf2-78ed-4e8c-9af9-04e8538f1bfd.png',0.0,0,'2026-01-10 23:57:13'),(21,'消失的爱人',2014,149,'剧情/悬疑/惊悚','英语','美国','平凡的一天，尼克发现妻子艾米失踪，随着调查深入，一段扭曲的婚姻关系浮出水面。','http://localhost:9000/movie-system/23b289d9-5e1c-4f03-a357-055db4d6acd1.png',0.0,0,'2026-01-11 00:24:09'),(22,'你的名字。',2016,106,'剧情/爱情/动画','日语','日本','在梦中幻化成他人的少男少女，在跨越时空的追寻中寻找彼此。','http://localhost:9000/movie-system/dc7ee7f6-d798-4f50-8e13-ea9b61116832.png',9.0,1,'2026-01-11 00:24:09'),(23,'火星救援',2015,144,'剧情/科幻/冒险','英语','美国','宇航员马克被误认为死亡而孤身留在火星，他必须依靠智慧在荒芜星球活下去。','http://localhost:9000/movie-system/dabb6395-a1ef-4503-9447-2db72600ce9a.png',0.0,0,'2026-01-11 00:24:09'),(24,'寻梦环游记',2017,105,'喜剧/动画/奇幻','英语','美国','热爱音乐的男孩米格尔误闯入亡灵世界，开启了一段连接家族与梦想的冒险。','http://localhost:9000/movie-system/36415328-81a6-4885-9087-3a1aa5302c4b.png',0.0,0,'2026-01-11 00:24:09'),(25,'用武之地',2025,131,'剧情 / 动作 / 战争','汉语普通话','中国','影片取材自境外幸存人质真实自救经历。\n　　驻外记者马笑（肖央 饰）和志愿医生潘文佳（齐溪 饰）夫妇，陪同工程师苗峰（郑恺 饰）赴郊区修理基站，当地局势突变。三人一路躲避恐怖组织的无差别袭击，最终还是被绑架沦为人质，并在狱中结识了华侨商人周伟杰（任达华 饰）。面对恐怖分子闻所未闻的屠杀暴行和以命换钱的勒索，他们历经105天，展开了一场惊心动魄的自救逃生。','http://localhost:9000/movie-system/47d03777-3cbf-4190-a960-e9eec7bfd3e1.png',0.0,0,'2026-01-11 04:28:15'),(27,'哪吒之魔童闹海',2025,144,'剧情 / 喜剧 / 动画 / 奇幻','汉语普通话','中国','天劫之后，哪吒、敖丙的灵魂虽保住了，但肉身很快会魂飞魄散。太乙真人打算用七色宝莲给二人重塑肉身。但是在重塑肉身的过程中却遇到重重困难，哪吒、敖丙的命运将走向何方？','http://localhost:9000/movie-system/7f102dea-10b6-4d68-aed8-785055e9fb38.png',9.0,1,'2026-01-11 09:11:01'),(28,'盗墓笔记',2016,123,'悬疑 / 奇幻 / 冒险','汉语','中国','落魄作家为了写作素材，寻访到了一个叫做吴邪（鹿晗 饰）的古董铺子老板，而吴邪正准备离开这个城市，临走之前，吴邪和他讲诉了关于自己奇怪的盗墓家族往事，并说出了自己第一次随家族探险所经历的诡异事件：那一次他们的家族因为偶然获取了一件特殊的青铜器， 追根溯源，寻找到了被掩埋在中国西北盆地中的西王母古国，他们招募了一批盗墓贼一同前往古城遗址探险，进入了位于古城地下的西王母陵中，发现了当年周穆王于西王母求长生不死之术的真相。作家听完吴邪的故事，却发现其中有很多疑点，吴邪到底说的是自己的臆想，还是真相更加可怕复杂，因为吴邪的离开变成了永恒之谜。','http://localhost:9000/movie-system/759d849a-496b-449d-a402-9b9981cb3d96.png',8.0,2,'2026-01-11 12:25:47'),(29,'阿甘正传',1994,142,'剧情','英语','美国','','http://localhost:9000/movie-system/19173430-8988-41a6-8f71-d1a18dca4a45.png',0.0,0,'2026-01-11 12:32:27'),(30,'阿凡达',2026,120,'','','','','',0.0,0,'2026-01-11 17:41:35');
/*!40000 ALTER TABLE `movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_actor`
--

DROP TABLE IF EXISTS `movie_actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_actor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `movie_id` bigint NOT NULL,
  `actor_id` bigint NOT NULL,
  `role_name` varchar(100) DEFAULT NULL COMMENT '饰演角色名',
  PRIMARY KEY (`id`),
  KEY `movie_id` (`movie_id`),
  KEY `actor_id` (`actor_id`),
  CONSTRAINT `movie_actor_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `movie_actor_ibfk_2` FOREIGN KEY (`actor_id`) REFERENCES `actor` (`actor_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影演员关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_actor`
--

LOCK TABLES `movie_actor` WRITE;
/*!40000 ALTER TABLE `movie_actor` DISABLE KEYS */;
INSERT INTO `movie_actor` VALUES (1,1,1,'柯布'),(2,2,2,'奥本海默'),(3,3,3,'芭比'),(4,3,6,'肯'),(5,4,4,'保罗·厄崔迪'),(6,1,7,'伊姆斯'),(7,1,11,'齐藤'),(8,2,8,'凯瑟琳·奥本海默'),(9,2,10,'刘易斯·斯特劳斯'),(10,4,9,'契妮'),(11,1,12,'阿丽瑞德妮'),(12,1,13,'亚瑟'),(13,2,14,'琼·塔特洛克'),(14,4,15,'菲德-罗萨·哈克南'),(15,4,16,'玛戈夫人'),(16,5,17,'荻野千寻 (配音)'),(17,5,18,'白龙 (配音)'),(36,10,1,'杰克'),(37,10,29,'罗丝'),(38,9,27,'安迪·杜佛兰'),(39,9,28,'艾利斯·波伊德·“瑞德”·瑞丁'),(40,8,25,'Imperator Furiosa'),(41,8,26,'Nux'),(42,7,22,'程蝶衣'),(43,7,23,'段小楼'),(44,7,24,'菊仙'),(45,6,19,'刘培强'),(46,6,20,'图恒宇'),(47,6,21,'周喆直'),(53,11,30,'紫霞仙子'),(54,12,31,'甘道夫'),(55,13,32,'泰勒·德顿'),(56,14,33,'金基泽'),(57,15,34,'兰彻'),(60,28,38,'张起灵'),(61,28,39,'吴邪');
/*!40000 ALTER TABLE `movie_actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_director`
--

DROP TABLE IF EXISTS `movie_director`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_director` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `movie_id` bigint NOT NULL,
  `director_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `movie_id` (`movie_id`),
  KEY `director_id` (`director_id`),
  CONSTRAINT `movie_director_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `movie_director_ibfk_2` FOREIGN KEY (`director_id`) REFERENCES `director` (`director_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影导演关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_director`
--

LOCK TABLES `movie_director` WRITE;
/*!40000 ALTER TABLE `movie_director` DISABLE KEYS */;
INSERT INTO `movie_director` VALUES (1,1,1),(2,2,1),(3,3,3),(4,4,5),(5,5,4),(15,10,12),(16,9,11),(17,8,10),(18,7,9),(19,6,8),(25,11,13),(26,12,14),(27,13,15),(28,14,16),(29,15,17),(30,16,1),(32,28,20);
/*!40000 ALTER TABLE `movie_director` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `review_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `movie_id` bigint NOT NULL COMMENT '电影ID',
  `score` tinyint NOT NULL COMMENT '评分(1-10分)',
  `content` text COMMENT '评论内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  PRIMARY KEY (`review_id`),
  UNIQUE KEY `uk_user_movie` (`user_id`,`movie_id`),
  KEY `movie_id` (`movie_id`),
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论评分表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,2,1,10,'烧脑神作，诺兰的想象力真的是天花板级别！！','2026-01-01 17:02:15',0),(2,2,3,7,'挺有趣的，比预想中的更有深度，不只是粉丝向电影。','2026-01-01 17:03:17',0),(3,3,1,9,'梦境的层级设定非常严谨，结尾的陀螺到底是倒了还是没倒？','2026-01-01 17:12:36',0),(4,3,4,9,'维伦纽瓦的暴力美学和镜头语言已经炉火纯青。','2026-01-01 17:13:14',0),(5,3,5,10,'宫崎骏的巅峰之作，无论看多少遍都能被治愈。','2026-01-01 17:13:31',2),(7,4,7,10,'华语影坛的巅峰之作，没有之一。','2026-01-10 16:47:04',0),(8,4,6,9,'工业化水平极高，建议一定要看IMAX版本。','2026-01-10 16:47:44',0),(9,5,9,10,'每次失落的时候都会拿出来看一遍，满分推荐！','2026-01-10 21:19:41',0),(14,7,2,9,'太震撼了！！！','2026-01-11 11:38:05',1),(15,7,10,8,'you jump i jump','2026-01-11 11:38:52',0),(16,7,27,9,'我命由我不由天','2026-01-11 11:39:20',0),(20,2,22,9,'画面每一帧都可以直接截图做壁纸！RADWIMPS 的配乐与剧情结合得近乎完美，尤其是《前前前世》响起的时候，鸡皮疙瘩都起来了。剧情虽然带有常见的灵魂互换套路，但在彗星陨落的大背景下，不仅有了科幻的宏大感，更有了命运的厚重感。','2026-01-11 16:23:11',1),(22,3,28,9,'作为原著粉，这一部的还原度超出了预期。无论是七星鲁王宫的阴森氛围，还是禁婆、血尸的特效处理，都非常有代入感。铁三角之间的默契互动也抓住了灵魂，尤其是吴邪那种带点天真但眼神逐渐坚定的感觉，张起灵的武打戏干净利落。是一部诚意满满的冒险大作。','2026-01-11 16:27:26',0),(23,5,28,7,'更打动我的是那种跨越时间的羁绊。有些逻辑衔接还是略显生硬，反派的动机可以刻画得更深一些。总体来说是一部合格的爆米花大片。','2026-01-11 16:47:47',0);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_review_insert` AFTER INSERT ON `review` FOR EACH ROW BEGIN
    UPDATE movie
    SET
        rating_count = (SELECT COUNT(*) FROM review WHERE movie_id = NEW.movie_id),
        rating = IFNULL((SELECT AVG(score) FROM review WHERE movie_id = NEW.movie_id), 0)
    WHERE movie_id = NEW.movie_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_review_update` AFTER UPDATE ON `review` FOR EACH ROW BEGIN
    -- 只有当评分或者是电影ID发生变化时才更新（虽然电影ID一般不变，但为了严谨）
    IF OLD.score != NEW.score OR OLD.movie_id != NEW.movie_id THEN
        -- 更新新指向的电影（通常是同一个）
        UPDATE movie
        SET
            rating_count = (SELECT COUNT(*) FROM review WHERE movie_id = NEW.movie_id),
            rating = IFNULL((SELECT AVG(score) FROM review WHERE movie_id = NEW.movie_id), 0)
        WHERE movie_id = NEW.movie_id;

        -- 如果极端情况下修改了movie_id（把评论移到了另一部电影），旧电影也要更新
        IF OLD.movie_id != NEW.movie_id THEN
            UPDATE movie
            SET
                rating_count = (SELECT COUNT(*) FROM review WHERE movie_id = OLD.movie_id),
                rating = IFNULL((SELECT AVG(score) FROM review WHERE movie_id = OLD.movie_id), 0)
            WHERE movie_id = OLD.movie_id;
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_review_delete` AFTER DELETE ON `review` FOR EACH ROW BEGIN
    UPDATE movie
    SET
        rating_count = (SELECT COUNT(*) FROM review WHERE movie_id = OLD.movie_id),
        -- IFNULL用于处理删除了最后一条评论的情况，AVG会返回NULL，我们需要置为0
        rating = IFNULL((SELECT AVG(score) FROM review WHERE movie_id = OLD.movie_id), 0)
    WHERE movie_id = OLD.movie_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `review_like`
--

DROP TABLE IF EXISTS `review_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '点赞人',
  `review_id` bigint NOT NULL COMMENT '被点赞的评论',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_review` (`user_id`,`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论点赞表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_like`
--

LOCK TABLES `review_like` WRITE;
/*!40000 ALTER TABLE `review_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `review_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码(加密存储)',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `role` varchar(20) DEFAULT 'user' COMMENT '角色: user/admin',
  `bio` varchar(255) DEFAULT '' COMMENT '个人简介/个性签名',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'kevin','83fb0878b67c9564236510f57e16ad61','white_tree','http://localhost:9000/movie-system/a98bf8c8-0d69-440f-9d90-4b557a9950e7.jpg','ck2474508686@163.com','19945652797',1,'2025-12-31 14:41:30','admin','我是管理员'),(2,'zhangsan','e10adc3949ba59abbe56e057f20f883e','张三','http://localhost:9000/movie-system/46e33f4b-442b-4a1b-85f7-b9057692d625.jpg','watcher@example.com','13812345678',1,'2026-01-01 17:01:02','user',''),(3,'lisi','96e79218965eb72c92a549dd5a330112','李四','http://localhost:9000/movie-system/d2abb0ca-e2d2-4a88-abba-68c74d9a03e9.jpg','expert01@163.com','13500001111',1,'2026-01-01 17:11:58','user','喜欢《千与千寻》'),(4,'wangwu','e10adc3949ba59abbe56e057f20f883e','用户1768030236116',NULL,NULL,NULL,1,'2026-01-10 15:30:36','user',''),(5,'jim','e10adc3949ba59abbe56e057f20f883e','光影追随者','http://localhost:9000/movie-system/4a044e81-2af1-4b75-8dc8-cc0e360643ca.jpg','noir99@gmail.com','13988887777',1,'2026-01-10 21:18:32','user',''),(7,'zhaoliu','e10adc3949ba59abbe56e057f20f883e','用户1768102631604',NULL,NULL,NULL,1,'2026-01-11 11:37:11','user',''),(9,'suep','e10adc3949ba59abbe56e057f20f883e','上海电力大学','http://localhost:9000/movie-system/b8f141b7-4852-4c40-af1d-f22c4164c3ef.jpg',NULL,NULL,1,'2026-01-11 17:37:08','user','');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_movie_details`
--

DROP TABLE IF EXISTS `v_movie_details`;
/*!50001 DROP VIEW IF EXISTS `v_movie_details`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_movie_details` AS SELECT 
 1 AS `movie_id`,
 1 AS `title`,
 1 AS `release_year`,
 1 AS `rating`,
 1 AS `directors`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_movie_details`
--

/*!50001 DROP VIEW IF EXISTS `v_movie_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_movie_details` AS select `m`.`movie_id` AS `movie_id`,`m`.`title` AS `title`,`m`.`release_year` AS `release_year`,`m`.`rating` AS `rating`,group_concat(`d`.`name` separator ', ') AS `directors` from ((`movie` `m` left join `movie_director` `md` on((`m`.`movie_id` = `md`.`movie_id`))) left join `director` `d` on((`md`.`director_id` = `d`.`director_id`))) group by `m`.`movie_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-11 19:41:52
