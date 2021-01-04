# Document de conception du projet *mesMusics*

## :black_medium_square: Description de l'application

L’application MES Musics est une application de musique. 
Elle regroupe toutes les musiques présentes sur le téléphone de l’utilisateur. 
De plus, les capteurs de mouvement du téléphone permettront de contrôler la lecture des musiques. 
L’application affichera notamment les informations de chaque musique (artiste, titre, album…).
L’utilisateur peut gérer la lecture en mettant « lecture », « pause », « suivant » ou « précédent ». 

De plus, il a également la possibilité de contrôler celle-ci grâce aux capteurs de mouvements :
  *	un mouvement du téléphone vers la haut pour lire ou mettre en pause la musique. :play_or_pause_button:
  *	un mouvement du téléphone vers la droite pour passer à la musique suivante. :next_track_button:
  *	un mouvement du téléphone vers la gauche pour revenir à la musique précédente. :previous_track_button:

L’utilisateur peut soit :
* lire les musiques déja présentes sur son téléphone.
* voir les informations concernant la musisque sélectionnée.
* ajouter des musiques à sa playlist à l'aide d'un bouton "Ajouter". 

Il pourra également utiliser l’application en arrière-plan ou profiter de celle-ci avec le téléphone verrouillé.

## :black_medium_square: But de l'application
  * Afficher toutes les musiques présentent dans le téléphone.
  *	Afficher tous les détails des musiques.
  *	Ecouter de la musique (arrière-plan, premier-plan et téléphone verrouillé).
  * Naviguer entre les différentes musiques (lecture, pause, suivant et précédent).
  *	Utiliser des détecteurs de mouvement pour contrôler la lecture.
  *	Créer une playlist.

## :black_medium_square: Activités

### :black_medium_small_square: MainActivity
Notre classe **MainActivity** est l'élément principal de notre application. Celle-ci hérite de la classe **_AppCompatActivity_** et implémente l'interface **_MediaController.MediaPlayerControl_**.
Nous allons détailler les principales méthodes de cette classe.
* La procédure **_onRequestPermissionsResult()_** permet de demander la permission de lire le stockage externe. Ainsi, si la permission n'est pas déja accepté, une demande apparait au lancemement de l'application **MES Musics** demandant à l'utilisateur d'autorisé cette lecture. 
* Les procédures **_onCreate()_** et **_onStart_** sont également importantes car elles permettent la création et le lancement de l'application. 
* De plus, la procédure **_addToPlaylist()_** permet l'ajout des musiques dans la playlist de l'utilisateur à l'aide d'un bouton "Ajouter".
* La procédure **_switchToAccueil()_** et **_switchToPlaylist()_** permet de switcher entre l'accueil de l'application et la playlist de l'utilisateur.
* Nous avons également mis en place les petites méthodes tels que **_start()_**, **_pause_**, **_playNext()_** ou encore **_playPrev()_** qui permette de gérer la musique.

### :black_medium_small_square: AddPlayslitActivity
Cette activité permet l'ajout de musique dans la playlist de l'utilisateur à l'aide d'un bouton mis en place dans la page d'accueil de l'application. Lorsque l'utilisateur appuie sur le bouton d'ajout, un message lui est affiché afin de lui demander s'il souhaite vraiment ajouter cette musique à sa playlist. L'utilisateur répond grâce à deux boutons mis à sa disposition :
* Ajouter, qui va permettre d'ajouter la musique à sa playlist (**_confirmAddToPlaylist()_**).
* Annuler, qui va permettre de revenir à la page d'accueil de l'application et qui n'ajoute pas la musique sélectionné à la playlist de l'utilisateur (**_goToMenu()_**).
Après avoir crée une playlist, l'utilisateur est en mesure d'écouter sa playlist comme il le souhaite.

### :black_medium_small_square: AudioInfosActivity
Cette activité permet d'afficher les détails de la musique sélectionnée tels que le titre, l'artiste, l'album et la durée. Cela grâce à la méthode **_initSongInfos()_**. Elle possède également un bouton qui va permettre de revenir à l'accueil. 

## :black_medium_square: Intents
Notre applications se décompose en activités distinctes les unes des autres. 
Les méthodes de nos différentes activités doivent pouvoir s'enchaîner et s'appeler, c'est pourquoi il faut mettre en place entres-elles une communication en utilisant des **_Intents_**.
Celles-ci permettent d'envoyer des messages d'une activité vers une autre. 
* Ainsi, nous utilisons dans le MainActivity, un **_Intent_** nommé *"playIntent"* qui nous permet de lancer notre application à travers la méthode **_onStart()_**. Cette Intent est également utilisé lorsque l'application est détruite, soit dans la méthode **_onDestroy()_**.
Nous utilisons également des Intents afin de pouvoir naviguer à travers les différentes activités, soit vers l'activité AddPlaylistActivity (swithToConfirmPlaylist) ou encore vers l'activité AudioInfoActivity (switchToAudioInfos()).
* Nous utilisons également un **_Intent_** nommé *myIntent* crée dans la classe AddPlaylistActivity afin de pouvoir revenir sur la page d'accueil de l'application.
* Enfin, nous utilisons un **_Intent_** dans la classe AudioInfosActivity afin de pouvoir retourner sur l'accueil de l'application.

