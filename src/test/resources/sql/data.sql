-- 1. Inserting data into Weighbridge
INSERT INTO weighbridge (id, weighbridge_number)
VALUES
    ('11111111-1111-1111-1111-111111111111', 1),
    ('22222222-2222-2222-2222-222222222222', 2),
    ('33333333-3333-3333-3333-333333333333', 3);

-- 2. Inserting data into Visit
INSERT INTO visit (id, arrival_time, leaving_time, weigh_bridge_id)
VALUES
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2023-09-28T10:15:30+01:00', '2023-09-28T10:45:30+01:00', '11111111-1111-1111-1111-111111111111'),
    ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '2023-09-28T11:15:30+01:00', null, '22222222-2222-2222-2222-222222222222'),
    ('ccccccc3-cccc-cccc-cccc-cccccccccccc', '2023-09-28T12:15:30+01:00', null, '33333333-3333-3333-3333-333333333333');

-- 3. Inserting data into UnloadingRequest
INSERT INTO unloading_request ( id, license_plate,created_at, visit_id,dtype)
VALUES
    ('ddddddd4-dddd-dddd-dddd-dddddddddddd', 'ABC123', '2023-09-28T09:00:00+01:00', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa','UnloadingRequest'),
('eeeeeee5-eeee-eeee-eeee-eeeeeeeeeeee', 'DEF456', '2023-09-28T10:00:00+01:00', 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb','UnloadingRequest'),
('fffffff6-ffff-ffff-ffff-ffffffffffff', 'GHI789', '2023-09-28T11:00:00+01:00', 'ccccccc3-cccc-cccc-cccc-cccccccccccc','UnloadingRequest');
