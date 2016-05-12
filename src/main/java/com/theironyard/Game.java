package com.theironyard;

public class Game {
    String name;
    String genre;
    String platform;
    int releaseYear;
    private int id = 0;

    public Game(String name, String genre, String platform, int releaseYear) {
        this.name = name;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
