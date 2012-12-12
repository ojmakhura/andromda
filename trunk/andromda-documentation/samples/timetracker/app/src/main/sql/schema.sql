
    alter table ROLES_USER 
        drop constraint FKAC90A02D5B88778A;

    alter table ROLES_USER 
        drop constraint FKAC90A02DE0D9D5F3;

    alter table TIMECARD 
        drop constraint FKB2DA8F5DFE6750ED;

    alter table TIMECARD 
        drop constraint FKB2DA8F5D30444EA9;

    alter table TIME_ALLOCATION 
        drop constraint FKA0527592E9D46723;

    alter table TIME_ALLOCATION 
        drop constraint FKA0527592C6D38653;

    drop table ROLES_USER if exists;

    drop table TASK if exists;

    drop table TIMECARD if exists;

    drop table TIME_ALLOCATION if exists;

    drop table USERS if exists;

    drop table USER_ROLE if exists;

    drop sequence hibernate_sequence;

    create table ROLES_USER (
        USER_ID bigint not null,
        USER_ROLE_ID bigint not null,
        unique (USER_ROLE_ID)
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
        APPROVER bigint,
        SUBMITTER bigint not null,
        primary key (TIMECARD_ID)
    );

    create table TIME_ALLOCATION (
        TIME_ALLOCATION_ID bigint not null,
        TIME_PERIOD_END_TIME timestamp,
        TIME_PERIOD_START_TIME timestamp,
        TASK bigint not null,
        TIMECARD bigint not null,
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
        add constraint FKAC90A02D5B88778A 
        foreign key (USER_ROLE_ID) 
        references USER_ROLE;

    alter table ROLES_USER 
        add constraint FKAC90A02DE0D9D5F3 
        foreign key (USER_ID) 
        references USERS;

    alter table TIMECARD 
        add constraint FKB2DA8F5DFE6750ED 
        foreign key (SUBMITTER) 
        references USERS;

    alter table TIMECARD 
        add constraint FKB2DA8F5D30444EA9 
        foreign key (APPROVER) 
        references USERS;

    alter table TIME_ALLOCATION 
        add constraint FKA0527592E9D46723 
        foreign key (TASK) 
        references TASK;

    alter table TIME_ALLOCATION 
        add constraint FKA0527592C6D38653 
        foreign key (TIMECARD) 
        references TIMECARD;

    create sequence hibernate_sequence start with 1 increment by 1;
