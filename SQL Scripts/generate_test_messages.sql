INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES ('00AAK', '00AAA', '2023-12-14 08:30:00', 'Buenos días, doctor. He estado sintiendo un dolor constante en mi rodilla izquierda desde hace una semana. ¿Podría aconsejarme qué hacer?', TRUE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAK', '00AAA', '2023-12-15 09:00:00', 'Buenos días. Lamento escuchar sobre su dolor de rodilla. ¿Podría describirme más sobre el dolor y decirme si ha tenido alguna lesión reciente o si ha cambiado su nivel de actividad física?', FALSE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAK', '00AAA', '2023-12-15 10:15:00', 'Gracias por su respuesta, doctor. El dolor es agudo y se intensifica cuando subo escaleras. No recuerdo haberme lesionado recientemente, pero empecé a correr con más frecuencia este mes.', TRUE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAK', '00AAA', '2023-12-15 11:00:00', 'Entiendo, parece que podría ser una sobrecarga debido al aumento de actividad. Le recomiendo reducir la intensidad de la carrera y aplicar hielo en la rodilla. Si el dolor persiste o empeora en los próximos días, sería bueno programar una cita para un examen más detallado.', FALSE, FALSE, FALSE);
