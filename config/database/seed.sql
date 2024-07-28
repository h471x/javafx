---FOR ADMIN TABLE ---
INSERT IGNORE INTO admin (
  AdmnNom,
  AdmnPrenom,
  UserName,
  Mdp
) VALUES (
  'Admin1', '', 'admin', '123password!'
);

--  FOR VOL TABLE --
INSERT IGNORE INTO vol (
  Avion,
  Origine,
  Destination,
  Decolage
) VALUES (
  'Boeing 737', 'Paris', 'New York', NOW()
);

INSERT IGNORE INTO vol (
  Avion,
  Origine,
  Destination,
  Decolage
) VALUES (
  'Airbus A380', 'New York', 'Tokyo', NOW()
);

INSERT IGNORE INTO vol (
  Avion,
  Origine,
  Destination,
  Decolage
) VALUES (
  'Airbus A380', 'Californie', 'Abidjan', '2024-10-25 20:33:00'
);

--FOR PASSAGER TABLE --
INSERT IGNORE INTO passager (
  NumPssrt,
  PassNom,
  PassPrenom,
  Naissance,
  Ist,
  Notam
) VALUES (
  '0193486323', 'Gates', 'Bill', '1955-10-28', TRUE, FALSE
);

INSERT IGNORE INTO passager (
  NumPssrt,
  PassNom,
  PassPrenom,
  Naissance
) VALUES (
  '9193486323', 'Torvalds', 'Linus', '1969-12-28'
);

--FOR PRENDRE TABLE --
INSERT IGNORE INTO prendre (
  NumPssrt,
  NumVol
) VALUES (
  '9193486323', 1
);

INSERT IGNORE INTO prendre (
  NumPssrt,
  NumVol
) VALUES (
  '9193486323', 2
);
