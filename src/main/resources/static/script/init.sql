create table if not exists role
(
    role_id   int auto_increment
        primary key,
    role_name enum ('ADMIN', 'SUPERADMIN', 'USER') null
);

create table if not exists topic
(
    topic_id    int auto_increment
        primary key,
    created_at  datetime(6)  null,
    status      varchar(255) null,
    topic_image varchar(255) null,
    topic_name  varchar(255) null,
    updated_at  datetime(6)  null
);

create table if not exists exam
(
    exam_id     int auto_increment
        primary key,
    audio_part1 text         null,
    audio_part2 text         null,
    audio_part3 text         null,
    audio_part4 text         null,
    created_at  datetime(6)  null,
    exam_image  varchar(255) null,
    exam_name   varchar(255) null,
    status      varchar(255) null,
    updated_at  datetime(6)  null,
    topic_id    int          null,
    constraint FKkcuey063e9q6my2rqbw3uo4ec
        foreign key (topic_id) references topic (topic_id)
            on delete cascade
);

create index topic_id_index
    on exam (topic_id);

create table if not exists part
(
    part_id            int auto_increment
        primary key,
    created_at         datetime(6)  null,
    number_of_question int          not null,
    part_audio         text         null,
    part_code          varchar(255) null,
    part_content       text         null,
    part_image         text         null,
    part_name          varchar(255) null,
    status             varchar(255) null,
    updated_at         datetime(6)  null,
    exam_id            int          null,
    constraint FKaaunypvxa92wlfqoglkmx0g9b
        foreign key (exam_id) references exam (exam_id)
            on delete cascade
);

create index exam_id_index
    on part (exam_id);

create table if not exists question
(
    question_id      int auto_increment
        primary key,
    answera          varchar(255) null,
    answerb          varchar(255) null,
    answerc          varchar(255) null,
    answerd          varchar(255) null,
    correct_answer   varchar(255) null,
    created_at       datetime(6)  null,
    paragraph1       text         null,
    paragraph2       text         null,
    question_audio   text         null,
    question_content text         null,
    question_image   text         null,
    question_number  varchar(255) null,
    updated_at       datetime(6)  null,
    part_id          int          null,
    constraint FKhhgpexaoh3njc4r07qhnal3wm
        foreign key (part_id) references part (part_id)
            on delete cascade
);

create index part_id_index
    on question (part_id);

create table if not exists user_account
(
    user_id    int auto_increment
        primary key,
    avatar     varchar(1000)                                             null,
    created_at datetime(6)                            default (now())    null,
    email      varchar(255)                                              not null,
    full_name  varchar(255)                                              null,
    password   varchar(255)                                              not null,
    status     enum ('ACTIVE', 'BLOCKED', 'INACTIVE') default 'INACTIVE' null,
    updated_at datetime(6)                            default (now())    null,
    constraint UK_hl02wv5hym99ys465woijmfib
        unique (email)
);

create table if not exists user_exam_history
(
    user_exam_history_id               int auto_increment
        primary key,
    exam_date                          datetime(6) null,
    number_of_correct_answer           int         not null,
    number_of_correct_answer_part1     int         not null,
    number_of_correct_answer_part2     int         not null,
    number_of_correct_answer_part3     int         not null,
    number_of_correct_answer_part4     int         not null,
    number_of_correct_answer_part5     int         not null,
    number_of_correct_answer_part6     int         not null,
    number_of_correct_answer_part7     int         not null,
    number_of_correct_listening_answer int         not null,
    number_of_correct_reading_answer   int         not null,
    number_of_not_answer               int         not null,
    number_of_wrong_answer             int         not null,
    number_of_wrong_listening_answer   int         not null,
    number_of_wrong_reading_answer     int         not null,
    total_score                        int         not null,
    exam_id                            int         null,
    user_id                            int         null,
    time_remaining                     int         null,
    time_to_do_exam                    int         null,
    constraint FK6ccum2icpm22whhr8ghrnxjiv
        foreign key (user_id) references user_account (user_id)
            on delete cascade,
    constraint FKb3p1w82tneq8wf9cwms8xa68w
        foreign key (exam_id) references exam (exam_id)
            on delete cascade
);

create table if not exists user_answer
(
    user_answer_id       int auto_increment
        primary key,
    selected_answer      varchar(255) null,
    question_id          int          null,
    is_correct           bit          null,
    user_exam_history_id int          null,
    constraint FK33v5ea65kc5flb2ej9b3k7dq4
        foreign key (user_exam_history_id) references user_exam_history (user_exam_history_id),
    constraint FKpsk90eok3ounaet92hku3gny1
        foreign key (question_id) references question (question_id)
);

create table if not exists user_role
(
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id),
    constraint FK7ojmv1m1vrxfl3kvt5bi5ur73
        foreign key (user_id) references user_account (user_id),
    constraint FKa68196081fvovjhkek5m97n3y
        foreign key (role_id) references role (role_id)
);

