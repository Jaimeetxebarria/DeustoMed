truncate person cascade;
drop sequence user_id_sequence;
create sequence
  user_id_sequence as int;

insert into person (name, surname1, surname2, birthdate, sex, encrypted_password, dni, address, phone, email) values
  ('Elena', 'García', 'Martínez', '1990-05-15', 'FEMALE', '$2a$12$HDSy6LBAAMSt3aT8.hOKx.LUDk.FUHY3NJK8UMelxb2toLcyznTFq', '12345678A', 'Street 123, City', '+123456789', 'elena.garcia@email.com'),
  ('Adrián', 'Rodríguez', 'Fernández', '1985-10-20', 'MALE', '$2a$12$gBiNC9REurG/j.jntFuZ8uxL3188CsA3GaaT/CGq6QDzbH3QzQD6u', '87654321B', 'Avenue 456, Town', '+987654321', 'adrian.rodriguez@email.com'),
  ('Lucía', 'Martínez', 'López', '2000-12-03', 'FEMALE', '$2a$12$1GL8Aq07iA.kSc2USiCDUuROBO3WSZFTl2fg0BHdAURbTllCVonrq', '23456789C', 'Boulevard 789, Village', '+234567890', 'lucia.martinez@email.com'),
  ('Diego', 'Pérez', 'Sánchez', '1995-03-25', 'MALE', '$2a$12$Yh6nt/jcjEkIpgG2MS9JM.vyVUjvZ7STOs.RxUVaafjb/ns/t33cq', '98765432D', 'Lane 012, Hamlet', '+876543210', 'diego.perez@email.com'),
  ('Sara', 'Gómez', 'Hernández', '1988-08-18', 'FEMALE', '$2a$12$UTA9REcytRGOxLcgP/j5d.c4mW0/iQVM3/1Yx2XVlC4sN5fx9FcvS', '34567890E', 'Square 345, City', '+345678901', 'sara.gomez@email.com'),
  ('Javier', 'Ruiz', 'González', '1992-11-29', 'MALE', '$2a$12$9PamnhLxeaNUqi8yZ8QieeSGQJ3z.d./m1X2FXBV0mtI6eW7lR5gq', '67890123F', 'Circle 678, Town', '+456789012', 'javier.ruiz@email.com'),
  ('María', 'Serrano', 'Díaz', '1986-07-12', 'FEMALE', '$2a$12$l1lFxIqfiyxITTvDU5asl.e/mJ7SorB/Kq2Aa8XPWSvweVIQKT3gC', '01234567G', 'Plaza 901, Village', '+567890123', 'maria.serrano@email.com'),
  ('Carlos', 'Hernández', 'Muñoz', '2002-02-10', 'MALE', '$2a$12$bkOT3sjoe7hpQMrQ7X2h5eSnq15qz0CGpOAO8hjhHKlJIZ1p4WbqO', '76543210H', 'Avenue 234, Hamlet', '+678901234', 'carlos.hernandez@email.com'),
  ('Laura', 'Díaz', 'Romero', '1998-09-05', 'FEMALE', '$2a$12$1GL8Aq07iA.kSc2USiCDUuROBO3WSZFTl2fg0BHdAURbTllCVonrq', '23456789I', 'Street 567, City', '+789012345', 'laura.diaz@email.com'),
  ('Alejandro', 'Martín', 'Pérez', '1993-06-28', 'MALE', '$2a$12$gBiNC9REurG/j.jntFuZ8uxL3188CsA3GaaT/CGq6QDzbH3QzQD6u', '87654321J', 'Boulevard 890, Town', '+890123456', 'alejandro.martin@email.com'),
  ('Carmen', 'Torres', 'García', '1983-04-14', 'FEMALE', '$2a$12$bkOT3sjoe7hpQMrQ7X2h5eSnq15qz0CGpOAO8hjhHKlJIZ1p4WbqO', '45678901K', 'Lane 123, Village', '+901234567', 'carmen.torres@email.com'),
  ('Pablo', 'Flores', 'Navarro', '2005-01-07', 'MALE', '$2a$12$LRqfuwaIPwRKX41TqWzknOkHLh9eHhevl.VOZPB8qgFkdAYXq9YaG', '98765432L', 'Square 456, Hamlet', '+012345678', 'pablo.flores@email.com'),
  ('Inés', 'Sánchez', 'Morales', '1997-12-22', 'FEMALE', '$2a$12$5BB.QxUGvukjYEszgYZZAuTHiz2K2QkiNx3cmZ1u0vD7zN1vVIMeK', '34567890M', 'Circle 789, City', '+123456789', 'ines.sanchez@email.com'),
  ('Jorge', 'Vázquez', 'Jiménez', '1987-10-03', 'MALE', '$2a$12$9PamnhLxeaNUqi8yZ8QieeSGQJ3z.d./m1X2FXBV0mtI6eW7lR5gq', '23456789N', 'Plaza 012, Town', '+234567890', 'jorge.vazquez@email.com'),
  ('Clara', 'Gutiérrez', 'Silva', '1994-09-17', 'FEMALE', '$2a$12$bkOT3sjoe7hpQMrQ7X2h5eSnq15qz0CGpOAO8hjhHKlJIZ1p4WbqO', '67890123O', 'Avenue 234, Village', '+345678901', 'clara.gutierrez@email.com'),
  ('Fernando', 'Iglesias', 'Torres', '2004-08-09', 'MALE', '$2a$12$qtOlBHrdNo7dO/Ng.mClpeCXVnWvruhn0bDwKy0RU.mwoKEOS6OyC', '01234567P', 'Boulevard 567, Hamlet', '+456789012', 'fernando.iglesias@email.com'),
  ('Sofía', 'Román', 'Castro', '1984-06-02', 'FEMALE', '$2a$12$UTA9REcytRGOxLcgP/j5d.c4mW0/iQVM3/1Yx2XVlC4sN5fx9FcvS', '76543210Q', 'Lane 678, City', '+567890123', 'sofia.roman@email.com'),
  ('Martín', 'Ortega', 'Molina', '1996-04-27', 'MALE', '$2a$12$l1lFxIqfiyxITTvDU5asl.e/mJ7SorB/Kq2Aa8XPWSvweVIQKT3gC', '23456789R', 'Square 901, Town', '+678901234', 'martin.ortega@email.com'),
  ('Luisa', 'Navarro', 'Pascual', '2001-03-19', 'FEMALE', '$2a$12$1GL8Aq07iA.kSc2USiCDUuROBO3WSZFTl2fg0BHdAURbTllCVonrq', '87654321S', 'Circle 234, Village', '+789012345', 'luisa.navarro@email.com'),
  ('Víctor', 'Dominguez', 'Sáez', '1989-12-12', 'MALE', '$2a$12$C6YqUHi2MMKgyfYFeCtAt.rMqH8T2PTB3yVgqIpv54634jH9Fhrka', '45678901T', 'Plaza 567, Hamlet', '+890123456', 'victor.dominguez@email.com'),
  ('Marina', 'Fernández', 'Vidal', '1991-08-25', 'FEMALE', '$2a$12$l1lFxIqfiyxITTvDU5asl.e/mJ7SorB/Kq2Aa8XPWSvweVIQKT3gC', '98765432U', 'Boulevard 789, City', '+012345678', 'marina.fernandez@email.com'),
  ('Raúl', 'López', 'Santana', '1986-06-14', 'MALE', '$2a$12$VP9UbNCxHCq08F0LZsNgnuhoS3I/Lso/aNS8IXILQEjw6ZN/UTjRO', '23456789V', 'Lane 012, Town', '+123456789', 'raul.lopez@email.com'),
  ('Eva', 'González', 'Ortega', '2002-03-07', 'FEMALE', '$2a$12$HDSy6LBAAMSt3aT8.hOKx.LUDk.FUHY3NJK8UMelxb2toLcyznTFq', '76543210W', 'Square 345, Village', '+234567890', 'eva.gonzalez@email.com'),
  ('Héctor', 'Martínez', 'Sánchez', '1998-12-30', 'MALE', '$2a$12$hF9hTActyAwIDPAbsG7JbusLIVbsZcXxJaXVvl2r9ER1Hy8uk97b6', '34567890X', 'Circle 678, Hamlet', '+345678901', 'hector.martinez@email.com'),
  ('Beatriz', 'Gómez', 'Garrido', '1993-09-19', 'FEMALE', '$2a$12$KfFl7cyZd53EQgL4Fj.OL.v1zrmGJyA.Jd94OHcsxaWF8MV.YH4OC', '01234567Y', 'Plaza 890, City', '+456789012', 'beatriz.gomez@email.com'),
  ('Mario', 'Sánchez', 'Durán', '2004-07-02', 'MALE', '$2a$12$l1lFxIqfiyxITTvDU5asl.e/mJ7SorB/Kq2Aa8XPWSvweVIQKT3gC', '87654321Z', 'Avenue 123, Town', '+567890123', 'mario.sanchez@email.com'),
  ('Ana', 'Pérez', 'Rojas', '1997-04-15', 'FEMALE', '$2a$12$gBiNC9REurG/j.jntFuZ8uxL3188CsA3GaaT/CGq6QDzbH3QzQD6u', '23456789AA', 'Boulevard 456, Village', '+678901234', 'ana.perez@email.com'),
  ('Gabriel', 'Romero', 'Molina', '1985-11-11', 'MALE', '$2a$12$Q.VgfGtvfz5/BaJwWGG4j.gq4OWub4ji0sbhq1Hsx7S2WofFsxEhy', '12345678AA', 'Square 789, Village', '+234567890', 'gabriel.romero@email'),
  ('Clara', 'Díaz', 'Vargas', '2000-10-05', 'FEMALE', '$2a$12$bkOT3sjoe7hpQMrQ7X2h5eSnq15qz0CGpOAO8hjhHKlJIZ1p4WbqO', '23456789BB', 'Circle 012, Hamlet', '+345678901', 'clara.diaz@email.com'),
  ('Francisco', 'García', 'Jiménez', '1995-05-28', 'MALE', '$2a$12$qtOlBHrdNo7dO/Ng.mClpeCXVnWvruhn0bDwKy0RU.mwoKEOS6OyC', '34567890CC', 'Plaza 123, City', '+456789012', 'francisco.garcia@email.com'),
  ('Lorena', 'Martín', 'Soto', '1990-02-20', 'FEMALE', '$2a$12$1GL8Aq07iA.kSc2USiCDUuROBO3WSZFTl2fg0BHdAURbTllCVonrq', '45678901DD', 'Lane 234, Town', '+567890123', 'lorena.martin@email.com'),
  ('Andres', 'Torres', 'Ferrer', '2001-01-10', 'MALE', '$2a$12$gBiNC9REurG/j.jntFuZ8uxL3188CsA3GaaT/CGq6QDzbH3QzQD6u', '56789012EE', 'Boulevard 567, Village', '+678901234', 'andres.torres@email.com'),
  ('Nuria', 'Navarro', 'Rivas', '1989-07-22', 'FEMALE', '$2a$12$QzeHNqyBe3TMG2B.ibqAGOSlm76KXP./Q0I60cmWMvvToAt4GfDWW', '67890123FF', 'Avenue 890, Hamlet', '+789012345', 'nuria.navarro@email.com'),
  ('Alberto', 'Ruiz', 'Salas', '1994-04-03', 'MALE', '$2a$12$gBiNC9REurG/j.jntFuZ8uxL3188CsA3GaaT/CGq6QDzbH3QzQD6u', '78901234GG', 'Street 901, City', '+890123456', 'alberto.ruiz@email.com'),
  ('Sara', 'Vega', 'Esteban', '2003-09-12', 'FEMALE', '$2a$12$UTA9REcytRGOxLcgP/j5d.c4mW0/iQVM3/1Yx2XVlC4sN5fx9FcvS', '89012345HH', 'Boulevard 234, Town', '+901234567', 'sara.vega@email.com'),
  ('Joaquín', 'Hernández', 'Moya', '1987-12-08', 'MALE', '$2a$12$9PamnhLxeaNUqi8yZ8QieeSGQJ3z.d./m1X2FXBV0mtI6eW7lR5gq', '90123456II', 'Circle 456, Village', '+012345678', 'joaquin.hernandez@email.com'),
  ('Lidia', 'Sanz', 'Montes', '1996-08-17', 'FEMALE', '$2a$12$1GL8Aq07iA.kSc2USiCDUuROBO3WSZFTl2fg0BHdAURbTllCVonrq', '01234567JJ', 'Plaza 789, Hamlet', '+123456789', 'lidia.sanz@email.com'),
  ('Pedro', 'Morales', 'Silvestre', '2005-06-29', 'MALE', '$2a$12$LRqfuwaIPwRKX41TqWzknOkHLh9eHhevl.VOZPB8qgFkdAYXq9YaG', '12345678KK', 'Lane 012, City', '+234567890', 'pedro.morales@email.com'),
  ('Alba', 'Ferrer', 'Crespo', '1984-03-04', 'FEMALE', '$2a$12$gBiNC9REurG/j.jntFuZ8uxL3188CsA3GaaT/CGq6QDzbH3QzQD6u', '23456789LL', 'Square 345, Town', '+345678901', 'alba.ferrer@email.com'),
  ('Ivan', 'Jiménez', 'Olivares', '1999-11-23', 'MALE', '$2a$12$5BB.QxUGvukjYEszgYZZAuTHiz2K2QkiNx3cmZ1u0vD7zN1vVIMeK', '34567890MM', 'Avenue 678, Village', '+456789012', 'ivan.jimenez@email.com');


