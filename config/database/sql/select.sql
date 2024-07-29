
 ---Admin Tab--
 SELECT Username AS "Nom d'utilisateur",AdmnNom AS Nom,AdmnPrenom AS Prenom FROM admin;

 --Notam Passager ---
 SELECT NumPssrt AS Passeport,PassNom AS Nom ,PassPrenom AS Prenom ,Naissance AS "Date de naissance" FROM passager WHERE Notam=TRUE ORDER BY PassNom;
 --Iste Passager --
 SELECT NumPssrt AS Passeport,PassNom AS Nom ,PassPrenom AS Prenom ,Naissance AS "Date de naissance" FROM passager WHERE =TRUE ORDER BY PassNom;
--Simple Passager--
 SELECT NumPssrt AS Passeport,PassNom AS Nom ,PassPrenom AS Prenom ,Naissance AS "Date de naissance" FROM passager WHERE Notam=FALSE AND Ist=FALSE ORDER BY PassNom;

 --Select Vol --
SELECT NumVol AS Numero,Origine AS Provenance,Destination AS Destinatioin ,Avion AS Avion,DATE_FORMAT(Decolage, '%Y-%m-%d %H:%i:%s') AS Decolage FROM vol WHERE Decolage >= NOW() ORDER BY Decolage ASC;


