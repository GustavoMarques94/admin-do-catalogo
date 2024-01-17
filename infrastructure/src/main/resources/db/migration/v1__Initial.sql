
CREATE TABLE category(
    id VARCHAR(36) NOT NULL PRIMARY KEY, --PK sinaliza que precisa criar uma UNIQUE, uma UNIQUE já é um index, então não é necessário criar um index
    name VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL
);