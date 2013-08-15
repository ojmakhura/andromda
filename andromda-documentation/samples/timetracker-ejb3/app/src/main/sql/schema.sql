
    alter table TIMECARD 
        drop constraint FKB2DA8F5D6F97679F;

    alter table TIMECARD 
        drop constraint FKB2DA8F5D1392C063;

    alter table USER_ROLES 
        drop constraint FKC6C79929B88C7F4C;

    alter table USER_ROLES 
        drop constraint FKC6C79929ABF229CD;

    drop table TIMECARD if exists;

    drop table USERS if exists;

    drop table USER_ROLE if exists;

    drop table USER_ROLES if exists;

    drop sequence hibernate_sequence;

    create table TIMECARD (
        TIMECARD_ID bigint not null,
        COMMENTS varchar(255) not null,
        START_DATE timestamp not null,
        STATUS VARCHAR(20) not null,
        APPROVER_FK bigint,
        SUBMITTER_FK bigint not null,
        primary key (TIMECARD_ID)
    );

    create table USERS (
        USER_ID bigint not null,
        COMMENT varchar(255),
        CREATION_DATE timestamp not null,
        EMAIL varchar(255) not null,
        FIRST_NAME varchar(255) not null,
        IS_ACTIVE boolean not null,
        LAST_NAME varchar(255) not null,
        PASSWORD varchar(255) not null,
        USERNAME varchar(255) not null unique,
        primary key (USER_ID)
    );

    create table USER_ROLE (
        USER_ROLE_ID bigint not null,
        ROLE VARCHAR(20) not null,
        primary key (USER_ROLE_ID)
    );

    create table USER_ROLES (
        USER_USER_ID_FK bigint not null,
        ROLES_USER_ROLE_ID_FK bigint not null,
        primary key (USER_USER_ID_FK, ROLES_USER_ROLE_ID_FK),
        unique (ROLES_USER_ROLE_ID_FK)
    );

    alter table TIMECARD 
        add constraint FKB2DA8F5D6F97679F 
        foreign key (SUBMITTER_FK) 
        references USERS;

    alter table TIMECARD 
        add constraint FKB2DA8F5D1392C063 
        foreign key (APPROVER_FK) 
        references USERS;

    alter table USER_ROLES 
        add constraint FKC6C79929B88C7F4C 
        foreign key (ROLES_USER_ROLE_ID_FK) 
        references USER_ROLE;

    alter table USER_ROLES 
        add constraint FKC6C79929ABF229CD 
        foreign key (USER_USER_ID_FK) 
        references USERS;

    create sequence hibernate_sequence start with 1 increment by 1;