## :black_medium_square: Classes

### :black_medium_small_square: *AudioFile*
La classe **AudioFile** nous permet de récupérer toutes les informations de notre musique.
Elle contient donc le chemin, le titre, l'album, l'artiste et la durée de la musique. Nous avons donc mis en place des *getter()* et des *setter()* afin de pouvoir récuperer et modifier chaques informations utiles tels que *getTitle()* ou encore *setTitle()*.

### :black_medium_small_square: *AudioFileManager*
La classe **AudioFileManager** nous permet de récupérer et stocker les différentes musiques et leurs informations (chemin, titre, artiste...) en les conservant dans une liste de type *ArrayList*. De plus, les musiques sont stockées sous-forme d'objet *AudioFile*.
 
### :black_medium_small_square: *AudioService*
La classe **AudioService** hérite de la classe **Service** et implémentes les différentes interfaces suivantes :
* **MediaPlayer.OnPreparedListener**
* **MediaPlayer.OnErrorListener**
* **MediaPlayer.OnCompletionListener**

Cette classe nous permet de mettre en place les différentes fonctionnalités désirées par l'utilisateur. De ce fait, la méthode **_onPrepared()_** permet le démarrage du MediaPlayer et permet la visualisation d'une notification lorsque qu'une musique est lancée par l'utilisateur. Elle permet notamment de mettre en marche les différents boutons mis en place sur notre applications tels que : **_Lecture - Pause - Suivant - Précédent_** et cela grâce aux méthodes :
* playAudio() pour lancer une musique et startAudio() pour la lire.
* pausePlayer() pour mettre pause.
* playNext() pour lancer la musique suivante.
* playPrev() pour revenir à la musique précédente.

## :black_medium_square: Permissions 
Notre application nécéssite plusieurs permissions énuméré dans le AndroidManifest tels que :
* **_INTERNET_**, afin d'avoir l'accès à Internet.
* **_WAKE_LOCK_**, afin de laisser le téléphone déverrouillé quand l'utilisateur lis une musique.
* **_WRITE_EXTERNAL_STORAGE_**, afin de pouvoir écrire sur le stockage externe.
* **_READ_EXTERNAL_STORAGE_**, afin de pouvoir lire le stockage externe.
* **_FOREGROUND_SERVICE_**, afin de pouvoir lire une musique en arrière-plan.

## :black_medium_square: Services d'arrière-plan 
L'utilisateur a également  la possibilité d'écouter de la musique en arrière plan. Pour cela, nous avons crée dans la classe AudioService, la méthode onPrepare() à laquelle on a rajouté un Intent du MainActivity afin de créer un pendingIntent qui va nous permettre de gérer la musique en arrière-plan. Nous avons rajouter à cela, une notification afin d'afficher le titre de la musique en cours.

## :black_medium_square: Capteurs de mouvement utilisés
L'utilisateur a également la possibilité de contrôler la lecture de ses musiques en utilisant les capteurs de mouvement du téléphone. Pour cela, nous avons enregistré un Listener dans la méthode onResume() du MainActivity. Puis, nous avons appliqué la méthode registerListener sur l'attribut sensorManager. De plus, nous avons notamment passé en paramètre de cette méthode un new SensorEventListener dans lequel nous avons redéfinis la méthode onSensorChanged(). 
Nous y avons attribué en fonction de l'inclinaison du téléphone 
* sur l'axe X, les évènements **Suivant** et **Précédent**.
* sur l'axe Z, les évènements **Lecture** et **Pause**.

## :black_medium_square: Threads
Nous avons également utilisé un Thread pour notre application.
En passant en paramètre de la méthode runOnUIThread() un new Runable, nous avons redéfinis la méthode run(). 
* Dans cette dernière, nous avons configuré notre SeekBar en paramètrant son maximum en fonction de la taille de la musique et en affichant le mouvement de la barre de progression grâce à la méthode setProgress.currentPosition().
* Le texte montrant le temps de la musique qui défile a également été mis en place à l'aide de ce Thread. Ainsi, à chaque seconde qui passe, le temps en seconde est incrémenté.

## :black_medium_square: Autres
Deux versions de ce fichier ont été réalisées :
* version française (readme_fr.md)
* version anglaise (readme_en.md)

## :black_medium_square: Auteurs
**_Projet réalisé par :_**
* **_Salma BENCHELKHA (salmabenchelkha@gmail.com)_**
* **_Mouncif LEKMITI (m.lekmiti@hotmail.com)_**
* **_Enzo CERINI (cerini.enzo@gmail.com)_**
