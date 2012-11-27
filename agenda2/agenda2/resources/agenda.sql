DROP DATABASE IF EXISTS `agenda`;
CREATE DATABASE `agenda` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `agenda`;

DROP TABLE IF EXISTS `contatos`;
CREATE TABLE `contatos` (
  `id` int(11) NOT NULL auto_increment,
  `nome` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `endereco` varchar(255) default NULL,
  `dataNascimento` date default NULL,
  `id_usuario` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

INSERT INTO `contatos` VALUES (4,'JucaRONALDO','juca@kfouri.com','Rua do Esporte','1951-07-12','juca');
INSERT INTO `contatos` VALUES (10,'Juca','juca@kfouri.com','Rua do Esporte','1951-07-12','admin');
INSERT INTO `contatos` VALUES (11,'Juca','juca@kfouri.com','Rua do Esporte','1951-07-12','admin');
INSERT INTO `contatos` VALUES (12,'Alterado Alterado','alterado@alterado.com','Rua Alterado','2009-03-12','admin');
INSERT INTO `contatos` VALUES (13,'Thiago','thiagotn@gmail.com','Rua da Mooca','1984-11-23','admin');
INSERT INTO `contatos` VALUES (14,'Antonio','antonio@gmail.com','Rua Maria da Silva Gomes, 28','1984-11-23','admin');
INSERT INTO `contatos` VALUES (18,'Maria','maria@gmail.com','Rua Ana Neri','1963-11-23','admin');
INSERT INTO `contatos` VALUES (19,'Pedro','pedro@gmail.com','Rua da Mooca','1953-10-03','admin');
INSERT INTO `contatos` VALUES (20,'Pedro','pedro@gmail.com','Rua da Mooca','1953-10-03','juca');
INSERT INTO `contatos` VALUES (22,'Josefa Fernandez','josefa@terra.com.br','Rua Piratininga, 88','1973-03-14','juca');
INSERT INTO `contatos` VALUES (23,'Joao','joao@hotmail.com','Rua Joao','1984-12-22','admin');
INSERT INTO `contatos` VALUES (24,'Joao','joao@hotmail.com','Rua Joao','1984-12-22','juca');
INSERT INTO `contatos` VALUES (25,'Joao','joao@hotmail.com','Rua Joao','1984-12-22','admin');
INSERT INTO `contatos` VALUES (26,'Joao','joao@hotmail.com','Rua Joao','1984-12-22','juca');

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `login` varchar(255) NOT NULL,
  `senha` varchar(255) default NULL,
  `nome` varchar(255) default NULL,
  PRIMARY KEY  (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `usuario` VALUES ('admin','admin','Administrador');
INSERT INTO `usuario` VALUES ('juca','123456','123456');

ALTER TABLE `contatos`
ADD CONSTRAINT `fk_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`login`);

