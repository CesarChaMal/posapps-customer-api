-- #1
-- DELETE FROM ADDRESS
--WHERE id in (
--    SELECT address_id
--    FROM Customer
--    WHERE address_id IS NOT null
--    AND emailAddress IS null
--);

--#2
DELETE FROM Device
    WHERE id in (
    SELECT devices_id FROM Customer_Device WHERE Customer_id IN (
        SELECT id  FROM Customer
        WHERE emailAddress IS null
    )
);

--#3
DELETE FROM Customer_Device
    WHERE Customer_id IN (
    SELECT id  FROM Customer
    WHERE emailAddress is null
);

--#4
DELETE FROM Customer
WHERE emailAddress IS null;

