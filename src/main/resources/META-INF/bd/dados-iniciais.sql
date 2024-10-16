INSERT INTO produto (id, preco, descricao, nome, data_criacao) VALUES (1,499.00,'Conheça o novo Kindle!','Kindle',date_sub(sysdate(), interval 1 day));
INSERT INTO produto (id, preco, descricao, nome, data_criacao) VALUES (2,1500.00,'Conheça o novo celular da Samsung!','Samsung Galaxy S24+',date_sub(sysdate(), interval 5 day));

INSERT INTO cliente (id, nome, cpf) VALUES (1, 'Guilherme Henrique', '000.000.000-00');
INSERT INTO cliente (id, nome, cpf) VALUES (2, 'Fernando Guimarães', '111.111.111-11');

INSERT INTO cliente_detalhe (cliente_id, sexo) VALUES (1, 'MASCULINO');
INSERT INTO cliente_detalhe (cliente_id, sexo) VALUES (2, 'MASCULINO');

insert into pedido (id, cliente_id, data_pedido, data_criacao, total, status) values (1, 1, sysdate(), date_sub(sysdate(), interval 5 day), 998.0, 'AGUARDANDO');
insert into pedido (id, cliente_id, data_pedido, data_criacao, total, status) values (2, 1, sysdate(), sysdate(), 499.0, 'AGUARDANDO');

insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (1, 1, 499, 2);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (1, 2, 499, 2);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (2, 1, 499, 1);

insert into pagamento (pedido_id, status, numero_cartao, tipo_pagamento, codigo_barras) values (2, 'PROCESSANDO', '123','cartao', null);
--insert into pagamento (pedido_id, status, tipo_pagamento) values (1, 'PROCESSANDO', 'cartao');
--insert into pagamento_cartao (pedido_id, numero_cartao) values (1, '123456789');

insert into categoria (nome) values ('Eletrônicos');
insert into categoria (nome) values ('Livros Digitais');
insert into categoria (nome) values ('Eletrodomésticos');
insert into categoria (nome) values ('Livros');
insert into categoria (nome) values ('Esportes');
insert into categoria (nome) values ('Futebol');
insert into categoria (nome) values ('Natação');
insert into categoria (nome) values ('Notebooks');
insert into categoria (nome) values ('Smartphones');

insert into produto_categoria (produto_id, categoria_id) values (1,1);
--insert into produto_categoria (produto_id, categoria_id) values (1,2);
--insert into produto_categoria (produto_id, categoria_id) values (2,1);

insert into nota_fiscal (pedido_id, xml, data_emissao) values (2, '<xml />', sysdate());