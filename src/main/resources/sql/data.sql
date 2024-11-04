INSERT INTO weighbridge (weighbridge_number, id)
VALUES (1, '11111111-1111-1111-1111-111111111111');
INSERT INTO vendor (id, name)
VALUES ('11111111-1111-1111-1111-111111111111', 'Acme Supplies');


INSERT INTO resource (id, name)
VALUES ('11111111-1111-1111-1111-111111111112', 'Gips');

INSERT INTO visit (id, arrival_time)
VALUES ('11111111-1111-1111-1111-111111111115',
        '2024-10-09T12:30:00+00:00');

INSERT INTO unloading_request (id, license_plate, created_at, visit_id, resource_id, vendor_id, dtype,
                               start_of_timeslot, end_of_timeslot)
VALUES ('11111111-1111-1111-1111-111111111113',
        'ABC123',
        '2024-10-09T12:30:00+00:00',
        '11111111-1111-1111-1111-111111111115',
        '11111111-1111-1111-1111-111111111112', -- resource_id (UUID of related resource entity)
        '11111111-1111-1111-1111-111111111111',
        'UnloadingAppointment',
        '2023-09-28T10:15:30+01:00',
        '2023-09-28T11:15:30+01:00');

INSERT INTO weighbridge (id, weighbridge_number)
VALUES ('22222222-2222-2222-2222-222222222222', 2),
       ('33333333-3333-3333-3333-333333333333', 3);

INSERT INTO visit (id, arrival_time, leaving_time, weigh_bridge_id)
VALUES ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2023-09-28T10:15:30+01:00', '2023-09-28T10:45:30+01:00',
        '11111111-1111-1111-1111-111111111111'),
       ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '2023-09-28T11:15:30+01:00', null,
        '22222222-2222-2222-2222-222222222222'),
       ('ccccccc3-cccc-cccc-cccc-cccccccccccc', '2023-09-28T12:15:30+01:00', null,
        '33333333-3333-3333-3333-333333333333');

INSERT INTO unloading_request (id, license_plate, created_at, visit_id, dtype, start_of_timeslot, end_of_timeslot)
VALUES ('eeeeeee5-eeee-eeee-eeee-eeeeeeeeeeee', 'DEF456', '2023-09-28T10:00:00+01:00',
        'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'UnloadingAppointment', '2023-09-28T11:00:00+01:00',
        '2023-09-28T12:00:00+01:00'),
       ('fffffff6-ffff-ffff-ffff-ffffffffffff', 'GHI789', '2023-09-28T11:00:00+01:00',
        'ccccccc3-cccc-cccc-cccc-cccccccccccc', 'UnloadingAppointment', '2023-09-28T12:00:00+01:00',
        '2023-09-28T13:00:00+01:00'),
       ('12345675-abcd-4abc-8def-9876543210ff', 'HJD458', '2023-09-28T10:00:00+01:00',
        'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'UnloadingAppointment', '2023-09-28T11:00:30+01:00',
        '2023-09-28T12:00:30+01:00');

INSERT INTO unloading_request (created_at, id, visit_id, dtype, license_plate, start_of_timeslot, end_of_timeslot)
VALUES ('2024-10-10T07:00:00+00:00', '3ec8149c-87f4-42d5-9b87-bc6a51b2e8ee', NULL, 'UnloadingAppointment', '1-ABC-123',
        '2024-10-10T08:00:00+00:00', '2024-10-10T09:00:00+00:00'),
       ('2024-10-10T09:30:00+00:00', '7338b1ae-d7da-4094-8e69-8c8c25b5e2bc', NULL, 'UnloadingAppointment', '2-XYZ-987',
        '2024-10-10T10:00:00+00:00', '2024-10-10T11:00:00+00:00'),
       ('2024-10-11T08:15:00+00:00', '9c792d2f-bf41-4ff4-b1de-b0ef64255f1f', NULL, 'UnloadingAppointment', '3-DEF-456',
        '2024-10-11T09:00:00+00:00', '2024-10-11T10:00:00+00:00'),
       ('2024-10-11T10:00:00+00:00', 'de3b3fc9-58b0-4e2c-8b12-00eb1bde82d3', NULL, 'UnloadingAppointment', '4-GHI-789',
        '2024-10-11T11:00:00+00:00', '2024-10-11T12:00:00+00:00'),
       ('2024-10-12T12:45:00+00:00', '7e988b9c-fb09-43ae-956b-14ab9d32de0b', NULL, 'UnloadingAppointment', '5-JKL-101',
        '2024-10-12T13:00:00+00:00', '2024-10-12T14:00:00+00:00');
