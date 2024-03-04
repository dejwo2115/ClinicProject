
insert into SPECIALIZATION(name)
values
('Stomatolog'),
('Logopeda'),
('Pediatra');

insert into ADDRESS(country, city, postal_code, address)
values
('Poland', 'Suwalki', '16-400', 'Wolska 15'),
('Poland', 'Suwalki', '16-400','Kowalskiego 5'),
('Poland', 'Suwalki', '16-400','Nowomiejska 15');

insert into DOCTOR(gender, name, surname, birth_date, email, specialization_id)
values
('male', 'Jan', 'Kowalski', '01-01-1985', 'jankowalski@gmail.com', 1),
('male', 'Mariusz', 'Logopedyczny', '02-02-1987', 'mariuszlogopedyczny@gmail.com', 2),
('male', 'Marek', 'Dentystyczny', '04-05-1990', 'marekdentystyczny@gmail.com', 3);

insert into PATIENT(gender, address_id, name, surname, birth_date, email, phone_number)
values
('male', 1 ,'Maciej', 'Wolski', '01-01-1994', 'maciejwolski@gmail.com', '+48 444 333 222'),
('male', 2 ,'Mateusz', 'Czeski', '02-02-1995', 'mateuszczeski@gmail.com', '+48 123 456 678'),
('male', 3 ,'Dariusz', 'Wolny', '03-12-1975', 'dariuszwolny@gmail.com', '+48 543 210 123');

insert into DOCTOR_SPECIALIZATION(doctor_id, specialization_id)
values
(1, 1),
(2, 2),
(3, 3);

insert into DOCTOR_SPECIALIZATION(doctor_id, specialization_id)
values
(3, 1),
(1, 2),
(2, 3);