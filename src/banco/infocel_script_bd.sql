-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema infocel
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema infocel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `neoli831_teste` DEFAULT CHARACTER SET utf8 ;
USE `neoli831_teste` ;

-- -----------------------------------------------------
-- Table `infocel`.`administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`administrador` (
  `id_administrador` INT(11) NOT NULL,
  PRIMARY KEY (`id_administrador`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`categoria_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`categoria_produto` (
  `id_categoria_produto` INT(11) NOT NULL,
  PRIMARY KEY (`id_categoria_produto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`categoria_saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`categoria_saida` (
  `id_categoria_saida` INT(11) NOT NULL,
  PRIMARY KEY (`id_categoria_saida`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`cliente` (
  `id_cliente` INT(11) NOT NULL,
  PRIMARY KEY (`id_cliente`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`forma_pagamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`forma_pagamento` (
  `id_forma_pagamento` INT(11) NOT NULL,
  PRIMARY KEY (`id_forma_pagamento`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`manutencao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`manutencao` (
  `id_manutencao` INT(11) NOT NULL,
  `administrador_id` INT(11) NOT NULL,
  `cliente_id` INT(11) NOT NULL,
  PRIMARY KEY (`id_manutencao`, `administrador_id`, `cliente_id`),
  INDEX `fk_manutencao_administrador1_idx` (`administrador_id` ASC),
  INDEX `fk_manutencao_cliente1_idx` (`cliente_id` ASC),
  CONSTRAINT `fk_manutencao_administrador1`
    FOREIGN KEY (`administrador_id`)
    REFERENCES `neoli831_teste`.`administrador` (`id_administrador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manutencao_cliente1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `neoli831_teste`.`cliente` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`marca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`marca` (
  `id_marca` INT(11) NOT NULL,
  PRIMARY KEY (`id_marca`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`unidade_medida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`unidade_medida` (
  `idunidade_medida` INT(11) NOT NULL,
  PRIMARY KEY (`idunidade_medida`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`produto` (
  `id_produto` INT(11) NOT NULL,
  `marca_id` INT(11) NOT NULL,
  `unidade_medida_id` INT(11) NOT NULL,
  `categoria_produto_id` INT(11) NOT NULL,
  PRIMARY KEY (`id_produto`, `marca_id`, `unidade_medida_id`, `categoria_produto_id`),
  INDEX `fk_produto_marca_idx` (`marca_id` ASC),
  INDEX `fk_produto_unidade_medida1_idx` (`unidade_medida_id` ASC),
  INDEX `fk_produto_categoria_produto1_idx` (`categoria_produto_id` ASC),
  CONSTRAINT `fk_produto_categoria_produto1`
    FOREIGN KEY (`categoria_produto_id`)
    REFERENCES `neoli831_teste`.`categoria_produto` (`id_categoria_produto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_marca`
    FOREIGN KEY (`marca_id`)
    REFERENCES `neoli831_teste`.`marca` (`id_marca`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_unidade_medida1`
    FOREIGN KEY (`unidade_medida_id`)
    REFERENCES `neoli831_teste`.`unidade_medida` (`idunidade_medida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`receita`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`receita` (
  `id_receita` INT(11) NOT NULL,
  `administrador_id` INT(11) NOT NULL,
  `cliente_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id_receita`, `administrador_id`),
  INDEX `fk_receita_administrador1_idx` (`administrador_id` ASC),
  INDEX `fk_receita_cliente1_idx` (`cliente_id` ASC),
  CONSTRAINT `fk_receita_administrador1`
    FOREIGN KEY (`administrador_id`)
    REFERENCES `neoli831_teste`.`administrador` (`id_administrador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_receita_cliente1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `neoli831_teste`.`cliente` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`saida` (
  `id_saida` INT(11) NOT NULL,
  `administrador_id` INT(11) NOT NULL,
  `categoria_saida_id` INT(11) NOT NULL,
  PRIMARY KEY (`id_saida`, `administrador_id`, `categoria_saida_id`),
  INDEX `fk_saida_administrador1_idx` (`administrador_id` ASC),
  INDEX `fk_saida_categoria_saida1_idx` (`categoria_saida_id` ASC),
  CONSTRAINT `fk_saida_administrador1`
    FOREIGN KEY (`administrador_id`)
    REFERENCES `neoli831_teste`.`administrador` (`id_administrador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_saida_categoria_saida1`
    FOREIGN KEY (`categoria_saida_id`)
    REFERENCES `neoli831_teste`.`categoria_saida` (`id_categoria_saida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`venda` (
  `id_venda` INT(11) NOT NULL,
  `forma_pagamento_id` INT(11) NOT NULL,
  `administrador_id` INT(11) NOT NULL,
  `cliente_id` INT(11) NOT NULL,
  PRIMARY KEY (`id_venda`, `forma_pagamento_id`, `administrador_id`, `cliente_id`),
  INDEX `fk_venda_forma_pagamento1_idx` (`forma_pagamento_id` ASC),
  INDEX `fk_venda_administrador1_idx` (`administrador_id` ASC),
  INDEX `fk_venda_cliente1_idx` (`cliente_id` ASC),
  CONSTRAINT `fk_venda_administrador1`
    FOREIGN KEY (`administrador_id`)
    REFERENCES `neoli831_teste`.`administrador` (`id_administrador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_cliente1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `neoli831_teste`.`cliente` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_forma_pagamento1`
    FOREIGN KEY (`forma_pagamento_id`)
    REFERENCES `neoli831_teste`.`forma_pagamento` (`id_forma_pagamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `infocel`.`venda_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`venda_produto` (
  `venda_id` INT(11) NOT NULL,
  `produto_id` INT(11) NOT NULL,
  PRIMARY KEY (`venda_id`, `produto_id`),
  INDEX `fk_venda_has_produto_produto1_idx` (`produto_id` ASC),
  INDEX `fk_venda_has_produto_venda1_idx` (`venda_id` ASC),
  CONSTRAINT `fk_venda_has_produto_produto1`
    FOREIGN KEY (`produto_id`)
    REFERENCES `neoli831_teste`.`produto` (`id_produto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_has_produto_venda1`
    FOREIGN KEY (`venda_id`)
    REFERENCES `neoli831_teste`.`venda` (`id_venda`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

