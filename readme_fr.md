# Document de conception du projet *mesMusics*

## :black_medium_square: Description de l'application

L’application MES Musics est une application de musique. 
Elle regroupe toutes les musiques présentes dans le téléphone de l’utilisateur. 
De plus, les capteurs de mouvement du téléphone permettront de contrôler la lecture des musiques. 
L’application affichera notamment les informations de chaque musique (artiste, titre, album…) grâce à une base de données en ligne.
L’utilisateur peuvent gérer la lecture en mettant « lecture », « pause », « suivant », « précédent » ou « aléatoire ». 

De plus, il a également la possibilité de contrôler celle-ci grâce aux capteurs de mouvements :
  *	un mouvement du téléphone vers la haut pour lire ou mettre en pause la musique. :play_or_pause_button:
  *	un mouvement du téléphone vers la droite pour passer à la musique suivante. :next_track_button:
  *	un mouvement du téléphone vers la gauche pour revenir à la musique précédente. :previous_track_button:

L’utilisateur peut soit :
* lire les musiques déja présentes sur son téléphone.
* ajouter des musiques à sa playlist à l'aide d'un bouton "Ajouter". 

Il pourra également utiliser l’application en arrière-plan ou profiter de celle-ci avec le téléphone verrouillé.

## :black_medium_square: But de l'application
  *	Ecouter de la musique (arrière-plan, premier-plan et téléphone verrouillé)
  * Regroupez toute la musique contenue dans le téléphone.
  *	Afficher tous les détails des chansons (à partir d'une base de données en ligne).
  *	Utiliser des détecteurs de mouvement pour contrôler la lecture.
  *	Créer sa playlist.

## :black_medium_square: Activités

### :black_medium_small_square: MainActivity
Notre classe **MainActivity** est l'élément principal de notre application. Celle-ci hérite de la classe **_AppCompatActivity_** et implémente l'interface **_MediaController.MediaPlayerControl_**.
Nous allons détailler les principales méthodes de cette classe.
* La procédure **_onRequestPermissionsResult()_** permet de demander la permission de lire le stockage externe. Ainsi, si la permission n'est pas déja accepté, une demande apparait au lancemement de l'application **MES Musics** demandant à l'utilisateur d'autorisé cette lecture. 
* Les procédures **_onCreate()_** et **_onStart_** sont également importantes car elles permettent la création et le lancement de l'application **MESMusics**. 
* De plus, la procédure **_addToPlaylist()_** permet l'ajout des musiques dans la playlist de l'utilisateur à l'aide d'un bouton "Ajouter".
* La procédure **_switchToAccueil()_** et **_switchToPlaylist()_** permet de switcher entre l'accueil de l'application et la playlist de l'utilisateur.
* Nous avons également mis en place les petites méthodes tels que **_start()_**, **_pause_**, **_playNext()_** ou encore **_playPrev()_** qui permette de gérer la musique.

### :black_medium_small_square: AddPlayslitActivity
Cette activité permet l'ajout de musique dans la playlist de l'utilisateur à l'aide d'un bouton mis en place dans la page d'accueil de l'application. Lorsque l'utilisateur appuie sur le bouton d'ajout, un message lui est affiché afin de lui demander s'il souhaite vraiment ajouter cette musique à sa playlist. L'utilisateur répond grâce à deux boutons mis à sa disposition :
* Ajouter, qui va permettre d'ajouter la musique à sa playlist.
* Annuler, qui va permettre de revenir à la page d'accueil de l'application et qui n'ajoute pas la musique sélectionné à la playlist de l'utilisateur.

### :black_medium_small_square: SongDetailActivity
Cette activité permet d'afficher les détails de la musique sélectionné tels que le titre, l'artiste, l'album et la durée de la musique. Elle possède égélement un bouton qui va permettre de revenir à l'accueil et un bouton qui permet d'accéder aux informations de la musique sélectionnée.

## :black_medium_square: Intentions
Notre applications se décompose en activités distinctes les unes des autres. 
Les méthodes de nos différentes activités doivent pouvoir s'enchaîner et s'appeler, c'est pourquoi il faut mettre en place entres-elles une communication en utilisant des **_intentions_**.
Celles-ci permettent d'envoyer des messages d'une activité vers une autre avec des données pour les activer. 
* Ainsi, nous utilisons, dans le MainActivity, un **_Intent_** nommé *"playIntent"* qui nous permet de lancer notre application à travers la méthode **_onStart()_**. Cette Intent est également utilisé lorsque l'application est détruite, soit dans la méthode **_onDestroy()_**.
* Nous utilisons également un **_Intent_** nommé *myIntent* crée dans la classe PlaylistActivity afin de pouvoir revenir dans la page d'accueil de l'application.
* Enfin, (parler de l'Intent dans la classe InfoActivity).

## :black_medium_square: Classes

### :black_medium_small_square: *AudioFile*
La classe **AudioFile** nous permet de récupérer toutes les informations de notre musique.
Elle contient donc, l'identificateur, le chemin, le titre, l'album, l'artiste et la durée de la musique. Nous avons donc mis en place des *getter()* et des *setter()* afin de pouvoir récuperer et modifier chaque informations utiles tels que *getTitle()* ou encore *setTitle()*.

### :black_medium_small_square: *AudioFileManager*
La classe **AudioFileManager** nous permet de stocker les différentes musiques en les conservant dans un tableau de type *ArrayList*.
 
### :black_medium_small_square: *AudioService*
La classe **AudioService** hérite de la classe **Service** et implémentes les différentes interfaces suivantes :
* **MediaPlayer.OnPreparedListener**
* **MediaPlayer.OnErrorListener**
* **MediaPlayer.OnCompletionListener**

Cette classe nous permet de mettre en place les différentes fonctionnalités désirées par l'utilisateur. Elle permet donc de mettre en marche les différents boutons mis en place sur notre applications tels que : **_Lecture - Pause - Suivant - Précédent - Aléatoire_**

## :black_medium_square: Permissions 
Notre application nécéssite plusieurs permissions tels que :
* **_INTERNET_**, afin d'avoir l'accès à Internet.
* **_WAKE_LOCK_**, afin de verrouiller le réveil. 
* **_WRITE_EXTERNAL_STORAGE_**, afin de pouvoir écrire sur le stockage externe.
* **_READ_EXTERNAL_STORAGE_**, afin de pouvoir lire le stockage externe.
* **_MANAGE_EXTERNAL_STORAGE_**, afin de pouvoir gérer le stockage externe.

## :black_medium_square: Services d'arrière-plan 
L'utilisateur a également  la possibilité d'écouter de la musique en arrière plan. Pour cela, nous avons crée dans la classe AudioService, la méthode onPrepare() à laquelle on a rajouté un Intent du MainActivity afin de créer ensuite un pendingIntent qui va permettre de gérer la musique en arrière-plan. Nous avons rajouter à cela, une notifcation afin d'afficher les bouton "Lecture" et "Pause" sur la barre de notification de l'utilisateur.

## :black_medium_square: Capteurs de mouvement utilisés
L'utilisateur a également la possibilité de contrôler la lecture de ses musiques en utilisant les capteurs de mouvement du téléphone. Pour cela, nous avons enregistré un Listener dans la méthode onResume() du MainActivity. Puis, nous avons appliqué la méthode registerListener sur l'attribut sensorManager. De plus, nous avons notamment passé en paramètre de cette méthode un new SensorEventListener dans lequel nous avons redéfinis la méthode onSensorChanged(). 
Nous y avons attribué en fonction de l'inclinaison du téléphone 
* sur l'axe X, les évènements **Suivant** et **Précédent**.
* sur l'axe Z, les évènements **Lecture** et **Pause**.

## :black_medium_square: Threads
Nous avons également utilisé un Thread pour notre application.
En passant en paramètre de la méthode runOnUIThread() un new Runable, nous avons redéfinis la méthode run(). 
* Dans cette dernière, nous avons configuré notre SeekBar en paramètrant son maximum en fonction de la taille de la musique et en affichant le mouvement de la barre de progression grâce à la méthode setProgress.currentPosition().
* Le texte montrant le temps de la musique qui défile a égéalment été mis en place à l'aide d'un Thread. Ainsi, à chaque seconde qui passe le temps en seconde et incrémenté.

## :black_medium_square: Autres
Deux versions de ce fichier ont été réalisées :
* version française (readme_fr.md)
* version anglaise (readme_en.md)

## :black_medium_square: Auteurs
**_Projet réalisé par :_**
* **_Salma BENCHELKHA (salmabenchelkha@gmail.com)_**
* **_Mouncif LEKMITI (m.lekmiti@hotmail.com)_**
* **_Enzo CERINI (cerini.enzo@gmail.com)_**
