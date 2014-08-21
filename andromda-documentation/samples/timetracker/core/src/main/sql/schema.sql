
    alter table ROLES_USER 
        drop constraint FK_ebw58iagjenlqywsv77hd06s6 if exists;

    alter table ROLES_USER 
        drop constraint FK_1sfp0uflxi1alf1kppnp5p4wr if exists;

    alter table TIMECARD 
        drop constraint FK_sdc0np556eya269aqdlgpm4e0 if exists;

    alter table TIMECARD 
        drop constraint FK_7cben9plp9v6s9pr1d2cnv2j7 if exists;

    alter table TIME_ALLOCATION 
        drop constraint FK_f2xe5x3jwhlrmykk7c4r4lqys if exists;

    alter table TIME_ALLOCATION 
        drop constraint FK_9q2adbtbt030qgm3rdxuk5vna if exists;

    drop table ROLES_USER if exists;

    drop table TASK if exists;

    drop table TIMECARD if exists;

    drop table TIME_ALLOCATION if exists;

    drop table USERS if exists;

    drop table USER_ROLE if exists;

    drop sequence hibernate_sequence;

    create table ROLES_USER (
        USER_FK bigint not null,
        ROLES_FK bigint not null
    );

    create table TASK (
        TASK_ID bigint not null,
        NAME varchar(255),
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
        EMAIL varchar(255),
        FIRST_NAME varchar(255),
        IS_ACTIVE boolean,
        LAST_NAME varchar(255),
        PASSWORD varchar(255),
        USERNAME varchar(255),
        primary key (USER_ID)
    );

    create table USER_ROLE (
        USER_ROLE_ID bigint not null,
        ROLE varchar(255),
        primary key (USER_ROLE_ID)
    );

    alter table ROLES_USER 
        add constraint UK_ebw58iagjenlqywsv77hd06s6  unique (ROLES_FK);

    alter table TASK 
        add constraint UK_tbaloihbh4a7pjhcgjmxfdmji  unique (NAME);

    alter table USERS 
        add constraint UK_81nqioeq3njjrwqaltk2mcobj  unique (EMAIL);

    alter table USERS 
        add constraint UK_h6k33r31i2nvrri9lok4r163j  unique (USERNAME);

    alter table ROLES_USER 
        add constraint FK_ebw58iagjenlqywsv77hd06s6 
        foreign key (ROLES_FK) 
        references USER_ROLE;

    alter table ROLES_USER 
        add constraint FK_1sfp0uflxi1alf1kppnp5p4wr 
        foreign key (USER_FK) 
        references USERS;

    alter table TIMECARD 
        add constraint FK_sdc0np556eya269aqdlgpm4e0 
        foreign key (APPROVER_FK) 
        references USERS;

    alter table TIMECARD 
        add constraint FK_7cben9plp9v6s9pr1d2cnv2j7 
        foreign key (SUBMITTER_FK) 
        references USERS;

    alter table TIME_ALLOCATION 
        add constraint FK_f2xe5x3jwhlrmykk7c4r4lqys 
        foreign key (TASK_FK) 
        references TASK;

    alter table TIME_ALLOCATION 
        add constraint FK_9q2adbtbt030qgm3rdxuk5vna 
        foreign key (TIMECARD_FK) 
        references TIMECARD;

    create sequence hibernate_sequence start with 1 increment by 1;
