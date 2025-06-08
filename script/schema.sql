-- -----------------------------------------------------
-- 세션 설정 백업 및 제약조건 해제 (임시)
-- -----------------------------------------------------
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- 스키마 생성 및 사용
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `kiriya` DEFAULT CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE `kiriya`;


-- -----------------------------------------------------
-- 테이블 삭제 (기존 존재할 경우 초기화)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `board_images`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `board_tags`;
DROP TABLE IF EXISTS `review_photos`;
DROP TABLE IF EXISTS `reviews`;
DROP TABLE IF EXISTS `attraction_likes`;
DROP TABLE IF EXISTS `plan_attractions`;
DROP TABLE IF EXISTS `plans`;
DROP TABLE IF EXISTS `trips`;
DROP TABLE IF EXISTS `group_members`;
DROP TABLE IF EXISTS `groups`;
DROP TABLE IF EXISTS `boards`;
DROP TABLE IF EXISTS `board_likes`;
DROP TABLE IF EXISTS `comments`;
DROP TABLE IF EXISTS `notices`;
DROP TABLE IF EXISTS `attractions`;
DROP TABLE IF EXISTS `guguns`;
DROP TABLE IF EXISTS `sidos`;
DROP TABLE IF EXISTS `contenttypes`;
DROP TABLE IF EXISTS `members`;
DROP TABLE IF EXISTS `roles`;

