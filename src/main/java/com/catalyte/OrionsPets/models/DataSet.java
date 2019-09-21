package com.catalyte.OrionsPets.models;

import java.util.ArrayList;

public class DataSet {
    public String name;
    public ArrayList<PlotPoint> dataPoints = new ArrayList<>();

    public DataSet(String name) {
        this.name = name;
    }

    public void addPoint(PlotPoint point) {
        this.dataPoints.add(point);
    }
}
