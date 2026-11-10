CREATE INDEX idx_bookings_resource_starts ON bookings (resource_id, starts_at);

CREATE INDEX idx_bookings_user ON bookings (user_id);