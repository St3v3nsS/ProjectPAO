-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema cineJohn
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cineJohn
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `CineJohn` DEFAULT CHARACTER SET utf8 ;
USE `CineJohn` ;

-- -----------------------------------------------------
-- Table `cineJohn`.`movies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CineJohn`.`movies` (
  `idmovies` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `cast` VARCHAR(45) NOT NULL,
  `duration` VARCHAR(45) NULL,
  `genre` VARCHAR(45) NULL,
  `location` VARCHAR(45) NULL,
  `normalSeats` INT NULL,
  `vipSeats` INT NULL,
  `type` VARCHAR(45) NULL,
  `imdbNote` VARCHAR(45) NULL,
  PRIMARY KEY (`idmovies`),
  UNIQUE INDEX `idmovies_UNIQUE` (`idmovies` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cineJohn`.`theatres`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cineJohn`.`theatres` (
  `idtheatres` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `cast` VARCHAR(45) NOT NULL,
  `duration` VARCHAR(45) NOT NULL,
  `genre` VARCHAR(45) NOT NULL,
  `location` VARCHAR(45) NOT NULL,
  `normalSeats` VARCHAR(45) NOT NULL,
  `vipSeats` VARCHAR(45) NULL,
  `scenery` VARCHAR(45) NULL,
  `author` VARCHAR(45) NULL,
  PRIMARY KEY (`idtheatres`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cineJohn`.`movies_to_add`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cineJohn`.`movies_to_add` (
  `idmovies` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `cast` VARCHAR(45) NOT NULL,
  `duration` VARCHAR(45) NULL,
  `genre` VARCHAR(45) NULL,
  `location` VARCHAR(45) NULL,
  `normalSeats` INT NULL,
  `vipSeats` INT NULL,
  `type` VARCHAR(45) NULL,
  `imdbNote` VARCHAR(45) NULL,
  PRIMARY KEY (`idmovies`),
  UNIQUE INDEX `idmovies_UNIQUE` (`idmovies` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cineJohn`.`theatres_to_add`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cineJohn`.`theatres_to_add` (
  `idtheatres` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `cast` VARCHAR(45) NOT NULL,
  `duration` VARCHAR(45) NOT NULL,
  `genre` VARCHAR(45) NOT NULL,
  `location` VARCHAR(45) NOT NULL,
  `normalSeats` VARCHAR(45) NOT NULL,
  `vipSeats` VARCHAR(45) NULL,
  `scenery` VARCHAR(45) NULL,
  `author` VARCHAR(45) NULL,
  PRIMARY KEY (`idtheatres`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cineJohn`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cineJohn`.`clients` (
  `idclients` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `seats` VARCHAR(45) NULL,
  `payment_type` VARCHAR(45) NULL,
  `spectacle` VARCHAR(45) NULL,
  `is_vip` VARCHAR(45) NULL,
  PRIMARY KEY (`idclients`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
