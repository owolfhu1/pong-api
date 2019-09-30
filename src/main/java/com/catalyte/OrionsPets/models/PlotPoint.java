package com.catalyte.OrionsPets.models;


public class PlotPoint {
    public Long x;
    public int y;
    public int old;
    public Game game;
    public String name;

    public PlotPoint(Long x, int y, Game game, String name, int old) {
        this.x = x;
        this.y = y;
        this.game = game;
        this.name = name;
        this.old = old;
    }
}
