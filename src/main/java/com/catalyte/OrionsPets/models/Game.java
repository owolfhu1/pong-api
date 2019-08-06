package com.catalyte.OrionsPets.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "games")
public class Game {
    @Id
    private String id;
    private String playerOne;
    private String playerTwo;
    private int scoreOne;
    private int scoreTwo;
    private String date;

    public Game(String playerOne, String playerTwo, int scoreOne, int scoreTwo, String date) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.scoreOne = scoreOne;
        this.scoreTwo = scoreTwo;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
