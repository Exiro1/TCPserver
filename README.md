# TCPserver
PV3e Serveur

Ce programme est le serveur TCP qui fera la liaison entre le raspberry et les PC.

Pré-requis :

Pour faire fonctionner le programme, vous devez avoir un serveur (vps ou autre) sur lequel est installé openJDK 11.
tuto pour debian : https://www.tecmint.com/install-java-on-debian-10/
(utiliser un client ssh comme PUTTY pour vous connecter au serveur distant)

Installation :

Apres avoir installer openJDK 11, il suffit d'envoyer le fichier .jar situé dans le dossier build sur votre serveur (avec Filezilla par exemple)


Démarrage :

Il vous faut ensuite lancer le programme, pour cela on va utiliser putty (mais n'importe quel autre client ssh fera l'affaire)
Connectez vous au serveur puis taper simplement cette commande (en remplaçant PATH par le chemin de votre .jar) :

  java -jar PATH/TCPserver.jar
  
Vous pouvez aussi spécifié un Port (en remplaçant XXXXX par le port souhaité):

  java -jar PATH/TCPserver.jar XXXXX

Le port par defaut est 11000.


Auteurs :

Corentin Generet alias Exiro
