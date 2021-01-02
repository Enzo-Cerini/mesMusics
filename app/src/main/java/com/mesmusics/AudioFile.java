package com.mesmusics;

public class AudioFile {

    long id;
    String path;
    String title;
    String album;
    String artist;
    long duration;


    public AudioFile(String path, String title, String album, String artist, long duration,long id) {
        this.path = path;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.id = id;
    }

    public AudioFile() {
        this.path = null;
        this.title = null;
        this.album = null;
        this.artist = null;
        this.duration = 0;
        this.id = 0;
    }

    public long getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
