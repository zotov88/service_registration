insert into specializations(description, title)
values ('Врач общей практики', 'Терапевт'),
       ('Хирург', 'Хирург'),
       ('Кардиолог', 'Кардиолог'),
       ('Эндокринолог', 'Эндокринолог'),
       ('Гинеколог', 'Гинеколог'),
       ('Офтальмолог', 'Офтальмолог'),
       ('Гастроэнтеролог', 'Гастроэнтеролог'),
       ('Невролог', 'Невролог'),
       ('Ультразвуковое исследование', 'УЗИ'),
       ('Магнитно-резонансная томография', 'МРТ');

insert into roles(description, title)
values ('Клиент', 'CLIENT'),
       ('Врач', 'DOCTOR');

insert into cabinets(description, number)
values ('кабинет', 1),
       ('кабинет', 2),
       ('кабинет', 3),
       ('кабинет', 4),
       ('кабинет', 5),
       ('кабинет', 6),
       ('кабинет', 10),
       ('кабинет', 11),
       ('кабинет', 12),
       ('кабинет', 13),
       ('кабинет', 14),
       ('кабинет', 15),
       ('кабинет', 20),
       ('кабинет', 21),
       ('кабинет', 22),
       ('кабинет', 23),
       ('кабинет', 24);

insert into slots(time_slot)
values ('09:00'),
       ('09:30'),
       ('10:00'),
       ('10:30'),
       ('11:00'),
       ('11:30'),
       ('12:00'),
       ('12:30'),
       ('13:00'),
       ('13:30'),
       ('14:00'),
       ('14:30'),
       ('15:00'),
       ('15:30'),
       ('16:00'),
       ('16:30'),
       ('17:00'),
       ('17:30'),
       ('18:00'),
       ('18:30'),
       ('19:00'),
       ('19:30'),
       ('20:00');


insert into days(day)
values ('2023-05-20'),
       ('2023-05-21'),
       ('2023-05-22'),
       ('2023-05-23'),
       ('2023-05-24'),
       ('2023-05-25'),
       ('2023-05-26'),
       ('2023-05-27'),
       ('2023-05-28'),
       ('2023-05-29'),
       ('2023-05-30'),
       ('2023-05-31'),
       ('2023-06-01'),
       ('2023-06-02'),
       ('2023-06-03'),
       ('2023-06-04'),
       ('2023-06-05'),
       ('2023-06-06'),
       ('2023-06-07'),
       ('2023-06-08'),
       ('2023-06-09'),
       ('2023-06-10'),
       ('2023-06-11'),
       ('2023-06-12'),
       ('2023-06-13'),
       ('2023-06-14'),
       ('2023-06-15'),
       ('2023-06-16'),
       ('2023-06-17'),
       ('2023-06-18'),
       ('2023-06-19'),
       ('2023-06-20'),
       ('2023-06-21'),
       ('2023-06-22'),
       ('2023-06-23'),
       ('2023-06-24'),
       ('2023-06-25'),
       ('2023-06-26'),
       ('2023-06-27'),
       ('2023-06-28'),
       ('2023-06-29'),
       ('2023-06-30'),
       ('2023-07-01'),
       ('2023-07-02'),
       ('2023-07-03'),
       ('2023-07-04'),
       ('2023-07-05');

insert into doctors
values (nextval('doctors_seq'), null, now(), null, null, false, null, 'Арина', 'Антонова', 'aad', 'Дмитриевна',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Роберт', 'Михеев', 'rmj', 'Ярославович',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 2),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Михаил', 'Сорокин', 'msn', 'Никитич',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 3),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Алексей', 'Андреев', 'aas', 'Сергеевич',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 4),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Павел', 'Николаев', 'pna', 'Александрович',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 5),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Виктория', 'Баранова', 'vbs', 'Семеновна',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 6),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Дмитрий', 'Соколов', 'dsd', 'Дмитриевич',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 7),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Алина', 'Нефедова', 'ana', 'Адамовна',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 8),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Злата', 'Аникина', 'zav', 'Васильевна',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 9),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Григорий', 'Фролов', 'gfa', 'Александрович',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 10),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Михаил', 'Климов', 'mka', 'Андреевич',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Артемий', 'Антонов', 'aad2', 'Дмитриевич',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 2),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Анастасия', 'Мартынова', 'ama', 'Антоновна',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 3),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Елена', 'Пахомова', 'epa', 'Арсентьевна',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 4),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Ярослав', 'Гончаров', 'jgr', 'Романович',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 5),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Тимофей', 'Гусев', 'tga', 'Александрович',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 6),
       (nextval('doctors_seq'), null, now(), null, null, false, null, 'Леонид', 'Алехин', 'lak', 'Константинович',
        '$2a$10$CgaonkUQqZxPji5K/0kV4uO3AiL0/Dii0fy9JHmFrE/1OK0t2hbOK', 2, 7);

