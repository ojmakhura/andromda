
    alter table ROLES_USER 
        drop constraint FKAC90A02D9EB15801;

    alter table ROLES_USER 
        drop constraint FKAC90A02DE0D9D59D;

    alter table TIMECARD 
        drop constraint FKB2DA8F5D6F97679F;

    alter table TIMECARD 
        drop constraint FKB2DA8F5D1392C063;

    alter table TIME_ALLOCATION 
        drop constraint FKA05275922A5D9FDD;

    alter table TIME_ALLOCATION 
        drop constraint FKA05275928DFDC35D;

    drop table ROLES_USER if exists;

    drop table TASK if exists;

    drop table TIMECARD if exists;

    drop table TIME_ALLOCATION if exists;

    drop table USERS if exists;

    drop table USER_ROLE if exists;

    drop sequence hibernate_sequence;

    create table ROLES_USER (
        USER_FK bigint not null,
        ROLES_FK bigint not null,
        unique (ROLES_FK)
    );

    create table TASK (
        TASK_ID bigint not null,
        NAME varchar(255) unique,
        primary key (TASK_ID)
    );

    create table TIMECARD (
        TIMECARD_ID bigint not null,
        COMMENTS varchar(255),
        START_DATE timestamp,
        STATUS varchar(255),
        APPROVER_FK bigint,
        SUBMITTER_FK bigint not null,
        primary key (TIMECARD_ID)
    );

    create table TIME_ALLOCATION (
        TIME_ALLOCATION_ID bigint not null,
        TIME_PERIOD_END_TIME timestamp,
        TIME_PERIOD_START_TIME timestamp,
        TASK_FK bigint not null,
        TIMECARD_FK bigint not null,
        primary key (TIME_ALLOCATION_ID)
    );

    create table USERS (
        USER_ID bigint not null,
        COMMENT varchar(255) not null,
        CREATION_DATE timestamp,
        EMAIL varchar(255) unique,
        FIRST_NAME varchar(255),
        IS_ACTIVE boolean,
        LAST_NAME varchar(255),
        PASSWORD varchar(255),
        USERNAME varchar(255) unique,
        primary key (USER_ID)
    );

    create table USER_ROLE (
        USER_ROLE_ID bigint not null,
        ROLE varchar(255),
        primary key (USER_ROLE_ID)
    );

    alter table ROLES_USER 
        add constraint FKAC90A02D9EB15801 
        foreign key (ROLES_FK) 
        references USER_ROLE;

    alter table ROLES_USER 
        add constraint FKAC90A02DE0D9D59D 
        foreign key (USER_FK) 
        references USERS;

    alter table TIMECARD 
        add constraint FKB2DA8F5D6F97679F 
        foreign key (SUBMITTER_FK) 
        references USERS;

    alter table TIMECARD 
        add constraint FKB2DA8F5D1392C063 
        foreign key (APPROVER_FK) 
        references USERS;

    alter table TIME_ALLOCATION 
        add constraint FKA05275922A5D9FDD 
        foreign key (TIMECARD_FK) 
        references TIMECARD;

    alter table TIME_ALLOCATION 
        add constraint FKA05275928DFDC35D 
        foreign key (TASK_FK) 
        references TASK;

    create sequence hibernate_sequence start with 1 increment by 1;
