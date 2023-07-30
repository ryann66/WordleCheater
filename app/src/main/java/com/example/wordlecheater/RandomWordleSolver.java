package com.example.wordlecheater;

public class RandomWordleSolver implements  WordleSolver{
    public RandomWordleSolver(){

    }

    public void reset() {

    }

    public String getBestWord() {
        return "Hello";
    }

    public boolean addConstraints(char c1, MainActivity.TileStyle ts1,
                                  char c2, MainActivity.TileStyle ts2,
                                  char c3, MainActivity.TileStyle ts3,
                                  char c4, MainActivity.TileStyle ts4,
                                  char c5, MainActivity.TileStyle ts5) {
        return true;
    }

    public void addConstraint() {

    }

    public boolean lastWord(){
        return false;
    }

    public boolean noWords(){
        return false;
    }
}
