-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema sgc_infocel
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sgc_infocel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sgc_infocel` DEFAULT CHARACTER SET utf8 ;
USE `sgc_infocel` ;

-- -----------------------------------------------------
-- Table `sgc_infocel`.`cidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`cidade` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`bairro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`bairro` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `id_cidade` INT(11) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `id_cidade`),
  INDEX `fk_bairro_cidade1_idx` (`id_cidade` ASC),
  CONSTRAINT `fk_bairro_cidade1`
    FOREIGN KEY (`id_cidade`)
    REFERENCES `sgc_infocel`.`cidade` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`endereco` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `rua` VARCHAR(30) NOT NULL,
  `numero` VARCHAR(8) NULL DEFAULT NULL,
  `id_bairro` INT(11) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `id_bairro`),
  INDEX `fk_endereco_bairro1_idx` (`id_bairro` ASC),
  CONSTRAINT `fk_endereco_bairro1`
    FOREIGN KEY (`id_bairro`)
    REFERENCES `sgc_infocel`.`bairro` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`administrador` (
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
    REFERENCES `sgc_infocel`.`endereco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`categoria_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`categoria_produto` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`categoria_saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`categoria_saida` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`cliente` (
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
    REFERENCES `sgc_infocel`.`endereco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`forma_pagamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`forma_pagamento` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `maximo_parcelas` INT(11) NOT NULL DEFAULT '1',
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`manutencao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`manutencao` (
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
    REFERENCES `sgc_infocel`.`forma_pagamento` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manutencao_administrador1`
    FOREIGN KEY (`id_administrador`)
    REFERENCES `sgc_infocel`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manutencao_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `sgc_infocel`.`cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`marca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`marca` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(20) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`unidade_medida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`unidade_medida` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(20) NOT NULL,
  `abreviacao` VARCHAR(5) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`produto` (
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
    REFERENCES `sgc_infocel`.`categoria_produto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_marca`
    FOREIGN KEY (`id_marca`)
    REFERENCES `sgc_infocel`.`marca` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_unidade_medida1`
    FOREIGN KEY (`id_unidade_medida`)
    REFERENCES `sgc_infocel`.`unidade_medida` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`receita`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`receita` (
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
    REFERENCES `sgc_infocel`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_receita_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `sgc_infocel`.`cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`saida` (
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
    REFERENCES `sgc_infocel`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_saida_categoria_saida1`
    FOREIGN KEY (`id_categoria_saida`)
    REFERENCES `sgc_infocel`.`categoria_saida` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`venda` (
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
    REFERENCES `sgc_infocel`.`administrador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `sgc_infocel`.`cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_forma_pagamento1`
    FOREIGN KEY (`id_forma_pagamento`)
    REFERENCES `sgc_infocel`.`forma_pagamento` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgc_infocel`.`venda_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`venda_produto` (
  `id_venda` INT(11) NOT NULL,
  `id_produto` INT(11) NOT NULL,
  `preco_total` FLOAT NOT NULL,
  `quantidade` FLOAT NOT NULL,
  PRIMARY KEY (`id_venda`, `id_produto`),
  INDEX `fk_venda_has_produto_produto1_idx` (`id_produto` ASC),
  INDEX `fk_venda_has_produto_venda1_idx` (`id_venda` ASC),
  CONSTRAINT `fk_venda_has_produto_produto1`
    FOREIGN KEY (`id_produto`)
    REFERENCES `sgc_infocel`.`produto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_has_produto_venda1`
    FOREIGN KEY (`id_venda`)
    REFERENCES `sgc_infocel`.`venda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

USE `sgc_infocel` ;

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_administrador` (`id` INT, `nome` INT, `login` INT, `senha` INT, `email` INT, `cpf` INT, `rg` INT, `data_cadastro` INT, `status` INT, `id_endereco` INT, `numero` INT, `rua` INT, `id_bairro` INT, `id_cidade` INT, `nome_cidade` INT, `nome_bairro` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_bairro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_bairro` (`id` INT, `nome` INT, `status` INT, `nome_cidade` INT, `id_cidade` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_cliente` (`id` INT, `nome` INT, `cpf` INT, `rg` INT, `telefone` INT, `data_cadastro` INT, `status` INT, `id_endereco` INT, `numero` INT, `rua` INT, `id_bairro` INT, `id_cidade` INT, `nome_cidade` INT, `nome_bairro` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_endereco` (`id` INT, `numero` INT, `rua` INT, `status` INT, `id_bairro` INT, `nome_bairro` INT, `nome_cidade` INT, `id_cidade` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_manutencao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_manutencao` (`id` INT, `id_administrador` INT, `quantidade_parcelas` INT, `id_cliente` INT, `id_forma_pagamento` INT, `descricao` INT, `marca` INT, `modelo` INT, `imei` INT, `cor` INT, `data_cadastro` INT, `data_previsao` INT, `data_entrega` INT, `preco` INT, `finalizado` INT, `nome_cliente` INT, `cpf_cliente` INT, `rg_cliente` INT, `telefone_cliente` INT, `id_endereco_cliente` INT, `numero_cliente` INT, `rua_cliente` INT, `id_bairro_cliente` INT, `id_cidade_cliente` INT, `nome_cidade_cliente` INT, `nome_bairro_cliente` INT, `nome_administrador` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT, `descricao_forma_pagamento` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_produto` (`id` INT, `status` INT, `id_marca` INT, `id_unidade_medida` INT, `id_categoria_produto` INT, `descricao` INT, `preco_compra` INT, `preco_venda` INT, `estoque` INT, `eh_celular` INT, `descricao_marca` INT, `descricao_categoria` INT, `descricao_unidade` INT, `abreviacao_unidade` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_receita`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_receita` (`id` INT, `id_administrador` INT, `id_cliente` INT, `descricao` INT, `data` INT, `valor` INT, `nome_cliente` INT, `cpf_cliente` INT, `rg_cliente` INT, `telefone_cliente` INT, `id_endereco_cliente` INT, `numero_cliente` INT, `rua_cliente` INT, `id_bairro_cliente` INT, `id_cidade_cliente` INT, `nome_cidade_cliente` INT, `nome_bairro_cliente` INT, `nome_administrador` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_saida` (`id` INT, `id_administrador` INT, `id_categoria_saida` INT, `nome_administrador` INT, `descricao` INT, `valor` INT, `data` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT, `descricao_categoria` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_venda` (`id` INT, `id_forma_pagamento` INT, `id_administrador` INT, `id_cliente` INT, `preco_total` INT, `data` INT, `quantidade_parcela` INT, `nome_cliente` INT, `cpf_cliente` INT, `rg_cliente` INT, `telefone_cliente` INT, `id_endereco_cliente` INT, `numero_cliente` INT, `rua_cliente` INT, `id_bairro_cliente` INT, `id_cidade_cliente` INT, `nome_cidade_cliente` INT, `nome_bairro_cliente` INT, `nome_administrador` INT, `cpf_administrador` INT, `id_endereco_administrador` INT, `numero_administrador` INT, `rua_administrador` INT, `id_bairro_administrador` INT, `id_cidade_administrador` INT, `nome_cidade_administrador` INT, `nome_bairro_administrador` INT, `descricao_forma_pagamento` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sgc_infocel`.`view_venda_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgc_infocel`.`view_venda_produto` (`id_venda` INT, `id_produto` INT, `preco_total` INT, `quantidade` INT, `descricao_produto` INT, `preco_venda` INT, `descricao_marca` INT, `descricao_categoria` INT, `abreviacao` INT);

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_administrador`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_administrador`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_administrador` AS select `sgc_infocel`.`administrador`.`id` AS `id`,`sgc_infocel`.`administrador`.`nome` AS `nome`,`sgc_infocel`.`administrador`.`login` AS `login`,`sgc_infocel`.`administrador`.`senha` AS `senha`,`sgc_infocel`.`administrador`.`email` AS `email`,`sgc_infocel`.`administrador`.`cpf` AS `cpf`,`sgc_infocel`.`administrador`.`rg` AS `rg`,`sgc_infocel`.`administrador`.`data_cadastro` AS `data_cadastro`,`sgc_infocel`.`administrador`.`status` AS `status`,`sgc_infocel`.`administrador`.`id_endereco` AS `id_endereco`,`view_endereco`.`numero` AS `numero`,`view_endereco`.`rua` AS `rua`,`view_endereco`.`id_bairro` AS `id_bairro`,`view_endereco`.`id_cidade` AS `id_cidade`,`view_endereco`.`nome_cidade` AS `nome_cidade`,`view_endereco`.`nome_bairro` AS `nome_bairro` from (`sgc_infocel`.`administrador` join `sgc_infocel`.`view_endereco` on((`sgc_infocel`.`administrador`.`id_endereco` = `view_endereco`.`id`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_bairro`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_bairro`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_bairro` AS select `sgc_infocel`.`bairro`.`id` AS `id`,`sgc_infocel`.`bairro`.`nome` AS `nome`,`sgc_infocel`.`bairro`.`status` AS `status`,`sgc_infocel`.`cidade`.`nome` AS `nome_cidade`,`sgc_infocel`.`cidade`.`id` AS `id_cidade` from (`sgc_infocel`.`bairro` join `sgc_infocel`.`cidade` on((`sgc_infocel`.`cidade`.`id` = `sgc_infocel`.`bairro`.`id_cidade`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_cliente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_cliente`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_cliente` AS select `sgc_infocel`.`cliente`.`id` AS `id`,`sgc_infocel`.`cliente`.`nome` AS `nome`,`sgc_infocel`.`cliente`.`cpf` AS `cpf`,`sgc_infocel`.`cliente`.`rg` AS `rg`,`sgc_infocel`.`cliente`.`telefone` AS `telefone`,`sgc_infocel`.`cliente`.`data_cadastro` AS `data_cadastro`,`sgc_infocel`.`cliente`.`status` AS `status`,`sgc_infocel`.`cliente`.`id_endereco` AS `id_endereco`,`view_endereco`.`numero` AS `numero`,`view_endereco`.`rua` AS `rua`,`view_endereco`.`id_bairro` AS `id_bairro`,`view_endereco`.`id_cidade` AS `id_cidade`,`view_endereco`.`nome_cidade` AS `nome_cidade`,`view_endereco`.`nome_bairro` AS `nome_bairro` from (`sgc_infocel`.`cliente` join `sgc_infocel`.`view_endereco` on((`sgc_infocel`.`cliente`.`id_endereco` = `view_endereco`.`id`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_endereco`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_endereco`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_endereco` AS select `sgc_infocel`.`endereco`.`id` AS `id`,`sgc_infocel`.`endereco`.`numero` AS `numero`,`sgc_infocel`.`endereco`.`rua` AS `rua`,`sgc_infocel`.`endereco`.`status` AS `status`,`view_bairro`.`id` AS `id_bairro`,`view_bairro`.`nome` AS `nome_bairro`,`view_bairro`.`nome_cidade` AS `nome_cidade`,`view_bairro`.`id_cidade` AS `id_cidade` from (`sgc_infocel`.`endereco` join `sgc_infocel`.`view_bairro` on((`view_bairro`.`id` = `sgc_infocel`.`endereco`.`id_bairro`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_manutencao`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_manutencao`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_manutencao` AS select `sgc_infocel`.`manutencao`.`id` AS `id`,`sgc_infocel`.`manutencao`.`id_administrador` AS `id_administrador`,`sgc_infocel`.`manutencao`.`quantidade_parcelas` AS `quantidade_parcelas`,`sgc_infocel`.`manutencao`.`id_cliente` AS `id_cliente`,`sgc_infocel`.`manutencao`.`id_forma_pagamento` AS `id_forma_pagamento`,`sgc_infocel`.`manutencao`.`descricao` AS `descricao`,`sgc_infocel`.`manutencao`.`marca` AS `marca`,`sgc_infocel`.`manutencao`.`modelo` AS `modelo`,`sgc_infocel`.`manutencao`.`imei` AS `imei`,`sgc_infocel`.`manutencao`.`cor` AS `cor`,`sgc_infocel`.`manutencao`.`data_cadastro` AS `data_cadastro`,`sgc_infocel`.`manutencao`.`data_previsao` AS `data_previsao`,`sgc_infocel`.`manutencao`.`data_entrega` AS `data_entrega`,`sgc_infocel`.`manutencao`.`preco` AS `preco`,`sgc_infocel`.`manutencao`.`finalizado` AS `finalizado`,`view_cliente`.`nome` AS `nome_cliente`,`view_cliente`.`cpf` AS `cpf_cliente`,`view_cliente`.`rg` AS `rg_cliente`,`view_cliente`.`telefone` AS `telefone_cliente`,`view_cliente`.`id_endereco` AS `id_endereco_cliente`,`view_cliente`.`numero` AS `numero_cliente`,`view_cliente`.`rua` AS `rua_cliente`,`view_cliente`.`id_bairro` AS `id_bairro_cliente`,`view_cliente`.`id_cidade` AS `id_cidade_cliente`,`view_cliente`.`nome_cidade` AS `nome_cidade_cliente`,`view_cliente`.`nome_bairro` AS `nome_bairro_cliente`,`view_administrador`.`nome` AS `nome_administrador`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador`,`sgc_infocel`.`forma_pagamento`.`descricao` AS `descricao_forma_pagamento` from (((`sgc_infocel`.`manutencao` join `sgc_infocel`.`view_cliente` on((`sgc_infocel`.`manutencao`.`id_cliente` = `view_cliente`.`id`))) join `sgc_infocel`.`forma_pagamento` on((`sgc_infocel`.`forma_pagamento`.`id` = `sgc_infocel`.`manutencao`.`id_forma_pagamento`))) join `sgc_infocel`.`view_administrador` on((`view_administrador`.`id` = `sgc_infocel`.`manutencao`.`id_administrador`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_produto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_produto`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_produto` AS select `sgc_infocel`.`produto`.`id` AS `id`,`sgc_infocel`.`produto`.`status` AS `status`,`sgc_infocel`.`produto`.`id_marca` AS `id_marca`,`sgc_infocel`.`produto`.`id_unidade_medida` AS `id_unidade_medida`,`sgc_infocel`.`produto`.`id_categoria_produto` AS `id_categoria_produto`,`sgc_infocel`.`produto`.`descricao` AS `descricao`,`sgc_infocel`.`produto`.`preco_compra` AS `preco_compra`,`sgc_infocel`.`produto`.`preco_venda` AS `preco_venda`,`sgc_infocel`.`produto`.`estoque` AS `estoque`,`sgc_infocel`.`produto`.`eh_celular` AS `eh_celular`,`sgc_infocel`.`marca`.`descricao` AS `descricao_marca`,`sgc_infocel`.`categoria_produto`.`descricao` AS `descricao_categoria`,`sgc_infocel`.`unidade_medida`.`descricao` AS `descricao_unidade`,`sgc_infocel`.`unidade_medida`.`abreviacao` AS `abreviacao_unidade` from (((`sgc_infocel`.`produto` join `sgc_infocel`.`marca` on((`sgc_infocel`.`produto`.`id_marca` = `sgc_infocel`.`marca`.`id`))) join `sgc_infocel`.`unidade_medida` on((`sgc_infocel`.`produto`.`id_unidade_medida` = `sgc_infocel`.`unidade_medida`.`id`))) join `sgc_infocel`.`categoria_produto` on((`sgc_infocel`.`produto`.`id_categoria_produto` = `sgc_infocel`.`categoria_produto`.`id`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_receita`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_receita`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_receita` AS select `sgc_infocel`.`receita`.`id` AS `id`,`sgc_infocel`.`receita`.`id_administrador` AS `id_administrador`,`sgc_infocel`.`receita`.`id_cliente` AS `id_cliente`,`sgc_infocel`.`receita`.`descricao` AS `descricao`,`sgc_infocel`.`receita`.`data` AS `data`,`sgc_infocel`.`receita`.`valor` AS `valor`,`view_cliente`.`nome` AS `nome_cliente`,`view_cliente`.`cpf` AS `cpf_cliente`,`view_cliente`.`rg` AS `rg_cliente`,`view_cliente`.`telefone` AS `telefone_cliente`,`view_cliente`.`id_endereco` AS `id_endereco_cliente`,`view_cliente`.`numero` AS `numero_cliente`,`view_cliente`.`rua` AS `rua_cliente`,`view_cliente`.`id_bairro` AS `id_bairro_cliente`,`view_cliente`.`id_cidade` AS `id_cidade_cliente`,`view_cliente`.`nome_cidade` AS `nome_cidade_cliente`,`view_cliente`.`nome_bairro` AS `nome_bairro_cliente`,`view_administrador`.`nome` AS `nome_administrador`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador` from ((`sgc_infocel`.`receita` join `sgc_infocel`.`view_cliente` on((`sgc_infocel`.`receita`.`id_cliente` = `view_cliente`.`id`))) join `sgc_infocel`.`view_administrador` on((`view_administrador`.`id` = `sgc_infocel`.`receita`.`id_administrador`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_saida`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_saida`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_saida` AS select `sgc_infocel`.`saida`.`id` AS `id`,`sgc_infocel`.`saida`.`id_administrador` AS `id_administrador`,`sgc_infocel`.`saida`.`id_categoria_saida` AS `id_categoria_saida`,`view_administrador`.`nome` AS `nome_administrador`,`sgc_infocel`.`saida`.`descricao` AS `descricao`,`sgc_infocel`.`saida`.`valor` AS `valor`,`sgc_infocel`.`saida`.`data` AS `data`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador`,`sgc_infocel`.`categoria_saida`.`descricao` AS `descricao_categoria` from ((`sgc_infocel`.`saida` join `sgc_infocel`.`categoria_saida` on((`sgc_infocel`.`saida`.`id_categoria_saida` = `sgc_infocel`.`categoria_saida`.`id`))) join `sgc_infocel`.`view_administrador` on((`view_administrador`.`id` = `sgc_infocel`.`saida`.`id_administrador`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_venda`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_venda`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_venda` AS select `sgc_infocel`.`venda`.`id` AS `id`,`sgc_infocel`.`venda`.`id_forma_pagamento` AS `id_forma_pagamento`,`sgc_infocel`.`venda`.`id_administrador` AS `id_administrador`,`sgc_infocel`.`venda`.`id_cliente` AS `id_cliente`,`sgc_infocel`.`venda`.`preco_total` AS `preco_total`,`sgc_infocel`.`venda`.`data` AS `data`,`sgc_infocel`.`venda`.`quantidade_parcela` AS `quantidade_parcela`,`view_cliente`.`nome` AS `nome_cliente`,`view_cliente`.`cpf` AS `cpf_cliente`,`view_cliente`.`rg` AS `rg_cliente`,`view_cliente`.`telefone` AS `telefone_cliente`,`view_cliente`.`id_endereco` AS `id_endereco_cliente`,`view_cliente`.`numero` AS `numero_cliente`,`view_cliente`.`rua` AS `rua_cliente`,`view_cliente`.`id_bairro` AS `id_bairro_cliente`,`view_cliente`.`id_cidade` AS `id_cidade_cliente`,`view_cliente`.`nome_cidade` AS `nome_cidade_cliente`,`view_cliente`.`nome_bairro` AS `nome_bairro_cliente`,`view_administrador`.`nome` AS `nome_administrador`,`view_administrador`.`cpf` AS `cpf_administrador`,`view_administrador`.`id_endereco` AS `id_endereco_administrador`,`view_administrador`.`numero` AS `numero_administrador`,`view_administrador`.`rua` AS `rua_administrador`,`view_administrador`.`id_bairro` AS `id_bairro_administrador`,`view_administrador`.`id_cidade` AS `id_cidade_administrador`,`view_administrador`.`nome_cidade` AS `nome_cidade_administrador`,`view_administrador`.`nome_bairro` AS `nome_bairro_administrador`,`sgc_infocel`.`forma_pagamento`.`descricao` AS `descricao_forma_pagamento` from (((`sgc_infocel`.`venda` join `sgc_infocel`.`forma_pagamento` on((`sgc_infocel`.`venda`.`id_forma_pagamento` = `sgc_infocel`.`forma_pagamento`.`id`))) join `sgc_infocel`.`view_cliente` on((`sgc_infocel`.`venda`.`id_cliente` = `view_cliente`.`id`))) join `sgc_infocel`.`view_administrador` on((`view_administrador`.`id` = `sgc_infocel`.`venda`.`id_administrador`)));

-- -----------------------------------------------------
-- View `sgc_infocel`.`view_venda_produto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sgc_infocel`.`view_venda_produto`;
USE `sgc_infocel`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sgc_infocel`.`view_venda_produto` AS select `sgc_infocel`.`venda_produto`.`id_venda` AS `id_venda`,`sgc_infocel`.`venda_produto`.`id_produto` AS `id_produto`,`sgc_infocel`.`venda_produto`.`preco_total` AS `preco_total`,`sgc_infocel`.`venda_produto`.`quantidade` AS `quantidade`,`sgc_infocel`.`produto`.`descricao` AS `descricao_produto`,`sgc_infocel`.`produto`.`preco_venda` AS `preco_venda`,`sgc_infocel`.`marca`.`descricao` AS `descricao_marca`,`sgc_infocel`.`categoria_produto`.`descricao` AS `descricao_categoria`,`sgc_infocel`.`unidade_medida`.`abreviacao` AS `abreviacao` from ((((`sgc_infocel`.`venda_produto` join `sgc_infocel`.`produto` on((`sgc_infocel`.`venda_produto`.`id_produto` = `sgc_infocel`.`produto`.`id`))) join `sgc_infocel`.`unidade_medida` on((`sgc_infocel`.`produto`.`id_unidade_medida` = `sgc_infocel`.`unidade_medida`.`id`))) join `sgc_infocel`.`categoria_produto` on((`sgc_infocel`.`produto`.`id_categoria_produto` = `sgc_infocel`.`categoria_produto`.`id`))) join `sgc_infocel`.`marca` on((`sgc_infocel`.`produto`.`id_marca` = `sgc_infocel`.`marca`.`id`)));

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


--------------------------------------------------------
USE sgc_infocel;

INSERT INTO `cidade` (`id`, `nome`, `status`) VALUES
(1, 'CIDADE', 1);
----------------------------------------------------------
INSERT INTO `bairro` (`id`, `nome`, `id_cidade`, `status`) VALUES
(1, 'BAIRRO', 1, 1);
----------------------------------------------------------
INSERT INTO `endereco` (`id`, `rua`, `numero`, `id_bairro`, `status`) VALUES
(1, 'RUA', 'NÚMERO', 1, 1);
----------------------------------------------------------
INSERT INTO `administrador` (`id`, `nome`, `login`, `senha`, `email`, `cpf`, `rg`, `data_cadastro`, `status`, `id_endereco`) VALUES
(1, 'ADMIN', 'admin', 'admin', 'admin@hotmail.com', '111.111.111-11', '11.111.111-11', 9999999999, 1, 1);