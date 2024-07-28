
 INSERT INTO prendre (
   NumPssrt,
   NumVol
 ) VALUES ( 
    (SELECT NumPssrt FROM passager WHERE PassNom="Gates" AND PassPrenom="Bill"),
     (SELECT NumVol FROM vol WHERE NumVol=3)
   );
