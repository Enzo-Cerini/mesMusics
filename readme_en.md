# Design document of *mesMusics* project

## :black_medium_square: Description of the application

The MES Musics application is a music application.
She regroup together all the music present on the user's phone.
In addition, the phone's motion sensors will control music playback.
The application will display the information of each music (artist, title, album, etc.).
The user can manage the playback by setting "play", "pause", "next" or "previous".
In addition, he also has the possibility of controlling it thanks to the movement sensors:
  *	move the phone up to play or pause music. :play_or_pause_button:
  *	a movement of the phone to the right to go to the next music. :next_track_button:
  *	a movement of the phone to the left to return to the previous music. :previous_track_button:

The user can either:
* read the music already on your phone.
* view information about the selected music.
* add music to your playlist using an "Add" button. 

They can also use the application in the background or enjoy it with the phone locked.

## :black_medium_square: Purpose of the application
  * Display all the musics present in the phone.
  *	View all music details.
  *	Listen to music (background, foreground and phone locked).
  * Naviguer entre les différentes musiques (lecture, pause, suivant et précédent).
  *	Navigate between the different music (play, pause, next and previous).
  *	Create a playlist.

## :black_medium_square: Activities

### :black_medium_small_square: MainActivity
Our **MainActivity** class is the main element of our application. This inherits from the **_AppCompatActivity_** class and implements the **_MediaController.MediaPlayerControl_** interface.
We will detail the main methods of this class.

* The **_onRequestPermissionsResult()_** procedure is used to request permission to read external storage. Thus, if the permission is not already accepted, a request appears when launching the **MES Musics** application asking the user to authorize this reading.
* The **_onCreate()_** and **_onStart()_** procedures are also important because they allow the creation and launching of the application.
* In addition, the **_addToPlaylist()_** procedure allows music to be added to the user's playlist using an "Add" button.
* The procedure **_switchToAccueil()_** and **_switchToPlaylist()_** allows to switch between the home of the application and the user's playlist.
* We also have implemented small methods such as **_start()_**, **_pause_**, **_playNext()_** or even **_playPrev()_** which allows you to manage the music.

### :black_medium_small_square: AddPlayslitActivity
This activity allows the addition of music in the user's playlist using a button set up in the home page of the application. When the user presses the add button, a message is displayed asking him if he really wants to add this music to his playlist.
The user responds thanks to two buttons available to him :
* Add, which will allow you to add the music to your playlist (**_confirmAddToPlaylist()_**).
* Cancel, which will allow to return to the home page of the application and which does not add the selected music to the user's playlist (**_goToMenu()_**).
After creating a playlist, the user is able to listen to their playlist as they wish.

### :black_medium_small_square: AudioInfosActivity
Use this activity to view details of the selected music such as title, artist, album, and duration. This, thanks to **_initSongInfos()_** method. It also has a button that will allow you to return to the reception.

## :black_medium_square: Intents
Our application break down into activities that are distinct from each other.
The methods of our different activities must be able to link and call each other, this is why we must set up a communication between them using **_Intents_**.
These allow you to send messages from one activity to another. 
* So we use in the MainActivity, an **_Intent_** nommé *"playIntent"* qui nous permet de lancer notre application à travers la méthode **_onStart()_**. Cette Intent est également utilisé lorsque l'application est détruite, soit dans la méthode **_onDestroy()_**.
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
