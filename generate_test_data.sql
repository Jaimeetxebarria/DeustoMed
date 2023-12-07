truncate person cascade;
drop sequence user_id_sequence;
create sequence
  user_id_sequence as int;

insert into person (name, surname1, surname2, birthdate, sex) values
  ('Elena', 'García', 'Martínez', '1990-05-15', 'FEMALE'),
  ('Adrián', 'Rodríguez', 'Fernández', '1985-10-20', 'MALE'),
  ('Lucía', 'Martínez', 'López', '2000-12-03', 'FEMALE'),
  ('Diego', 'Pérez', 'Sánchez', '1995-03-25', 'MALE'),
  ('Sara', 'Gómez', 'Hernández', '1988-08-18', 'FEMALE'),
  ('Javier', 'Ruiz', 'González', '1992-11-29', 'MALE'),
  ('María', 'Serrano', 'Díaz', '1986-07-12', 'FEMALE'),
  ('Carlos', 'Hernández', 'Muñoz', '2002-02-10', 'MALE'),
  ('Laura', 'Díaz', 'Romero', '1998-09-05', 'FEMALE'),
  ('Alejandro', 'Martín', 'Pérez', '1993-06-28', 'MALE'),
  ('Carmen', 'Torres', 'García', '1983-04-14', 'FEMALE'),
  ('Pablo', 'Flores', 'Navarro', '2005-01-07', 'MALE'),
  ('Inés', 'Sánchez', 'Morales', '1997-12-22', 'FEMALE'),
  ('Jorge', 'Vázquez', 'Jiménez', '1987-10-03', 'MALE'),
  ('Clara', 'Gutiérrez', 'Silva', '1994-09-17', 'FEMALE'),
  ('Fernando', 'Iglesias', 'Torres', '2004-08-09', 'MALE'),
  ('Sofía', 'Román', 'Castro', '1984-06-02', 'FEMALE'),
  ('Martín', 'Ortega', 'Molina', '1996-04-27', 'MALE'),
  ('Luisa', 'Navarro', 'Pascual', '2001-03-19', 'FEMALE'),
  ('Víctor', 'Dominguez', 'Sáez', '1989-12-12', 'MALE'),
  ('Marina', 'Fernández', 'Vidal', '1991-08-25', 'FEMALE'),
  ('Raul', 'López', 'Santana', '1986-06-14', 'MALE'),
  ('Eva', 'González', 'Ortega', '2002-03-07', 'FEMALE'),
  ('Hector', 'Martínez', 'Sanchez', '1998-12-30', 'MALE'),
  ('Beatriz', 'Gómez', 'Garrido', '1993-09-19', 'FEMALE'),
  ('Mario', 'Sánchez', 'Duran', '2004-07-02', 'MALE'),
  ('Ana', 'Pérez', 'Rojas', '1997-04-15', 'FEMALE'),
  ('Gabriel', 'Romero', 'Molina', '1985-11-11', 'MALE'),
  ('Clara', 'Díaz', 'Vargas', '2000-10-05', 'FEMALE'),
  ('Francisco', 'García', 'Jiménez', '1995-05-28', 'MALE'),
  ('Lorena', 'Martín', 'Soto', '1990-02-20', 'FEMALE'),
  ('Andres', 'Torres', 'Ferrer', '2001-01-10', 'MALE'),
  ('Nuria', 'Navarro', 'Rivas', '1989-07-22', 'FEMALE'),
  ('Alberto', 'Ruiz', 'Salas', '1994-04-03', 'MALE'),
  ('Sara', 'Vega', 'Esteban', '2003-09-12', 'FEMALE'),
  ('Joaquín', 'Hernández', 'Moya', '1987-12-08', 'MALE'),
  ('Lidia', 'Sanz', 'Montes', '1996-08-17', 'FEMALE'),
  ('Pedro', 'Morales', 'Silvestre', '2005-06-29', 'MALE'),
  ('Alba', 'Ferrer', 'Crespo', '1984-03-04', 'FEMALE'),
  ('Ivan', 'Jiménez', 'Olivares', '1999-11-23', 'MALE');


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