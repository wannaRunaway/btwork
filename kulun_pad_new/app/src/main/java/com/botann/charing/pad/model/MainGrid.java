package com.botann.charing.pad.model;

/**
 * Created by mengchenyun on 2017/1/16.
 */

public class MainGrid {

    private String name;

    private String background;

    public MainGrid(String name, String background) {
        this.name = name;
        this.background = background;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
