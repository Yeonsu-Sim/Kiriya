SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ssafytrip` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ssafytrip`;

--

DROP TABLE IF EXISTS `schedules`;
DROP TABLE IF EXISTS `plans`;
DROP TABLE IF EXISTS `bucketlists`;

DROP TABLE IF EXISTS `attractions`;
DROP TABLE IF EXISTS `guguns`;
DROP TABLE IF EXISTS `sidos`;
DROP TABLE IF EXISTS `contenttypes`;

DROP TABLE IF EXISTS `boards`;
DROP TABLE IF EXISTS `notices`;
DROP TABLE IF EXISTS `members`;

--

CREATE TABLE IF NOT EXISTS `sidos` (
  `no` int NOT NULL AUTO_INCREMENT  comment '시도번호',
  `sido_code` int NOT NULL comment '시도코드',
  `sido_name` varchar(20) DEFAULT NULL comment '시도이름',
  PRIMARY KEY (`no`),
  UNIQUE INDEX `sido_code_UNIQUE` (`sido_code` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci comment '시도정보테이블';

CREATE TABLE IF NOT EXISTS `ssafytrip`.`guguns` (
  `no` int NOT NULL AUTO_INCREMENT comment '구군번호',
  `sido_code` int NOT NULL comment '시도코드',
  `gugun_code` int NOT NULL comment '구군코드',
  `gugun_name` varchar(20) DEFAULT NULL comment '구군이름',
  PRIMARY KEY (`no`),
  UNIQUE INDEX `sido_gugun_unique` (`sido_code`, `gugun_code`),
  INDEX `guguns_sido_to_sidos_code_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `gugun_code_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `guguns_sido_to_sidos_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `ssafytrip`.`sidos` (`sido_code`)
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
comment '구군정보테이블';

CREATE TABLE IF NOT EXISTS `ssafytrip`.`contenttypes` (
  `content_type_id` int NOT NULL comment '콘텐츠타입번호',
  `content_type_name` varchar(45) DEFAULT NULL comment '콘텐츠타입이름',
  PRIMARY KEY (`content_type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci comment '콘텐츠타입정보테이블';

CREATE TABLE IF NOT EXISTS `ssafytrip`.`attractions` (
  `no` int NOT NULL AUTO_INCREMENT  comment '명소코드',
  `content_id` int DEFAULT NULL comment '콘텐츠번호',
  `title` varchar(500) DEFAULT NULL comment '명소이름',
  `content_type_id` int DEFAULT NULL comment '콘텐츠타입',
  `area_code` int DEFAULT NULL comment '시도코드',
  `si_gun_gu_code` int DEFAULT NULL comment '구군코드',
  `first_image1` varchar(100) DEFAULT NULL comment '이미지경로1',
  `first_image2` varchar(100) DEFAULT NULL comment '이미지경로2',
  `map_level` int DEFAULT NULL comment '줌레벨',
  `latitude` decimal(20,17) DEFAULT NULL comment '위도',
  `longitude` decimal(20,17) DEFAULT NULL comment '경도',
  `tel` varchar(20) DEFAULT NULL comment '전화번호',
  `addr1` varchar(100) DEFAULT NULL comment '주소1',
  `addr2` varchar(100) DEFAULT NULL comment '주소2',
  `homepage` varchar(1000) DEFAULT NULL comment '홈페이지',
  `overview` varchar(10000) DEFAULT NULL comment '설명',
  PRIMARY KEY (`no`),
  INDEX `attractions_typeid_to_types_typeid_fk_idx` (`content_type_id` ASC) VISIBLE,
  INDEX `attractions_sido_to_sidos_code_fk_idx` (`area_code` ASC) VISIBLE,
  INDEX `attractions_sigungu_to_guguns_gugun_fk_idx` (`si_gun_gu_code` ASC) VISIBLE,
  CONSTRAINT `attractions_area_to_sidos_code_fk`
    FOREIGN KEY (`area_code`)
    REFERENCES `ssafytrip`.`sidos` (`sido_code`),
  CONSTRAINT `attractions_sigungu_to_guguns_gugun_fk`
    FOREIGN KEY (`area_code`,`si_gun_gu_code`)
    REFERENCES `ssafytrip`.`guguns` (`sido_code`, `gugun_code`),
  CONSTRAINT `attractions_typeid_to_types_typeid_fk`
    FOREIGN KEY (`content_type_id`)
    REFERENCES `ssafytrip`.`contenttypes` (`content_type_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
comment '명소정보테이블';


CREATE TABLE `members` (
    `mno` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL UNIQUE,
    `password` VARCHAR(45) NOT NULL,
    `role` VARCHAR(45) NOT NULL,
    `profile` BLOB DEFAULT NULL,
    PRIMARY KEY (`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `plans` (
    `pno` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(45) NOT NULL,
    `days` INT NULL,
    `schedule_count` INT NOT NULL DEFAULT 0,
    `mno` INT NOT NULL,
    PRIMARY KEY (`pno`),
    CONSTRAINT `FK_members_TO_plans_1` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `schedules` (
    `sno` INT NOT NULL AUTO_INCREMENT,
    `order` INT NOT NULL DEFAULT 0,
    `pno` INT NOT NULL,
    `ano` INT NOT NULL,
    PRIMARY KEY (`sno`),
    CONSTRAINT `FK_plans_TO_schedules_1` FOREIGN KEY (`pno`) REFERENCES `plans` (`pno`),
    CONSTRAINT `FK_attractions_TO_schedules_1` FOREIGN KEY (`ano`) REFERENCES `attractions` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bucketlists` (
    `mno` INT NOT NULL,
    `ano` INT NOT NULL,
    PRIMARY KEY (`mno`, `ano`),
    CONSTRAINT `FK_members_TO_bucketlists_1` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`),
    CONSTRAINT `FK_attractions_TO_bucketlists_1` FOREIGN KEY (`ano`) REFERENCES `attractions` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `boards` (
    `bno` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(45) NOT NULL,
    `content` LONGTEXT NOT NULL,
    `created_date` DATETIME NOT NULL,
    `updated_date` DATETIME DEFAULT NULL,
    `like` INT DEFAULT 0,
    `mno` INT NOT NULL,
    PRIMARY KEY (`bno`),
    CONSTRAINT `FK_members_TO_boards_1` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `notices` (
  `nno` INT NOT NULL AUTO_INCREMENT,
  `mno` INT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `updated_date` DATETIME NULL,
  PRIMARY KEY (`nno`),
  UNIQUE KEY `nno_UNIQUE` (`nno`),
  CONSTRAINT `fk_notices_members`
    FOREIGN KEY (`mno`)
    REFERENCES `members` (`mno`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;