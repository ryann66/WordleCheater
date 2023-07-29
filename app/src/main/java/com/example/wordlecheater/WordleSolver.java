package com.example.wordlecheater;

public interface WordleSolver {
    public void reset();

    public String getBestWord();

    public void addConstraint();//todo add paramters

    public boolean lastWord();
}
