package com.catalyte.OrionsPets.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "games")
public class Game {
    @Id
    private String id;
    private String playerOne;
    private String playerTwo;
    private int scoreOne;
    private int scoreTwo;
    private long time;
    private ArrayList<Game> history = new ArrayList<>();

    public Game(String playerOne, String playerTwo, int scoreOne, int scoreTwo, long time) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.scoreOne = scoreOne;
        this.scoreTwo = scoreTwo;
        this.time = time;
    }

    public void addHistory(Game game) {
        this.history.add(game);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<Game> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Game> history) {
        this.history = history;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getScoreOne() {
        return scoreOne;
    }

    public void setScoreOne(int scoreOne) {
        this.scoreOne = scoreOne;
    }

    public int getScoreTwo() {
        return scoreTwo;
    }

    public void setScoreTwo(int scoreTwo) {
        this.scoreTwo = scoreTwo;
    }
}
