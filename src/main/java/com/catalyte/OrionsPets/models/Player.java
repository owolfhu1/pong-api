package com.catalyte.OrionsPets.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players")
public class Player {

  @Id
  private String id;
  private String username;
  private double rating = 1500;
  private double zachRating = 1500;
  private int wins = 0;
  private int losses = 0;

  public Player(String username) {
    this.username = username;
  }

  public double getZachRating() {
    return zachRating;
  }

  public void setZachRating(double zachRating) {
    this.zachRating = zachRating;
  }

  public void win() {
    this.wins++;
  }

  public void lose() {
    this.losses++;
  }

  public int getWins() {
    return wins;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public int getLosses() {
    return losses;
  }

  public void setLosses(int losses) {
    this.losses = losses;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
