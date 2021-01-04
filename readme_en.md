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
* So we use in the MainActivity, an **_Intent_** named *"playIntent"* which allows us to launch our application through the method **_onStart()_**. This Intent is also used when the application is destroyed, either in the method **_onDestroy()_**.
We also use Intents in order to be able to navigate through the different activities, either towards the AddPlaylistActivity activity (swithToConfirmPlaylist) or to the AudioInfoActivity activity  (switchToAudioInfos()).
* We also use an **_Intent_** named *myIntent* created in AddPlaylistActivity class in order to be able to return to the home page of the application.
* Finally, we use an **_Intent_** in the AudioInfosActivity class in order to be able to return to the home page of the application.

## :black_medium_square: Classes

### :black_medium_small_square: *AudioFile*
The **AudioFile** class allows us to retrieve all the information about our music.
It contains the path, title, album, artist and duration of the music. We have therefore set up *getter()* and *setter()* in order to be able to retrieve and modify any useful information such as *getTitle()* or *setTitle()*.

### :black_medium_small_square: *AudioFileManager*
The **AudioFileManager** class allows us to retrieve and store the different music and their information (path, title, artist...) by keeping them in a list of type *ArrayList*. In addition, the music is stored as an *AudioFile* object.
 
### :black_medium_small_square: *AudioService*
The **AudioService** class inherits from the **Service** class and implements the following different interfaces :
* **MediaPlayer.OnPreparedListener**
* **MediaPlayer.OnErrorListener**
* **MediaPlayer.OnCompletionListener**

This class allows us to set up the various functionalities desired by the user. Therefore, the method **_onPrepared()_** allows the start of the MediaPlayer and allows the visualization of a notification when a music is started by the user.
It allows in particular to turn on the various buttons set up on our applications such as : **_ Play - Pause - Next - Previous_** and this thanks to the methods :
* playAudio() to start music and startAudio() to play it.
* pausePlayer() to pause.
* playNext() to start the next music
* playPrev() to return to the previous music.

## :black_medium_square: Permissions 
Our app requires several permissions listed in the AndroidManifest such as:
* **_INTERNET_**, in order to have Internet access.
* **_WAKE_LOCK_**, in order to leave the phone unlocked when the user is playing music.
* **_WRITE_EXTERNAL_STORAGE_**, so that you can write to the external storage.
* **_READ_EXTERNAL_STORAGE_**, so that you can read the external storage.
* **_FOREGROUND_SERVICE_**, so that you can play music in the background.

## :black_medium_square: Background services
The user also has the option of listening to music in the background. For this, we have created in the AudioService class, the onPrepare() method to which we have added an Intent of the MainActivity in order to create a pendingIntent which will allow us to manage the music in the background. We have added to this, a notification to display the title of the current music.

L'utilisateur a également  la possibilité d'écouter de la musique en arrière plan. Pour cela, nous avons crée dans la classe AudioService, la méthode onPrepare() à laquelle on a rajouté un Intent du MainActivity afin de créer un pendingIntent qui va nous permettre de gérer la musique en arrière-plan. Nous avons rajouter à cela, une notification afin d'afficher le titre de la musique en cours.

## :black_medium_square: Motion sensors used
The user also has the option of controlling the playback of their music using the phone's motion sensors. To do this, we have registered a Listener in the onResume() method of the MainActivity. Then, we applied the registerListener() method on the sensorManager attribute. In addition, we have passed as a parameter of this method a new SensorEventListener in which we have redefined the onSensorChanged() method.
We assigned it based on the tilt of the phone :
* on the X axis, the **Next** and **Previous** events.
* on the Z axis, the **Play** and **Pause** events.

## :black_medium_square: Threads
We also used a Thread for our application.
By passing a new Runable as a parameter of the runOnUIThread() method, we have redefined the run() method. 
* In the latter, we have configured our SeekBar by setting its maximum according to the size of the music and by displaying the movement of the progress bar thanks to the setProgress.currentPosition() method.
* The text showing the time of the scrolling music was also implemented using this Thread. Thus, with each passing second, the time in seconds is incremented.

## :black_medium_square: Other
Two versions of this file have been produced :
* french version (readme_fr.md)
* english version (readme_en.md)

## :black_medium_square: Authors
**_Project made by :_**
* **_Salma BENCHELKHA (salmabenchelkha@gmail.com)_**
* **_Mouncif LEKMITI (m.lekmiti@hotmail.com)_**
* **_Enzo CERINI (cerini.enzo@gmail.com)_**
