-- liquibase formatted sql

-- changeset loulou1945:1
CREATE INDEX student_name_index ON student (name);

-- changeset loulou1945:2
CREATE INDEX faculty_name_color_index ON faculty (name, color);