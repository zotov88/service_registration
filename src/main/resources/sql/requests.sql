select * from doctors_slots;

select * from doctors;

select * from specializations;

select * from days;

select * from slots;

select * from cabinets;

select * from clients;

select * from roles;

select * from registrations;

insert into specializations(description, title)
values ('Врач общей практики', 'Терапевт'),
       ('Хирург', 'Хирург');

insert into roles(description, title)
values ('Клиент', 'CLIENT'),
       ('Врач', 'DOCTOR');

insert into cabinets(description, number)
values ('Палата', 1),
       ('Палата', 13);

insert into slots(time_slot)
values
('09:00'),
('10:00'),
('11:00'),
('12:00');

insert into days(day)
values
    ('01-06-2023'),
    ('02-06-2023'),
    ('03-06-2023'),
    ('04-06-2023');

insert into doctors
values (nextval('doctors_seq'), null, now(), null, null, false, 'Петр', 'Иванов', 'l', 'Иванович', 'p', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Семен', 'Семенов', 'lщ', 'Иванович', 'p', 2, 2),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Иван', 'Петров', 'lр', 'Иванович', 'p', 2, 1);

insert into clients
values (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', now(), 'as1d2@sf.ru', 'Алексей', 'm', 'Зотов', 'login', 'Владимирович', 'pass', '89031103775', 12345, 1),
       (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', now(), 'vs1d2@sf.ru', 'Иван', 'm', 'Иванов', 'login2', 'Владимирович', 'pass', '89131103765', 16345, 1);

insert into doctors_slots
select nextval('doctor_slot_seq'), null, now(), null, null, null, false,cabinets.id, days.id, doctors.id, slots.id
from days
    cross join slots
    cross join doctors
    cross join cabinets
where
    days.id = 1
    and
    doctors.id = 1
    and
    cabinets.id = 1;

insert into registrations
values (nextval('registrations_seq'), null, now(), null, null, false, null, false, 1, 2, 1),
       (nextval('registrations_seq'), null, now(), null, null, false, null, false, 1, 2, 6),
       (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 11);

delete from doctors_slots
where
    doctor_id = 2
and
    day_id = 2;

delete from doctors
where id = 1;