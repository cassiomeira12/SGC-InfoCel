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
(1, 'ADMIN', 'admin', 'admin', 'email@hotmail.com', '111.111.111-11', '11.111.111-11', 9999999999, 1, 1);
