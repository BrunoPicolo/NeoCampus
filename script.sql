CREATE DATABASE IF NOT EXIST OLLIVIER_PICOLO_POUJOL_PROJETS5;

CREATE TABLE IF NOT EXISTS Capteurs (
    NomCapteur VARCHAR(30) PRIMARY KEY,
    NomFluide VARCHAR(20) NOT NULL,
    Batiment VARCHAR(20) NOT NULL,
    Etage INTEGER NOT NULL,
    LieuDetails VARCHAR(40) NOT NULL,
    SeuilMin DOUBLE NOT NULL,
    SeuilMax DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS Releves (
    Id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    NomCapteur VARCHAR(30) NOT NULL,
    Valeur DOUBLE NOT NULL,
    DateReleve DATETIME NOT NULL
);
