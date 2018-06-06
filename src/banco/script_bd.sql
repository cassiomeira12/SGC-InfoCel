-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema neoli831_teste
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema neoli831_teste
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `neoli831_teste` DEFAULT CHARACTER SET utf8 ;
USE `neoli831_teste` ;

-- -----------------------------------------------------
-- Table `neoli831_teste`.`cidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`cidade` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`bairro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`bairro` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `id_cidade` INT(11) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `id_cidade`),
  INDEX `fk_bairro_cidade1_idx` (`id_cidade` ASC),
  CONSTRAINT `fk_bairro_cidade1`
    FOREIGN KEY (`id_cidade`)
    REFERENCES `neoli831_teste`.`cidade` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`endereco` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `rua` VARCHAR(30) NOT NULL,
  `numero` VARCHAR(8) NULL DEFAULT NULL,
  `id_bairro` INT(11) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `id_bairro`),
  INDEX `fk_endereco_bairro1_idx` (`id_bairro` ASC),
  CONSTRAINT `fk_endereco_bairro1`
    FOREIGN KEY (`id_bairro`)
    REFERENCES `neoli831_teste`.`bairro` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`administrador` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `login` VARCHAR(20) NOT NULL,
  `senha` VARCHAR(20) NOT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `cpf` VARCHAR(15) NOT NULL,
  `rg` VARCHAR(20) NOT NULL,
  `data_cadastro` BIGINT(20) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  `id_endereco` INT(11) NOT NULL,
  PRIMARY KEY (`id`, `id_endereco`),
  INDEX `fk_administrador_endereco1_idx` (`id_endereco` ASC),
  CONSTRAINT `fk_administrador_endereco1`
    FOREIGN KEY (`id_endereco`)
    REFERENCES `neoli831_teste`.`endereco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`categoria_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`categoria_produto` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`categoria_saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`categoria_saida` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`cliente` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `cpf` VARCHAR(15) NOT NULL,
  `rg` VARCHAR(15) NOT NULL,
  `telefone` VARCHAR(20) NOT NULL,
  `data_cadastro` BIGINT(20) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  `id_endereco` INT(11) NOT NULL,
  PRIMARY KEY (`id`, `id_endereco`),
  INDEX `fk_cliente_endereco1_idx` (`id_endereco` ASC),
  CONSTRAINT `fk_cliente_endereco1`
    FOREIGN KEY (`id_endereco`)
    REFERENCES `neoli831_teste`.`endereco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`forma_pagamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`forma_pagamento` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `maximo_parcelas` INT(11) NOT NULL DEFAULT '1',
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`manutencao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`manutencao` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_administrador` INT(11) NOT NULL,
  `id_forma_pagamento` INT(11) NOT NULL,
  `id_cliente` INT(11) NOT NULL,
  `descricao` VARCHAR(256) NOT NULL,
  `marca` VARCHAR(20) NOT NULL,
  `modelo` VARCHAR(20) NOT NULL,
  `imei` VARCHAR(100) NOT NULL,
  `cor` VARCHAR(20) NOT NULL,
  `data_cadastro` BIGINT(20) NOT NULL,
  `data_previsao` BIGINT(20) NULL DEFAULT NULL,
  `data_entrega` BIGINT(20) NULL DEFAULT NULL,
  `preco` FLOAT NOT NULL,
  `finalizado` TINYINT(1) NOT NULL,
  `quantidade_parcelas` INT(11) NULL DEFAULT '1',
  PRIMARY KEY (`id`, `id_administrador`, `id_forma_pagamento`, `id_cliente`),
  INDEX `fk_manutencao_forma_pagamento1_idx` (`id_forma_pagamento` ASC),
  INDEX `fk_manutencao_administrador1_idx` (`id_administrador` ASC),
  INDEX `fk_manutencao_cliente1_idx` (`id_cliente` ASC),
  CONSTRAINT `fk_manutencao_forma_pagamento1`
    FOREIGN KEY (`id_forma_pagamento`)
    REFERENCES `neoli831_teste`.`forma_pagamento` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manutencao_administrador1`
    FOREIGN KEY (`id_administrador`)
    REFERENCES `neoli831_teste`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manutencao_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `neoli831_teste`.`cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`marca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`marca` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(20) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`unidade_medida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`unidade_medida` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(20) NOT NULL,
  `abreviacao` VARCHAR(5) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`produto` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_marca` INT(11) NOT NULL,
  `id_unidade_medida` INT(11) NOT NULL,
  `id_categoria_produto` INT(11) NOT NULL,
  `descricao` VARCHAR(100) NOT NULL,
  `preco_compra` FLOAT NOT NULL,
  `preco_venda` FLOAT NOT NULL,
  `estoque` FLOAT NOT NULL,
  `eh_celular` TINYINT(1) NULL DEFAULT NULL,
  `status` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `id_marca`, `id_unidade_medida`, `id_categoria_produto`),
  INDEX `fk_produto_marca_idx` (`id_marca` ASC),
  INDEX `fk_produto_unidade_medida1_idx` (`id_unidade_medida` ASC),
  INDEX `fk_produto_categoria_produto1_idx` (`id_categoria_produto` ASC),
  CONSTRAINT `fk_produto_categoria_produto1`
    FOREIGN KEY (`id_categoria_produto`)
    REFERENCES `neoli831_teste`.`categoria_produto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_marca`
    FOREIGN KEY (`id_marca`)
    REFERENCES `neoli831_teste`.`marca` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_unidade_medida1`
    FOREIGN KEY (`id_unidade_medida`)
    REFERENCES `neoli831_teste`.`unidade_medida` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`receita`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`receita` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_administrador` INT(11) NOT NULL,
  `id_cliente` INT(11) NULL DEFAULT NULL,
  `descricao` VARCHAR(256) NOT NULL,
  `data` BIGINT(20) NOT NULL,
  `valor` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`, `id_administrador`),
  INDEX `fk_receita_administrador1_idx` (`id_administrador` ASC),
  INDEX `fk_receita_cliente1_idx` (`id_cliente` ASC),
  CONSTRAINT `fk_receita_administrador1`
    FOREIGN KEY (`id_administrador`)
    REFERENCES `neoli831_teste`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_receita_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `neoli831_teste`.`cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`saida` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_administrador` INT(11) NOT NULL,
  `id_categoria_saida` INT(11) NOT NULL,
  `descricao` VARCHAR(256) NOT NULL,
  `valor` FLOAT NOT NULL,
  `data` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`, `id_administrador`, `id_categoria_saida`),
  INDEX `fk_saida_administrador1_idx` (`id_administrador` ASC),
  INDEX `fk_saida_categoria_saida1_idx` (`id_categoria_saida` ASC),
  CONSTRAINT `fk_saida_administrador1`
    FOREIGN KEY (`id_administrador`)
    REFERENCES `neoli831_teste`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_saida_categoria_saida1`
    FOREIGN KEY (`id_categoria_saida`)
    REFERENCES `neoli831_teste`.`categoria_saida` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`venda` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_forma_pagamento` INT(11) NOT NULL,
  `id_administrador` INT(11) NOT NULL,
  `id_cliente` INT(11) NOT NULL,
  `preco_total` FLOAT NOT NULL,
  `data` BIGINT(20) NOT NULL,
  `quantidade_parcela` INT(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`, `id_forma_pagamento`, `id_administrador`, `id_cliente`),
  INDEX `fk_venda_forma_pagamento1_idx` (`id_forma_pagamento` ASC),
  INDEX `fk_venda_administrador1_idx` (`id_administrador` ASC),
  INDEX `fk_venda_cliente1_idx` (`id_cliente` ASC),
  CONSTRAINT `fk_venda_administrador1`
    FOREIGN KEY (`id_administrador`)
    REFERENCES `neoli831_teste`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `neoli831_teste`.`cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_forma_pagamento1`
    FOREIGN KEY (`id_forma_pagamento`)
    REFERENCES `neoli831_teste`.`forma_pagamento` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `neoli831_teste`.`venda_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`venda_produto` (
  `id_venda` INT(11) NOT NULL,
  `id_produto` INT(11) NOT NULL,
  `preco_total` FLOAT NOT NULL,
  `quantidade` FLOAT NOT NULL,
  PRIMARY KEY (`id_venda`, `id_produto`),
  INDEX `fk_venda_has_produto_produto1_idx` (`id_produto` ASC),
  INDEX `fk_venda_has_produto_venda1_idx` (`id_venda` ASC),
  CONSTRAINT `fk_venda_has_produto_produto1`
    FOREIGN KEY (`id_produto`)
    REFERENCES `neoli831_teste`.`produto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_has_produto_venda1`
    FOREIGN KEY (`id_venda`)
    REFERENCES `neoli831_teste`.`venda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

USE `neoli831_teste` ;

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_administrador` (`id` INT, `nome` INT, `login` INT, `senha` INT, `email` INT, `cpf` INT, `rg` INT, `data_cadastro` INT, `status` INT, `id_endereco` INT, `numero` INT, `rua` INT, `id_bairro` INT, `id_cidade` INT, `nome_cidade` INT, `nome_bairro` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_bairro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_bairro` (`id` INT, `nome` INT, `status` INT, `nome_cidade` INT, `id_cidade` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_cliente` (`id` INT, `nome` INT, `cpf` INT, `rg` INT, `telefone` INT, `data_cadastro` INT, `status` INT, `id_endereco` INT, `numero` INT, `rua` INT, `id_bairro` INT, `id_cidade` INT, `nome_cidade` INT, `nome_bairro` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_endereco` (`id` INT, `numero` INT, `rua` INT, `status` INT, `id_bairro` INT, `nome_bairro` INT, `nome_cidade` INT, `id_cidade` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_manutencao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_manutencao` (`id` INT, `id_administrador` INT, `quantidade_parcelas` INT, `id_cliente` INT, `id_forma_pagamento` INT, `descricao` INT, `marca` INT, `modelo` INT, `imei` INT, `cor` INT, `data_cadastro` INT, `data_previsao` INT, `data_entrega` INT, `preco` INT, `finalizado` INT, `nome_cliente` INT, `cpf_cliente` INT, `rg_cliente` INT, `telefone_cliente` INT, `id_endereco_cliente` INT, `numero_cliente` INT, `rua_cliente` INT, `id_bairro_cliente` INT, `id_cidade_cliente` INT, `nome_cidade_cliente` INT, `nome_bairro_cliente` INT, `nome_administrador` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT, `descricao_forma_pagamento` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_produto` (`id` INT, `status` INT, `id_marca` INT, `id_unidade_medida` INT, `id_categoria_produto` INT, `descricao` INT, `preco_compra` INT, `preco_venda` INT, `estoque` INT, `eh_celular` INT, `descricao_marca` INT, `descricao_categoria` INT, `descricao_unidade` INT, `abreviacao_unidade` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_receita`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_receita` (`id` INT, `id_administrador` INT, `id_cliente` INT, `descricao` INT, `data` INT, `valor` INT, `nome_cliente` INT, `cpf_cliente` INT, `rg_cliente` INT, `telefone_cliente` INT, `id_endereco_cliente` INT, `numero_cliente` INT, `rua_cliente` INT, `id_bairro_cliente` INT, `id_cidade_cliente` INT, `nome_cidade_cliente` INT, `nome_bairro_cliente` INT, `nome_administrador` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_saida` (`id` INT, `id_administrador` INT, `id_categoria_saida` INT, `nome_administrador` INT, `descricao` INT, `valor` INT, `data` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT, `descricao_categoria` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_venda` (`id` INT, `id_forma_pagamento` INT, `id_administrador` INT, `id_cliente` INT, `preco_total` INT, `data` INT, `quantidade_parcela` INT, `nome_cliente` INT, `cpf_cliente` INT, `rg_cliente` INT, `telefone_cliente` INT, `id_endereco_cliente` INT, `numero_cliente` INT, `rua_cliente` INT, `id_bairro_cliente` INT, `id_cidade_cliente` INT, `nome_cidade_cliente` INT, `nome_bairro_cliente` INT, `nome_administrador` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT, `descricao_forma_pagamento` INT);

-- -----------------------------------------------------
-- Placeholder table for view `neoli831_teste`.`view_venda_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `neoli831_teste`.`view_venda_produto` (`id_venda` INT, `id_produto` INT, `preco_total` INT, `quantidade` INT, `descricao_produto` INT, `preco_venda` INT, `descricao_marca` INT, `descricao_categoria` INT, `abreviacao` INT);

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_administrador`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_administrador`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_administrador` AS select `neoli831_teste`.`administrador`.`id` AS `id`,`neoli831_teste`.`administrador`.`nome` AS `nome`,`neoli831_teste`.`administrador`.`login` AS `login`,`neoli831_teste`.`administrador`.`senha` AS `senha`,`neoli831_teste`.`administrador`.`email` AS `email`,`neoli831_teste`.`administrador`.`cpf` AS `cpf`,`neoli831_teste`.`administrador`.`rg` AS `rg`,`neoli831_teste`.`administrador`.`data_cadastro` AS `data_cadastro`,`neoli831_teste`.`administrador`.`status` AS `status`,`neoli831_teste`.`administrador`.`id_endereco` AS `id_endereco`,`view_endereco`.`numero` AS `numero`,`view_endereco`.`rua` AS `rua`,`view_endereco`.`id_bairro` AS `id_bairro`,`view_endereco`.`id_cidade` AS `id_cidade`,`view_endereco`.`nome_cidade` AS `nome_cidade`,`view_endereco`.`nome_bairro` AS `nome_bairro` from (`neoli831_teste`.`administrador` join `neoli831_teste`.`view_endereco` on((`neoli831_teste`.`administrador`.`id_endereco` = `view_endereco`.`id`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_bairro`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_bairro`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_bairro` AS select `neoli831_teste`.`bairro`.`id` AS `id`,`neoli831_teste`.`bairro`.`nome` AS `nome`,`neoli831_teste`.`bairro`.`status` AS `status`,`neoli831_teste`.`cidade`.`nome` AS `nome_cidade`,`neoli831_teste`.`cidade`.`id` AS `id_cidade` from (`neoli831_teste`.`bairro` join `neoli831_teste`.`cidade` on((`neoli831_teste`.`cidade`.`id` = `neoli831_teste`.`bairro`.`id_cidade`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_cliente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_cliente`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_cliente` AS select `neoli831_teste`.`cliente`.`id` AS `id`,`neoli831_teste`.`cliente`.`nome` AS `nome`,`neoli831_teste`.`cliente`.`cpf` AS `cpf`,`neoli831_teste`.`cliente`.`rg` AS `rg`,`neoli831_teste`.`cliente`.`telefone` AS `telefone`,`neoli831_teste`.`cliente`.`data_cadastro` AS `data_cadastro`,`neoli831_teste`.`cliente`.`status` AS `status`,`neoli831_teste`.`cliente`.`id_endereco` AS `id_endereco`,`view_endereco`.`numero` AS `numero`,`view_endereco`.`rua` AS `rua`,`view_endereco`.`id_bairro` AS `id_bairro`,`view_endereco`.`id_cidade` AS `id_cidade`,`view_endereco`.`nome_cidade` AS `nome_cidade`,`view_endereco`.`nome_bairro` AS `nome_bairro` from (`neoli831_teste`.`cliente` join `neoli831_teste`.`view_endereco` on((`neoli831_teste`.`cliente`.`id_endereco` = `view_endereco`.`id`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_endereco`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_endereco`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_endereco` AS select `neoli831_teste`.`endereco`.`id` AS `id`,`neoli831_teste`.`endereco`.`numero` AS `numero`,`neoli831_teste`.`endereco`.`rua` AS `rua`,`neoli831_teste`.`endereco`.`status` AS `status`,`view_bairro`.`id` AS `id_bairro`,`view_bairro`.`nome` AS `nome_bairro`,`view_bairro`.`nome_cidade` AS `nome_cidade`,`view_bairro`.`id_cidade` AS `id_cidade` from (`neoli831_teste`.`endereco` join `neoli831_teste`.`view_bairro` on((`view_bairro`.`id` = `neoli831_teste`.`endereco`.`id_bairro`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_manutencao`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_manutencao`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_manutencao` AS select `neoli831_teste`.`manutencao`.`id` AS `id`,`neoli831_teste`.`manutencao`.`id_administrador` AS `id_administrador`,`neoli831_teste`.`manutencao`.`quantidade_parcelas` AS `quantidade_parcelas`,`neoli831_teste`.`manutencao`.`id_cliente` AS `id_cliente`,`neoli831_teste`.`manutencao`.`id_forma_pagamento` AS `id_forma_pagamento`,`neoli831_teste`.`manutencao`.`descricao` AS `descricao`,`neoli831_teste`.`manutencao`.`marca` AS `marca`,`neoli831_teste`.`manutencao`.`modelo` AS `modelo`,`neoli831_teste`.`manutencao`.`imei` AS `imei`,`neoli831_teste`.`manutencao`.`cor` AS `cor`,`neoli831_teste`.`manutencao`.`data_cadastro` AS `data_cadastro`,`neoli831_teste`.`manutencao`.`data_previsao` AS `data_previsao`,`neoli831_teste`.`manutencao`.`data_entrega` AS `data_entrega`,`neoli831_teste`.`manutencao`.`preco` AS `preco`,`neoli831_teste`.`manutencao`.`finalizado` AS `finalizado`,`view_cliente`.`nome` AS `nome_cliente`,`view_cliente`.`cpf` AS `cpf_cliente`,`view_cliente`.`rg` AS `rg_cliente`,`view_cliente`.`telefone` AS `telefone_cliente`,`view_cliente`.`id_endereco` AS `id_endereco_cliente`,`view_cliente`.`numero` AS `numero_cliente`,`view_cliente`.`rua` AS `rua_cliente`,`view_cliente`.`id_bairro` AS `id_bairro_cliente`,`view_cliente`.`id_cidade` AS `id_cidade_cliente`,`view_cliente`.`nome_cidade` AS `nome_cidade_cliente`,`view_cliente`.`nome_bairro` AS `nome_bairro_cliente`,`view_administrador`.`nome` AS `nome_administrador`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador`,`neoli831_teste`.`forma_pagamento`.`descricao` AS `descricao_forma_pagamento` from (((`neoli831_teste`.`manutencao` join `neoli831_teste`.`view_cliente` on((`neoli831_teste`.`manutencao`.`id_cliente` = `view_cliente`.`id`))) join `neoli831_teste`.`forma_pagamento` on((`neoli831_teste`.`forma_pagamento`.`id` = `neoli831_teste`.`manutencao`.`id_forma_pagamento`))) join `neoli831_teste`.`view_administrador` on((`view_administrador`.`id` = `neoli831_teste`.`manutencao`.`id_administrador`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_produto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_produto`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_produto` AS select `neoli831_teste`.`produto`.`id` AS `id`,`neoli831_teste`.`produto`.`status` AS `status`,`neoli831_teste`.`produto`.`id_marca` AS `id_marca`,`neoli831_teste`.`produto`.`id_unidade_medida` AS `id_unidade_medida`,`neoli831_teste`.`produto`.`id_categoria_produto` AS `id_categoria_produto`,`neoli831_teste`.`produto`.`descricao` AS `descricao`,`neoli831_teste`.`produto`.`preco_compra` AS `preco_compra`,`neoli831_teste`.`produto`.`preco_venda` AS `preco_venda`,`neoli831_teste`.`produto`.`estoque` AS `estoque`,`neoli831_teste`.`produto`.`eh_celular` AS `eh_celular`,`neoli831_teste`.`marca`.`descricao` AS `descricao_marca`,`neoli831_teste`.`categoria_produto`.`descricao` AS `descricao_categoria`,`neoli831_teste`.`unidade_medida`.`descricao` AS `descricao_unidade`,`neoli831_teste`.`unidade_medida`.`abreviacao` AS `abreviacao_unidade` from (((`neoli831_teste`.`produto` join `neoli831_teste`.`marca` on((`neoli831_teste`.`produto`.`id_marca` = `neoli831_teste`.`marca`.`id`))) join `neoli831_teste`.`unidade_medida` on((`neoli831_teste`.`produto`.`id_unidade_medida` = `neoli831_teste`.`unidade_medida`.`id`))) join `neoli831_teste`.`categoria_produto` on((`neoli831_teste`.`produto`.`id_categoria_produto` = `neoli831_teste`.`categoria_produto`.`id`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_receita`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_receita`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_receita` AS select `neoli831_teste`.`receita`.`id` AS `id`,`neoli831_teste`.`receita`.`id_administrador` AS `id_administrador`,`neoli831_teste`.`receita`.`id_cliente` AS `id_cliente`,`neoli831_teste`.`receita`.`descricao` AS `descricao`,`neoli831_teste`.`receita`.`data` AS `data`,`neoli831_teste`.`receita`.`valor` AS `valor`,`view_cliente`.`nome` AS `nome_cliente`,`view_cliente`.`cpf` AS `cpf_cliente`,`view_cliente`.`rg` AS `rg_cliente`,`view_cliente`.`telefone` AS `telefone_cliente`,`view_cliente`.`id_endereco` AS `id_endereco_cliente`,`view_cliente`.`numero` AS `numero_cliente`,`view_cliente`.`rua` AS `rua_cliente`,`view_cliente`.`id_bairro` AS `id_bairro_cliente`,`view_cliente`.`id_cidade` AS `id_cidade_cliente`,`view_cliente`.`nome_cidade` AS `nome_cidade_cliente`,`view_cliente`.`nome_bairro` AS `nome_bairro_cliente`,`view_administrador`.`nome` AS `nome_administrador`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador` from ((`neoli831_teste`.`receita` join `neoli831_teste`.`view_cliente` on((`neoli831_teste`.`receita`.`id_cliente` = `view_cliente`.`id`))) join `neoli831_teste`.`view_administrador` on((`view_administrador`.`id` = `neoli831_teste`.`receita`.`id_administrador`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_saida`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_saida`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_saida` AS select `neoli831_teste`.`saida`.`id` AS `id`,`neoli831_teste`.`saida`.`id_administrador` AS `id_administrador`,`neoli831_teste`.`saida`.`id_categoria_saida` AS `id_categoria_saida`,`view_administrador`.`nome` AS `nome_administrador`,`neoli831_teste`.`saida`.`descricao` AS `descricao`,`neoli831_teste`.`saida`.`valor` AS `valor`,`neoli831_teste`.`saida`.`data` AS `data`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador`,`neoli831_teste`.`categoria_saida`.`descricao` AS `descricao_categoria` from ((`neoli831_teste`.`saida` join `neoli831_teste`.`categoria_saida` on((`neoli831_teste`.`saida`.`id_categoria_saida` = `neoli831_teste`.`categoria_saida`.`id`))) join `neoli831_teste`.`view_administrador` on((`view_administrador`.`id` = `neoli831_teste`.`saida`.`id_administrador`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_venda`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_venda`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_venda` AS select `neoli831_teste`.`venda`.`id` AS `id`,`neoli831_teste`.`venda`.`id_forma_pagamento` AS `id_forma_pagamento`,`neoli831_teste`.`venda`.`id_administrador` AS `id_administrador`,`neoli831_teste`.`venda`.`id_cliente` AS `id_cliente`,`neoli831_teste`.`venda`.`preco_total` AS `preco_total`,`neoli831_teste`.`venda`.`data` AS `data`,`neoli831_teste`.`venda`.`quantidade_parcela` AS `quantidade_parcela`,`view_cliente`.`nome` AS `nome_cliente`,`view_cliente`.`cpf` AS `cpf_cliente`,`view_cliente`.`rg` AS `rg_cliente`,`view_cliente`.`telefone` AS `telefone_cliente`,`view_cliente`.`id_endereco` AS `id_endereco_cliente`,`view_cliente`.`numero` AS `numero_cliente`,`view_cliente`.`rua` AS `rua_cliente`,`view_cliente`.`id_bairro` AS `id_bairro_cliente`,`view_cliente`.`id_cidade` AS `id_cidade_cliente`,`view_cliente`.`nome_cidade` AS `nome_cidade_cliente`,`view_cliente`.`nome_bairro` AS `nome_bairro_cliente`,`view_administrador`.`nome` AS `nome_administrador`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador`,`neoli831_teste`.`forma_pagamento`.`descricao` AS `descricao_forma_pagamento` from (((`neoli831_teste`.`venda` join `neoli831_teste`.`forma_pagamento` on((`neoli831_teste`.`venda`.`id_forma_pagamento` = `neoli831_teste`.`forma_pagamento`.`id`))) join `neoli831_teste`.`view_cliente` on((`neoli831_teste`.`venda`.`id_cliente` = `view_cliente`.`id`))) join `neoli831_teste`.`view_administrador` on((`view_administrador`.`id` = `neoli831_teste`.`venda`.`id_administrador`)));

-- -----------------------------------------------------
-- View `neoli831_teste`.`view_venda_produto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `neoli831_teste`.`view_venda_produto`;
USE `neoli831_teste`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`neoli831`@`localhost` SQL SECURITY DEFINER VIEW `neoli831_teste`.`view_venda_produto` AS select `neoli831_teste`.`venda_produto`.`id_venda` AS `id_venda`,`neoli831_teste`.`venda_produto`.`id_produto` AS `id_produto`,`neoli831_teste`.`venda_produto`.`preco_total` AS `preco_total`,`neoli831_teste`.`venda_produto`.`quantidade` AS `quantidade`,`neoli831_teste`.`produto`.`descricao` AS `descricao_produto`,`neoli831_teste`.`produto`.`preco_venda` AS `preco_venda`,`neoli831_teste`.`marca`.`descricao` AS `descricao_marca`,`neoli831_teste`.`categoria_produto`.`descricao` AS `descricao_categoria`,`neoli831_teste`.`unidade_medida`.`abreviacao` AS `abreviacao` from ((((`neoli831_teste`.`venda_produto` join `neoli831_teste`.`produto` on((`neoli831_teste`.`venda_produto`.`id_produto` = `neoli831_teste`.`produto`.`id`))) join `neoli831_teste`.`unidade_medida` on((`neoli831_teste`.`produto`.`id_unidade_medida` = `neoli831_teste`.`unidade_medida`.`id`))) join `neoli831_teste`.`categoria_produto` on((`neoli831_teste`.`produto`.`id_categoria_produto` = `neoli831_teste`.`categoria_produto`.`id`))) join `neoli831_teste`.`marca` on((`neoli831_teste`.`produto`.`id_marca` = `neoli831_teste`.`marca`.`id`)));

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
