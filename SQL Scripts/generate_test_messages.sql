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

--Conversation 2

INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES ('00AAL', '00AAA', '2023-12-16 10:00:00', 'Buenos días, doctor. Desde hace unos días me he sentido muy cansado y con falta de energía. ¿Tiene alguna recomendación?', TRUE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAL', '00AAA', '2023-12-16 10:30:00', 'Buenos días. Siento escuchar que se siente cansado. ¿Podría decirme más sobre su dieta y rutina diaria? Además, ¿ha experimentado algún otro síntoma?', FALSE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAL', '00AAA', '2023-12-16 11:00:00', 'Gracias por responder. Últimamente he estado saltándome comidas debido al trabajo y durmiendo menos. No he notado otros síntomas, solo el cansancio general.', TRUE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAL', '00AAA', '2023-12-16 11:30:00', 'Parece que su cansancio puede estar relacionado con la falta de sueño adecuado y nutrición insuficiente. Le aconsejaría intentar establecer una rutina de sueño regular y asegurarse de comer comidas balanceadas. Si el cansancio persiste, podríamos considerar hacer algunos análisis para descartar otras causas.', FALSE, FALSE, FALSE);


--Conversation 3

INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES ('00AAK', '00AAB', '2023-12-17 09:00:00', 'Hola Doctor, he estado experimentando dolores de cabeza frecuentes en las últimas dos semanas. ¿Podría ayudarme?', TRUE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAK', '00AAB', '2023-12-17 09:30:00', 'Buenos días. Lamento escuchar sobre sus dolores de cabeza. ¿Puede describirme la naturaleza del dolor y si ha habido algún cambio en su rutina diaria o niveles de estrés?', FALSE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAK', '00AAB', '2023-12-17 10:00:00', 'Gracias por responder. Los dolores son pulsátiles y generalmente ocurren en la parte frontal de la cabeza. He estado bajo bastante estrés en el trabajo recientemente.', TRUE, FALSE, FALSE);
INSERT INTO message(fk_patient_id, fk_doctor_id, date, message, patient_sent, patient_read, doctor_read)
VALUES
('00AAK', '00AAB', '2023-12-17 10:30:00', 'Entendido. Los dolores de cabeza pueden ser desencadenados o exacerbados por el estrés. Le sugeriría intentar técnicas de relajación y asegurarse de tomar descansos regulares durante el trabajo. Si los dolores persisten, podríamos considerar una consulta presencial para explorar otras posibles causas.', FALSE, FALSE, FALSE);
