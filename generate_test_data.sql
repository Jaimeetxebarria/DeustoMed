truncate person cascade;
drop sequence user_id_sequence;
create sequence
  user_id_sequence as int;

insert into person (name, surname1, surname2, dni, birthdate, email, phone, adress, sex) values
 ('Elena', 'García', 'Martínez', '12345678A', '1990-05-15', 'elena.garcia@email.com', '+123456789', 'Street 123, City', 'FEMALE'),
  ('Adrián', 'Rodríguez', 'Fernández', '87654321B', '1985-10-20', 'adrian.rodriguez@email.com', '+987654321', 'Avenue 456, Town', 'MALE'),
  ('Lucía', 'Martínez', 'López', '23456789C', '2000-12-03', 'lucia.martinez@email.com', '+234567890', 'Boulevard 789, Village', 'FEMALE'),
  ('Diego', 'Pérez', 'Sánchez', '98765432D', '1995-03-25', 'diego.perez@email.com', '+876543210', 'Lane 012, Hamlet', 'MALE'),
  ('Sara', 'Gómez', 'Hernández', '34567890E', '1988-08-18', 'sara.gomez@email.com', '+345678901', 'Square 345, City', 'FEMALE'),
  ('Javier', 'Ruiz', 'González', '67890123F', '1992-11-29', 'javier.ruiz@email.com', '+456789012', 'Circle 678, Town', 'MALE'),
  ('María', 'Serrano', 'Díaz', '01234567G', '1986-07-12', 'maria.serrano@email.com', '+567890123', 'Plaza 901, Village', 'FEMALE'),
  ('Carlos', 'Hernández', 'Muñoz', '76543210H', '2002-02-10', 'carlos.hernandez@email.com', '+678901234', 'Avenue 234, Hamlet', 'MALE'),
  ('Laura', 'Díaz', 'Romero', '23456789I', '1998-09-05', 'laura.diaz@email.com', '+789012345', 'Street 567, City', 'FEMALE'),
  ('Alejandro', 'Martín', 'Pérez', '87654321J', '1993-06-28', 'alejandro.martin@email.com', '+890123456', 'Boulevard 890, Town', 'MALE'),
  ('Carmen', 'Torres', 'García', '45678901K', '1983-04-14', 'carmen.torres@email.com', '+901234567', 'Lane 123, Village', 'FEMALE'),
  ('Pablo', 'Flores', 'Navarro', '98765432L', '2005-01-07', 'pablo.flores@email.com', '+012345678', 'Square 456, Hamlet', 'MALE'),
  ('Inés', 'Sánchez', 'Morales', '34567890M', '1997-12-22', 'ines.sanchez@email.com', '+123456789', 'Circle 789, City', 'FEMALE'),
  ('Jorge', 'Vázquez', 'Jiménez', '23456789N', '1987-10-03', 'jorge.vazquez@email.com', '+234567890', 'Plaza 012, Town', 'MALE'),
  ('Clara', 'Gutiérrez', 'Silva', '67890123O', '1994-09-17', 'clara.gutierrez@email.com', '+345678901', 'Avenue 234, Village', 'FEMALE'),
  ('Fernando', 'Iglesias', 'Torres', '01234567P', '2004-08-09', 'fernando.iglesias@email.com', '+456789012', 'Boulevard 567, Hamlet', 'MALE'),
  ('Sofía', 'Román', 'Castro', '76543210Q', '1984-06-02', 'sofia.roman@email.com', '+567890123', 'Lane 678, City', 'FEMALE'),
  ('Martín', 'Ortega', 'Molina', '23456789R', '1996-04-27', 'martin.ortega@email.com', '+678901234', 'Square 901, Town', 'MALE'),
  ('Luisa', 'Navarro', 'Pascual', '87654321S', '2001-03-19', 'luisa.navarro@email.com', '+789012345', 'Circle 234, Village', 'FEMALE'),
  ('Víctor', 'Dominguez', 'Sáez', '45678901T', '1989-12-12', 'victor.dominguez@email.com', '+890123456', 'Plaza 567, Hamlet', 'MALE'),
  ('Marina', 'Fernández', 'Vidal', '98765432U', '1991-08-25', 'marina.fernandez@email.com', '+012345678', 'Boulevard 789, City', 'FEMALE'),
  ('Raul', 'López', 'Santana', '23456789V', '1986-06-14', 'raul.lopez@email.com', '+123456789', 'Lane 012, Town', 'MALE'),
  ('Eva', 'González', 'Ortega', '76543210W', '2002-03-07', 'eva.gonzalez@email.com', '+234567890', 'Square 345, Village', 'FEMALE'),
  ('Hector', 'Martínez', 'Sanchez', '34567890X', '1998-12-30', 'hector.martinez@email.com', '+345678901', 'Circle 678, Hamlet', 'MALE'),
  ('Beatriz', 'Gómez', 'Garrido', '01234567Y', '1993-09-19', 'beatriz.gomez@email.com', '+456789012', 'Plaza 890, City', 'FEMALE'),
  ('Mario', 'Sánchez', 'Duran', '87654321Z', '2004-07-02', 'mario.sanchez@email.com', '+567890123', 'Avenue 123, Town', 'MALE'),
  ('Ana', 'Pérez', 'Rojas', '23456789AA', '1997-04-15', 'ana.perez@email.com', '+678901234', 'Boulevard 456, Village', 'FEMALE'),
    ('Gabriel', 'Romero', 'Molina', '12345678AA', '1985-11-11', 'gabriel.romero@email.com', '+234567890', 'Square 789, Village', 'MALE'),
  ('Clara', 'Díaz', 'Vargas', '23456789BB', '2000-10-05', 'clara.diaz@email.com', '+345678901', 'Circle 012, Hamlet', 'FEMALE'),
  ('Francisco', 'García', 'Jiménez', '34567890CC', '1995-05-28', 'francisco.garcia@email.com', '+456789012', 'Plaza 123, City', 'MALE'),
  ('Lorena', 'Martín', 'Soto', '45678901DD', '1990-02-20', 'lorena.martin@email.com', '+567890123', 'Lane 234, Town', 'FEMALE'),
  ('Andres', 'Torres', 'Ferrer', '56789012EE', '2001-01-10', 'andres.torres@email.com', '+678901234', 'Boulevard 567, Village', 'MALE'),
  ('Nuria', 'Navarro', 'Rivas', '67890123FF', '1989-07-22', 'nuria.navarro@email.com', '+789012345', 'Avenue 890, Hamlet', 'FEMALE'),
  ('Alberto', 'Ruiz', 'Salas', '78901234GG', '1994-04-03', 'alberto.ruiz@email.com', '+890123456', 'Street 901, City', 'MALE'),
  ('Sara', 'Vega', 'Esteban', '89012345HH', '2003-09-12', 'sara.vega@email.com', '+901234567', 'Boulevard 234, Town', 'FEMALE'),
  ('Joaquín', 'Hernández', 'Moya', '90123456II', '1987-12-08', 'joaquin.hernandez@email.com', '+012345678', 'Circle 456, Village', 'MALE'),
  ('Lidia', 'Sanz', 'Montes', '01234567JJ', '1996-08-17', 'lidia.sanz@email.com', '+123456789', 'Plaza 789, Hamlet', 'FEMALE'),
  ('Pedro', 'Morales', 'Silvestre', '12345678KK', '2005-06-29', 'pedro.morales@email.com', '+234567890', 'Lane 012, City', 'MALE'),
  ('Alba', 'Ferrer', 'Crespo', '23456789LL', '1984-03-04', 'alba.ferrer@email.com', '+345678901', 'Square 345, Town', 'FEMALE'),
  ('Ivan', 'Jiménez', 'Olivares', '34567890MM', '1999-11-23', 'ivan.jimenez@email.com', '+456789012', 'Avenue 678, Village', 'MALE');




insert into doctor values
  ('00AAA'),
  ('00AAB'),
  ('00AAC'),
  ('00AAD'),
  ('00AAE'),
  ('00AAF'),
  ('00AAG'),
  ('00AAH'),
  ('00AAI'),
  ('00AAJ');