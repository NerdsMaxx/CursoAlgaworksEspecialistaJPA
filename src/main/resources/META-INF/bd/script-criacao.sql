create table testando (id integer not null auto_increment, primary key (id)) engine=InnoDB;

create table produto_loja (id integer not null auto_increment, nome varchar(100), descricao longtext, preco decimal(19, 2), data_criacao datetime(6), data_ultima_atualizacao datetime(6), foto longblob, primary key (id)) engine=InnoDB;

create table ecm_produto (prd_id integer not null auto_increment, prd_nome varchar(100), prd_descricao longtext, prd_preco decimal(19, 2), prd_data_criacao datetime(6), prd_data_ultima_atualizacao datetime(6), prd_foto longblob, primary key (prd_id)) engine=InnoDB;

create table erp_produto (id integer not null auto_increment, nome varchar(100), descricao longtext, preco decimal(19, 2), primary key (id)) engine=InnoDB;

create table ecm_categoria (cat_id integer not null auto_increment, cat_nome varchar(100), cat_categoria_pai_id integer, primary key (cat_id)) engine=InnoDB;

create function acima_media_faturamento(valor double) returns boolean reads sql data return valor > (select avg(total) from pedido);

create procedure buscar_nome_produto(in produto_id int, out produto_nome varchar(255)) begin select nome into produto_nome from produto where id = produto_id; end

create procedure compraram_acima_media(in ano integer) begin select cli.*, clid.* from cliente cli join cliente_detalhe clid on clid.cliente_id = cli.id join pedido ped on ped.cliente_id = cli.id where ped.status = 'PAGO' and year(ped.data_criacao) = ano group by ped.cliente_id having sum(ped.total) >= (select avg(total_por_cliente.sum_total) from (select sum(ped2.total) sum_total from pedido ped2 where ped2.status = 'PAGO' and year(ped2.data_criacao) = ano group by ped2.cliente_id) as total_por_cliente); end

create procedure ajustar_preco_produto(in produto_id int, in percentual_ajuste double, out preco_ajustado double) begin declare produto_preco double; select preco into produto_preco from produto where id = produto_id; set preco_ajustado = produto_preco + (produto_preco * percentual_ajuste); update produto set preco = preco_ajustado where id = produto_id; end

create view view_clientes_acima_media as select cli.*, clid.* from cliente cli join cliente_detalhe clid on clid.cliente_id = cli.id join pedido ped on ped.cliente_id = cli.id where ped.status = 'PAGO' and year(ped.data_criacao) = year(current_date) group by ped.cliente_id having sum(ped.total) >= (select avg(total_por_cliente.sum_total) from (select sum(ped2.total) sum_total from pedido ped2 where ped2.status = 'PAGO' and year(ped2.data_criacao) = year(current_date) group by ped2.cliente_id) as total_por_cliente);

--create table categoria (id integer not null auto_increment, nome varchar(100) not null, categoria_pai_id integer, primary key (id)) engine=InnoDB;
--create table cliente (id integer not null auto_increment, cpf varchar(14) not null, nome varchar(100) not null, primary key (id)) engine=InnoDB;
--create table cliente_contato (cliente_id integer not null, descricao varchar(255), tipo varchar(255) not null, primary key (cliente_id, tipo)) engine=InnoDB;
--create table cliente_detalhe (data_nascimento date, sexo varchar(30) not null, cliente_id integer not null, primary key (cliente_id)) engine=InnoDB;
--create table estoque (id integer not null auto_increment, quantidade integer, produto_id integer not null, primary key (id)) engine=InnoDB;
--create table item_pedido (pedido_id integer not null, produto_id integer not null, preco_produto decimal(19,2) not null, quantidade integer not null, primary key (pedido_id, produto_id)) engine=InnoDB;
--create table nota_fiscal (pedido_id integer not null, data_emissao datetime(6) not null, xml longblob not null, primary key (pedido_id)) engine=InnoDB;
--create table pagamento (tipo_pagamento varchar(31) not null, pedido_id integer not null, status varchar(30) not null, numero_cartao varchar(50), codigo_barras varchar(100), primary key (pedido_id)) engine=InnoDB;
--create table pedido (id integer not null auto_increment, data_conclusao datetime(6), data_criacao datetime(6) not null, data_ultima_atualizacao datetime(6), bairro varchar(50), cep varchar(9), cidade varchar(50), complemento varchar(50), estado varchar(2), logradouro varchar(100), numero varchar(10), status varchar(30) not null, total decimal(19,2) not null, cliente_id integer not null, primary key (id)) engine=InnoDB;
--create table produto (id integer not null auto_increment, data_criacao datetime(6) not null, data_ultima_atualizacao datetime(6), descricao longtext, foto longblob, nome varchar(100) not null, preco decimal(19,2), primary key (id)) engine=InnoDB;
--create table produto_atributo (produto_id integer not null, nome varchar(100) not null, valor varchar(255)) engine=InnoDB;
--create table produto_categoria (produto_id integer not null, categoria_id integer not null) engine=InnoDB;
--create table produto_tag (produto_id integer not null, tag varchar(50) not null) engine=InnoDB;
--
--alter table categoria add constraint unq_nome unique (nome);
--create index idx_nome on cliente (nome);
--alter table cliente add constraint unq_cpf unique (cpf);
--alter table estoque add constraint UK_g72w2sa50w9a647f0eyhogus5 unique (produto_id);
--create index idx_nome on produto (nome);
--alter table produto add constraint unq_nome unique (nome);
--alter table categoria add constraint fk_categoria_categoriapai foreign key (categoria_pai_id) references categoria (id);
--alter table cliente_contato add constraint fk_cliente_contato_cliente foreign key (cliente_id) references cliente (id);
--alter table cliente_detalhe add constraint fk_cliente_detalhe_cliente foreign key (cliente_id) references cliente (id);
--alter table estoque add constraint fk_estoque_produto foreign key (produto_id) references produto (id);
--alter table item_pedido add constraint fk_item_pedido_pedido foreign key (pedido_id) references pedido (id);
--alter table item_pedido add constraint fk_item_pedido_produto foreign key (produto_id) references produto (id);
--alter table nota_fiscal add constraint fk_nota_fiscal_pedido foreign key (pedido_id) references pedido (id);
--alter table pagamento add constraint fk_pagamento_pedido foreign key (pedido_id) references pedido (id);
--alter table pedido add constraint fk_pedido_cliente foreign key (cliente_id) references cliente (id);
--alter table produto_atributo add constraint fk_produto_atributo_produto foreign key (produto_id) references produto (id);
--alter table produto_categoria add constraint fk_produto_categoria_categoria foreign key (categoria_id) references categoria (id);
--alter table produto_categoria add constraint fk_produto_categoria_produto foreign key (produto_id) references produto (id);
--alter table produto_tag add constraint fk_produto_tag_produto foreign key (produto_id) references produto (id);