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
values
('Стандарт', 'user');

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

insert into doctors(first_name, last_name, login, password, specialization_id, role_id)
values ('Иван', 'Петров', 'log431', 'pass', 1, 1),
       ('Петр', 'Иванов', 'log4312', 'pass', 2, 1);

insert into clients(birth_date, age, email, phone, address, gender, policy, first_name, last_name, login, password, role_id)
values (now(), 34, 'as1d2@sf.ru', '89031103775', 'Изм проспект', 'м', 153435, 'Сергей', 'lastname1', 'lыog422', 'pass', 1);

insert into doctors_slots(doctor_id, day_id, slot_id, cabinet_id)
select doctors.id, days.id, slots.id, cabinets.id
from days
    cross join slots
    cross join doctors
    cross join cabinets
where
    days.id = 4
    and
    doctors.id = 2
    and
    cabinets.id = 2;

insert into doctors_slots(doctor_id, day_id, slot_id, cabinet_id)
select doctors.id, days.id, slots.id, cabinets.id
from days
         cross join slots
         cross join doctors
         cross join cabinets
where
        day = '2023-03-06'
  and
        first_name = 'Иван'
  and
        cabinets.number = 1;