-- Borrador tabla nodes para BOM de Auto (semana 1)
CREATE TABLE IF NOT EXISTS nodes (
    id          VARCHAR(50)  PRIMARY KEY,
    value       VARCHAR(255) NOT NULL,
    parent_id   VARCHAR(50),
    FOREIGN KEY (parent_id) REFERENCES nodes(id) ON DELETE CASCADE
);