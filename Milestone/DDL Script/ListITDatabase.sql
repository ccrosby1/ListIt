-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema listit
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema listit
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `listit` DEFAULT CHARACTER SET utf8 ;
USE `listit` ;

-- -----------------------------------------------------
-- Table `listit`.`user_credentials`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`user_credentials` (
  `user_id` INT(10) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `listit`.`catalog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`catalog` (
  `catalog_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) NOT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(500) NULL DEFAULT NULL,
  `image` VARCHAR(200) NULL DEFAULT NULL,
  `color` VARCHAR(45) NULL DEFAULT NULL,
  `created_date` DATETIME NULL DEFAULT NULL,
  `updated_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`catalog_id`),
  UNIQUE INDEX `catalog_id_UNIQUE` (`catalog_id` ASC),
  INDEX `fk_catalog_user_credentials1_idx` (`user_id` ASC),
  CONSTRAINT `fk_catalog_user_credentials1`
    FOREIGN KEY (`user_id`)
    REFERENCES `listit`.`user_credentials` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `listit`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`category` (
  `category_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `parent_id` INT(11) NULL DEFAULT NULL,
  `user_id` INT(10) NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE INDEX `category_id_UNIQUE` (`category_id` ASC),
  INDEX `fk_category_user_credentials1_idx` (`user_id` ASC),
  CONSTRAINT `fk_category_user_credentials1`
    FOREIGN KEY (`user_id`)
    REFERENCES `listit`.`user_credentials` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `listit`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`product` (
  `product_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `brand` VARCHAR(45) NULL DEFAULT NULL,
  `quntity` INT(11) NULL DEFAULT NULL,
  `description` VARCHAR(500) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `image` VARCHAR(200) NULL DEFAULT NULL,
  `created_date` DATETIME NULL DEFAULT NULL,
  `updated_date` DATETIME NULL DEFAULT NULL,
  `category_category_id` INT(11) NOT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE INDEX `product_id_UNIQUE` (`product_id` ASC),
  INDEX `fk_product_category1_idx` (`category_category_id` ASC),
  CONSTRAINT `fk_product_category1`
    FOREIGN KEY (`category_category_id`)
    REFERENCES `listit`.`category` (`category_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `listit`.`catalog_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`catalog_product` (
  `product_product_id` INT(11) NOT NULL,
  `catalog_catalog_id` INT(11) NOT NULL,
  INDEX `fk_catalog_product_catalog1_idx` (`catalog_catalog_id` ASC),
  INDEX `fk_catalog_product_product1` (`product_product_id` ASC),
  CONSTRAINT `fk_catalog_product_catalog1`
    FOREIGN KEY (`catalog_catalog_id`)
    REFERENCES `listit`.`catalog` (`catalog_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_catalog_product_product1`
    FOREIGN KEY (`product_product_id`)
    REFERENCES `listit`.`product` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `listit`.`catalog_share`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`catalog_share` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    catalog_id INT NOT NULL,
    shared_user_id INT NOT NULL,
    permission ENUM('view','edit') DEFAULT 'view',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_share_catalog
        FOREIGN KEY (catalog_id)
        REFERENCES catalog(catalog_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_share_user
        FOREIGN KEY (shared_user_id)
        REFERENCES user_credentials(user_id)
        ON DELETE CASCADE,

    UNIQUE KEY uq_catalog_user (catalog_id, shared_user_id)
);


-- -----------------------------------------------------
-- Table `listit`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`roles` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name` (`name` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `listit`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`user` (
  `user_id` INT(10) NOT NULL,
  `first_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `phone` VARCHAR(15) NULL DEFAULT NULL,
  `created_date` DATETIME NULL DEFAULT NULL,
  `update_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_credentials_user_id_UNIQUE` (`user_id` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  CONSTRAINT `fk_user_user_credentials1`
    FOREIGN KEY (`user_id`)
    REFERENCES `listit`.`user_credentials` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `listit`.`user_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `listit`.`user_roles` (
  `roles_id` INT(11) NOT NULL,
  `user_id` INT(10) NOT NULL,
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),
  INDEX `fk_user_roles_roles1_idx` (`roles_id` ASC),
  INDEX `fk_user_roles_user_credentials1_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_roles_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `listit`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_roles_user_credentials1`
    FOREIGN KEY (`user_id`)
    REFERENCES `listit`.`user_credentials` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `listit`.`user_credentials`
-- -----------------------------------------------------
START TRANSACTION;
USE `listit`;
INSERT INTO `listit`.`user_credentials` (`user_id`, `username`, `password`) VALUES (1, 'admin', '$2a$10$JPeLvy9/LVoyYP3kUm3NCeeIAOkPpU5x1/mz.FKuvgTsb5m/TAx4u');

COMMIT;


-- -----------------------------------------------------
-- Data for table `listit`.`category`
-- -----------------------------------------------------
START TRANSACTION;
USE `listit`;
INSERT INTO `listit`.`category` (`category_id`, `name`, `parent_id`, `user_id`) VALUES (1, 'Default', NULL, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `listit`.`roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `listit`;
INSERT INTO `listit`.`roles` (`id`, `name`) VALUES (1, 'admin');
INSERT INTO `listit`.`roles` (`id`, `name`) VALUES (2, 'user');

COMMIT;


-- -----------------------------------------------------
-- Data for table `listit`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `listit`;
INSERT INTO `listit`.`user` (`user_id`, `first_name`, `last_name`, `email`, `phone`, `created_date`, `update_date`) VALUES (1, 'Admin', 'User', 'na@na.com', '5555555555', '2025-05-10 18:15:55', '2025-05-10 18:15:55');

COMMIT;


-- -----------------------------------------------------
-- Data for table `listit`.`user_roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `listit`;
INSERT INTO `listit`.`user_roles` (`roles_id`, `user_id`) VALUES (1, 1);

COMMIT;

