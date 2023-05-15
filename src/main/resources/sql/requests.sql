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
values
('Врач общей практики', 'Терапевт');

insert into roles(description, title)
values
('Стандарт', 'user');

insert into cabinets(description, number)
values
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
values ('Петр', 'lastname', 'log4', 'pass', 1, 1);

insert into doctors_slots(doctor_id, day_id, slot_id, cabinet_id)
select doctors.id, days.id, slots.id, cabinets.id
from days
    cross join slots
    cross join doctors
    cross join cabinets
where
    days.id = 2
    and
    first_name = 'Петр'
    and
    cabinets.number = 1;

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