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

insert into slots(time_slot)
values
('09:00'),
('10:00'),
('11:00'),
('12:00');

insert into days(day)
values
    ('2023-05-01'),
    ('2023-07-01'),
    ('2023-07-02');


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
values (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 3);
--        (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 2),
--        (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 3);

update doctors_slots
set is_registered = true
where id = 3;

select login
from (
        select login from clients
        union all
        select login from doctors
    ) as t
where login = 'lp';


select dc.id, d.id
from doctors_slots ds
    join days d on ds.day_id = d.id
    join doctors dc on ds.doctor_id = dc.id
where day > TIMESTAMP 'today' and ds.is_registered = false
group by dc.id, d.id;

select *
from doctors_slots ds
         join days d on ds.day_id = d.id
where day > TIMESTAMP 'today';

select d.*
from doctors d left join specializations s on d.specialization_id = s.id
where last_name like '%'
and first_name like '%'
and mid_name like '%'
and s.title like '%';

select d.*
from doctors d left join specializations s on d.specialization_id = s.id
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




