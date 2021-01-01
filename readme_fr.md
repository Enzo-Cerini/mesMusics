# Document de conception du projet *mesMusics*

## Description de l'application

L’application MES Musics est une application de musique. 
Elle regroupe toutes les musiques présentes dans le téléphone de l’utilisateur. 
De plus, les capteurs de mouvement du téléphone permettront de contrôler la lecture des musiques. 
L’application affichera notamment les informations de chaque musique (artiste, titre, album…) grâce à une base de données en ligne.
L’utilisateur peuvent gérer la lecture en mettant « lecture », « pause », « suivant » ou « précédent ». 
De plus, il a également la possibilité de contrôler celle-ci grâce aux capteurs de mouvements :
  *	Deux tapes sur l’arrière du téléphone pour activer/désactiver la lecture.
  *	Un mouvement du téléphone vers l’avant pour passer à la musique suivante.
  *	Un mouvement du téléphone vers l’arrière pour revenir à la musique précédente.

L’utilisateur peut créer ses playlists ou utiliser celle crée automatiquement par l’application. 
Il pourra également utiliser l’application en arrière-plan ou profiter de celle-ci avec le téléphone verrouillé.

## But de l'application
  *	Ecouter de la musique (arrière-plan, premier-plan et téléphone vérrouillé)
  * Regroupez toute la musique contenue dans le téléphone.
  *	Afficher tous les détails des chansons (à partir d'une base de données en ligne).
  *	Utilisez des détecteurs de mouvement pour contrôler la lecture.
  *	Créer des playlists.

## Activités
Nous possèdons une classe activité nommée **MainActivity** qui hérite de la classe **AppCompatActivity** et qui implémente l'interface **MediaController.MediaPlayerControl**.
Dans cette classe, 
La classe **MainActivity** est l'élement principal de notre application Android.
Les activités sont lancées et rassemblées edans cette classe.
Nous allons donc détailler les principales méthodes de cette classe.

### La procédure **_onRequestPermissionsResult()_**
Cette procédure nous permet de demander la permission de lire le stockage externe.
Ainsi, si celle-ci n'est pas déja accepté, une demande apparait au lancemement de l'application **MES Musics**

### La procédure **_onCreate()_**
Cette procédure nous permet de créer l'instance de notre application.

### La procédure **_onStart()_**
Cette procédure nous permet de mettre en marche notre application.

## Intentions
Notre applications se décompose en activités distinctes les unes des autres. 
Nos méthodes dans notre **MainActivity** doivent pouvoir s'enchaîner, s'appeler, retourner à leur activité principale. 
C'est pourquoi il faut mettre en place cette communication entres-elles en utilisant des **intentions**.
Celles-ci permettent d'envoyer des messages d'une activité vers une autre avec des données pour les activer.
Aisi, nous utilisons un **Intent** nommé *playIntent* qui nous permet de lancer notre application à travers la méthode **onStart()**. Cette Intent est également utilisé lors que l'application est détruite, soit la méthode **onDestroy()**.

## Classes

### AudioFile
La classe **AudioFile** nous permet de récupérer toute les informations que notre musique.
Elle contient donc, l'id, le chemin, le titre, l'album, l'artiste et la durrée de la musique.
Nous avons donc mis en place les *getter()* et les *setter()* afin de pouvoir récuperer chaque informations utiles tels que *getTitle()* ou encore *setTitle()*.

### AudioFileManager
La classe **AudioFileManager** nous permet de stocker les différentes musiques en les conservant dans un tableau de type *ArrayList<AdioFile>*.
 
### AudioService
La classe **AudioService** hérite de la classe **Service** et implémentes les différentes interfaces suivantes :
* **MediaPlayer.OnPreparedListener**
* **MediaPlayer.OnErrorListener**
* **MediaPlayer.OnCompletionListener**

Cette classe nous permet de mettre en place les différentes fonctionnalités désirées par l'utilisateur. Elle permet donc de mettre en marche les différents boutons mis en place sur notre applications tels que :
 * Play
 * Pause
 * Suivant 
 * Précédent
 * Aléatoire

## Permissions 

## Background Services/Threads

## Sensor(s) used

## Autres

## Auteurs
**_Projet réalisé par :_**
* **_Salma BENCHELKHA_**
* **_Mouncif LEKMITI_**
* **_Enzo CERINI_**
