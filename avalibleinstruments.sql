SELECT i.instrument_id, i.brand, i.instrument_type, i.instrument_code, i.rental_fee
FROM instrument AS i
LEFT JOIN rental AS r ON i.instrument_id = r.instrument_id AND r.end_date IS NULL
WHERE r.instrument_id IS NULL OR (r.instrument_id IS NOT NULL AND r.end_date IS NOT NULL);