INSERT INTO unloading_request (created_at, id, visit_id, dtype, license_plate)
VALUES ('2024-10-10T07:00:00+00:00', 'f1ff76c3-5f67-4324-9470-5883d3f65825', NULL, 'UnloadingWithoutAppointment',
        '6-MNO-202'),
       ('2024-10-10T09:30:00+00:00', 'd11036c4-e754-48e8-8f92-d62cb33a7a08', NULL, 'UnloadingWithoutAppointment',
        '7-PQR-303'),
       ('2024-10-11T08:15:00+00:00', 'bc9b4a7a-72b8-486c-b89c-048907dce953', NULL, 'UnloadingWithoutAppointment',
        '8-STU-404'),
       ('2024-10-11T10:00:00+00:00', '81daffbc-0837-414e-b9d6-843cc0dc5e0e', NULL, 'UnloadingWithoutAppointment',
        '9-VWX-505'),
       ('2024-10-12T12:45:00+00:00', '1f063a36-6889-405f-81bc-3dbaae85b4e0', NULL, 'UnloadingWithoutAppointment',
        '0-YZB-606');


-- Vendor Table
INSERT INTO vendor (id, name)
VALUES ('22222222-2222-2222-2222-222222222222', 'Global Logistics');

-- Resource Table
INSERT INTO resource (id, name)
VALUES ('22222222-2222-2222-2222-222222222223', 'Goud');

INSERT INTO weighbridge_ticket (id, start_weight_amount_in_ton, start_weight_timestamp, end_weight_amount_in_ton,
                                end_weight_timestamp)
VALUES ('33333333-3333-3333-3333-333333333333', -- id (UUID)
        100, -- start_weight_amount_in_ton
        '2024-10-14T12:30:00+00:00', -- start_weight_timestamp
        NULL, -- end_weight_amount_in_ton (NULL because endWeight is missing)
        NULL);

-- Visit Table
INSERT INTO visit (id, arrival_time, weighbridge_ticket_id)
VALUES ('22222222-2222-2222-2222-222222222225',
        '2024-11-15T14:45:00+00:00',
        '33333333-3333-3333-3333-333333333333');

-- Unloading Request Table
INSERT INTO unloading_request (id,start_of_timeslot,end_of_timeslot, license_plate, created_at, visit_id, resource_id, vendor_id, dtype)
VALUES ('22222222-2222-2222-2222-222222222224',
        '2024-10-10T08:00:00+00:00',
        '2024-10-10T09:00:00+00:00',
        'XYZ789',
        '2024-11-15T14:45:00+00:00',
        '22222222-2222-2222-2222-222222222225', -- visit_id
        '22222222-2222-2222-2222-222222222223', -- resource_id
        '22222222-2222-2222-2222-222222222222', -- vendor_id
        'UnloadingAppointment');

INSERT INTO unloading_request (id,start_of_timeslot,end_of_timeslot, license_plate, created_at, resource_id, vendor_id, dtype)
VALUES ('22222222-0202-2222-2222-222222222224',
        '2024-11-04T03:00:00+00:00',
        '2024-11-04T04:00:00+00:00',
        'BARBIE',
        null,
        '22222222-2222-2222-2222-222222222223', -- resource_id
        '22222222-2222-2222-2222-222222222222', -- vendor_id
        'UnloadingAppointment');