insert into speciality values
  (default, 'Alergología'),
  (default, 'Anestesiología'),
  (default, 'Angiología'),
  (default, 'Cardiología'),
  (default, 'Endocrinología'),
  (default, 'Gastroenterología'),
  (default, 'Geriatría'),
  (default, 'Hematología'),
  (default, 'Hepatología'),
  (default, 'Infectología'),
  (default, 'Medicina interna'),
  (default, 'Medico de Familia'),
  (default, 'Nefrología'),
  (default, 'Neumología'),
  (default, 'Neurología'),
  (default, 'Oncología'),
  (default, 'Pediatría'),
  (default, 'Psiquiatría'),
  (default, 'Reumatología'),
  (default, 'Toxicología'),
  (default, 'Urología');

insert into doctor values
  ('00AAA', 1),
  ('00AAB', 3),
  ('00AAC', 5),
  ('00AAD', 7),
  ('00AAE', 8),
  ('00AAF', 10),
  ('00AAG', 12),
  ('00AAH', 11),
  ('00AAI', 13),
  ('00AAJ', 15);


insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAA', 'Saturday', '09:00', '17:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAA', 'Tuesday', '07:00', '16:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAA', 'Thursday', '07:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAA', 'Wednesday', '08:00', '14:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAA', 'Friday', '08:00', '14:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAB', 'Saturday', '10:00', '16:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAB', 'Friday', '07:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAB', 'Thursday', '08:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAC', 'Wednesday', '10:00', '19:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAC', 'Friday', '09:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAC', 'Thursday', '07:00', '13:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAD', 'Saturday', '08:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAD', 'Monday', '08:00', '14:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAD', 'Tuesday', '08:00', '16:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAD', 'Thursday', '07:00', '13:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAE', 'Saturday', '10:00', '19:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAE', 'Tuesday', '07:00', '14:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAE', 'Thursday', '10:00', '19:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAE', 'Monday', '10:00', '18:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAE', 'Friday', '07:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAF', 'Saturday', '10:00', '16:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAF', 'Monday', '08:00', '14:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAF', 'Tuesday', '09:00', '18:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAF', 'Thursday', '07:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAG', 'Saturday', '09:00', '18:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAG', 'Monday', '10:00', '18:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAG', 'Tuesday', '08:00', '17:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAH', 'Saturday', '09:00', '15:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAH', 'Monday', '07:00', '16:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAH', 'Friday', '07:00', '16:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAH', 'Thursday', '09:00', '17:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAI', 'Wednesday', '10:00', '17:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAI', 'Friday', '10:00', '19:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAI', 'Tuesday', '10:00', '19:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAJ', 'Wednesday', '07:00', '13:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAJ', 'Friday', '10:00', '18:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAJ', 'Tuesday', '08:00', '14:00');
insert into doctor_schedule (doctor_id, day_of_week, start_time, end_time) values ('00AAJ', 'Thursday', '09:00', '18:00');


