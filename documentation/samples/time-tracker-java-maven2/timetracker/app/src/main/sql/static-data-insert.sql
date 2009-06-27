-- Password is 'cooldude' encoded using MD5
insert into USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE, CREATION_DATE, COMMENT) values (1, 'nbhatia',      '756slLjeNViurJBGI5JeqA==', 'Naresh', 'Bhatia',      'nbhatia@northwind.com',      1, '2006/01/01 09:00', null);
insert into USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE, CREATION_DATE, COMMENT) values (2, 'lcoude',       '756slLjeNViurJBGI5JeqA==', 'Louis',  'Coude',       'lcoude@northwind.com',       1, '2006/01/01 09:00', null);
insert into USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE, CREATION_DATE, COMMENT) values (3, 'ecrutchfield', '756slLjeNViurJBGI5JeqA==', 'Eric',   'Crutchfield', 'ecrutchfield@northwind.com', 1, '2006/01/01 09:00', null);
insert into USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, IS_ACTIVE, CREATION_DATE, COMMENT) values (4, 'cmicali',      '756slLjeNViurJBGI5JeqA==', 'Chris',  'Micali',      'cmicali@northwind.com',      1, '2006/01/01 09:00', null);
commit;

insert into USER_ROLE (ID, ROLE, USER_FK) values (1, 'StandardUser',  1);
insert into USER_ROLE (ID, ROLE, USER_FK) values (2, 'Administrator', 1);
insert into USER_ROLE (ID, ROLE, USER_FK) values (3, 'StandardUser',  2);
insert into USER_ROLE (ID, ROLE, USER_FK) values (4, 'StandardUser',  3);
insert into USER_ROLE (ID, ROLE, USER_FK) values (5, 'StandardUser',  4);
commit;

insert into TASK (ID, NAME) values (1, 'Research');
insert into TASK (ID, NAME) values (2, 'Development');
insert into TASK (ID, NAME) values (3, 'Testing');
insert into TASK (ID, NAME) values (4, 'Admin');
insert into TASK (ID, NAME) values (5, 'Meeting');
commit;