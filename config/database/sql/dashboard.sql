SELECT Pr.NumVol AS "Numero du vol",V.Origine AS Provenance,V.Destination AS Destination,V.Decolage AS Decolage , COUNT(P.PassNom) AS "Nombre de passager" FROM prende AS Pr
INNER JOIN vol AS V 
ON Pr.NumVol=V.NumVol
INNER JOIN passager AS P
ON Pr.NumPssrt=P.NumPssrt
GROUP BY V.NumVol;