--insert into patient values

CREATE OR REPLACE FUNCTION insert_person_records()
RETURNS VOID AS $$
DECLARE
    i INTEGER := 0;
BEGIN
    FOR i IN 5..30 LOOP
        INSERT INTO patient (id, fk_doctor_id)
        VALUES (
            '00A' || CHR(65 + FLOOR(i / 26)::INTEGER % 26) || CHR(65 + i % 26),
            '00AA' || CHR(65 + FLOOR(RANDOM() * 10)::INTEGER)
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;


select insert_person_records();

-- insert into admin
insert into admin values ('00AAB');

-- insert into medication
insert into medication (activesubstance, commercialname, stock, dose, company, shortdescription) values
    ("acetaminofén", "paracetamol", 5, 500, "cinfa", "fármaco con propiedades analgésicas y antipiréticas"),
    ("metamizol", "nolotil", 5, 500, "cinfa", "analgésico útil para reduci el dolor o la fiebre"),
    ("levotiroxina", "eutirox", 5, 500, "cinfa", "tratamiento de enfermedades y disfunciones de la glándula tiroides"),
    ("salbutamol", "ventolin", 5, 500, "cinfa", "tratamiento del cierre de conductos de aire en los pulmones");

insert into disease (name, chronic, hereditary) values
    ("bronquitis aguda", false, false),
    ("bronquitis crónica", true, false),
    ("resfriado común", false, false),
    ("otitis", false, false),
    ("otitis crónica supurativa", true, false),
    ("influenza", true, false),
    ("sinusitis", true, false);
