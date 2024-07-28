--INSERT --
  --Admin --

INSERT INTO admin (
  AdmnNom ,
  AdmnPrenom,
  UserName,
  Mdp
) VALUES ( '','','','' );

 --Vol--

INSERT INTO vol (
  Avion,
  Origine,
  Destination,
  DateVol 
) VALUES ( "","","","");

--Passager--

INSERT INTO passager (
  NumPssrt,
  PassNom ,
  PassPrenom,
  Naissance,
  Iste,
  Notam
) VALUES ( "","","","" ,,);

--Prendre--

INSERT INTO prendre (
  NumPssrt ,
  NumVol
) VALUES ( "",);

--Delete

DELETE FROM tableName
  WHERE condition;

--Update --
 UPDATE tableName
  SET attribute(s)
  WHERE condition; 

  --Iste--
UPDATE passager
  SET Iste=TRUE
  WHERE condition;
 --Notam-- 
UPDATE passager
  SET Notam=TRUE
  WHERE condition;


  --Iste--
UPDATE passager
  SET Iste=FALSE
  WHERE condition;
 --Notam-- 
UPDATE passager
  SET Notam=FALSE
  WHERE condition;
