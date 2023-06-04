insert into specializations(description, title)
values ('Врач общей практики', 'Терапевт'),
       ('Хирург', 'Хирург'),
       ('Кардиолог', 'Кардиолог'),
       ('Эндокринолог', 'Эндокринолог'),
       ('Гинеколог', 'Гинеколог'),
       ('Офтальмолог', 'Офтальмолог'),
       ('Гастроэнтеролог', 'Гастроэнтеролог');

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

-- insert into slots(time_slot)
-- values ('09:00'),
--        ('09:30'),
--        ('10:00'),
--        ('10:30'),
--        ('11:00'),
--        ('11:30'),
--        ('12:00'),
--        ('12:30'),
--        ('13:00'),
--        ('13:30'),
--        ('14:00'),
--        ('14:30'),
--        ('15:00'),
--        ('15:30'),
--        ('16:00'),
--        ('16:30'),
--        ('17:00'),
--        ('17:30'),
--        ('18:00'),
--        ('18:30'),
--        ('19:00'),
--        ('19:30'),
--        ('20:00'),
--        ('20:30'),
--        ('21:00'),
--        ('21:30'),
--        ('22:00'),
--        ('22:30'),
--        ('23:00'),
--        ('23:30');

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
       ('21:30'),
       ('21:30'),
       ('21:45'),
       ('22:00'),
       ('22:15'),
       ('22:30'),
       ('22:45'),
       ('23:00'),
       ('23:15'),
       ('23:30'),
       ('23:45');




insert into days(day)
values ('2023-06-02'),
       ('2023-06-03'),
       ('2023-06-04'),
       ('2023-06-05'),
       ('2023-06-06'),
       ('2023-06-07'),
       ('2023-06-08'),
       ('2023-06-09'),
       ('2023-06-10'),
       ('2023-06-11');


insert into doctors
values (nextval('doctors_seq'), null, now(), null, null, false, 'Петр', 'Иванов', 'l', 'Иванович', 'p', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Семен', 'Семенов', 'lщ', 'Иванович', 'p', 2, 2),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Иван', 'Петров', 'lр', 'Иванович', 'p', 2, 1);

insert into clients
values (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', now(), 'as1d2@sf.ru', 'Алексей', 'm', 'Зотов', 'login', 'Владимирович', 'pass', '89031103775',
        12345, 1),
       (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', now(), 'vs1d2@sf.ru', 'Иван', 'm', 'Иванов', 'login2', 'Владимирович', 'pass', '89131103765',
        16345, 1);

insert into doctors_slots
select nextval('doctor_slot_seq'),
       null,
       now(),
       null,
       null,
       null,
       false,
       cabinets.id,
       days.id,
       doctors.id,
       slots.id
from days
         cross join slots
         cross join doctors
         cross join cabinets
where days.id = 1
  and doctors.id = 1
  and cabinets.id = 1;

insert into registrations
values (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 3);
--        (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 2),
--        (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 3);

update doctors_slots
set is_registered = true
where id = 3;

select login
from (select login
      from clients
      union all
      select login
      from doctors) as t
where login = 'lp';


select dc.id, d.id
from doctors_slots ds
         join days d on ds.day_id = d.id
         join doctors dc on ds.doctor_id = dc.id
where day > TIMESTAMP 'today'
  and ds.is_registered = false
group by dc.id, d.id;

select *
from doctors_slots ds
         join days d on ds.day_id = d.id
where day > TIMESTAMP 'today';

select d.*
from doctors d
         left join specializations s on d.specialization_id = s.id
where last_name like '%'
  and first_name like '%'
  and mid_name like '%'
  and s.title like '%';

select d.*
from doctors d
         left join specializations s on d.specialization_id = s.id
where last_name like '%'
  and first_name like '%'
  and mid_name like '%';

select id, slot_id
from doctors_slots
where doctor_id = 2
  and day_id = 3
  and is_registered = false

select *
from doctors d
         join doctors_slots ds on d.id = ds.doctor_id
         join days d2 on ds.day_id = d2.id
         join slots s on ds.slot_id = s.id
         join cabinets c on ds.cabinet_id = c.id
         left join registrations r on ds.id = r.doctor_slot_id
where d.id = 4
  and d2.day > TIMESTAMP 'today'
order by d2.day desc

select r.id
from doctors_slots
         join doctors d on doctors_slots.doctor_id = d.id
         join registrations r on doctors_slots.id = r.doctor_slot_id
where doctor_id = 4
  and r.is_active = true


select *
from doctors_slots;

select c.id
from registrations r
         join clients c on c.id = r.client_id
where r.id = 56;

select r.id as id, ((now() at time zone 'utc-3') - (d.day + s.time_slot)) as diff
from registrations r
         join doctors_slots ds on r.doctor_slot_id = ds.id
         join days d on ds.day_id = d.id
         join slots s on s.id = ds.slot_id
where ((now() at time zone 'utc-3') - (d.day + s.time_slot)) > '00:01:00'
  and r.is_active = true

select r.*
from registrations r
         join doctors_slots ds on r.doctor_slot_id = ds.id
         join days d on ds.day_id = d.id
         join doctors doc on doc.id = ds.doctor_id
where doc.id = 1
  and d.id = 1
  and ds.is_registered = true;

select *
from registrations;

update registrations
set is_active = false
where client_id = 1;

select *
from registrations r
         join doctors_slots ds on r.doctor_slot_id = ds.id
         join days d on ds.day_id = d.id
         join doctors doc on doc.id = ds.doctor_id
where doc.id = 1
  and d.id = 1
  and r.is_active = false
  and ds.is_registered = true;

select r.is_active, ds.is_registered
from registrations r
         join doctors_slots ds on ds.id = r.doctor_slot_id
where client_id = 1;


select r.*
from registrations r
         join doctors_slots ds on r.doctor_slot_id = ds.id
         join days d on ds.day_id = d.id
         join slots s on s.id = ds.slot_id
where ((now() at time zone 'utc-3') - (d.day + s.time_slot)) > '00:01:00'
  and r.is_active = true;

select id
from days
where days.day = date(now());

select cast(day as text)
from days
where id = 2;

select *
from days
where day < (TIMESTAMP 'today' + (interval '1 days') * 3)
order by day

