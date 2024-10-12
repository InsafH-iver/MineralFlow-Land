INSERT INTO weighbridge (weighbridge_number, id)
VALUES (1,'11111111-1111-1111-1111-111111111111');
INSERT INTO vendor (id, name)
VALUES ('11111111-1111-1111-1111-111111111111', 'Acme Supplies');


INSERT INTO resource (id, name)
VALUES ('11111111-1111-1111-1111-111111111112', 'Warehouse A');

INSERT INTO visit (id, arrival_time)
VALUES ('11111111-1111-1111-1111-111111111115',
        '2024-10-09T12:30:00+00:00');

INSERT INTO unloading_request (id, license_plate, created_at, visit_id, resource_id, vendor_id, dtype)
VALUES ('11111111-1111-1111-1111-111111111113',
        'ABC123',
        '2024-10-09T12:30:00+00:00',
        '11111111-1111-1111-1111-111111111115',
        '11111111-1111-1111-1111-111111111112', -- resource_id (UUID of related resource entity)
        '11111111-1111-1111-1111-111111111111',
        'UnloadingAppointment');
