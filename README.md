# NeoCampus

## Installation

- Installer [WampServer](http://www.wampserver.com/)
    * Suivre les instructions sur le site web

- Création de la base de données
    * Lancer WampServer si ce n'est pas déjà fait
    * Se connecter à PHPMyAdmin dans un navigateur web à l'adresse: localhost/phpmyadmin
        --> Le nom d'utilisateur est "root", laisser le mot de passe vide
    * Sélectionner l'onglet "Importer"
    * Importer le fichier "script.sql"
    * Cliquer sur le bouton "Exécuter" en bas de page pour générer la BD et les tables


## Lancement

- Sous Windows: double-cliquer sur le fichier .jar devrait suffire
- Autre OS: exécuter la commande ```java -jar ollivier_picolo_poujol_projetS5.jar```


## Notes pour la compilation

- Télécharger [MySQL Connector/J](https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.13)
    * Télécharger le fichier .jar
    * Le déplacer dans NeoCampus/project/lib
    * L'ajouter dans le build path d'éclipse

- Télécharger [JFreeChart](https://sourceforge.net/projects/jfreechart/files/)
    * Déplacer jcommon-XXX.jar et jfreechart-XXX.jar dans NeoCampus/project/lib
    * Les ajouter dans le build path d'éclipse

- Télécharger [JDatePicker](https://sourceforge.net/projects/jdatepicker/)
    * Télécharger le fichier .jar
    * Le déplacer dans NeoCampus/project/lib
    * L'ajouter dans le build path d'éclipse
