--==================== PUBLIC =====================
------------------ General Types -----------------
drop type sex cascade;
create type
  sex as enum('MALE', 'FEMALE');

drop table user_type cascade;
create table
  user_type (
    id smallint generated by default as identity primary key,
    name text
  );

insert into
  user_type
values
  (default, 'patient'),
  (default, 'doctor'),
  (default, 'admin');

-- User Id generator
drop sequence user_id_sequence;
create sequence
  user_id_sequence as int;

create or replace function
  generate_user_id () RETURNS text AS $$
declare
    numeric_id int := nextval('user_id_sequence') - 1; -- -1 because the sequence starts at 1
    letter_part text := '';
    i int;
begin
    for i in 1..3 loop
        letter_part := chr(65 + (numeric_id % 26)) || letter_part;
        numeric_id := numeric_id / 26;
    end loop;

    return lpad(numeric_id::text, 2, '0') || letter_part;
end;
$$ language plpgsql;

drop table if exists person cascade;
create table
  person (
    id char(5) default generate_user_id() primary key,
    name text not null,
    surname1 text not null,
    surname2 text not null,
    dni text not null,
    birthdate date not null,
    email text not null,
    phone text not null,
    address text not null,
    sex sex not null,

    constraint valid_age check(EXTRACT(YEAR FROM age(CURRENT_DATE, birthdate)) BETWEEN 0 AND 150)
  );

drop table if exists doctor cascade;
create table
  doctor (id char(5) primary key references person);

-- ALTER table doctor ENABLE row level SECURITY;

drop table if exists doctor_schedule cascade;
create table doctor_schedule (
    schedule_id SERIAL primary KEY,
    doctor_id CHAR(5),
    day_of_week VARCHAR(10),
    start_time TIME,
    end_time TIME,
    foreign KEY (doctor_id) references doctor(id)
);


drop table if exists patient cascade;
create table
  patient (
    id char(5) primary key references person,
    fk_doctor_id char(5) references doctor
  );

-- alter table patient enable row level security;
drop table if exists appointment_type cascade;
create table
  appointment_type (
    id int generated by default as identity primary key,
    name text not null
  );

drop table if exists appointment cascade;
create table
  appointment (
    id int generated by default as identity primary key,
    datetime date,
    hour time,
    reason text,
    fk_patient_id char(5) references patient,
    fk_doctor_id char(5) not null references doctor,
    fk_appointment_type_id int not null references appointment_type
  );

drop table if exists diagnosis cascade;
create table
  diagnosis (
    id int generated by default as identity primary key,
    fk_doctor_id char(5) references doctor
  );

drop table if exists appointment_has_diagnosis cascade;
create table
  appointment_has_diagnosis (
    fk_appointment_id int not null references appointment,
    fk_diagnosis_id int not null references diagnosis,
    constraint pk_appointment_has_diagnosis primary key (fk_appointment_id, fk_diagnosis_id)
  );

drop table if exists surgery cascade;
create table
  surgery (
    id int generated by default as identity primary key,
    datetime timestamp with time zone not null
  );

drop table if exists doctor_performs_surgery cascade;
create table
  doctor_performs_surgery (
    fk_doctor_id char(5) not null references doctor,
    fk_surgery_id int not null references surgery,
    constraint pk_doctor_performs_surgery primary key (fk_doctor_id, fk_surgery_id)
  );

drop table if exists patient_undergoes_surgery cascade;
create table
  patient_undergoes_surgery (
    fk_patient_id char(5) not null references patient,
    fk_surgery_id int not null references surgery,
    constraint pk_patient_undergoes_surgery primary key (fk_patient_id, fk_surgery_id)
  );

drop table if exists disease cascade;
create table
  disease (
    id int generated by default as identity primary key,
    name text not null unique,
    chronic boolean not null default false,
    hereditary boolean not null default false
  );

drop table if exists patient_suffers_disease cascade;
create table
  patient_suffers_disease (
    fk_patient_id char(5) not null references patient,
    fk_disease_id int not null references disease,
    constraint pk_patient_suffers_disease primary key (fk_patient_id, fk_disease_id)
  );

drop table if exists dose cascade;
create table
  dose (
    id int generated by default as identity primary key,
    "timestamp" time not null
  );

drop table if exists prescription cascade;
create table
  prescription (
    id int generated by default as identity primary key,
    fk_patient_id char(5) references patient,
    fk_dose_id int references dose
  );

--===================== CUSTOM AUTH =====================
create schema
  if not exists custom_auth;

drop table if exists
  custom_auth.session cascade;

create table
  custom_auth.session (
    id uuid primary key default gen_random_uuid (),
    user_id char(5) not null references person,
    fk_user_type smallint references user_type not null,
    access_token uuid not null,
    access_token_expiry timestamptz default current_timestamp + interval '5 minutes',
    session_expiry timestamptz default current_timestamp + interval '6 hours'
  );

drop table if exists
  custom_auth.refresh_token cascade;

create table
  custom_auth.refresh_token (
    id bigint generated by default as identity primary key,
    token uuid not null,
    revoked boolean,
    fk_session_id uuid references session on delete cascade
  );

create or replace function
  custom_auth.find_session (session_id uuid, access_token uuid) returns table (session_user_id text, session_user_type smallint) security definer as $$
  declare
  	session_user_id_temp text;
  	session_user_type_temp smallint;
  begin
    select user_id, fk_user_type into session_user_id_temp, session_user_type_temp
    from custom_auth.session s
    where s.session_id = session_id and s.access_token = access_token
    limit 1;

    return query select session_user_id_temp, session_user_type_temp;
  end;
$$ language plpgsql;

create or replace function
  custom_auth.authenticate () returns void as $$
  declare
    session_id uuid;
    access_token uuid;
    session_user_id char(5);
    session_user_type smallint;
  begin
    select current_setting('request.cookie.session_id', true) into session_id;
    select current_setting('request.cookie.access_token', true) into access_token;
    select auth.session_user_id(session_id, access_token) into session_user_id;

  	select user_id, fk_user_type into session_user_id, session_user_type
    from custom_auth.session s
    where s.session_id = session_id and s.access_token = access_token;

    if session_user_id is not null then
        set local role to authenticated;
        perform set_config('auth.user_id', session_user_id, true);
  		perform set_config('auth.user_type', session_user_type::text, true);
    else
      set local role to anonymous;
        perform set_config('auth.user_id', '', true);
  		perform set_config('auth.user_type', '', true);
    end if;
  end;
$$ language plpgsql;