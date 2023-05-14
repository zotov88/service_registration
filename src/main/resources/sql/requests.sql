select * from doctors_slots;

select * from doctors;

select * from specializations;

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
('Палата', 1);

insert into slots(time_slot)
values
('2023-04-25 09:00'),
('2023-04-25 10:00'),
('2023-04-25 11:00'),
('2023-04-25 12:00');