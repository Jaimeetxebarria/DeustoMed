insert into appointment_type values (1,'General');

insert into appointment(id, date, reason, fk_patient_id, fk_doctor_id, fk_appointment_type_id) values
('1', '2024-12-01 14:30', 'Malestar general', '00AAK', '00AAH', '1'),
('2', '2024-01-16 18:45', 'Revision', '00AAK', '00AAJ', '1'),
('3', '2024-03-25 10:00', 'Vacuna', '00AAK', '00AAH', '1');