-- (0) 권한
CREATE TABLE IF NOT EXISTS `roles` (
  `no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(20) UNIQUE,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (1) 시도
CREATE TABLE IF NOT EXISTS `sidos` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `sido_code` INT NOT NULL,
  `sido_name` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`no`),
  UNIQUE INDEX `sido_code_UNIQUE` (`sido_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (2) 구군
CREATE TABLE IF NOT EXISTS `guguns` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `sido_code` INT NOT NULL,
  `gugun_code` INT NOT NULL,
  `gugun_name` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`no`),
  UNIQUE INDEX `sido_gugun_unique` (`sido_code`, `gugun_code`),
  FOREIGN KEY (`sido_code`) REFERENCES `sidos` (`sido_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (3) 콘텐츠 타입
CREATE TABLE IF NOT EXISTS `contenttypes` (
  `content_type_id` INT NOT NULL,
  `content_type_name` VARCHAR(45),
  PRIMARY KEY (`content_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (4) 사용자
CREATE TABLE IF NOT EXISTS `members` (
  `mno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(254) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `role` INT UNSIGNED NOT NULL,
  `profile_url` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`mno`),
  FOREIGN KEY (`role`) REFERENCES `roles` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (5) 관광지
CREATE TABLE IF NOT EXISTS `attractions` (
  `no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `content_id` INT,
  `title` VARCHAR(500),
  `content_type_id` INT,
  `area_code` INT,
  `si_gun_gu_code` INT,
  `first_image1` VARCHAR(100),
  `first_image2` VARCHAR(100),
  `map_level` INT,
  `latitude` DECIMAL(20,17),
  `longitude` DECIMAL(20,17),
  `tel` VARCHAR(20),
  `addr1` VARCHAR(100),
  `addr2` VARCHAR(100),
  `homepage` VARCHAR(1000),
  `overview` VARCHAR(10000),
  PRIMARY KEY (`no`),
  FOREIGN KEY (`content_type_id`) REFERENCES `contenttypes` (`content_type_id`),
  FOREIGN KEY (`area_code`) REFERENCES `sidos` (`sido_code`),
  FOREIGN KEY (`area_code`, `si_gun_gu_code`) REFERENCES `guguns` (`sido_code`, `gugun_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (6) 후기
CREATE TABLE IF NOT EXISTS `reviews` (
  `rno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED,
  `ano` INT UNSIGNED,
  `rating` TINYINT NOT NULL,
  `content` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rno`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`),
  FOREIGN KEY (`ano`) REFERENCES `attractions`(`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (7) 후기 사진
CREATE TABLE IF NOT EXISTS `review_photos` (
  `no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rno` INT UNSIGNED NOT NULL,
  `image_path` VARCHAR(500),
  PRIMARY KEY (`no`),
  FOREIGN KEY (`rno`) REFERENCES `reviews`(`rno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (8) 관광지 좋아요
CREATE TABLE IF NOT EXISTS `attraction_likes` (
  `no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED NOT NULL,
  `ano` INT UNSIGNED,
  `liked_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`no`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`ano`) REFERENCES `attractions`(`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (9) 그룹
CREATE TABLE IF NOT EXISTS `groups` (
  `gno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`gno`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (10) 그룹-회원
CREATE TABLE IF NOT EXISTS `group_members` (
  `no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `gno` INT UNSIGNED,
  `mno` INT UNSIGNED,
  `role` INT UNSIGNED NOT NULL,
  `joined_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`no`),
  FOREIGN KEY (`gno`) REFERENCES `groups`(`gno`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`),
  FOREIGN KEY (`role`) REFERENCES `roles` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (11) 여행
CREATE TABLE IF NOT EXISTS `trips` (
  `tno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `gno` INT UNSIGNED,
  `mno` INT UNSIGNED,
  `title` VARCHAR(150),
  `description` TEXT,
  `start_date` DATE,
  `end_date` DATE,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `thumbnail_url` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`tno`),
  FOREIGN KEY (`gno`) REFERENCES `groups`(`gno`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (12) 계획 (경로)
CREATE TABLE IF NOT EXISTS `plans` (
  `pno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED,
  `tno` INT UNSIGNED,
  `title` VARCHAR(150),
  `description` TEXT,
  `start_date` DATE,
  `end_date` DATE,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pno`),
  FOREIGN KEY (`tno`) REFERENCES `trips`(`tno`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (13) 계획-관광지 매핑
CREATE TABLE IF NOT EXISTS `plan_attractions` (
  `no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `pno` INT UNSIGNED NOT NULL,
  `ano` INT UNSIGNED,
  `sequence_order` INT NOT NULL,
  `start_date` DATETIME,
  `end_date` DATETIME,
  PRIMARY KEY (`no`),
  FOREIGN KEY (`pno`) REFERENCES `plans`(`pno`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`ano`) REFERENCES `attractions`(`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (14) 게시판
CREATE TABLE IF NOT EXISTS `boards` (
  `bno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED,
  `title` VARCHAR(200) NOT NULL,
  `content` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`bno`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (15) 게시글 좋아요
CREATE TABLE IF NOT EXISTS `board_likes` (
  `no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED NOT NULL,
  `bno` INT UNSIGNED,
  `liked_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`no`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`bno`) REFERENCES `boards`(`bno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (16) 댓글
CREATE TABLE IF NOT EXISTS `comments` (
  `cno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `bno` INT UNSIGNED NOT NULL,
  `mno` INT UNSIGNED,
  `content` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cno`),
  FOREIGN KEY (`bno`) REFERENCES `boards`(`bno`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (17) 공지사항
CREATE TABLE IF NOT EXISTS `notices` (
  `nno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED,
  `title` VARCHAR(200) NOT NULL,
  `content` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`nno`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (18) 질문게시판
CREATE TABLE IF NOT EXISTS `questions` (
  `question_no` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mno` INT UNSIGNED,
  `title` VARCHAR(200) NOT NULL,
  `content` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`question_no`),
  FOREIGN KEY (`mno`) REFERENCES `members`(`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (19) 답변
CREATE TABLE IF NOT EXISTS `answers` (
  `answer_no`    INT UNSIGNED      NOT NULL AUTO_INCREMENT,
  `question_no`  INT UNSIGNED      NOT NULL,
  `mno`          INT UNSIGNED      NOT NULL,
  `content`      TEXT              NOT NULL,
  `created_at`   DATETIME          DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`answer_no`),
  FOREIGN KEY (`question_no`) REFERENCES `questions` (`question_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`mno`) REFERENCES `members` (`mno`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- (20) 게시판 이미지
CREATE TABLE IF NOT EXISTS `board_images` (
    `img_id` INT AUTO_INCREMENT PRIMARY KEY,
    `bno` INT UNSIGNED NOT NULL,
    `url` VARCHAR(300) NOT NULL,
    `seq` INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (`bno`) REFERENCES `boards`(`bno`) ON DELETE CASCADE
);

-- (21) 태그
CREATE TABLE IF NOT EXISTS `tags` (
    `tag_id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(30) NOT NULL UNIQUE
);

-- (22) 게시판 태그
CREATE TABLE IF NOT EXISTS `board_tags` (
    `bno` INT UNSIGNED NOT NULL,
    `tag_id` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`bno`, `tag_id`),
    FOREIGN KEY (`bno`) REFERENCES `boards`(`bno`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `tags`(`tag_id`) ON DELETE CASCADE
);


-- -----------------------------------------------------
-- 설정 복원
-- -----------------------------------------------------
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;