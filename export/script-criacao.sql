
    create table categoria (
        categoria_pai_id integer,
        id integer not null auto_increment,
        nome varchar(100) not null,
        primary key (id)
    ) engine=InnoDB;

    create table cliente (
        id integer not null auto_increment,
        cpf varchar(14) not null,
        nome varchar(100) not null,
        primary key (id)
    ) engine=InnoDB;

    create table cliente_contato (
        cliente_id integer not null,
        descricao varchar(255),
        tipo varchar(255) not null,
        primary key (cliente_id, tipo)
    ) engine=InnoDB;

    create table cliente_detalhe (
        cliente_id integer not null,
        data_nascimento date,
        sexo enum ('FEMININO','MASCULINO') not null,
        primary key (cliente_id)
    ) engine=InnoDB;

    create table estoque (
        id integer not null auto_increment,
        produto_id integer not null,
        quantidade integer,
        primary key (id)
    ) engine=InnoDB;

    create table item_pedido (
        pedido_id integer not null,
        preco_produto decimal(19,2),
        produto_id integer not null,
        quantidade integer not null,
        primary key (pedido_id, produto_id)
    ) engine=InnoDB;

    create table nota_fiscal (
        pedido_id integer not null,
        data_emissao datetime(6) not null,
        xml blob not null,
        primary key (pedido_id)
    ) engine=InnoDB;

    create table pagamento (
        pedido_id integer not null,
        status enum ('CANCELADO','PROCESSANDO','RECEBIDO') not null,
        tipo_pagamento varchar(31) not null,
        numero_cartao varchar(50) not null,
        codigo_barras varchar(100) not null,
        primary key (pedido_id)
    ) engine=InnoDB;

    create table pedido (
        cliente_id integer not null,
        estado varchar(2),
        id integer not null auto_increment,
        total decimal(19,2) not null,
        data_conclusao datetime(6),
        data_criacao datetime(6) not null,
        data_pedido datetime(6),
        data_ultima_atualizacao datetime(6),
        cep varchar(9),
        numero varchar(10),
        status enum ('AGUARDANDO','CANCELADO','PAGO') not null,
        bairro varchar(50),
        cidade varchar(50),
        complemento varchar(50),
        logradouro varchar(100),
        primary key (id)
    ) engine=InnoDB;

    create table produto (
        id integer not null auto_increment,
        preco decimal(38,2),
        data_criacao datetime(6) not null,
        data_ultima_atualizacao datetime(6),
        nome varchar(100) not null,
        descricao longtext,
        foto blob,
        primary key (id)
    ) engine=InnoDB;

    create table produto_atributo (
        produto_id integer not null,
        nome varchar(100) not null,
        valor varchar(255)
    ) engine=InnoDB;

    create table produto_categoria (
        categoria_id integer not null,
        produto_id integer not null
    ) engine=InnoDB;

    create table produto_tag (
        produto_id integer not null,
        tag varchar(50) not null
    ) engine=InnoDB;

    create index idx_categoria_nome 
       on categoria (nome);

    alter table categoria 
       add constraint unq_categoria_nome unique (nome);

    create index idx_cliente_nome 
       on cliente (nome);

    alter table cliente 
       add constraint unq_cliente_cpf unique (cpf);

    alter table estoque 
       add constraint UK_g72w2sa50w9a647f0eyhogus5 unique (produto_id);

    create index idx_produto_nome 
       on produto (nome);

    alter table produto 
       add constraint unq_produto_nome unique (nome);

    alter table categoria 
       add constraint fk_categoria_categoria 
       foreign key (categoria_pai_id) 
       references categoria (id);

    alter table cliente_contato 
       add constraint fk_cliente_contatos 
       foreign key (cliente_id) 
       references cliente (id);

    alter table cliente_detalhe 
       add constraint fk_cliente_detalhe_cliente 
       foreign key (cliente_id) 
       references cliente (id);

    alter table estoque 
       add constraint fk_estoque_produto 
       foreign key (produto_id) 
       references produto (id);

    alter table item_pedido 
       add constraint fk_item_pedido_pedido 
       foreign key (pedido_id) 
       references pedido (id);

    alter table item_pedido 
       add constraint fk_item_pedido_produto 
       foreign key (produto_id) 
       references produto (id);

    alter table nota_fiscal 
       add constraint fk_nota_fiscal_pedido 
       foreign key (pedido_id) 
       references pedido (id);

    alter table pagamento 
       add constraint fk_pagamento_pedido 
       foreign key (pedido_id) 
       references pedido (id);

    alter table pedido 
       add constraint fk_pedido_cliente 
       foreign key (cliente_id) 
       references cliente (id);

    alter table produto_atributo 
       add constraint fk_produto_atributo 
       foreign key (produto_id) 
       references produto (id);

    alter table produto_categoria 
       add constraint fk_categoria_produto_categoria 
       foreign key (categoria_id) 
       references categoria (id);

    alter table produto_categoria 
       add constraint fk_produto_produto_categoria 
       foreign key (produto_id) 
       references produto (id);

    alter table produto_tag 
       add constraint fk_produto_tag 
       foreign key (produto_id) 
       references produto (id);

    create table testando (
        id integer not null auto_increment,
        primary key (id)
    ) engine=InnoDB;

//do dados-iniciais.sql
INSERT INTO produto (id, preco, descricao, nome, data_criacao) VALUES (1,499.00,'Conheça o novo Kindle!','Kindle',date_sub(sysdate(), interval 1 day));
INSERT INTO produto (id, preco, descricao, nome, data_criacao) VALUES (2,1500.00,'Conheça o novo celular da Samsung!','Samsung Galaxy S24+',date_sub(sysdate(), interval 5 day));
INSERT INTO cliente (id, nome, cpf) VALUES (1, 'Guilherme Henrique', '000.000.000-00');
INSERT INTO cliente (id, nome, cpf) VALUES (2, 'Fernando Guimarães', '111.111.111-11');
INSERT INTO cliente_detalhe (cliente_id, sexo) VALUES (1, 'MASCULINO');
INSERT INTO cliente_detalhe (cliente_id, sexo) VALUES (2, 'MASCULINO');
insert into pedido (id, cliente_id, data_pedido, data_criacao, total, status) values (1, 1, sysdate(), sysdate(), 998.0, 'AGUARDANDO');
insert into pedido (id, cliente_id, data_pedido, data_criacao, total, status) values (2, 1, sysdate(), sysdate(), 499.0, 'AGUARDANDO');
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (1, 1, 499, 2);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (2, 1, 499, 1);
insert into pagamento (pedido_id, status, numero_cartao, tipo_pagamento) values (2, 'PROCESSANDO', '123','cartao');
insert into categoria (id, nome) values (1, 'Eletrônicos');
