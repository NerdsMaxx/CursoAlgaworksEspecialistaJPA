INSERT INTO `algaworks_ecommerce`.`produto` (`id`,`preco`,`descricao`,`nome`) VALUES (1,499.00,'Conheça o novo Kindle!','Kindle');

INSERT INTO `algaworks_ecommerce`.`cliente` (`id`,`nome`) VALUES (1, 'Guilherme Henrique');
INSERT INTO `algaworks_ecommerce`.`cliente` (`id`,`nome`) VALUES (2, 'Fernando Guimarães');

insert into pedido (id, cliente_id, data_pedido, total, status) values (1, 1, sysdate(), 100.0, 'AGUARDANDO');

insert into item_pedido (id, pedido_id, produto_id, preco_produto, quantidade) values (1, 1, 1, 5.0, 2);

insert into categoria (id, nome) values (1, 'Eletrônicos');