insert into users(login, role)
values ('aad', 2),
       ('rmj', 2),
       ('msn', 2),
       ('aas', 2),
       ('pna', 2),
       ('vbs', 2),
       ('dsd', 2),
       ('ana', 2),
       ('zav', 2),
       ('gfa', 2),
       ('mka', 2),
       ('aad2', 2),
       ('ama', 2),
       ('epa', 2),
       ('jgr', 2),
       ('tga', 2),
       ('lak', 2);



-- select login
-- from (select login
--       from clients
--       union all
--       select login
--       from doctors) as t
-- where login = 'lp';
--
--
-- select dc.id, d.id
-- from doctors_slots ds
--          join days d on ds.day_id = d.id
--          join doctors dc on ds.doctor_id = dc.id
-- where day > TIMESTAMP 'today'
--   and ds.is_registered = false
-- group by dc.id, d.id;
--
-- select *
-- from doctors_slots ds
--          join days d on ds.day_id = d.id
-- where day > TIMESTAMP 'today';
--
-- select d.*
-- from doctors d
--          left join specializations s on d.specialization_id = s.id
-- where last_name like '%'
--   and first_name like '%'
--   and mid_name like '%'
--   and s.title like '%';
--
-- select d.*
-- from doctors d
--          left join specializations s on d.specialization_id = s.id
-- where last_name like '%'
--   and first_name like '%'
--   and mid_name like '%';
--
-- select id, slot_id
-- from doctors_slots
-- where doctor_id = 2
--   and day_id = 3
--   and is_registered = false
--
-- select *
-- from doctors d
--          join doctors_slots ds on d.id = ds.doctor_id
--          join days d2 on ds.day_id = d2.id
--          join slots s on ds.slot_id = s.id
--          join cabinets c on ds.cabinet_id = c.id
--          left join registrations r on ds.id = r.doctor_slot_id
-- where d.id = 4
--   and d2.day > TIMESTAMP 'today'
-- order by d2.day desc
--
-- select r.id
-- from doctors_slots
--          join doctors d on doctors_slots.doctor_id = d.id
--          join registrations r on doctors_slots.id = r.doctor_slot_id
-- where doctor_id = 4
--   and r.is_active = true
--
--
-- select *
-- from doctors_slots;
--
-- select c.id
-- from registrations r
--          join clients c on c.id = r.client_id
-- where r.id = 56;
--
-- select r.id as id, ((now() at time zone 'utc-3') - (d.day + s.time_slot)) as diff
-- from registrations r
--          join doctors_slots ds on r.doctor_slot_id = ds.id
--          join days d on ds.day_id = d.id
--          join slots s on s.id = ds.slot_id
-- where ((now() at time zone 'utc-3') - (d.day + s.time_slot)) > '00:01:00'
--   and r.is_active = true
--
-- select r.*
-- from registrations r
--          join doctors_slots ds on r.doctor_slot_id = ds.id
--          join days d on ds.day_id = d.id
--          join doctors doc on doc.id = ds.doctor_id
-- where doc.id = 1
--   and d.id = 1
--   and ds.is_registered = true;
--
-- select *
-- from registrations;
--
-- update registrations
-- set is_active = false
-- where client_id = 1;
--
-- select *
-- from registrations r
--          join doctors_slots ds on r.doctor_slot_id = ds.id
--          join days d on ds.day_id = d.id
--          join doctors doc on doc.id = ds.doctor_id
-- where doc.id = 1
--   and d.id = 1
--   and r.is_active = false
--   and ds.is_registered = true;
--
-- select r.is_active, ds.is_registered
-- from registrations r
--          join doctors_slots ds on ds.id = r.doctor_slot_id
-- where client_id = 1;
--
--
-- select r.*
-- from registrations r
--          join doctors_slots ds on r.doctor_slot_id = ds.id
--          join days d on ds.day_id = d.id
--          join slots s on s.id = ds.slot_id
-- where ((now() at time zone 'utc-3') - (d.day + s.time_slot)) > '00:01:00'
--   and r.is_active = true;
--
-- select id
-- from days
-- where days.day = date(now());
--
-- select cast(day as text)
-- from days
-- where id = 2;
--
-- select *
-- from days
-- where day < (TIMESTAMP 'today' + (interval '1 days') * 3)
-- order by day

