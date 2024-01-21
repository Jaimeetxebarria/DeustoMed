create or replace view doctor_with_personal_data with (security_invoker = on) as
select p.id, p.name, p.surname1, p.surname2, p.birthdate, p.sex, p.dni, p.email, p.phone, p.address, s.name as speciality
from doctor d
natural join person p
join speciality s on d.fk_speciality_id = s.id;

create or replace view patient_with_personal_data with (security_invoker = on) as
select id, name, surname1, surname2, birthdate, sex, dni, email, phone, address, fk_doctor_id as doctor_id
from patient
natural join person;

create or replace view chats with (security_invoker = on) as
select distinct fk_patient_id as patient_id, fk_doctor_id as doctor_id
from message;

create or replace view appointment_with_type with (security_invoker = on) as
select a.id, a.date, a.reason, a.fk_patient_id, a.fk_doctor_id, at.name as appointment_type
from appointment a join appointment_type at on a.fk_appointment_type_id = at.id;