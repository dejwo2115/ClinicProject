ALTER TABLE patient
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES clinic_user (user_id);

ALTER TABLE doctor
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES clinic_user (user_id);

insert into clinic_user (user_name, email, password, active) values ('maciej_wolski', 'maciejwolski@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (user_name, email, password, active) values ('mateusz_czeski', 'mateuszczeski@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (user_name, email, password, active) values ('dariusz_wolny', 'dariuszwolny@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into clinic_user (user_name, email, password, active) values ('jan_kowalski', 'jankowalski@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (user_name, email, password, active) values ('mariusz_logopedyczny', 'mariuszlogopedyczny@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (user_name, email, password, active) values ('marek_dentystyczny', 'marekdentystyczny@gmail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into clinic_user (user_name, email, password, active) values ('test_user', 'testuser@mail.com', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);


UPDATE patient SET user_id = 1 WHERE email = 'maciejwolski@gmail.com';
UPDATE patient SET user_id = 2 WHERE email = 'mateuszczeski@gmail.com';
UPDATE patient SET user_id = 3 WHERE email = 'dariuszwolny@gmail.com';

UPDATE doctor SET user_id = 4 WHERE email = 'jankowalski@gmail.com';
UPDATE doctor SET user_id = 5 WHERE email = 'mariuszlogopedyczny@gmail.com';
UPDATE doctor SET user_id = 6 WHERE email = 'marekdentystyczny@gmail.com';

insert into clinic_role (role_id, role) values (1, 'PATIENT'), (2, 'DOCTOR'), (3, 'REST_API');

insert into clinic_user_role (user_id, role_id) values (1, 1), (2, 1), (3, 1);
insert into clinic_user_role (user_id, role_id) values (4, 2), (5, 2), (6, 2);
insert into clinic_user_role (user_id, role_id) values (7, 3);

ALTER TABLE patient
ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE doctor
ALTER COLUMN user_id SET NOT NULL;