---FOR ADMIN TABLE ---
INSERT INTO admin (
  AdmnNom ,
  AdmnPrenom,
  UserName,
  Mdp
) VALUES ( 'Admin1','','admin','123password!' );

--  FOR VOL TABLE --
INSERT INTO vol (
  Avion,
  Origine,
  Destination,
  DateVol 
) VALUES ( "Boeing 737","Paris","New York",NOW());

INSERT INTO vol (
  Avion,
  Origine,
  Destination,
  DateVol 
) VALUES ( "Airbus A380","New York","Tokyo",NOW());

INSERT INTO vol (
  Avion,
  Origine,
  Destination,
  DateVol 
) VALUES ( "Airbus A380","Californie","Abidjan","2024-10-25 20:33:00");

--FOR PASSAGER TABLE --
INSERT INTO passager (
  NumPssrt,
  PassNom ,
  PassPrenom,
  Naissance,
  Iste,
  Notam
) VALUES ( "0193486323","Gates","Bill","1955-10-28" ,TRUE,FALSE);

INSERT INTO passager (
  NumPssrt,
  PassNom ,
  PassPrenom,
  Naissance
) VALUES ( "9193486323","Torvalds","Linus","1969-12-28" );

--FOR PRENDRE TABLE --
INSERT INTO prendre (
  NumPssrt ,
  NumVol
) VALUES (  "9193486323",1);

INSERT INTO prendre (
  NumPssrt ,
  NumVol
) VALUES (  "9193486323",2);

