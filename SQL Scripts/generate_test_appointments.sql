insert into appointment_type values (1,'General');

insert into appointment (reason, fk_patient_id, fk_doctor_id, fk_appointment_type_id, date) values
    ('Malestar general', '00AAK', '00AAH', 1, '2024-12-01 14:30'),
    ('Revisión', '00AAK', '00AAJ', 1, '2024-12-01 14:30'),
    ('Vacuna', '00AAK', '00AAH', 1, '2024-12-01 14:30'),
    ('Dolor abdominal', '00ABA', '00AAG', 1, '2024-01-22T12:30:00Z'),
    ('Nauseas severas', '00ABB', '00AAG', 1, '2024-01-22T13:00:00Z'),
    ('Dolor muscular en la rodilla', '00ABC', '00AAG', 1, '2024-01-22T13:30:00Z'),
    ('Malestar general', '00ABA', '00AAG', 1, '2024-01-22T13:45:00Z'),
    ('Resfriado con fuerte dolor de garganta', '00ABE', '00AAG', 1, '2024-01-22T14:00:00Z'),
    ('Vértigos repentinos', '00ABF', '00AAG', 1, '2024-01-22T14:15:00Z'),
    ('Dolor de oido y falta de audición', '00ABG', '00AAG', 1, '2024-01-22T14:30:00'),
    ('Falta de motrizidad en la pierna derecha', '00ABH', '00AAG', 1, '2024-01-22T14:45:00Z'),
    ('Posible choque anafiláctico leve', '00ABG', '00AAA', 1, '2024-01-22T14:30:00'),
    ('Síntomas de alérgia', '00ABH', '00AAA', 1, '2024-01-22T14:45:00Z');
